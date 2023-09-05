package com.juaracoding.TA.service;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 9/1/2023 2:14 AM
@Last Modified 9/1/2023 2:14 AM
Version 1.0
*/

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.ScheduleDTO;
import com.juaracoding.TA.handler.ResponseHandler;
import com.juaracoding.TA.model.Schedule;
import com.juaracoding.TA.repo.ScheduleRepo;
import com.juaracoding.TA.utils.ConstantMessage;
import com.juaracoding.TA.utils.LoggingFile;
import com.juaracoding.TA.utils.TransformToDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@Service
@Transactional
public class ScheduleService {
/*
    MODUL 08
 */

    private ScheduleRepo scheduleRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private List<Schedule> lsCPUpload = new ArrayList<Schedule>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public ScheduleService(ScheduleRepo scheduleRepo) {
        strExceptionArr[0]="ScheduleService";
        mapColumn();
        this.scheduleRepo = scheduleRepo;
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID SCHEDULE");
        mapColumnSearch.put("session","SESSION DATE");
        mapColumnSearch.put("start","START TIME");
        mapColumnSearch.put("end","END TIME");
        mapColumnSearch.put("classGroup","CLASS GROUP");
        mapColumnSearch.put("subject","SUBJECT");
        mapColumnSearch.put("teacher","TEACHER");
        mapColumnSearch.put("classroom","CLASSROOM");
        mapColumnSearch.put("status","STATUS");
    }

    //001-010 SAVE
    public Map<String, Object> saveSchedule(Schedule schedule, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);


        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV08001",request);
            }
            schedule.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            schedule.setCreatedDate(new Date());
            scheduleRepo.save(schedule);
        } catch (Exception e) {
            strExceptionArr[1] = "saveSchedule(Schedule schedule, WebRequest request) --- LINE 88";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE08001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, schedule.getScheduleId(),mapColumnSearch),
                null, request);
    }

    //011-020 UPDATE
    public Map<String, Object> updateSchedule(Long scheduleId, Schedule schedule, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Schedule nextSchedule = scheduleRepo.findById(scheduleId).orElseThrow(
                    ()->null
            );

            if(nextSchedule==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV08011",request);
            }
            if(strUserIdz==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV08012",request);
            }
            nextSchedule.setSessionDate(schedule.getSessionDate());
            nextSchedule.setStartTime(schedule.getStartTime());
            nextSchedule.setEndTime(schedule.getEndTime());
            nextSchedule.setClassGroup(schedule.getClassGroup());
            nextSchedule.setSubject(schedule.getSubject());
            nextSchedule.setTeacher(schedule.getTeacher());
            nextSchedule.setClassroom(schedule.getClassroom());
            nextSchedule.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextSchedule.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "updateSchedule(Long scheduleId, Schedule schedule, WebRequest request) --- LINE 134";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE08011", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    //021-030 DELETE
    public Map<String, Object> deleteSchedule(Long scheduleId, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Schedule nextSchedule = null;
        try {
            nextSchedule = scheduleRepo.findById(scheduleId).orElseThrow(
                    ()->null
            );

            if(nextSchedule==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV08021",request);
            }
            if(strUserIdz==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV08022",request);
            }
            nextSchedule.setIsDelete((byte)0);
            nextSchedule.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextSchedule.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "deleteSchedule(Long scheduleId, WebRequest request) --- LINE 174";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE08021", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    //031-040 FINDALL
    public Map<String,Object> findAllSchedule(Pageable pageable, WebRequest request) {
        List<ScheduleDTO> scheduleDTOList = null;
        Map<String,Object> mapResult = null;
        Page<Schedule> schedulePage = null;
        List<Schedule> scheduleList = null;

        try {
            schedulePage = scheduleRepo.findByIsDelete(pageable,(byte)1);
            scheduleList = schedulePage.getContent();
            if(scheduleList.size()==0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV08031",
                                request);
            }
            scheduleDTOList = modelMapper.map(scheduleList, new TypeToken<List<ScheduleDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,scheduleDTOList,schedulePage,mapColumnSearch);
        }
        catch (Exception e) {
            strExceptionArr[1] = "findAllSchedule(Pageable pageable, WebRequest request) --- LINE 210";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE08031", request);
        }

        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

    //041-050 FINDBYID
    public Map<String,Object> findById(Long scheduleId, WebRequest request) {
        Schedule schedule = scheduleRepo.findById(scheduleId).orElseThrow (
                ()-> null
        );
        if(schedule == null) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV08041",request);
        }
        ScheduleDTO scheduleDTO = modelMapper.map(schedule, new TypeToken<ScheduleDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        scheduleDTO,
                        null,
                        request);
    }

    //051-060 FINDBYPAGE
    public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) {
        Page<Schedule> schedulePage = null;
        List<Schedule> scheduleList = null;
        List<ScheduleDTO> scheduleDTOList = null;
        Map<String,Object> mapResult = null;

        try {
            if(columFirst.equals("id")) {
                if(!valueFirst.equals("") && valueFirst!=null) {
                    try {
                        Long.parseLong(valueFirst);
                    }
                    catch (Exception e) {
                        strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 260";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                        return new ResponseHandler().
                                generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                        HttpStatus.OK,
                                        transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                        "FE08051",
                                        request);
                    }
                }
            }
            schedulePage = getDataByValue(pageable,columFirst,valueFirst);
            scheduleList = schedulePage.getContent();
            if(scheduleList.size()==0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV08051",
                                request);
            }
            scheduleDTOList = modelMapper.map(scheduleList, new TypeToken<List<ScheduleDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,scheduleDTOList,schedulePage,mapColumnSearch);
        }

        catch (Exception e) {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 286";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE08005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    //061-070 GETDATA
    private Page<Schedule> getDataByValue(Pageable pageable, String paramColumn, String paramValue) {
        if(paramValue.equals("") || paramValue==null) {
            return scheduleRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id")) {
            return scheduleRepo.findByIsDeleteAndScheduleId(pageable,(byte) 1,Long.parseLong(paramValue));
        } //untuk searching
        return scheduleRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }

    //071-080 GETALL
    public List<ScheduleDTO> getAllSchedule(){//KHUSUS UNTUK FORM INPUT SAJA{
        List<ScheduleDTO> scheduleDTOList = null;
        List<Schedule> scheduleList = null;
        try{
            scheduleList = scheduleRepo.findByIsDelete((byte)1);
            if(scheduleList.size()==0) {
                return new ArrayList<ScheduleDTO>();
            }
            scheduleDTOList = modelMapper.map(scheduleList, new TypeToken<List<ScheduleDTO>>() {}.getType());
        }
        catch (Exception e){
            strExceptionArr[1] = "getAllDemo() ---> LINE 324";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return scheduleDTOList;
        }
        return scheduleDTOList;
    }

}
