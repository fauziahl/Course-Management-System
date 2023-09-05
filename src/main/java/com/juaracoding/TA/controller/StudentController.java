package com.juaracoding.TA.controller;

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.ClassGroupDTO;
import com.juaracoding.TA.dto.StudentDTO;
import com.juaracoding.TA.model.ClassGroup;
import com.juaracoding.TA.model.Student;
import com.juaracoding.TA.service.ClassGroupService;
import com.juaracoding.TA.service.StudentService;
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
@RequestMapping("/api/school")
public class StudentController {
	private StudentService studentService;
	private ClassGroupService classGroupService;
	@Autowired
	private ModelMapper modelMapper;
	private Map<String,Object> objectMapper = new HashMap<String,Object>();
	private Map<String,String> mapSorting = new HashMap<String,String>();
	private List<Student> lsCPUpload = new ArrayList<Student>();
	private String [] strExceptionArr = new String[2];
	private MappingAttribute mappingAttribute = new MappingAttribute();

	@Autowired
	public StudentController(StudentService studentService, ClassGroupService classGroupService) {
		strExceptionArr[0] = "StudentController";
		mapSorting();
		this.studentService = studentService;
		this.classGroupService = classGroupService;
	}

	private void mapSorting()
	{
		mapSorting.put("studentId","studentId");
		mapSorting.put("name","name");
		mapSorting.put("grade","grade");
		mapSorting.put("schoolName","schoolName");
		mapSorting.put("phoneNumber","phoneNumber");
		mapSorting.put("email","email");
	}

	@GetMapping("/v1/students")
	public String getAllData(Model model,WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
		mappingAttribute.setAttribute(model,request);//untuk set session
		Pageable pageable = PageRequest.of(0,5, Sort.by("studentId"));
		objectMapper = studentService.findAllStudent(pageable,request);
		mappingAttribute.setAttribute(model,objectMapper);

		model.addAttribute("student",new StudentDTO());
		model.addAttribute("sortBy","studentId");
		model.addAttribute("currentPage",1);
		model.addAttribute("asc","asc");
		model.addAttribute("columnFirst","");
		model.addAttribute("valueFirst","");
		model.addAttribute("sizeComponent",5);
		return "student/students";
	}

	@GetMapping("/v1/fbpsb/{page}/{sort}/{sortby}")
	public String findByStudent(
			Model model,
			@PathVariable("page") Integer pagez,
			@PathVariable("sort") String sortz,
			@PathVariable("sortby") String sortzBy,
			@RequestParam String columnFirst,
			@RequestParam String valueFirst,
			@RequestParam String sizeComponent,
			WebRequest request
	){
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
		mappingAttribute.setAttribute(model,request);//untuk set session
		if(columnFirst.equals("id") && valueFirst.equals(""))
		{
			return "redirect:api/school/v1/students";
		}
		sortzBy = mapSorting.get(sortzBy);
		sortzBy = sortzBy==null?"studentId":sortzBy;
		Pageable pageable = PageRequest.of(pagez==0?pagez:pagez-1,Integer.parseInt(sizeComponent.equals("")?"5":sizeComponent), sortz.equals("asc")?Sort.by(sortzBy):Sort.by(sortzBy).descending());
		objectMapper = studentService.findByPage(pageable,request,columnFirst,valueFirst);
		mappingAttribute.setAttribute(model,objectMapper,request);
		model.addAttribute("student",new StudentDTO());
		model.addAttribute("currentPage",pagez==0?1:pagez);
		model.addAttribute("sortBy", ManipulationMap.getKeyFromValue(mapSorting,sortzBy));
		model.addAttribute("columnFirst",columnFirst);
		model.addAttribute("valueFirst",valueFirst);
		model.addAttribute("sizeComponent",sizeComponent);

		return "student/students";
	}

