package com.juaracoding.TA.service;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 10:07 PM
@Last Modified 8/30/2023 10:07 PM
Version 1.0
*/

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.ClassroomDTO;
import com.juaracoding.TA.handler.ResponseHandler;
import com.juaracoding.TA.model.Classroom;
import com.juaracoding.TA.repo.ClassroomRepo;
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
public class ClassroomService {
/*
    MODUL 05
 */

    private ClassroomRepo classroomRepo;
    private String[] strExceptionArr = new String[2];

    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public ClassroomService(ClassroomRepo classroomRepo) {
        strExceptionArr[0]="ClassroomService";
        mapColumn();
        this.classroomRepo = classroomRepo;
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID CLASSROOM");
        mapColumnSearch.put("kode","KODE CLASSROOM");
    }

    //001-010 SAVE
    public Map<String, Object> saveClassroom(Classroom classroom, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV05001",request);
            }
            classroom.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            classroom.setCreatedDate(new Date());
            classroomRepo.save(classroom);
        } catch (Exception e) {
            strExceptionArr[1] = "saveClassroom(Classroom classroom, WebRequest request) --- LINE 82";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE05001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, classroom.getClassroomId(),mapColumnSearch),
                null, request);
    }

    //011-020 UPDATE
    public Map<String, Object> updateClassroom(Long classroomId, Classroom classroom, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Classroom nextClassroom = classroomRepo.findById(classroomId).orElseThrow(
                    ()->null
            );

            if(nextClassroom==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV05011",request);
            }
            if(strUserIdz==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV05012",request);
            }
            nextClassroom.setClassroom(classroom.getClassroom());
            nextClassroom.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextClassroom.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updateClassroom(Long classroomId, Classroom classroom, WebRequest request) --- LINE 122";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE05011", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    //021-030 DELETE
    public Map<String, Object> deleteClassroom(Long classroomId, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Classroom nextClassroom = null;
        try {
            nextClassroom = classroomRepo.findById(classroomId).orElseThrow(
                    ()->null
            );

            if(nextClassroom==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV05021",request);
            }
            if(strUserIdz==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV05022",request);
            }
            nextClassroom.setIsDelete((byte)0);
            nextClassroom.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextClassroom.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " deleteClassroom(Long classroomId, WebRequest request) --- LINE 162";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE05021", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    //031-040 FINDALL
    public Map<String,Object> findAllClassroom(Pageable pageable, WebRequest request) {
        List<ClassroomDTO> classroomDTOList = null;
        Map<String,Object> mapResult = null;
        Page<Classroom> classroomPage = null;
        List<Classroom> classroomList = null;

        try {
            classroomPage = classroomRepo.findByIsDelete(pageable,(byte)1);
            classroomList = classroomPage.getContent();
            if(classroomList.size()==0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV05031",
                                request);
            }
            classroomDTOList = modelMapper.map(classroomList, new TypeToken<List<ClassroomDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,classroomDTOList,classroomPage,mapColumnSearch);
        }
        catch (Exception e) {
            strExceptionArr[1] = "findAllClassroom(Pageable pageable, WebRequest request) --- LINE 198";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE05031", request);
        }

        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

    //041-050 FINDBYID
    public Map<String,Object> findById(Long classroomId, WebRequest request) {
        Classroom classroom = classroomRepo.findById(classroomId).orElseThrow (
                ()-> null
        );
        if(classroom == null) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV05041",request);
        }
        ClassroomDTO classroomDTO = modelMapper.map(classroom, new TypeToken<ClassroomDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        classroomDTO,
                        null,
                        request);
    }

    //051-060 FINDBYPAGE
    public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) {
        Page<Classroom> classroomPage = null;
        List<Classroom> classroomList = null;
        List<ClassroomDTO> classroomDTOList = null;
        Map<String,Object> mapResult = null;

        try {
            if(columFirst.equals("id")) {
                if(!valueFirst.equals("") && valueFirst!=null) {
                    try {
                        Long.parseLong(valueFirst);
                    }
                    catch (Exception e) {
                        strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 248";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                        return new ResponseHandler().
                                generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                        HttpStatus.OK,
                                        transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                        "FE05051",
                                        request);
                    }
                }
            }
            classroomPage = getDataByValue(pageable,columFirst,valueFirst);
            classroomList = classroomPage.getContent();
            if(classroomList.size()==0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV05051",
                                request);
            }
            classroomDTOList = modelMapper.map(classroomList, new TypeToken<List<ClassroomDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,classroomDTOList,classroomPage,mapColumnSearch);
        }

        catch (Exception e) {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 274";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE05005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    //061-070 GETDATA
    private Page<Classroom> getDataByValue(Pageable pageable, String paramColumn, String paramValue) {
        if(paramValue.equals("") || paramValue==null) {
            return classroomRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id")) {
            return classroomRepo.findByIsDeleteAndClassroomId(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("kode")) {
            return classroomRepo.findByIsDeleteAndClassroomContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }
        return classroomRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }

    //071-080 GETALL
    public List<ClassroomDTO> getAllClassroom(){ //KHUSUS UNTUK FORM INPUT SAJA
        List<ClassroomDTO> classroomDTOList = null;
        Map<String,Object> mapResult = null;
        List<Classroom> classroomList = null;

        try{
            classroomList = classroomRepo.findByIsDelete((byte)1);
            if(classroomList.size()==0) {
                return new ArrayList<ClassroomDTO>();
            }
            classroomDTOList = modelMapper.map(classroomList, new TypeToken<List<ClassroomDTO>>() {}.getType());
        }
        catch (Exception e) {
            strExceptionArr[1] = "getAllClassroom() --- LINE 315";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return classroomDTOList;
        }
        return classroomDTOList;
    }

//    public Map<String,Object> findAllDivisi()//KHUSUS UNTUK FORM INPUT SAJA{
//        List<DivisiDTO> listDivisiDTO = null;
//        Map<String,Object> mapResult = null;
//        List<Divisi> listDivisi = null;
//
//        try{
//            listDivisi = ClassroomRepo.findByIsDelete((byte)1);
//            if(listDivisi.size()==0){
//                return new ResponseHandler().
//                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
//                                HttpStatus.OK,
//                                null,
//                                null,
//                                null);
//            }
//            listDivisiDTO = modelMapper.map(listDivisi, new TypeToken<List<DivisiDTO>>() {}.getType());
//        }
//        catch (Exception e){
//            strExceptionArr[1] = "findAllDivisi() --- LINE 304";
//            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
//            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
//                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FE05006", null);
//        }
//        return new ResponseHandler().
//                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
//                        HttpStatus.OK,
//                        listDivisiDTO,
//                        null,
//                        null);
//    }
}
