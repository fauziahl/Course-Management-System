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
import com.juaracoding.TA.dto.SubjectDTO;
import com.juaracoding.TA.model.Subject;
import com.juaracoding.TA.service.SubjectService;
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
@RequestMapping("/api/subject")
public class SubjectController {
    private SubjectService subjectService;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();
    private List<Subject> lsCPUpload = new ArrayList<Subject>();
    private String [] strExceptionArr = new String[2];
    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public SubjectController(SubjectService subjectService) {
        strExceptionArr[0] = "SubjectController";
        mapSorting();
        this.subjectService = subjectService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","subjectId");
        mapSorting.put("subject","Subject");
    }

    @GetMapping("/v1/subject")
    public String getAllData(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        Pageable pageable = PageRequest.of(0,5, Sort.by("subjectId"));
        objectMapper = subjectService.findAllSubject(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper);

        model.addAttribute("subject",new SubjectDTO());
        model.addAttribute("sortBy","subjectId");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/subject/subject";
    }

    @GetMapping("/v1/fbpsb/{page}/{sort}/{sortby}")
    public String findBySubject(
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
            return "redirect:/api/subject/v1/subject";
        }
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"subjectId":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = subjectService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("subject",new SubjectDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/subject/subject";
    }

    @GetMapping("/v1/new")
    public String createSubject(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        model.addAttribute("subject", new SubjectDTO());
        return "subject/create_subject";
    }

    @PostMapping("/v1/new")
    public String newSubject(@ModelAttribute(value = "subject")
                                @Valid SubjectDTO subjectDTO
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
            model.addAttribute("subject",subjectDTO);

            return "subject/create_subject";
        }
        Boolean isValid = true;

        if(!isValid) {
            model.addAttribute("subject",subjectDTO);
            return "subject/create_subject";
        }
        /* END OF VALIDATION */

        Subject subject = modelMapper.map(subjectDTO, new TypeToken<Subject>() {}.getType());
        objectMapper = subjectService.saveSubject(subject,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/subject/v1/fbpsb/0/asc/subjectId?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("subject",new SubjectDTO());
            model.addAttribute("status","error");
            return "subject/create_subject";
        }
    }

    @GetMapping("/v1/edit/{id}")
    public String editSubject(Model model, WebRequest request, @PathVariable("id") Long id) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        objectMapper = subjectService.findById(id,request);
        SubjectDTO subjectDTO = (objectMapper.get("data")==null?null:(SubjectDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);//untuk set session
            SubjectDTO subjectDTOForSelect = (SubjectDTO) objectMapper.get("data");
            model.addAttribute("subject", subjectDTO);
            return "subject/edit_subject";
        }
        else {
            model.addAttribute("subject", new SubjectDTO());
            return "redirect:/api/subject/v1/subject";
        }
    }

    @PostMapping("/v1/edit/{id}")
    public String editSubject(@ModelAttribute(value = "subject")
                                 @Valid SubjectDTO subjectDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        subjectDTO.setSubjectId(id);//agar validasi bisa berjalan di form nya
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session

        System.out.println("BINDING " + bindingResult);

        /* START VALIDATION */
        if(bindingResult.hasErrors()) {
            model.addAttribute("subject",subjectDTO);
            return "subject/edit_subject";
        }
        Boolean isValid = true;

        if(!isValid) {
            model.addAttribute("subject",subjectDTO);
            return "subject/edit_subject";
        }
        /* END OF VALIDATION */

        Subject subject = modelMapper.map(subjectDTO, new TypeToken<Subject>() {}.getType());
        objectMapper = subjectService.updateSubject(id,subject,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("subject",new SubjectDTO());
            return "redirect:/api/subject/v1/fbpsb/0/asc/subjectId?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("subject",new SubjectDTO());
            return "subject/edit_subject";
        }
    }

    @GetMapping("/v1/delete/{id}")
    public String deleteSubject(Model model, WebRequest request, @PathVariable("id") Long id) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        objectMapper = subjectService.deleteSubject(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set session
        model.addAttribute("subject", new SubjectDTO());
        return "redirect:/api/subject/v1/subject";
    }
}
