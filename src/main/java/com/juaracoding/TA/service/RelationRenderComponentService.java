package com.juaracoding.TA.service;


import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.RelationRenderComponentDTO;
import com.juaracoding.TA.handler.ResourceNotFoundException;
import com.juaracoding.TA.handler.ResponseHandler;
import com.juaracoding.TA.model.RelationRenderComponent;
import com.juaracoding.TA.repo.RelationRenderComponentRepo;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RelationRenderComponentService {

    private RelationRenderComponentRepo relationRenderComponentRepo;

    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();

    private String [] strExceptionArr = new String[2];

    @Autowired
    public RelationRenderComponentService(RelationRenderComponentRepo relationRenderComponentRepo) {
        mapColumn();
        strExceptionArr[0]= "RelationRenderComponentService";
        this.relationRenderComponentRepo = relationRenderComponentRepo;
    }

    public Map<String, Object> saveRelRender(RelationRenderComponent relationRenderComponent, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV90001",request);
            }
            relationRenderComponentRepo.save(relationRenderComponent);
        } catch (Exception e) {
            strExceptionArr[1] = "saveRelRender(RelationRenderComponent relationRenderComponent, WebRequest request) --- LINE 67";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV90001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, relationRenderComponent.getRelationRenderComponentId(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updateRelRender(Long idRelRender, RelationRenderComponent relationRenderComponent, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_UPDATE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            RelationRenderComponent nextRelationRenderComponent = relationRenderComponentRepo.findById(idRelRender).orElseThrow(
                    ()->null
            );

            if(nextRelationRenderComponent==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_AKSES_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV90002",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV90003",request);
            }
            nextRelationRenderComponent.setRelationRenderComponentName(relationRenderComponent.getRelationRenderComponentName());
            nextRelationRenderComponent.setRenderComponentList(relationRenderComponent.getRenderComponentList());
        } catch (Exception e) {
            strExceptionArr[1] = "updateRelRender(Long idRelRender, RelationRenderComponent relationRenderComponent, WebRequest request) --- LINE 92";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV90002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveUploadFileRelRender(List<RelationRenderComponent> relationRenderComponentList,
                                                       MultipartFile multipartFile,
                                                       WebRequest request) throws Exception {
        List<RelationRenderComponent> relationRenderComponentListResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            relationRenderComponentListResult = relationRenderComponentRepo.saveAll(relationRenderComponentList);
            if (relationRenderComponentListResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFileRelRender(List<RelationRenderComponent> relationRenderComponentList, MultipartFile multipartFile, WebRequest request) --- LINE 136";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV90004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFileRelRender(List<RelationRenderComponent> relationRenderComponentList, MultipartFile multipartFile, WebRequest request)  --- LINE 140";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FV90002", request);
        }
        return new ResponseHandler().
                generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        null,
                        request);
    }

    public Map<String,Object> findAllRelRender(Pageable pageable, WebRequest request)
    {
        List<RelationRenderComponentDTO> relationRenderComponentListDTO = null;
        Map<String,Object> mapResult = null;
        Page<RelationRenderComponent> relationRenderComponentPage = null;
        List<RelationRenderComponent> relationRenderComponentList = null;

        try
        {
            relationRenderComponentPage = relationRenderComponentRepo.findAll(pageable);
            relationRenderComponentList = relationRenderComponentPage.getContent();
            if(relationRenderComponentList.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV90005",
                                request);
            }
            relationRenderComponentListDTO = modelMapper.map(relationRenderComponentList, new TypeToken<List<RelationRenderComponentDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,relationRenderComponentListDTO,relationRenderComponentPage,mapColumnSearch);

        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllRelRender(Pageable pageable, WebRequest request) --- LINE 182";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FV90003", request);
        }

        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

    public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst)
    {
        Page<RelationRenderComponent> relationRenderComponentPage = null;
        List<RelationRenderComponent> relationRenderComponentList = null;
        List<RelationRenderComponentDTO> relationRenderComponentListDTO = null;
        Map<String,Object> mapResult = null;

        try
        {
            if(columFirst.equals("id"))
            {
                try
                {
                    Long.parseLong(valueFirst);
                }
                catch (Exception e)
                {
                    strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 209";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().
                            generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                    HttpStatus.OK,
                                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                    "FV90004",
                                    request);
                }
            }
            relationRenderComponentPage = getDataByValue(pageable,columFirst,valueFirst);
            relationRenderComponentList = relationRenderComponentPage.getContent();
            if(relationRenderComponentList.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV90006",
                                request);
            }
            relationRenderComponentListDTO = modelMapper.map(relationRenderComponentList, new TypeToken<List<RelationRenderComponentDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,relationRenderComponentListDTO,relationRenderComponentPage,mapColumnSearch);
            System.out.println("LIST DATA => "+relationRenderComponentListDTO.size());
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 243";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FV90005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    public List<RelationRenderComponentDTO> dataToExport(WebRequest request,String columFirst,String valueFirst)
    {
        List<RelationRenderComponent> relationRenderComponentList = null;
        List<RelationRenderComponentDTO> relationRenderComponentListDTO = null;
        Map<String,Object> mapResult = null;

        try
        {
            if(columFirst.equals("id"))
            {
                try
                {
                    Long.parseLong(valueFirst);
                }
                catch (Exception e)
                {
                    strExceptionArr[1] = "dataToExport(WebRequest request,String columFirst,String valueFirst) --- LINE 209";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ArrayList<RelationRenderComponentDTO>();
                }
            }
            relationRenderComponentList = getDataToExport(columFirst,valueFirst);
            if(relationRenderComponentList.size()==0)
            {
                return new ArrayList<RelationRenderComponentDTO>();
            }
            relationRenderComponentListDTO = modelMapper.map(relationRenderComponentList, new TypeToken<List<RelationRenderComponentDTO>>() {}.getType());
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "dataToExport(WebRequest request,String columFirst,String valueFirst) --- LINE 243";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ArrayList<RelationRenderComponentDTO>();
        }
        return relationRenderComponentListDTO;
    }

    public Map<String,Object> findById(Long idRelRender, WebRequest request)
    {
        RelationRenderComponent relationRenderComponent = relationRenderComponentRepo.findById(idRelRender).orElseThrow (
                ()-> null
        );
        if(relationRenderComponent == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_AKSES_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV90007",request);
        }
        RelationRenderComponentDTO relationRenderComponentDTO = modelMapper.map(relationRenderComponent, new TypeToken<RelationRenderComponentDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        relationRenderComponentDTO,
                        null,
                        request);
    }

    public Map<String, Object> deleteRelRender(Long idRelRender, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            RelationRenderComponent nextRelationRenderComponent = relationRenderComponentRepo.findById(idRelRender).orElseThrow(
                    ()->null
            );

            if(nextRelationRenderComponent==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_AKSES_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        "FV90008",request);
            }
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV90009",request);
            }

        } catch (Exception e) {
            strExceptionArr[1] = "deleteRelRender(Long idRelRender, WebRequest request) --- LINE 303";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV90006", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    private void mapColumn()
    {
        mapColumnSearch.put("id","ID RENDER");
        mapColumnSearch.put("nama","NAMA RENDER");
    }

    private Page<RelationRenderComponent> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        return relationRenderComponentRepo.findAll(pageable);
    }
    private List<RelationRenderComponent> getDataToExport(String paramColumn, String paramValue)
    {
        return relationRenderComponentRepo.findAll();
    }

    public List<RelationRenderComponentDTO> getAllRelRender()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<RelationRenderComponentDTO> relationRenderComponentDTOList = null;
        Map<String,Object> mapResult = null;
        List<RelationRenderComponent> relationRenderComponentList = null;

        try
        {
            relationRenderComponentList = relationRenderComponentRepo.findAll();
            if(relationRenderComponentList.size()==0)
            {
                return new ArrayList<RelationRenderComponentDTO>();
            }
            relationRenderComponentDTOList = modelMapper.map(relationRenderComponentList, new TypeToken<List<RelationRenderComponentDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "getAllRelRender() --- LINE 356";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return relationRenderComponentDTOList;
        }
        return relationRenderComponentDTOList;
    }
}
