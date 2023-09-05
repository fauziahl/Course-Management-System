package com.juaracoding.TA.controller;

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.AksesDTO;
import com.juaracoding.TA.dto.DivisiDTO;
import com.juaracoding.TA.dto.MenuDTO;
import com.juaracoding.TA.dto.MenuHeaderDTO;
import com.juaracoding.TA.model.Akses;
import com.juaracoding.TA.model.Demo;
import com.juaracoding.TA.service.AksesService;
import com.juaracoding.TA.service.DivisiService;
import com.juaracoding.TA.service.MenuService;
import com.juaracoding.TA.utils.*;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/usrmgmnt")
public class AksesController {
    private AksesService aksesService;
    private MenuService menuService;
    private DivisiService divisiService;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();
    private List<Demo> lsCPUpload = new ArrayList<Demo>();
    private String [] strExceptionArr = new String[2];
    private MappingAttribute mappingAttribute = new MappingAttribute();
    private PdfGeneratorLibre generator = null;
    private String [][] strBody = null;
    private String strNamaDivisi = "";
    private String strKodeDivisi = "";
    @Autowired
    PdfGenaratorUtil pdfGenaratorUtil;//wajib di deklarasikan kalau pakai thymeleaf engine

    private StringBuilder sBuild = new StringBuilder();
    public AksesController(AksesService aksesService, MenuService menuService, DivisiService divisiService) {
        strExceptionArr[0] = "AksesController";
        mapSorting();
        this.aksesService = aksesService;
        this.menuService = menuService;
        this.divisiService = divisiService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","idAkses");
        mapSorting.put("nama","namaAkses");
    }

    @GetMapping("/v1/akses/new")
    public String createAkses(Model model, WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        model.addAttribute("akses", new AksesDTO());
        model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
        model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya
        return "akses/create_akses";
    }

    @GetMapping("/v1/akses/edit/{id}")
    public String editAkses(Model model,WebRequest request,@PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        objectMapper = aksesService.findById(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut

        if((Boolean) objectMapper.get("success"))
        {
            AksesDTO aksesDTOForSelect = (AksesDTO) objectMapper.get("data");
            List<DivisiDTO> listDivisi = divisiService.getAllDivisi();
            List<MenuDTO> listMenuDTO = menuService.getAllMenu();

            List<MenuDTO> selectedMenuDTO = new ArrayList<MenuDTO>();
            for (MenuDTO menuDTO:
                 listMenuDTO) {
                for (MenuDTO menuz:
                        aksesDTOForSelect.getListMenuAkses()) {
                    if(menuDTO.getIdMenu()==menuz.getIdMenu())
                    {
                        selectedMenuDTO.add(menuz);
                    }
                }
            }
            Set<Long> menuSelected = selectedMenuDTO.stream().map(MenuDTO::getIdMenu).collect(Collectors.toSet());
            model.addAttribute("akses", aksesDTOForSelect);
            model.addAttribute("listDivisi", listDivisi);//untuk parent nya
            model.addAttribute("listMenu", listMenuDTO);//untuk parent nya
            model.addAttribute("menuSelected", menuSelected);//untuk parent nya
            if(aksesDTOForSelect.getDivisi() != null)
            {
                model.addAttribute("selectedValues", aksesDTOForSelect.getDivisi().getIdDivisi());//yang akan diselect saat modals muncul
            }
            return "akses/edit_akses";
        }
        else
        {
            model.addAttribute("akses", new Akses());
            return "redirect:/api/usrmgmnt/v1/akses/default";
        }
    }
    @PostMapping("/v1/akses/new")
    public String newAkses(@ModelAttribute(value = "akses")
                          @Valid Akses aksesDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
    )
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("akses",aksesDTO);
            model.addAttribute("status","error");
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya

            return "akses/create_akses";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("akses",aksesDTO);
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya
            return "akses/create_akses";
        }
        /* END OF VALIDATION */

