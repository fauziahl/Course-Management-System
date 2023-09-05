package com.juaracoding.TA.controller;

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.RelationRenderComponentDTO;
import com.juaracoding.TA.dto.RenderComponentDTO;
import com.juaracoding.TA.model.RenderComponent;
import com.juaracoding.TA.service.RelationRenderComponentService;
import com.juaracoding.TA.service.RenderComponentService;
import com.juaracoding.TA.utils.*;
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
@RequestMapping("/api/de")
public class RenderComponentController {

    private RenderComponentService renderComponentService;
    private RelationRenderComponentService relationRenderComponentService;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();
    private List<RenderComponent> lsCPUpload = new ArrayList<RenderComponent>();
    private String [] strExceptionArr = new String[2];
    private MappingAttribute mappingAttribute = new MappingAttribute();
    private PdfGeneratorLibre generator = null;
    private String [][] strBody = null;
    private String strNamaDivisi = "";
    private String strKodeDivisi = "";
    @Autowired
    PdfGenaratorUtil pdfGenaratorUtil;//wajib di deklarasikan kalau pakai thymeleaf engine

    private StringBuilder sBuild = new StringBuilder();

    @Autowired
    public RenderComponentController(RenderComponentService renderComponentService, RelationRenderComponentService relationRenderComponentService) {
        strExceptionArr[0] = "RenderComponentController";
        mapSorting();
        this.renderComponentService = renderComponentService;
        this.relationRenderComponentService = relationRenderComponentService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","renderComponentId");
        mapSorting.put("nama","renderComponentName");
    }

    @GetMapping("/v1/rendercomponent/new")
    public String createRenderComponent(Model model, WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        model.addAttribute("renderComponent", new RenderComponentDTO());
        model.addAttribute("listMenu", relationRenderComponentService.getAllRelRender());//untuk parent nya
        return "rendercomponent/create_render_component";
    }

    @GetMapping("/v1/rendercomponent/edit/{id}")
    public String editRenderComponent(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        objectMapper = renderComponentService.findById(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut

        if((Boolean) objectMapper.get("success"))
        {
            RenderComponentDTO renderComponentDTOForSelect = (RenderComponentDTO) objectMapper.get("data");
            List<RelationRenderComponentDTO> relationRenderComponentDTOS = relationRenderComponentService.getAllRelRender();

            List<RelationRenderComponentDTO> selectedRelationRenderComponentDTO = new ArrayList<RelationRenderComponentDTO>();
            for (RelationRenderComponentDTO relationRenderComponentDTO:
                    relationRenderComponentDTOS) {
                for (RelationRenderComponentDTO relationRenderComponentDTOz:
                        renderComponentDTOForSelect.getRelationRenderComponentList()) {
                    if(relationRenderComponentDTO.getRelationRenderComponentId()==relationRenderComponentDTOz.getRelationRenderComponentId())
                    {
                        selectedRelationRenderComponentDTO.add(relationRenderComponentDTOz);
                    }
                }
            }
            Set<Long> relationRenderComponentSelected = selectedRelationRenderComponentDTO.stream().map(RelationRenderComponentDTO::getRelationRenderComponentId).collect(Collectors.toSet());
            model.addAttribute("renderComponent", renderComponentDTOForSelect);
            model.addAttribute("listRelationRenderComponent", relationRenderComponentDTOS);//untuk parent nya
            model.addAttribute("menuSelected", relationRenderComponentSelected);//untuk parent nya
            return "rendercomponent/edit_render_component";
        }
        else
        {
            model.addAttribute("renderComponent", new RenderComponent());
            return "redirect:/api/de/v1/rendercomponent/default";
        }
    }
    @PostMapping("/v1/rendercomponent/new")
    public String newRenderComponent(@ModelAttribute(value = "renderComponent")
                           @Valid RenderComponent renderComponent
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
            model.addAttribute("renderComponent",renderComponent);
            model.addAttribute("status","error");
            model.addAttribute("listRelationRenderComponent", relationRenderComponentService.getAllRelRender());//untuk parent nya

            return "rendercomponent/create_render_component";
        }
        Boolean isValid = true;

        if(!isValid)
        {
            model.addAttribute("renderComponent",renderComponent);
            model.addAttribute("listRelationRenderComponent", relationRenderComponentService.getAllRelRender());//untuk parent nya
            return "rendercomponent/create_render_component";
        }
        /* END OF VALIDATION */

        RenderComponent renderComponentz = modelMapper.map(renderComponent, new TypeToken<RenderComponent>() {}.getType());
        objectMapper = renderComponentService.saveRenderComponent(renderComponentz,request);

        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session ke attribut
        if((Boolean) objectMapper.get("success"))
        {
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/de/v1/rendercomponent/fbpsb/0/asc/id?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("listRelationRenderComponent", relationRenderComponentService.getAllRelRender());//untuk parent nya
            model.addAttribute("renderComponent",new RenderComponentDTO());
            model.addAttribute("status","error");
            return "rendercomponent/create_render_component";
        }
    }

    @PostMapping("/v1/rendercomponent/edit/{id}")
    public String editRenderComponent(@ModelAttribute("renderComponent")
                            @Valid RenderComponent renderComponent
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        renderComponent.setRenderComponentId(id);
        if(renderComponent.getRelationRenderComponentList()==null)
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
            model.addAttribute("renderComponent",renderComponent);
            model.addAttribute("listRelationRenderComponent", relationRenderComponentService.getAllRelRender());//untuk parent nya
            model.addAttribute("relationRenderSelected", new ArrayList<RelationRenderComponentDTO>());//untuk parent nya
            return "rendercomponent/edit_render_component";
        }

        /* END OF VALIDATION */

        objectMapper = renderComponentService.updateRenderComponent(id,renderComponent,request);

        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success"))
        {
            mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut
            model.addAttribute("renderComponent",new RenderComponentDTO());
            return "redirect:/api/de/v1/rendercomponent/fbpsb/0/asc/id?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else
        {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("renderComponent",new RenderComponentDTO());
            model.addAttribute("listRelationRenderComponent", relationRenderComponentService.getAllRelRender());//untuk parent nya
            return "rendercomponent/edit_render_component";
        }
    }


    @GetMapping("/v1/rendercomponent/default")
    public String getDefaultData(Model model,WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        Pageable pageable = PageRequest.of(0,5, Sort.by("renderComponentId"));
        objectMapper = renderComponentService.findAllRenderComponent(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut;

        model.addAttribute("renderComponent",new RenderComponentDTO());
        model.addAttribute("sortBy","id");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/rendercomponent/render_component";
    }

    @GetMapping("/v1/rendercomponent/fbpsb/{page}/{sort}/{sortby}")
    public String findByRenderComponent(
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
            return "redirect:/api/de/v1/rendercomponent/default";
        }

        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"id":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = renderComponentService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut
        model.addAttribute("renderComponent",new RenderComponentDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/rendercomponent/render_component";
    }
    @GetMapping("/v1/rendercomponent/delete/{id}")
    public String deleteRenderComponent(Model model, WebRequest request, @PathVariable("id") Long id)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        objectMapper = renderComponentService.deleteRenderComponent(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set result ke attribut

        model.addAttribute("renderComponent",new RenderComponentDTO());
        return "redirect:/api/de/v1/rendercomponent/default";
    }

}