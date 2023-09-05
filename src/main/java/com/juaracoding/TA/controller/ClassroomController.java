package com.juaracoding.TA.controller;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 10:25 PM
@Last Modified 8/30/2023 10:25 PM
Version 1.0
*/

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.ClassroomDTO;
import com.juaracoding.TA.model.Classroom;
import com.juaracoding.TA.service.ClassroomService;
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
@RequestMapping("/api/classroom")
public class ClassroomController {
    private ClassroomService classroomService;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();
    private List<Classroom> lsCPUpload = new ArrayList<Classroom>();
    private String [] strExceptionArr = new String[2];
    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public ClassroomController(ClassroomService classroomService) {
        strExceptionArr[0] = "ClassroomController";
        mapSorting();
        this.classroomService = classroomService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","classroomId");
        mapSorting.put("kode","classroom");
    }

    @GetMapping("/v1/classrooms")
    public String getAllData(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        Pageable pageable = PageRequest.of(0,5, Sort.by("classroomId"));
        objectMapper = classroomService.findAllClassroom(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper);

        model.addAttribute("classroom",new ClassroomDTO());
        model.addAttribute("sortBy","classroomId");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/classroom/classrooms";
    }

    @GetMapping("/v1/fbpsb/{page}/{sort}/{sortby}")
    public String findByClassroom(
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
            return "redirect:/api/classroom/v1/classrooms";
        }
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"classroomId":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = classroomService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("classroom",new ClassroomDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/classroom/classrooms";
    }

    @GetMapping("/v1/new")
    public String createClassroom(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        model.addAttribute("classroom", new ClassroomDTO());
        return "classroom/create_classroom";
    }

    @PostMapping("/v1/new")
    public String newClassroom(@ModelAttribute(value = "classroom")
                                @Valid ClassroomDTO classroomDTO
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
            model.addAttribute("classroom",classroomDTO);

            return "classroom/create_classroom";
        }
        Boolean isValid = true;

        if(!isValid) {
            model.addAttribute("classroom",classroomDTO);
            return "classroom/create_classroom";
        }
        /* END OF VALIDATION */

        Classroom classroom = modelMapper.map(classroomDTO, new TypeToken<Classroom>() {}.getType());
        objectMapper = classroomService.saveClassroom(classroom,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/classroom/v1/fbpsb/0/asc/classroomId?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("classroom",new ClassroomDTO());
            model.addAttribute("status","error");
            return "classroom/create_classroom";
        }
    }

    @GetMapping("/v1/edit/{id}")
    public String editClassroom(Model model, WebRequest request, @PathVariable("id") Long id) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        objectMapper = classroomService.findById(id,request);
        ClassroomDTO classroomDTO = (objectMapper.get("data")==null?null:(ClassroomDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);//untuk set session
            ClassroomDTO classroomDTOForSelect = (ClassroomDTO) objectMapper.get("data");
            model.addAttribute("classroom", classroomDTO);
            return "classroom/edit_classroom";
        }
        else {
            model.addAttribute("classroom", new ClassroomDTO());
            return "redirect:/api/classroom/v1/classrooms";
        }
    }

    @PostMapping("/v1/edit/{id}")
    public String editClassroom(@ModelAttribute(value = "classroom")
                                 @Valid ClassroomDTO classroomDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        classroomDTO.setClassroomId(id);//agar validasi bisa berjalan di form nya
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session

        System.out.println("BINDING " + bindingResult);

        /* START VALIDATION */
        if(bindingResult.hasErrors()) {
            model.addAttribute("classroom",classroomDTO);
            return "classroom/edit_classroom";
        }
        Boolean isValid = true;

        if(!isValid) {
            model.addAttribute("classroom",classroomDTO);
            return "classroom/edit_classroom";
        }
        /* END OF VALIDATION */

        Classroom classroom = modelMapper.map(classroomDTO, new TypeToken<Classroom>() {}.getType());
        objectMapper = classroomService.updateClassroom(id,classroom,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("classroom",new ClassroomDTO());
            return "redirect:/api/classroom/v1/fbpsb/0/asc/classroomId?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("classroom",new ClassroomDTO());
            return "classroom/edit_classroom";
        }
    }

    @GetMapping("/v1/delete/{id}")
    public String deleteClassroom(Model model, WebRequest request, @PathVariable("id") Long id) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        objectMapper = classroomService.deleteClassroom(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set session
        model.addAttribute("classroom", new ClassroomDTO());
        return "redirect:/api/classroom/v1/classrooms";
    }
}
