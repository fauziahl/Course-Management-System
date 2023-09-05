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
import com.juaracoding.TA.dto.SubjectDTO;
import com.juaracoding.TA.handler.ResponseHandler;
import com.juaracoding.TA.model.Subject;
import com.juaracoding.TA.repo.SubjectRepo;
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
public class SubjectService {
/*
    MODUL 04
 */

    private SubjectRepo subjectRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public SubjectService(SubjectRepo subjectRepo) {
        strExceptionArr[0]="SubjectService";
        mapColumn();
        this.subjectRepo = subjectRepo;
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID SUBJECT");
        mapColumnSearch.put("subject","NAMA SUBJECT");
    }

    //001-010 SAVE
    public Map<String, Object> saveSubject(Subject subject, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV04001",request);
            }
            subject.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            subject.setCreatedDate(new Date());
            subjectRepo.save(subject);
        } catch (Exception e) {
            strExceptionArr[1] = "saveSubject(Subject subject, WebRequest request) --- LINE 82";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE04001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, subject.getSubjectId(),mapColumnSearch),
                null, request);
    }

    //011-020 UPDATE
    public Map<String, Object> updateSubject(Long subjectId, Subject subject, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            Subject nextSubject = subjectRepo.findById(subjectId).orElseThrow(
                    ()->null
            );

            if(nextSubject==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV04011",request);
            }
            if(strUserIdz==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV04012",request);
            }
            nextSubject.setSubject(subject.getSubject());
            nextSubject.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextSubject.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updateSubject(Long subjectId, Subject subject, WebRequest request) --- LINE 122";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE04011", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    //021-030 DELETE
    public Map<String, Object> deleteSubject(Long subjectId, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        Subject nextSubject = null;
        try {
            nextSubject = subjectRepo.findById(subjectId).orElseThrow(
                    ()->null
            );

            if(nextSubject==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV04021",request);
            }
            if(strUserIdz==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV04022",request);
            }
            nextSubject.setIsDelete((byte)0);
            nextSubject.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextSubject.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " deleteSubject(Long subjectId, WebRequest request) --- LINE 162";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE04021", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    //031-040 FINDALL
    public Map<String,Object> findAllSubject(Pageable pageable, WebRequest request) {
        List<SubjectDTO> subjectDTOList = null;
        Map<String,Object> mapResult = null;
        Page<Subject> subjectPage = null;
        List<Subject> subjectList = null;

        try {
            subjectPage = subjectRepo.findByIsDelete(pageable,(byte)1);
            subjectList = subjectPage.getContent();
            if(subjectList.size()==0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV04031",
                                request);
            }
            subjectDTOList = modelMapper.map(subjectList, new TypeToken<List<SubjectDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,subjectDTOList,subjectPage,mapColumnSearch);
        }
        catch (Exception e) {
            strExceptionArr[1] = "findAllSubject(Pageable pageable, WebRequest request) --- LINE 198";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE04031", request);
        }

        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

    //041-050 FINDBYID
    public Map<String,Object> findById(Long subjectId, WebRequest request) {
        Subject subject = subjectRepo.findById(subjectId).orElseThrow (
                ()-> null
        );
        if(subject == null) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV04041",request);
        }
        SubjectDTO subjectDTO = modelMapper.map(subject, new TypeToken<SubjectDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        subjectDTO,
                        null,
                        request);
    }

    //051-060 FINDBYPAGE
    public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) {
        Page<Subject> subjectPage = null;
        List<Subject> subjectList = null;
        List<SubjectDTO> subjectDTOList = null;
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
                                        "FE04051",
                                        request);
                    }
                }
            }
            subjectPage = getDataByValue(pageable,columFirst,valueFirst);
            subjectList = subjectPage.getContent();
            if(subjectList.size()==0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV04051",
                                request);
            }
            subjectDTOList = modelMapper.map(subjectList, new TypeToken<List<SubjectDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,subjectDTOList,subjectPage,mapColumnSearch);
        }

        catch (Exception e) {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 274";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE04005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    //061-070 GETDATA
    private Page<Subject> getDataByValue(Pageable pageable, String paramColumn, String paramValue) {
        if(paramValue.equals("") || paramValue==null) {
            return subjectRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id")) {
            return subjectRepo.findByIsDeleteAndSubjectId(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("subject")) {
            return subjectRepo.findByIsDeleteAndSubjectContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }
        return subjectRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }

    //071-080 GETALL
    public List<SubjectDTO> getAllSubject(){ //KHUSUS UNTUK FORM INPUT SAJA
        List<SubjectDTO> subjectDTOList = null;
        Map<String,Object> mapResult = null;
        List<Subject> subjectList = null;

        try{
            subjectList = subjectRepo.findByIsDelete((byte)1);
            if(subjectList.size()==0) {
                return new ArrayList<SubjectDTO>();
            }
            subjectDTOList = modelMapper.map(subjectList, new TypeToken<List<SubjectDTO>>() {}.getType());
        }
        catch (Exception e) {
            strExceptionArr[1] = "getAllSubject() --- LINE 313";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return subjectDTOList;
        }
        return subjectDTOList;
    }

//    public Map<String,Object> findAllDivisi()//KHUSUS UNTUK FORM INPUT SAJA{
//        List<DivisiDTO> listDivisiDTO = null;
//        Map<String,Object> mapResult = null;
//        List<Divisi> listDivisi = null;
//
//        try{
//            listDivisi = SubjectRepo.findByIsDelete((byte)1);
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
