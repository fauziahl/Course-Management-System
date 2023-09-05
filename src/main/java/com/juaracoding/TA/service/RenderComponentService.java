package com.juaracoding.TA.service;

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.RenderComponentDTO;
import com.juaracoding.TA.handler.ResourceNotFoundException;
import com.juaracoding.TA.handler.ResponseHandler;
import com.juaracoding.TA.model.RenderComponent;
import com.juaracoding.TA.repo.RenderComponentRepo;
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
public class RenderComponentService {

    private RenderComponentRepo renderComponentRepo;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String,String> mapColumnSearch = new HashMap<String,String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String [] strColumnSearch = new String[2];

    @Autowired
    public RenderComponentService(RenderComponentRepo renderComponentRepo) {
        mapColumn();
        strExceptionArr[0] = "RenderComponentService";
        this.renderComponentRepo = renderComponentRepo;
    }

    public Map<String, Object> saveRenderComponent(RenderComponent renderComponent, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            if(strUserIdz==null)
            {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,null,"FV90001",request);
            }
            renderComponentRepo.save(renderComponent);
        } catch (Exception e) {
            strExceptionArr[1] = "saveRenderComponent(RenderComponent renderComponent, WebRequest request) --- LINE 67";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE90001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, renderComponent.getRenderComponentId(),mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updateRenderComponent(Long idRenderComponent, RenderComponent renderComponent, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_UPDATE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            RenderComponent nextRenderComponent = renderComponentRepo.findById(idRenderComponent).orElseThrow(
                    ()->null
            );

            if(nextRenderComponent==null)
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
            nextRenderComponent.setRenderComponentName(renderComponent.getRenderComponentName());
            nextRenderComponent.setRelationRenderComponentList(renderComponent.getRelationRenderComponentList());

        } catch (Exception e) {
            strExceptionArr[1] = "updateRenderComponent(Long idRenderComponent, RenderComponent renderComponent, WebRequest request) --- LINE 92";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE90002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveUploadFileRenderComponent(List<RenderComponent> renderComponentList,
                                                             MultipartFile multipartFile,
                                                             WebRequest request) throws Exception {
        List<RenderComponent> renderComponentResults = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            renderComponentResults = renderComponentRepo.saveAll(renderComponentList);
            if (renderComponentResults.size() == 0) {
                strExceptionArr[1] = "saveUploadFileRenderComponent(List<RenderComponent> renderComponentList, MultipartFile multipartFile, WebRequest request)  --- LINE 136";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV90004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFileRenderComponent(List<RenderComponent> renderComponentList, MultipartFile multipartFile, WebRequest request)  --- LINE 140";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE90002", request);
        }
        return new ResponseHandler().
                generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                        null,
                        request);
    }

    public Map<String,Object> findAllRenderComponent(Pageable pageable, WebRequest request)
    {
        List<RenderComponentDTO> renderComponentDTOList = null;
        Map<String,Object> mapResult = null;
        Page<RenderComponent> renderComponentPage = null;
        List<RenderComponent> renderComponentList = null;

        try
        {
            renderComponentPage = renderComponentRepo.findAll(pageable);
            renderComponentList = renderComponentPage.getContent();
            if(renderComponentList.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                                "FV90005",
                                request);
            }
            renderComponentDTOList = modelMapper.map(renderComponentList, new TypeToken<List<RenderComponentDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,renderComponentDTOList,renderComponentPage,mapColumnSearch);

        }
        catch (Exception e)
        {
            strExceptionArr[1] = "findAllRenderComponent(Pageable pageable, WebRequest request) --- LINE 182";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
                    "FE90003", request);
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
        Page<RenderComponent> renderComponentPage = null;
        List<RenderComponent> renderComponentList = null;
        List<RenderComponentDTO> renderComponentDTOList = null;
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
                                    "FE90004",
                                    request);
                }
            }
            renderComponentPage = getDataByValue(pageable,columFirst,valueFirst);
            renderComponentList = renderComponentPage.getContent();
            if(renderComponentList.size()==0)
            {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
                                "FV90006",
                                request);
            }
            renderComponentDTOList = modelMapper.map(renderComponentList, new TypeToken<List<RenderComponentDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper,renderComponentDTOList,renderComponentPage,mapColumnSearch);
            System.out.println("LIST DATA => "+renderComponentDTOList.size());
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 243";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
                    "FE90005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    public List<RenderComponentDTO> dataToExport(WebRequest request,String columFirst,String valueFirst)
    {
        List<RenderComponent> renderComponentList = null;
        List<RenderComponentDTO> renderComponentDTOList = null;
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
                    return new ArrayList<RenderComponentDTO>();
                }
            }
            renderComponentList = getDataToExport(columFirst,valueFirst);
            if(renderComponentList.size()==0)
            {
                return new ArrayList<RenderComponentDTO>();
            }
            renderComponentDTOList = modelMapper.map(renderComponentList, new TypeToken<List<RenderComponentDTO>>() {}.getType());
        }

        catch (Exception e)
        {
            strExceptionArr[1] = "dataToExport(WebRequest request,String columFirst,String valueFirst) --- LINE 243";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ArrayList<RenderComponentDTO>();
        }
        return renderComponentDTOList;
    }

    public Map<String,Object> findById(Long idRenderComponent, WebRequest request)
    {
        RenderComponent renderComponent = renderComponentRepo.findById(idRenderComponent).orElseThrow (
                ()-> null
        );
        if(renderComponent == null)
        {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_AKSES_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FV90007",request);
        }
        RenderComponentDTO renderComponentDTO = modelMapper.map(renderComponent, new TypeToken<RenderComponentDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        renderComponentDTO,
                        null,
                        request);
    }

    public Map<String, Object> deleteRenderComponent(Long idRenderComponent, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);

        try {
            RenderComponent nextRenderComponent = renderComponentRepo.findById(idRenderComponent).orElseThrow(
                    ()->null
            );

            if(nextRenderComponent==null)
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
            strExceptionArr[1] = "deleteRenderComponent(Long idRenderComponent, WebRequest request)  --- LINE 303";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
                    "FE90006", request);
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

    private Page<RenderComponent> getDataByValue(Pageable pageable, String paramColumn, String paramValue)
    {
        return renderComponentRepo.findAll(pageable);
    }
    private List<RenderComponent> getDataToExport(String paramColumn, String paramValue)
    {
        return renderComponentRepo.findAll();
    }

    public List<RenderComponentDTO> getAllRenderComponent()//KHUSUS UNTUK FORM INPUT SAJA
    {
        List<RenderComponentDTO> renderComponentDTOList = null;
        Map<String,Object> mapResult = null;
        List<RenderComponent> renderComponentList = null;

        try
        {
            renderComponentList = renderComponentRepo.findAll();
            if(renderComponentList.size()==0)
            {
                return new ArrayList<RenderComponentDTO>();
            }
            renderComponentDTOList = modelMapper.map(renderComponentList, new TypeToken<List<RenderComponentDTO>>() {}.getType());
        }
        catch (Exception e)
        {
            strExceptionArr[1] = "getAllRenderComponent() --- LINE 356";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return renderComponentDTOList;
        }
        return renderComponentDTOList;
    }
}