	@GetMapping("/v1/new")
	public String createStudent(Model model, WebRequest request)
	{
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
		mappingAttribute.setAttribute(model,request);//untuk set session
		model.addAttribute("student", new StudentDTO());
		model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());//untuk parent nya
		return "student/create_student";
	}

	@PostMapping("/v1/new")
	public String newStudent(@ModelAttribute(value = "student")
							 @Valid StudentDTO studentDTO
			, BindingResult bindingResult
			, Model model
			, WebRequest request
	)
	{
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
		mappingAttribute.setAttribute(model,request);//untuk set session

		ClassGroupDTO classGroupDTO = studentDTO.getClassGroup();
		String strIdClassGroup = classGroupDTO==null?null:String.valueOf(classGroupDTO.getClassGroupId());
		if(strIdClassGroup==null || strIdClassGroup.equals("null") || strIdClassGroup.equals(""))
		{
			mappingAttribute.setErrorMessage(bindingResult,"CLASS GROUP WAJIB DIPILIH !!");
		}

		/* START VALIDATION */
		if(bindingResult.hasErrors())
		{
			model.addAttribute("student",studentDTO);
			model.addAttribute("status","error");
			model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());

			return "student/create_student";
		}
		Boolean isValid = true;

		if(!isValid)
		{
			model.addAttribute("student",studentDTO);
			model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
			return "student/create_student";
		}
		/* END OF VALIDATION */

		Student student = modelMapper.map(studentDTO, new TypeToken<Student>() {}.getType());
		objectMapper = studentService.saveStudent(student,request);
        if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
        {
            return "redirect:/api/check/logout";
        }

		if((Boolean) objectMapper.get("success"))
		{
			mappingAttribute.setAttribute(model,objectMapper);
			model.addAttribute("message","DATA BERHASIL DISIMPAN");
			Long idDataSave = objectMapper.get("idDataSave")==null?1:Long.parseLong(objectMapper.get("idDataSave").toString());
			return "redirect:api/school/v1/fbpsb/0/asc/studentId?columnFirst=id&valueFirst="+idDataSave+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
		}
		else
		{
			mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
			model.addAttribute("student",new StudentDTO());
			model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
			model.addAttribute("status","error");
			return "student/create_student";
		}
	}

	@GetMapping("/v1/edit/{id}")
	public String editStudent(Model model, WebRequest request, @PathVariable("id") Long id)
	{
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
		objectMapper = studentService.findById(id,request);
		StudentDTO studentDTO = (objectMapper.get("data")==null?null:(StudentDTO) objectMapper.get("data"));
		if((Boolean) objectMapper.get("success"))
		{
			mappingAttribute.setAttribute(model,objectMapper);//untuk set session
			StudentDTO studentDTOForSelect = (StudentDTO) objectMapper.get("data");
			model.addAttribute("student", studentDTO);
			model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
			ClassGroupDTO classGroupDTO = studentDTOForSelect.getClassGroup();
			String strSelected = classGroupDTO==null?"null":classGroupDTO.getClassGroupId().toString();
			model.addAttribute("selectedValues", strSelected);
			return "student/edit_student";
		}
		else
		{
			model.addAttribute("student", new StudentDTO());
			return "redirect:/api/school/v1/students";
		}
	}

	@PostMapping("/v1/edit/{id}")
	public String editStudent(@ModelAttribute(value = "student")
							  @Valid StudentDTO studentDTO
			, BindingResult bindingResult
			, Model model
			, WebRequest request
			, @PathVariable("id") Long id
	)
	{
		studentDTO.setStudentId(id);//agar validasi bisa berjalan di form nya
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session

		studentDTO.setStudentId(id);
		ClassGroupDTO classGroupDTO = studentDTO.getClassGroup();
		String strIdClassGroup = classGroupDTO==null?null:String.valueOf(classGroupDTO.getClassGroupId());
		if(strIdClassGroup==null || strIdClassGroup.equals("null") || strIdClassGroup.equals(""))
		{
			mappingAttribute.setErrorMessage(bindingResult,"CLASS GROUP WAJIB DIPILIH !!");
		}

		System.out.println("BINDING " + bindingResult);

		/* START VALIDATION */
		if(bindingResult.hasErrors())
		{
			model.addAttribute("student",studentDTO);
			model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
			model.addAttribute("selectedValues", "null");
			return "student/edit_student";
		}
		Boolean isValid = true;

		if(!isValid)
		{
			model.addAttribute("student",studentDTO);
			model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
			return "student/edit_student";
		}
		/* END OF VALIDATION */

		Student student = modelMapper.map(studentDTO, new TypeToken<Student>() {}.getType());
		objectMapper = studentService.updateStudent(id,student,request);
		if(objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID))//AUTO LOGOUT JIKA ADA PESAN INI
		{
			return "redirect:/api/check/logout";
		}

		if((Boolean) objectMapper.get("success"))
		{
			mappingAttribute.setAttribute(model,objectMapper);
			model.addAttribute("student",new StudentDTO());
			return "redirect:/api/school/v1/fbpsb/0/asc/studentId?columnFirst=id&valueFirst="+id+"&sizeComponent=5";//LANGSUNG DITAMPILKAN FOKUS KE HASIL EDIT USER TADI
		}
		else
		{
			mappingAttribute.setErrorMessage(bindingResult,objectMapper.get("message").toString());
			model.addAttribute("student",new StudentDTO());
			model.addAttribute("listClassGroup", classGroupService.getAllClassGroup());
			return "student/edit_student";
		}
	}

	@GetMapping("/v1/delete/{id}")
	public String deleteStudent(Model model, WebRequest request, @PathVariable("id") Long id)
	{
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session
		objectMapper = studentService.deleteStudent(id,request);
        mappingAttribute.setAttribute(model,objectMapper);//untuk set session
		model.addAttribute("student", new StudentDTO());
		return "redirect:/api/school/v1/students";
	}
	
}
