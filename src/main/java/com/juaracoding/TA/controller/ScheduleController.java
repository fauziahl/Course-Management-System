package com.juaracoding.TA.controller;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 9/1/2023 2:41 AM
@Last Modified 9/1/2023 2:41 AM
Version 1.0
*/

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.*;
import com.juaracoding.TA.model.Schedule;
import com.juaracoding.TA.model.Subject;
import com.juaracoding.TA.model.Teacher;
import com.juaracoding.TA.service.*;
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
@RequestMapping("/api/schedule")
public class ScheduleController {
    private ScheduleService scheduleService;
    private ClassGroupService classGroupService;
    private SubjectService subjectService;
    private TeacherService teacherService;
    private ClassroomService classroomService;
    @Autowired
    private ModelMapper modelMapper;
    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private Map<String,String> mapSorting = new HashMap<String,String>();
    private List<Schedule> lsCPUpload = new ArrayList<Schedule>();
    private String [] strExceptionArr = new String[2];
    private MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public ScheduleController(ScheduleService scheduleService,
                              ClassGroupService classGroupService,
                              SubjectService subjectService,
                              TeacherService teacherService,
                              ClassroomService classroomService) {
        strExceptionArr[0] = "ScheduleController";
        mapSorting();
        this.scheduleService = scheduleService;
        this.classGroupService = classGroupService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
    }

    private void mapSorting()
    {
        mapSorting.put("id","scheduleId");
        mapSorting.put("id","ID SCHEDULE");
        mapSorting.put("session","SESSION DATE");
        mapSorting.put("start","START TIME");
        mapSorting.put("end","END TIME");
        mapSorting.put("classGroup","CLASS GROUP");
        mapSorting.put("subject","SUBJECT");
        mapSorting.put("teacher","TEACHER");
        mapSorting.put("classroom","CLASSROOM");
        mapSorting.put("status","STATUS");
    }

    @GetMapping("/v1/schedule")
    public String getAllData(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        Pageable pageable = PageRequest.of(0,5, Sort.by("scheduleId"));
        objectMapper = scheduleService.findAllSchedule(pageable,request);
        mappingAttribute.setAttribute(model,objectMapper);

        model.addAttribute("schedule",new ScheduleDTO());
        model.addAttribute("sortBy","scheduleId");
        model.addAttribute("currentPage",1);
        model.addAttribute("asc","asc");
        model.addAttribute("columnFirst","");
        model.addAttribute("valueFirst","");
        model.addAttribute("sizeComponent",5);
        return "/schedule/schedule";
    }

    @GetMapping("/v1/fbpsb/{page}/{sort}/{sortby}")
    public String findBySchedule(
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
            return "redirect:/api/schedule/v1/schedule";
        }
        sortzBy = mapSorting.get(sortzBy);
        sortzBy = sortzBy==null?"scheduleId":sortzBy;
        Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
        objectMapper = scheduleService.findByPage(pageable,request,columnFirst,valueFirst);
        mappingAttribute.setAttribute(model,objectMapper,request);
        model.addAttribute("schedule",new ScheduleDTO());
        model.addAttribute("currentPage",pagez==0?1:pagez);
        model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
        model.addAttribute("columnFirst",columnFirst);
        model.addAttribute("valueFirst",valueFirst);
        model.addAttribute("sizeComponent",sizeComponent);