        Akses akses = modelMapper.map(aksesDTO, new TypeToken<Akses>() {}.getType());
        objectMapper = aksesService.saveAkses(akses,request);

        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session ke attribut
        if((Boolean) objectMapper.get("success"))
        {
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/usrmgmnt/v1/akses/fbpsb/0/asc/id?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya
            model.addAttribute("akses",new AksesDTO());
            model.addAttribute("status","error");
            return "akses/create_akses";
        }
    }

    @PostMapping("/v1/akses/edit/{id}")
    public String editAkses(@ModelAttribute("akses")
                          @Valid Akses akses
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        akses.setIdAkses(id);
        if(akses.getListMenuAkses()==null)
        {
            mappingAttribute.setErrorMessage(bindingResult,"HARAP PILIH MENU LIST ");
        }
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("akses",akses);
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya
            model.addAttribute("menuSelected", new ArrayList<MenuDTO>());//untuk parent nya
            return "akses/edit_akses";
        }

        /* END OF VALIDATION */

        objectMapper = aksesService.updateAkses(id,akses,request);

        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut
            model.addAttribute("akses",new AksesDTO());
            return "redirect:/api/usrmgmnt/v1/akses/fbpsb/0/asc/id?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("akses",new AksesDTO());
            model.addAttribute("listDivisi", divisiService.getAllDivisi());//untuk parent nya
            model.addAttribute("listMenu", menuService.getAllMenu());//untuk parent nya
            return "akses/edit_akses";
        }
    }


    @GetMapping("/v1/akses/default")
    public String getDefaultData(Model model,WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        Pageable pageable = PageRequest.of(0,5, Sort.by("idAkses"));
        objectMapper = aksesService.findAllAkses(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut;

        model.addAttribute("akses",new AksesDTO());
        model.addAttribute("sortBy","id");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/akses/akses";
    }

    @GetMapping("/v1/akses/fbpsb/{page}/{sort}/{sortby}")
    public String findByAkses(
            Model model,
            @PathVariable("page") Integer pagez,
            @PathVariable("sort") String sortz,
            @PathVariable("sortby") String sortzBy,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            @RequestParam String sizeComponent,
            WebRequest request
    ){
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut

        if(columnFirst.equals("id") && valueFirst.equals(""))
        {
            return "redirect:/api/usrmgmnt/v1/akses/default";
        }

        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"id":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = aksesService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut
        model.addAttribute("akses",new AksesDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/akses/akses";
    }

    @GetMapping("/v1/akses/vml/{id}")
    public String viewAksesMenu(Model model ,
                                WebRequest request,
                                @PathVariable("id")Long idAkses)//untuk menampilkan menu yang sudah dimapping ke akses
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        objectMapper = aksesService.findById(idAkses,request);
        AksesDTO aksesDTO = (AksesDTO) objectMapper.get("data");
        String[] arrColumnTitle = new String[3];
        arrColumnTitle[0] = "ID MENU";
        arrColumnTitle[1] = "NAMA MENU";
        arrColumnTitle[2] = "GROUP MENU";

        List<MenuDTO> listMenu = aksesDTO.getListMenuAkses();
        int listSize = listMenu.size();
        String[][] arrContent = new String[listSize][arrColumnTitle.length];

        for (int i=0;i<listMenu.size();i++)
        {
            MenuHeaderDTO mHeader = listMenu.get(i).getMenuHeader();
            arrContent[i][0] = listMenu.get(i).getIdMenu().toString();
            arrContent[i][1] = listMenu.get(i).getNamaMenu().toString();
            arrContent[i][2] = mHeader==null?"":mHeader.getNamaMenuHeader();
        }
        model.addAttribute("arrColumnTitle",arrColumnTitle);
        model.addAttribute("arrContent",arrContent);
        model.addAttribute("title","LIST AKSES MENU "+aksesDTO.getNamaAkses());

        return "globalview";
    }

    @GetMapping("/v1/akses/delete/{id}")
    public String deleteAkses(Model model,WebRequest request,@PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        objectMapper = aksesService.deleteAkses(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut

        model.addAttribute("akses",new AksesDTO());
        return "redirect:/api/usrmgmnt/v1/akses/default";
    }



    @GetMapping("/v1/akses/xportpdflibre")
    public void exportToPDFLibre(
            Model model,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            WebRequest request,
            HttpServletResponse response
    ){
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        List<AksesDTO> listAksesDTO= aksesService.dataToExport(request,columnFirst,valueFirst);
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYYMMDDHHMMSS.sss");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        sBuild.setLength(0);
        String headervalue = sBuild.append(OtherConfig.getHowToDownloadReport()).append("; filename=akseslist").
                append(currentDateTime).append(".pdf").toString();
        response.setHeader(headerkey, headervalue);
        generator = new PdfGeneratorLibre();
        int intStrHeader=4;// INI YANG DIRUBAH SESUAIKAN DENGAN JUMLAH KOLOM
        String[] strHeader = new String[intStrHeader];
        /*
            DEFINISIKAN KOLOM NYA DISINI HARUS SESUAI JUMLAH NYA DENGAN HEADER YANG DI SET DI VARIABEL intStrHeader
         */
        strHeader[0] ="ID";
        strHeader[1] ="AKSES";
        strHeader[2] ="DIVISI";
        strHeader[3] ="KODE DIVISI";
        int intListAksesDTO = listAksesDTO.size();
        strBody = new String[intListAksesDTO][intStrHeader];


        for(int i=0;i<listAksesDTO.size();i++)
        {
            /*
                INI KALIAN MAPPING TAPI HATI2 DENGAN OBJECT, HARUS DI HANDLE NULL NYA
             */

            strNamaDivisi = listAksesDTO.get(i).getDivisi()==null?"-":listAksesDTO.get(i).getDivisi().getNamaDivisi();
            strKodeDivisi = listAksesDTO.get(i).getDivisi()==null?"-":listAksesDTO.get(i).getDivisi().getKodeDivisi();
            strBody[i][0] = String.valueOf(listAksesDTO.get(i).getIdAkses());
            strBody[i][1] = listAksesDTO.get(i).getNamaAkses();
            strBody[i][2] = strNamaDivisi;
            strBody[i][3] = strKodeDivisi;
        }

        sBuild.setLength(0);
        generator.generate(sBuild.
                append("LIST AKSES \n").//JUDUL REPORT
                append("total data : ").append(intListAksesDTO).//VARIABEL TOTAL DATA
                toString(),strHeader,strBody, response,"1");
    }

    @GetMapping("/v1/akses/xportpdfthyme")
    public ResponseEntity  exportToPDFThyme(
            Model model,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            WebRequest request,
            HttpServletResponse response
    ){
        Resource resource = null;
        try {

            mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
            String property = OtherConfig.getPathThymeleafTemplateReport();
            String tempDir = System.getProperty(property);
            List<AksesDTO> listAksesDTO= aksesService.dataToExport(request,columnFirst,valueFirst);
            sBuild.setLength(0);
            String fileName = sBuild.append(tempDir).append(OtherConfig.getPathSeparatorReport()).append(UUID.randomUUID().toString()).append(".pdf").toString();
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("content",listAksesDTO);
            pdfGenaratorUtil.createPdf("reportz/thymeleaf_template_akses",data,fileName);
            Path path = Paths.get(fileName);
            resource = new UrlResource(path.toUri());

        } catch (Exception e) {
            System.out.println(e);
        }
        sBuild.setLength(0);
        return ResponseEntity.ok()

                .contentType(MediaType.parseMediaType(MediaType.APPLICATION_PDF_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, sBuild.append(OtherConfig.getHowToDownloadReport()).append("; filename=\"")
                .append(resource.getFilename()).append("\"").toString())
                .body(resource);
    }

    @GetMapping("/v1/akses/xportpdfjasper")
    public void exportToPDFJasper(
            Model model,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            WebRequest request,
            HttpServletResponse response
    ){
        try
        {
            mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
            String sourceFileName = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "\\data\\reportz\\Akses_Report.jasper").getAbsolutePath();
            List<AksesDTO> listAksesDTO = aksesService.dataToExport(request,columnFirst,valueFirst);
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(listAksesDTO);
            Map parameters = new HashMap();
            parameters.put("hue","VARIABEL - hue 1");
            parameters.put("hue2","VARIABEL hue 2");
            parameters.put("pageHead","PAGE HEADER TEST !!");
            JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            response.setContentType("application/pdf");
            sBuild.setLength(0);
            response.addHeader("Content-Disposition", sBuild.append(OtherConfig.getHowToDownloadReport()).append("; filename=jasper.pdf;").toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/v1/akses/xportexcel")
    public void exportToExcel (
            Model model,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            WebRequest request,
            HttpServletResponse response
    ){
        try
        {
            mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String currentDateTime = dateFormatter.format(new Date());
            String headerKey = "Content-Disposition";
            sBuild.setLength(0);
            String headerValue = sBuild.append(OtherConfig.getHowToDownloadReport()).append("; filename=akseslist").append(currentDateTime).append(".xlsx").toString();
            response.setHeader(headerKey, headerValue);
            List<AksesDTO> listAksesDTO = aksesService.dataToExport(request,columnFirst,valueFirst);
            int listSize = listAksesDTO.size();
            if(listSize!=0)
            {
                int intLenthHeader = 4;
                String [] strHeader = new String[intLenthHeader];
                strHeader[0] = "ID";
                strHeader[1] = "AKSES";
                strHeader[2] = "NAMA DIVISI";
                strHeader[3] = "KODE DIVISI";

                strBody = new String[listSize][intLenthHeader];
                for(int i=0;i<listSize;i++)
                {
                    strNamaDivisi = listAksesDTO.get(i).getDivisi()==null?"-":listAksesDTO.get(i).getDivisi().getNamaDivisi();
                    strKodeDivisi = listAksesDTO.get(i).getDivisi()==null?"-":listAksesDTO.get(i).getDivisi().getKodeDivisi();
                    strBody[i][0] = String.valueOf(listAksesDTO.get(i).getIdAkses());
                    strBody[i][1] = listAksesDTO.get(i).getNamaAkses();
                    strBody[i][2] = strNamaDivisi;
                    strBody[i][3] = strKodeDivisi;
                }
                ExcelWriter excelWriter = new ExcelWriter(strBody,strHeader,"LIST_AKSES",response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/v1/akses/xportcsv")
    public void exportToCSV(
            Model model,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            WebRequest request,
            HttpServletResponse response
    ){
        try
        {
            mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
            response.setContentType("application/CSV");
            String headerKey = "Content-Disposition";
            String property = OtherConfig.getPathThymeleafTemplateReport();
            String tempDir = System.getProperty(property);
            sBuild.setLength(0);
            String fileName = sBuild.append(tempDir).append(OtherConfig.getPathSeparatorReport()).append(UUID.randomUUID().toString()).append(".csv").toString();
            sBuild.setLength(0);
            String headerValue = sBuild.append(OtherConfig.getHowToDownloadReport()).append("; filename=").append(fileName).toString();
            response.setHeader(headerKey, headerValue);
            List<AksesDTO> listAksesDTO = aksesService.dataToExport(request,columnFirst,valueFirst);
            int listSize = listAksesDTO.size();
            if(listSize!=0)
            {
                int intLenthHeader = 4;
                String [] strHeader = new String[intLenthHeader];
                strHeader[0] = "ID";
                strHeader[1] = "AKSES";
                strHeader[2] = "NAMA DIVISI";
                strHeader[3] = "KODE DIVISI";

                strBody = new String[listSize][intLenthHeader];
                for(int i=0;i<listSize;i++)
                {
                    strNamaDivisi = listAksesDTO.get(i).getDivisi()==null?"-":listAksesDTO.get(i).getDivisi().getNamaDivisi();
                    strKodeDivisi = listAksesDTO.get(i).getDivisi()==null?"-":listAksesDTO.get(i).getDivisi().getKodeDivisi();
                    strBody[i][0] = String.valueOf(listAksesDTO.get(i).getIdAkses());
                    strBody[i][1] = listAksesDTO.get(i).getNamaAkses();
                    strBody[i][2] = strNamaDivisi;
                    strBody[i][3] = strKodeDivisi;
                }
                CSVWriting csvWriting = new CSVWriting(strBody,strHeader,response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @GetMapping("/v1/akses/texcel")
    public void downloadTemplateExcel(
            HttpServletResponse response
    ){
        try {
            sBuild.setLength(0);
            File file = ResourceUtils.getFile(sBuild.append("classpath:").append(OtherConfig.getPathTemplateDownload()).append("template_akses.xlsx").toString());
            String fileName = URLDecoder.decode(file.getName(), "UTF-8");
            response.reset();//hanya untuk file excel agar tidak corrupt
            response.setContentType("application/octet-stream");
            sBuild.setLength(0);
            response.addHeader("content-disposition", sBuild.append(OtherConfig.getHowToDownloadReport()).append("; filename=\"").append(fileName).append("\"").toString());
            try (ServletOutputStream out = response.getOutputStream();
                 FileInputStream in = new FileInputStream(file);
                 ) {
                int octet = 0;
                while ((octet = in.read()) != -1) {
                    out.write(octet);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @GetMapping("/v1/akses/tcsv")
    public void downloadTemplateCSV(
            HttpServletResponse response
    ){
        try {
            sBuild.setLength(0);
            File file = ResourceUtils.getFile(sBuild.append("classpath:").append(OtherConfig.getPathTemplateDownload()).append("template_akses.csv").toString());
            String fileName = URLDecoder.decode(file.getName(), "UTF-8");
            response.setContentType("application/octet-stream");
            sBuild.setLength(0);
            response.addHeader("content-disposition", sBuild.append(OtherConfig.getHowToDownloadReport()).append("; filename=\"").append(fileName).append("\"").toString());
            try (ServletOutputStream out = response.getOutputStream();
                 FileInputStream in = new FileInputStream(file);
            ) {
                int octet = 0;
                while ((octet = in.read()) != -1) {
                    out.write(octet);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}