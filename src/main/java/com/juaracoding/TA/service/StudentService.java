package com.juaracoding.TA.service;

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.ClassGroupDTO;
import com.juaracoding.TA.dto.StudentDTO;
import com.juaracoding.TA.handler.ResponseHandler;
import com.juaracoding.TA.model.ClassGroup;
import com.juaracoding.TA.model.Student;
import com.juaracoding.TA.repo.StudentRepo;
import com.juaracoding.TA.utils.ConstantMessage;
import com.juaracoding.TA.utils.LoggingFile;
import com.juaracoding.TA.utils.TransformToDTO;
import io.swagger.models.auth.In;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@Service
public class StudentService {
/*
    MODUL 02
 */

	private StudentRepo studentRepo;
	private String[] strExceptionArr = new String[2];
	private TransformToDTO transformToDTO = new TransformToDTO();

	@Autowired
	private ModelMapper modelMapper;

	private Map<String,Object> objectMapper = new HashMap<String,Object>();
	private Map<String,String> mapColumnSearch = new HashMap<String,String>();
	private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
	private String [] strColumnSearch = new String[2];

	@Autowired
	public StudentService(StudentRepo studentRepo) {
		strExceptionArr[0] = "StudentService";
		this.studentRepo = studentRepo;
	}

	private void mapColumn()
	{
		mapColumnSearch.put("id","ID STUDENT");
		mapColumnSearch.put("name","NAMA STUDENT");
	}

	//001-010 SAVE
	public Map<String, Object> saveStudent(Student student, WebRequest request) {
		String strMessage = ConstantMessage.SUCCESS_SAVE;
		Object strUserIdz = request.getAttribute("USR_ID",1);

		try {
			if(strUserIdz==null)
			{
				return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
						HttpStatus.NOT_ACCEPTABLE,null,"FV05001",request);
			}
			student.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
			student.setCreatedDate(new Date());
			studentRepo.save(student);
		} catch (Exception e) {
			strExceptionArr[1] = "saveStudent(Student student, WebRequest request) --- LINE 81";
			LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
			return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
					HttpStatus.BAD_REQUEST,
					transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
					"FE01001", request);
		}
		return new ResponseHandler().generateModelAttribut(strMessage,
				HttpStatus.CREATED,
				transformToDTO.transformObjectDataSave(objectMapper, student.getStudentId(),mapColumnSearch),
				null, request);
	}

	//011-020 UPDATE
	public Map<String, Object> updateStudent(Long studentId, Student student, WebRequest request) {
		String strMessage = ConstantMessage.SUCCESS_SAVE;
		Object strUserIdz = request.getAttribute("USR_ID",1);
		try {
			Student nextStudent = studentRepo.findById(studentId).orElseThrow(
					()->null
			);
			if(nextStudent==null)
			{
				return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_STUDENT_NOT_EXISTS,
						HttpStatus.NOT_ACCEPTABLE,
						transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
						"FV01011",request);
			}
			if(strUserIdz==null)
			{
				return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
						HttpStatus.NOT_ACCEPTABLE,
						null,
						"FV01012",request);
			}
			nextStudent.setName(student.getName());
			nextStudent.setGrade(student.getGrade());
			nextStudent.setSchoolName(student.getSchoolName());
			nextStudent.setPhoneNumber(student.getPhoneNumber());
			nextStudent.setEmail(student.getEmail());
			nextStudent.setClassGroup(student.getClassGroup());
			nextStudent.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
			nextStudent.setModifiedDate(new Date());

		} catch (Exception e) {
			strExceptionArr[1] = "updateStudent(Long studentId, Student student, WebRequest request) --- LINE 125";
			LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
			return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
					HttpStatus.BAD_REQUEST,
					transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
					"FE01011", request);
		}
		return new ResponseHandler().generateModelAttribut(strMessage,
				HttpStatus.CREATED,
				transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
				null, request);
	}

	//021-030 DELETE
	public Map<String, Object> deleteStudent(Long studentId, WebRequest request) {
		String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID",1);
		Student nextStudent = null;
		try {
			nextStudent = studentRepo.findById(studentId).orElseThrow(
					()->null
			);

			if(nextStudent==null)
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
                        "FV05007",request);
            }
			nextStudent.setIsDelete((byte)0);
            nextStudent.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
			nextStudent.setModifiedDate(new Date());

		} catch (Exception e) {
			strExceptionArr[1] = " deleteStudent(Long studentId, WebRequest request) --- LINE 166";
			LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
			return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
					HttpStatus.BAD_REQUEST,
					transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
					"FE05007", request);
		}
		return new ResponseHandler().generateModelAttribut(strMessage,
				HttpStatus.OK,
				transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
				null, request);
	}