        return "/schedule/schedule";
    }

    @GetMapping("/v1/new")
    public String createschedule(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        model.addAttribute("schedule", new ScheduleDTO());
        model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
        model.addAttribute("listSubject", subjectService.getAllSubject());
        model.addAttribute("listTeacher", teacherService.getAllTeacher());
        model.addAttribute("listClassroom", classroomService.getAllClassroom());
        return "schedule/create_schedule";
    }

    @PostMapping("/v1/new")
    public String newSchedule(@ModelAttribute(value = "schedule")
                                @Valid ScheduleDTO scheduleDTO
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

        ClassGroupDTO classGroupDTO = scheduleDTO.getClassGroup();
        String strIdClassGroup = classGroupDTO==null?null:String.valueOf(classGroupDTO.getClassGroupId());
        if(strIdClassGroup==null || strIdClassGroup.equals("null") || strIdClassGroup.equals("")) {
            mappingAttribute.setErrorMessage(bindingResult,"CLASS GROUP WAJIB DIPILIH !!");
        }

        SubjectDTO subjectDTO = scheduleDTO.getSubject();
        String strIdSubject = subjectDTO==null?null:String.valueOf(subjectDTO.getSubjectId());
        if(strIdSubject==null || strIdSubject.equals("null") || strIdSubject.equals("")) {
            mappingAttribute.setErrorMessage(bindingResult,"SUBJECT WAJIB DIPILIH !!");
        }


        TeacherDTO teacherDTO = scheduleDTO.getTeacher();
        String strIdTeacher = teacherDTO==null?null:String.valueOf(teacherDTO.getTeacherId());
        if(strIdTeacher==null || strIdTeacher.equals("null") || strIdTeacher.equals("")) {
            mappingAttribute.setErrorMessage(bindingResult,"TEACHER WAJIB DIPILIH !!");
        }

        ClassroomDTO classroomDTO = scheduleDTO.getClassroom();
        String strIdClassroom = classroomDTO==null?null:String.valueOf(classroomDTO.getClassroomId());
        if(strIdClassroom==null || strIdClassroom.equals("null") || strIdClassroom.equals("")) {
            mappingAttribute.setErrorMessage(bindingResult,"CLASSROOM WAJIB DIPILIH !!");
        }

        /* START VALIDATION */
        if(bindingResult.hasErrors()) {
            model.addAttribute("schedule",scheduleDTO);
            model.addAttribute("status","error");
            model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
            model.addAttribute("listSubject", subjectService.getAllSubject());
            model.addAttribute("listTeacher", teacherService.getAllTeacher());
            model.addAttribute("listClasssroom", classroomService.getAllClassroom());

            return "schedule/create_schedule";
        }
        Boolean isValid = true;

        if(!isValid) {
            model.addAttribute("schedule",scheduleDTO);
            model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
            model.addAttribute("listSubject", subjectService.getAllSubject());
            model.addAttribute("listTeacher", teacherService.getAllTeacher());
            model.addAttribute("listClasssroom", classroomService.getAllClassroom());
            return "schedule/create_schedule";
        }
        /* END OF VALIDATION */

        Schedule schedule = modelMapper.map(scheduleDTO, new TypeToken<Schedule>() {}.getType());
        objectMapper = scheduleService.saveSchedule(schedule,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("message","DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/schedule/v1/fbpsb/0/asc/scheduleId?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("schedule",new ScheduleDTO());
            model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
            model.addAttribute("listSubject", subjectService.getAllSubject());
            model.addAttribute("listTeacher", teacherService.getAllTeacher());
            model.addAttribute("listClasssroom", classroomService.getAllClassroom());
            model.addAttribute("status","error");
            return "schedule/create_schedule";
        }
    }

    @GetMapping("/v1/edit/{id}")
    public String editSchedule(Model model, WebRequest request, @PathVariable("id") Long id) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session

        objectMapper = scheduleService.findById(id,request);
        ScheduleDTO scheduleDTO = (objectMapper.get("data")==null?null:(ScheduleDTO) objectMapper.get("data"));
        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);//untuk set session
            ScheduleDTO scheduleDTOForSelect = (ScheduleDTO) objectMapper.get("data");
            model.addAttribute("schedule", scheduleDTO);
            model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
            model.addAttribute("listSubject", subjectService.getAllSubject());
            model.addAttribute("listTeacher", teacherService.getAllTeacher());
            model.addAttribute("listClasssroom", classroomService.getAllClassroom());

            ClassGroupDTO classGroupDTO = scheduleDTOForSelect.getClassGroup();
            String strSelectedGroup = classGroupDTO==null?"null":classGroupDTO.getClassGroupId().toString();
            model.addAttribute("selectedGroup", strSelectedGroup);

            SubjectDTO subjectDTO = scheduleDTOForSelect.getSubject();
            String strSelectedSubject = subjectDTO==null?"null":subjectDTO.getSubjectId().toString();
            model.addAttribute("selectedSubject", strSelectedSubject);

            TeacherDTO teacherDTO = scheduleDTOForSelect.getTeacher();
            String strSelectedTeacher = teacherDTO==null?"null":teacherDTO.getTeacherId().toString();
            model.addAttribute("selectedTeacher", strSelectedTeacher);

            ClassroomDTO classroomDTO = scheduleDTOForSelect.getClassroom();
            String strSelectedRoom = classroomDTO==null?"null":classroomDTO.getClassroomId().toString();
            model.addAttribute("selectedRoom", strSelectedRoom);

            return "schedule/edit_schedule";
        }
        else {
            model.addAttribute("schedule", new ScheduleDTO());
            return "redirect:/api/schedule/v1/schedule";
        }
    }

    @PostMapping("/v1/edit/{id}")
    public String editSchedule(@ModelAttribute(value = "schedule")
                                 @Valid ScheduleDTO scheduleDTO
            , BindingResult bindingResult
            , Model model
            , WebRequest request
            , @PathVariable("id") Long id
    )
    {
        scheduleDTO.setScheduleId(id);//agar validasi bisa berjalan di form nya
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session

        scheduleDTO.setScheduleId(id);

        ClassGroupDTO classGroupDTO = scheduleDTO.getClassGroup();
        String strIdClassGroup = classGroupDTO==null?null:String.valueOf(classGroupDTO.getClassGroupId());
        if(strIdClassGroup==null || strIdClassGroup.equals("null") || strIdClassGroup.equals(""))
        {
            mappingAttribute.setErrorMessage(bindingResult,"CLASS GROUP WAJIB DIPILIH !!");
        }

        SubjectDTO subjectDTO = scheduleDTO.getSubject();
        String strIdSubject = subjectDTO==null?null:String.valueOf(subjectDTO.getSubjectId());
        if(strIdSubject==null || strIdSubject.equals("null") || strIdSubject.equals(""))
        {
            mappingAttribute.setErrorMessage(bindingResult,"SUBJECT WAJIB DIPILIH !!");
        }

        TeacherDTO teacherDTO = scheduleDTO.getTeacher();
        String strIdTeacher = teacherDTO==null?null:String.valueOf(teacherDTO.getTeacherId());
        if(strIdTeacher==null || strIdTeacher.equals("null") || strIdTeacher.equals(""))
        {
            mappingAttribute.setErrorMessage(bindingResult,"TEACHER WAJIB DIPILIH !!");
        }

        ClassroomDTO classroomDTO = scheduleDTO.getClassroom();
        String strIdClassroom = classroomDTO==null?null:String.valueOf(classroomDTO.getClassroomId());
        if(strIdClassroom==null || strIdClassroom.equals("null") || strIdClassroom.equals(""))
        {
            mappingAttribute.setErrorMessage(bindingResult,"CLASSROOM WAJIB DIPILIH !!");
        }

        System.out.println("BINDING " + bindingResult);

        /* START VALIDATION */
        if(bindingResult.hasErrors()) {
            model.addAttribute("schedule",scheduleDTO);
            model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
            model.addAttribute("listSubject", subjectService.getAllSubject());
            model.addAttribute("listTeacher", teacherService.getAllTeacher());
            model.addAttribute("listClasssroom", classroomService.getAllClassroom());
            return "schedule/edit_schedule";
        }
        Boolean isValid = true;

        if(!isValid) {
            model.addAttribute("schedule",scheduleDTO);
            model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
            model.addAttribute("listSubject", subjectService.getAllSubject());
            model.addAttribute("listTeacher", teacherService.getAllTeacher());
            model.addAttribute("listClasssroom", classroomService.getAllClassroom());
            return "schedule/edit_schedulep";
        }
        /* END OF VALIDATION */

        Schedule schedule = modelMapper.map(scheduleDTO, new TypeToken<Schedule>() {}.getType());
        objectMapper = scheduleService.updateSchedule(id,schedule,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

        if((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model,objectMapper);
            model.addAttribute("schedule",new ScheduleDTO());
            return "redirect:/api/schedule/v1/fbpsb/0/asc/scheduleId?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
        }
        else {
            mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
            model.addAttribute("schedule",new ScheduleDTO());
            model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
            model.addAttribute("listSubject", subjectService.getAllSubject());
            model.addAttribute("listTeacher", teacherService.getAllTeacher());
            model.addAttribute("listClasssroom", classroomService.getAllClassroom());
            return "schedule/edit_schedule";
        }
    }

    @GetMapping("/v1/delete/{id}")
    public String deleteSchedule(Model model, WebRequest request, @PathVariable("id") Long id) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
        objectMapper = scheduleService.deleteSchedule(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set session
        model.addAttribute("schedule", new ScheduleDTO());
        return "redirect:/api/schedule/v1/schedule";
    }
}
