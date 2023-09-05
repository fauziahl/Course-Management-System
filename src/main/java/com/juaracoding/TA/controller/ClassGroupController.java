package com.juaracoding.TA.controller;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 11:43 AM
@Last Modified 8/30/2023 11:43 AM
Version 1.0
*/

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.ClassGroupDTO;
import com.juaracoding.TA.model.ClassGroup;
import com.juaracoding.TA.service.ClassGroupService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/classgroup")
public class ClassGroupController {
    private ClassGroupService classGroupService;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();
    private List<ClassGroup> lsCPUpload = new ArrayList<ClassGroup>();
    private String [] strExceptionArr = new String[2];
    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public ClassGroupController(ClassGroupService classGroupService) {
        strExceptionArr[0] = "ClassGroupController";
        mapSorting();
        this.classGroupService = classGroupService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","classGroupId");
        mapSorting.put("grade","grade");
        mapSorting.put("kode","classGroupCode");
    }

    @GetMapping("/v1/classgroup")
    public String getAllData(Model model,WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        Pageable pageable = PageRequest.of(0,5, Sort.by("classGroupId"));
        objectMapper = classGroupService.findAllClassGroup(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper);

        model.addAttribute("classgroup",new ClassGroupDTO());
        model.addAttribute("sortBy","classGroupId");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/classgroup/classgroup";
    }

    @GetMapping("/v1/fbpsb/{page}/{sort}/{sortby}")
    public String findByClassgroup(
            Model model,
            @PathVariable("page") Integer pagez,
            @PathVariable("sort") String sortz,
            @PathVariable("sortby") String sortzBy,
            @RequestParam String columnFirst,
            @RequestParam String valueFirst,
            @RequestParam String sizeComponent,
            WebRequest request
    ){
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        if(columnFirst.equals("id") && valueFirst.equals("")) {
            return "redirect:/api/classgroup/v1/classgroup";
        }
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"classGroupId":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = classGroupService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("classgroup",new ClassGroupDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/classgroup/classgroup";
    }

    @GetMapping("/v1/new")
    public String createClassGroup(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        model.addAttribute("classgroup", new ClassGroupDTO());
        return "classgroup/create_classgroup";
    }

    @PostMapping("/v1/new")
    public String newClassGroup(@ModelAttribute(value = "classgroup")
                            @Valid ClassGroupDTO classGroupDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
    )
    {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session

        /* START VALIDATION */
        if(bindingResult.hasErrors()) {
            model.addAttribute("classgroup",classGroupDTO);

            return "classgroup/create_classgroup";
        }
        Boolean isValid = true;

        if(!isValid) {
            model.addAttribute("classgroup",classGroupDTO);
            return "classgroup/create_classgroup";
        }
        /* END OF VALIDATION */

        ClassGroup classGroup = modelMapper.map(classGroupDTO, new TypeToken<ClassGroup>() {}.getType());
        objectMapper = classGroupService.saveClassGroup(classGroup,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/classgroup/v1/fbpsb/0/asc/classGroupId?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("classGroup",new ClassGroupDTO());
            model.addAttribute("status","error");
            return "classGroup/create_classgroup";
        }
    }

    @GetMapping("/v1/edit/{id}")
    public String editClassGroup(Model model, WebRequest request, @PathVariable("id") Long id) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        objectMapper = classGroupService.findById(id,request);
        ClassGroupDTO classGroupDTO = (objectMapper.get("data")==null?null:(ClassGroupDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);//untuk set session
            ClassGroupDTO classGroupDTOForSelect = (ClassGroupDTO) objectMapper.get("data");
            model.addAttribute("classgroup", classGroupDTO);
            return "classgroup/edit_classgroup";
        }
        else {
            model.addAttribute("classgroup", new ClassGroupDTO());
            return "redirect:/api/classgroup/v1/classgroup";
        }
    }

    @PostMapping("/v1/edit/{id}")
    public String editClassGroup(@ModelAttribute(value = "classgroup")
                             @Valid ClassGroupDTO classGroupDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        classGroupDTO.setClassGroupId(id);//agar validasi bisa berjalan di form nya
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session

        System.out.println("BINDING " + bindingResult);

        /* START VALIDATION */
        if(bindingResult.hasErrors()) {
            model.addAttribute("classgroup",classGroupDTO);
            return "classgroup/edit_classgroup";
        }
        Boolean isValid = true;

        if(!isValid) {
            model.addAttribute("classgroup",classGroupDTO);
            return "classgroup/edit_classgroup";
        }
        /* END OF VALIDATION */

        ClassGroup classGroup = modelMapper.map(classGroupDTO, new TypeToken<ClassGroup>() {}.getType());
        objectMapper = classGroupService.updateClassGroup(id,classGroup,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("classGroup",new ClassGroupDTO());
            return "redirect:/api/classgroup/v1/fbpsb/0/asc/classGroupId?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("classgroup",new ClassGroupDTO());
            return "classgroup/edit_classgroup";
        }
    }

    @GetMapping("/v1/delete/{id}")
    public String deleteClassGroup(Model model, WebRequest request, @PathVariable("id") Long id) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        objectMapper = classGroupService.deleteClassGroup(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set session
        model.addAttribute("classgroup", new ClassGroupDTO());
        return "redirect:/api/classgroup/v1/classgroup";
    }
}
