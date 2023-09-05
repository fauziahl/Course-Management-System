package com.juaracoding.TA.service;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/29/2023 5:30 PM
@Last Modified 8/29/2023 5:30 PM
Version 1.0
*/

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.TeacherDTO;
import com.juaracoding.TA.handler.ResourceNotFoundException;
import com.juaracoding.TA.handler.ResponseHandler;
import com.juaracoding.TA.model.Teacher;
import com.juaracoding.TA.repo.TeacherRepo;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class TeacherService {
/*
    MODUL 03
 */

    private TeacherRepo teacherRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public TeacherService(TeacherRepo teacherRepo) {
        strExceptionArr[0]="TeacherService";
        mapColumn();
        this.teacherRepo = teacherRepo;
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID TEACHER");
        mapColumnSearch.put("nama","NAMA TEACHER");
    }

    //001-010 SAVE
    public Map<String, Object> saveTeacher(Teacher teacher, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE; 
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV03001",request);
            }
            teacher.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            teacher.setCreatedDate(new Date());
            teacherRepo.save(teacher);
        } catch (Exception e) {
            strExceptionArr[1] = "saveTeacher(Teacher teacher, WebRequest request) --- LINE 79";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE03001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, teacher.getTeacherId(),mapColumnSearch),
                null, request);
    }

    //011-020 UPDATE
    public Map<String, Object> updateTeacher(Long teacherId, Teacher teacher, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Teacher nextTeacher = teacherRepo.findById(teacherId).orElseThrow(
                    ()->null
            );

            if(nextTeacher==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV03011",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV03012",request);
            }
            nextTeacher.setName(teacher.getName());
            nextTeacher.setPhoneNumber(teacher.getPhoneNumber());
            nextTeacher.setEmail(teacher.getEmail());
            nextTeacher.setSubjectTeacherList(teacher.getSubjectTeacherList());
            nextTeacher.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextTeacher.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "updateTeacher(Long teacherId, Teacher teacher, WebRequest request) --- LINE 128";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE03011", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }
    
    //021-030 DELETE
    public Map<String, Object> deleteTeacher(Long teacherId, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Teacher nextTeacher = null;
        try {
            nextTeacher = teacherRepo.findById(teacherId).orElseThrow(
                    ()->null
            );

            if(nextTeacher==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV05006",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV03007",request);
            }
            nextTeacher.setIsDelete((byte)0);
            nextTeacher.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextTeacher.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "deleteTeacher(Long teacherId, WebRequest request) --- LINE 165";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE03007", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    //031-040 FINDALL
    public Map<String,Object> findAllTeacher(Pageable pageable, WebRequest request) {
        List<TeacherDTO> teacherDTOList = null;
        Map<String,Object> mapResult = null;
        Page<Teacher> teacherPage = null;
        List<Teacher> teacherList = null;

        try {
            teacherPage = teacherRepo.findByIsDelete(pageable,(byte)1);
            teacherList = teacherPage.getContent();
            if(teacherList.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV03031",
                                request);
            }
            teacherDTOList = modelMapper.map(teacherList, new TypeToken<List<TeacherDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,teacherDTOList,teacherPage,mapColumnSearch);
        }
        catch (Exception e) {
            strExceptionArr[1] = "findAllTeacher(Pageable pageable, WebRequest request) --- LINE 205";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE03031", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

//    public Map<String,Object> findAllDivisi()//KHUSUS UNTUK FORM INPUT SAJA
//    {
//        List<DivisiDTO> listDivisiDTO = null;
//        Map<String,Object> mapResult = null;
//        List<Divisi> listDivisi = null;
//
//        try
//        {
//            listDivisi = teacherRepo.findByIsDelete((byte)1);
//            if(listDivisi.size()==0)
//            {
//                return new ResponseHandler().
//                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
//                                HttpStatus.OK,
//                                null,
//                                null,
//                                null);
//            }
//            listDivisiDTO = modelMapper.map(listDivisi, new TypeToken<List<DivisiDTO>>() {}.getType());
//        }
//        catch (Exception e)
//        {
//            strExceptionArr[1] = "findAllDivisi() --- LINE 304";
//            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
//            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
//                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE05006", null);
//        }
//
//
//
//        return new ResponseHandler().
//                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
//                        HttpStatus.OK,
//                        listDivisiDTO,
//                        null,
//                        null);
//    }

    //041-050 FINDBYID
    public Map<String,Object> findById(Long teacherId, WebRequest request) {
        Teacher teacher = teacherRepo.findById(teacherId).orElseThrow (
                ()-> null
        );
        if(teacher == null) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV03041",request);
        }
        TeacherDTO teacherDTO = modelMapper.map(teacher, new TypeToken<TeacherDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        teacherDTO,
                        null,
                        request);
    }

    //051-060 FINDBYPAGE
    public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) {
        Page<Teacher> teacherPage = null;
        List<Teacher> teacherList = null;
        List<TeacherDTO> teacherDTOList = null;
        Map<String,Object> mapResult = null;

        try {
            if(columFirst.equals("id")) {
                if(!valueFirst.equals("") && valueFirst!=null) {
                    try {
                        Long.parseLong(valueFirst);
                    }
                    catch (Exception e) {
                        strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 233";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                        return new ResponseHandler().
                                generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                        HttpStatus.OK,
                                        transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                        "FE03051",
                                        request);
                    }
                }
            }
            teacherPage = getDataByValue(pageable,columFirst,valueFirst);
            teacherList = teacherPage.getContent();
            if(teacherList.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV05006",
                                request);
            }
            teacherDTOList = modelMapper.map(teacherList, new TypeToken<List<TeacherDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,teacherDTOList,teacherPage,mapColumnSearch);
        }

        catch (Exception e) {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 260";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE03052", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    //061-070 GETDATA
    private Page<Teacher> getDataByValue(Pageable pageable, String paramColumn, String paramValue) {
        if(paramValue.equals("") || paramValue==null) {
            return teacherRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id")) {
            return teacherRepo.findByIsDeleteAndTeacherId(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return teacherRepo.findByIsDeleteAndNameContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }
        return teacherRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }

    //071-080 GETALL
    public List<TeacherDTO> getAllTeacher()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<TeacherDTO> teacherDTOList = null;
        Map<String,Object> mapResult = null;
        List<Teacher> teacherList = null;

        try {
            teacherList = teacherRepo.findByIsDelete((byte)1);
            if(teacherList.size()==0) {
                return new ArrayList<TeacherDTO>();
            }
            teacherDTOList = modelMapper.map(teacherList, new TypeToken<List<TeacherDTO>>() {}.getType());
        }
        catch (Exception e) {
            strExceptionArr[1] = "findAllTeacher() --- LINE 363";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return teacherDTOList;
        }
        return teacherDTOList;
    }
}