//    public List<StudentDTO> getAllDivisi()//KHUSUS UNTUK FORM INPUT SAJA
//    {
//        List<DivisiDTO> listDivisiDTO = null;
//        Map<String,Object> mapResult = null;
//        List<Divisi> listDivisi = null;
//
//        try
//        {
//            listDivisi = divisiRepo.findByIsDelete((byte)1);
//            if(listDivisi.size()==0)
//            {
//                return new ArrayList<DivisiDTO>();
//            }
//            listDivisiDTO = modelMapper.map(listDivisi, new TypeToken<List<DivisiDTO>>() {}.getType());
//        }
//        catch (Exception e)
//        {
//            strExceptionArr[1] = "findAllDivisi() --- LINE 331";
//            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
//            return listDivisiDTO;
//        }
//        return listDivisiDTO;
//    }

	//031-040 FINDALL
	public Map<String,Object> findAllStudent(Pageable pageable, WebRequest request) {
		List<StudentDTO> studentDTOList = null;
		Map<String,Object> mapResult = null;
		Page<Student> studentPage = null;
		List<Student> studentList = null;

		try {
			studentPage = studentRepo.findByIsDelete(pageable,(byte)1);
			studentList = studentPage.getContent();
			if(studentList.size()==0)
			{
				return new ResponseHandler().
						generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
								HttpStatus.OK,
								transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
								"FV01031",
								request);
			}
			studentDTOList = modelMapper.map(studentList, new TypeToken<List<StudentDTO>>() {}.getType());
			mapResult = transformToDTO.transformObject(objectMapper,studentDTOList,studentPage,mapColumnSearch);
		}
		catch (Exception e)
		{
			strExceptionArr[1] = "findAllStudent(Pageable pageable, WebRequest request) --- LINE 166";
			LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
			return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
					HttpStatus.INTERNAL_SERVER_ERROR,
					transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
					"FE01032", request);
		}
		return new ResponseHandler().
				generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
						HttpStatus.OK,
						mapResult,
						null,
						null);
	}

	//041-050 FINDBYID
	public Map<String,Object> findById(Long studentId, WebRequest request) {
		Student student = studentRepo.findById(studentId).orElseThrow (
				()-> null
		);
		if(student == null)
		{
			return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_STUDENT_NOT_EXISTS,
					HttpStatus.NOT_ACCEPTABLE,
					transformToDTO.transformObjectDataEmpty(objectMapper,mapColumnSearch),
					"FV01031",request);
		}
		StudentDTO studentDTO = modelMapper.map(student, new TypeToken<StudentDTO>() {}.getType());
		return new ResponseHandler().
				generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
						HttpStatus.OK,
						studentDTO,
						null,
						request);
	}

	//051-060 FINDBYPAGE
	public Map<String,Object> findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) {
		Page<Student> studentPage = null;
		List<Student> studentList = null;
		List<StudentDTO> studentDTOList = null;
		Map<String,Object> mapResult = null;

		try {
			if(columFirst.equals("id"))
			{
				if(!valueFirst.equals("") && valueFirst!=null)
				{
					try {
						Long.parseLong(valueFirst);
					}
					catch (Exception e) {
						strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 218";
						LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
						return new ResponseHandler().
								generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
										HttpStatus.OK,
										transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN
										"FE01041",
										request);
					}
				}
			}
			studentPage = getDataByValue(pageable,columFirst,valueFirst);
			studentList = studentPage.getContent();
			if(studentList.size()==0) {
				return new ResponseHandler().
						generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
								HttpStatus.OK,
								transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),//HANDLE NILAI PENCARIAN EMPTY
								"FV01041",
								request);
			}
			studentDTOList = modelMapper.map(studentList, new TypeToken<List<StudentDTO>>() {}.getType());
			mapResult = transformToDTO.transformObject(objectMapper,studentDTOList,studentPage,mapColumnSearch);
		}

		catch (Exception e) {
			strExceptionArr[1] = "findByPage(Pageable pageable,WebRequest request,String columFirst,String valueFirst) --- LINE 244";
			LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
			return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
					HttpStatus.INTERNAL_SERVER_ERROR,
					transformToDTO.transformObjectDataEmpty(objectMapper,pageable,mapColumnSearch),
					"FE01042", request);
		}
		return new ResponseHandler().
				generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
						HttpStatus.OK,
						mapResult,
						null,
						request);
	}

	//061-070 GETDATA
	private Page<Student> getDataByValue(Pageable pageable, String paramColumn, String paramValue) {
		if(paramValue.equals("") || paramValue==null)
		{
			return studentRepo.findByIsDelete(pageable,(byte) 1);
		}
		if(paramColumn.equals("id"))
		{
			return studentRepo.findByIsDeleteAndStudentId(pageable,(byte) 1,Long.parseLong(paramValue));
		} else if (paramColumn.equals("nama")) {
			return studentRepo.findByIsDeleteAndNameContainsIgnoreCase(pageable,(byte) 1,paramValue);
		}

		return studentRepo.findByIsDelete(pageable,(byte) 1);// ini default kalau parameter search nya tidak sesuai--- asumsi nya di hit bukan dari web
	}

	//071-080 GET CLASS GROUP
	public List<ClassGroupDTO> getGroup(Integer grade){
		List<ClassGroupDTO> classGroupDTOList = null;
		List<ClassGroup> classGroupList = null;

		if(grade == null) {
			return null;
		}
		else{
			classGroupList = studentRepo.findClassGroup(grade);
//			return new ArrayList<ClassGroupDTO>();
			classGroupDTOList = modelMapper.map(classGroupList, new TypeToken<List<ClassGroupDTO>>() {}.getType());
		}
		return classGroupDTOList;
	}
}
