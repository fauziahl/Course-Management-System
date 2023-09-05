package com.juaracoding.TA.controller;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/29/2023 7:34 PM
@Last Modified 8/29/2023 7:34 PM
Version 1.0
*/

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.*;
import com.juaracoding.TA.model.Teacher;
import com.juaracoding.TA.service.SubjectService;
import com.juaracoding.TA.service.TeacherService;
import com.juaracoding.TA.utils.ConstantMessage;
import com.juaracoding.TA.utils.ManipulationMap;
import com.juaracoding.TA.utils.MappingAttribute;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/teacher")
public class TeacherController {
    private TeacherService teacherService;
    private SubjectService subjectService;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();
    private List<Teacher> lsCPUpload = new ArrayList<Teacher>();
    private String [] strExceptionArr = new String[2];
    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public TeacherController(TeacherService teacherService, SubjectService subjectService) {
        strExceptionArr[0] = "TeacherController";
        mapSorting();
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    private void mapSorting()
    {
        mapSorting.put("teacherId","teacherId");
        mapSorting.put("phoneNumber","phoneNumber");
        mapSorting.put("email","email");
    }

    @GetMapping("/v1/teachers")
    public String getAllData(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        Pageable pageable = PageRequest.of(0,5, Sort.by("teacherId"));
        objectMapper = teacherService.findAllTeacher(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper);

        model.addAttribute("teacher",new TeacherDTO());
        model.addAttribute("sortBy","teacherId");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "teacher/teachers";
    }

    @GetMapping("/v1/fbpsb/{page}/{sort}/{sortby}")
    public String findByTeacher(
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
        mappingAttribute.setAttribute(model,request);//untuk set session
        if(columnFirst.equals("id") && valueFirst.equals(""))
        {
            return "redirect:api/teacher/v1/teachers";
        }
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"teacherId":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = teacherService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("teacher",new TeacherDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "teacher/teachers";
    }

    @GetMapping("/v1/new")
    public String createTeacher(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        model.addAttribute("teacher", new TeacherDTO());
//        model.addAttribute("listSubject", subjectService.getAllSubject);
        return "teacher/create_teacher";
    }

    @PostMapping("/v1/new")
    public String newTeacher(@ModelAttribute(value = "teacher")
                             @Valid TeacherDTO teacherDTO
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
        mappingAttribute.setAttribute(model,request);//untuk set session

        /* START VALIDATION */
        if(bindingResult.hasErrors()) {
            model.addAttribute("teacher",teacherDTO);
            model.addAttribute("status","error");
            model.addAttribute("subjectList", subjectService.getAllSubject());//untuk parent nya

            return "teacher/create_teacher";
        }
        Boolean isValid = true;

        if(!isValid) {
            model.addAttribute("teacher",teacherDTO);
            model.addAttribute("subjectList", subjectService.getAllSubject());
            return "teacher/create_teacher";
        }
        /* END OF VALIDATION */

        Teacher teacher = modelMapper.map(teacherDTO, new TypeToken<Teacher>() {}.getType());
        objectMapper = teacherService.saveTeacher(teacher,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
//            return "teacher/teachers";
            return "redirect:api/teacher/v1/fbpsb/0/asc/teacherId?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("subjectList", subjectService.getAllSubject());
            model.addAttribute("teacher",new TeacherDTO());
            model.addAttribute("status","error");
            return "teacher/create_teacher";
        }
    }

    @GetMapping("/v1/edit/{id}")
    public String editTeacher(Model model, WebRequest request, @PathVariable("id") Long id) {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        objectMapper = teacherService.findById(id,request);
        mappingAttribute.setAttribute(model,objectMapper);

        TeacherDTO teacherDTO = (objectMapper.get("data")==null?null:(TeacherDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success"))
        {
            TeacherDTO teacherDTOForSelect = (TeacherDTO) objectMapper.get("data");
            mappingAttribute.setAttribute(model,objectMapper);//untuk set session
            List<SubjectDTO> subjectDTOList = subjectService.getAllSubject();

            List<SubjectDTO> selectedSubjectDTO = new ArrayList<SubjectDTO>();
            for (SubjectDTO subjectDTO:
                    subjectDTOList) {
                for (SubjectDTO subjectz:
                        teacherDTOForSelect.getSubjectTeacherList()) {
                    if(subjectDTO.getSubjectId()==subjectz.getSubjectId())
                    {
                        selectedSubjectDTO.add(subjectz);
                    }
                }
            }

            Set<Long> subjectSelected = selectedSubjectDTO.stream().map(SubjectDTO::getSubjectId).collect(Collectors.toSet());
            model.addAttribute("teacher", teacherDTOForSelect);
            model.addAttribute("subjectList", subjectDTOList);//untuk parent nya
            model.addAttribute("subjectSelected", subjectSelected);//untuk parent nya

//            model.addAttribute("teacher", teacherDTO);
            return "teacher/edit_teacher";
        }
        else
        {
            model.addAttribute("teacher", new TeacherDTO());
            return "redirect:/api/teacher/v1/teachers";
        }
    }

    @PostMapping("/v1/edit/{id}")
    public String editTeacher(@ModelAttribute(value = "teacher")
                              @Valid Teacher teacher
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        teacher.setTeacherId(id);//agar validasi bisa berjalan di form nya
        if(teacher.getSubjectTeacherList()==null)
        {
            mappingAttribute.setErrorMessage(bindingResult,"HARAP PILIH SUBJECT ");
        }
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session

        System.out.println("BINDING " + bindingResult);

        /* START VALIDATION */
        if(bindingResult.hasErrors())
        {
            model.addAttribute("teacher",teacher);
            model.addAttribute("subjectList", subjectService.getAllSubject());
            model.addAttribute("subjectSelected", new ArrayList<SubjectDTO>());
            return "teacher/edit_teacher";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("teacher",teacher);
            return "teacher/edit_teacher";
        }
        /* END OF VALIDATION */

//        Teacher teacher = modelMapper.map(teacher, new TypeToken<Teacher>() {}.getType());
        objectMapper = teacherService.updateTeacher(id,teacher,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/teacher/v1/teachers";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("teacher",new TeacherDTO());
            return "redirect:/api/teacher/v1/fbpsb/0/asc/teacherId?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("teacher",new TeacherDTO());
            model.addAttribute("subjectList", subjectService.getAllSubject());
            return "teacher/edit_teacher";
        }
    }

    @GetMapping("/v1/delete/{id}")
    public String deleteTeacher(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        objectMapper = teacherService.deleteTeacher(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set session
        model.addAttribute("teacher", new TeacherDTO());
        return "redirect:/api/teacher/v1/teachers";
    }

    @GetMapping("/v1/vsl/{id}")
    public String viewSubjectTeacher(Model model ,
                                WebRequest request,
                                @PathVariable("id")Long teacherId)//untuk menampilkan subject yang sudah dimapping ke akses
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        objectMapper = teacherService.findById(teacherId,request);
        TeacherDTO teacherDTO = (TeacherDTO) objectMapper.get("data");
        String[] arrColumnTitle = new String[2];
        arrColumnTitle[0] = "ID SUBJECT";
        arrColumnTitle[1] = "SUBJECT";

        List<SubjectDTO> subjectDTOList = teacherDTO.getSubjectTeacherList();
        int listSize = subjectDTOList.size();
        String[][] arrContent = new String[listSize][arrColumnTitle.length];

        for (int i=0;i<subjectDTOList.size();i++)
        {
//            MenuHeaderDTO mHeader = subjectDTOList.get(i).getMenuHeader();
            arrContent[i][0] = subjectDTOList.get(i).getSubjectId().toString();
            arrContent[i][1] = subjectDTOList.get(i).getSubject().toString();
//            arrContent[i][2] = mHeader==null?"":mHeader.getNamaMenuHeader();
        }
        model.addAttribute("arrColumnTitle",arrColumnTitle);
        model.addAttribute("arrContent",arrContent);
        model.addAttribute("title","List Subject Teacher "+teacherDTO.getName());

        return "globalview";
    }
}
