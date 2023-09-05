package com.juaracoding.TA.service;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 11:16 AM
@Last Modified 8/30/2023 11:16 AM
Version 1.0
*/

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.ClassGroupDTO;
import com.juaracoding.TA.handler.ResponseHandler;
import com.juaracoding.TA.model.ClassGroup;
import com.juaracoding.TA.repo.ClassGroupRepo;
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
public class ClassGroupService {
/*
    MODUL 06
 */
    private ClassGroupRepo classGroupRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();
    private List<ClassGroup> lsCPUpload = new ArrayList<ClassGroup>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public ClassGroupService(ClassGroupRepo classGroupRepo) {
        strExceptionArr[0]="ClassGroupService";
        mapColumn();
        this.classGroupRepo = classGroupRepo;
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID CLASS GROUP");
        mapColumnSearch.put("kode","KODE CLASS GROUP");
    }

    //001-010 SAVE
    public Map<String, Object> saveClassGroup(ClassGroup classGroup, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID", 1);

        ClassGroup nextClassGroup;
        try {
            if (strUserIdz == null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE, null, "FV06001", request);
            }
            nextClassGroup = classGroupRepo.findByIsDeleteAndClassGroupCodeContainsIgnoreCase((byte) 1, classGroup.getClassCode());
            if (nextClassGroup != null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_CLASSGROUP_NOT_AVAILABLE,
                        HttpStatus.BAD_REQUEST,
                        transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                        "FE06001", request);
            }
            classGroup.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            classGroup.setCreatedDate(new Date());
            classGroupRepo.save(classGroup);
        } catch (Exception e) {
            strExceptionArr[1] = "saveClassGroup(ClassGroup classGroup, WebRequest request) --- LINE 81";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                    "FE06001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, classGroup.getClassGroupId(), mapColumnSearch),
                null, request);
    }

    //011-020 UPDATE
    public Map<String, Object> updateClassGroup(Long classGroupId, ClassGroup classGroup, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            ClassGroup nextClassGroup = classGroupRepo.findById(classGroupId).orElseThrow(
                    ()->null
            );

            if(nextClassGroup==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV06011",request);
            }
            if(strUserIdz==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV06012",request);
            }
            nextClassGroup.setGrade(classGroup.getGrade());
            nextClassGroup.setClassCode(classGroup.getClassCode());
            nextClassGroup.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextClassGroup.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " updateClassGroup(Long classGroupId, ClassGroup classGroup, WebRequest request) --- LINE 122";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE06011", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    //021-030 DELETE
    public Map<String, Object> deleteClassGroup(Long classGroupId, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
        ClassGroup nextClassGroup = null;
        try {
            nextClassGroup = classGroupRepo.findById(classGroupId).orElseThrow(
                    ()->null
            );

            if(nextClassGroup==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV06021",request);
            }
            if(strUserIdz==null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV06022",request);
            }
            nextClassGroup.setIsDelete((byte)0);
            nextClassGroup.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextClassGroup.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = " deleteClassGroup(Long classGroupId, WebRequest request) --- LINE 162";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE06021", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    //031-040 FINDALL
    public Map<String,Object> findAllClassGroup(Pageable pageable, WebRequest request) {
        List<ClassGroupDTO> classGroupDTOList = null;
        Map<String,Object> mapResult = null;
        Page<ClassGroup> classGroupPage = null;
        List<ClassGroup> classGroupList = null;

        try {
            classGroupPage = classGroupRepo.findByIsDelete(pageable,(byte)1);
            classGroupList = classGroupPage.getContent();
            if(classGroupList.size()==0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV06031",
                                request);
            }
            classGroupDTOList = modelMapper.map(classGroupList, new TypeToken<List<ClassGroupDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,classGroupDTOList,classGroupPage,mapColumnSearch);
        }
        catch (Exception e) {
            strExceptionArr[1] = "findAllClassGroup(Pageable pageable, WebRequest request) --- LINE 198";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE06031", request);
        }

        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

    //041-050 FINDBYID
    public Map<String,Object> findById(Long classGroupId, WebRequest request) {
        ClassGroup classGroup = classGroupRepo.findById(classGroupId).orElseThrow (
                ()-> null
        );
        if(classGroup == null) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DIVISI_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV06041",request);
        }
        ClassGroupDTO classGroupDTO = modelMapper.map(classGroup, new TypeToken<ClassGroupDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        classGroupDTO,
                        null,
                        request);
    }

    //051-060 FINDBYPAGE
    public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) {
        Page<ClassGroup> classGroupPage = null;
        List<ClassGroup> classGroupList = null;
        List<ClassGroupDTO> classGroupDTOList = null;
        Map<String,Object> mapResult = null;

        try {
            if(columFirst.equals("id")) {
                if(!valueFirst.equals("") && valueFirst!=null) {
                    try {
                        Long.parseLong(valueFirst);
                    }
                    catch (Exception e) {
                        strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 231";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                        return new ResponseHandler().
                                generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                        HttpStatus.OK,
                                        transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                        "FE06051",
                                        request);
                    }
                }
            }
            classGroupPage = getDataByValue(pageable,columFirst,valueFirst);
            classGroupList = classGroupPage.getContent();
            if(classGroupList.size()==0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV06051",
                                request);
            }
            classGroupDTOList = modelMapper.map(classGroupList, new TypeToken<List<ClassGroupDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,classGroupDTOList,classGroupPage,mapColumnSearch);
        }

        catch (Exception e) {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 257";
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
    private Page<ClassGroup> getDataByValue(Pageable pageable, String paramColumn, String paramValue) {
        if(paramValue.equals("") || paramValue==null) {
            return classGroupRepo.findByIsDelete(pageable,(byte) 1);
        }
        if(paramColumn.equals("id")) {
            return classGroupRepo.findByIsDeleteAndClassGroupId(pageable,(byte) 1,Long.parseLong(paramValue));
        } else if (paramColumn.equals("kode")) {
            return classGroupRepo.findByIsDeleteAndClassGroupCodeContainsIgnoreCase(pageable,(byte) 1,paramValue);
        }
        return classGroupRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
    }

    //071-080 GETALL
    public List<ClassGroupDTO> getAllClassGroup(){//KHUSUS UNTUK FORM INPUT SAJA{
        List<ClassGroupDTO> classGroupDTOList = null;
        List<ClassGroup> classGroupList = null;
        try{
            classGroupList = classGroupRepo.findByIsDelete((byte)1);
            if(classGroupList.size()==0) {
                return new ArrayList<ClassGroupDTO>();
            }
            classGroupDTOList = modelMapper.map(classGroupList, new TypeToken<List<ClassGroupDTO>>() {}.getType());
        }
        catch (Exception e){
            strExceptionArr[1] = "getAllDemo() ---> LINE 314";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return classGroupDTOList;
        }
        return classGroupDTOList;
    }

    //GET DATA BY GRADE
    public List<ClassGroupDTO> getByGrade(Integer grade) {
        List<ClassGroupDTO> classGroupDTOList = null;
        List<ClassGroup> classGroupList = null;
        if (grade == null) {
            return null;
        } else {
            classGroupList = classGroupRepo.findByGrade(grade);
            classGroupDTOList = modelMapper.map(classGroupList, new TypeToken<List<ClassGroupDTO>>() {}.getType());
        }
        return classGroupDTOList;
    }
}
