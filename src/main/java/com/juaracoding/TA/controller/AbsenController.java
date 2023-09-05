package com.juaracoding.TA.controller;

import com.juaracoding.TA.configuration.OtherConfig;
import com.juaracoding.TA.dto.AbsenDTO;
import com.juaracoding.TA.dto.UserDTO;
import com.juaracoding.TA.model.Absen;
import com.juaracoding.TA.repo.AbsenRepo;
import com.juaracoding.TA.utils.MappingAttribute;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/absen")
public class AbsenController {
    @Autowired
    private AbsenRepo absenRepo;
    @Autowired
    private ModelMapper modelMapper;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEEE dd-MMMM-yy HH:mm:ss");
    private MappingAttribute mappingAttribute = new MappingAttribute();
    @PostMapping("/v1/in")
    public String  absenIn(@ModelAttribute(value = "absen")
                           @Valid AbsenDTO absenDTO
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
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        Long idUserzz = Long.parseLong(request.getAttribute("USR_ID",1).toString());

        List<Absen> listAbsen = absenRepo.findByAbsenOutAndUserzIdUser(null,idUserzz);
        if(listAbsen.size()==0)
        {
            UserDTO userDTO = new UserDTO();
            userDTO.setIdUser(idUserzz);
            absenDTO.setUserz(userDTO);
            absenDTO.setAbsenIn(new Date());
            Absen absen = modelMapper.map(absenDTO, new TypeToken<Absen>() {}.getType());
            absenRepo.save(absen);
        }
//        model.addAttribute("absen",new AbsenDTO());
//        model.addAttribute("flag","OUT");
        return "index_1";
    }

    @PostMapping("/v1/out")
    public String absenOut(@ModelAttribute(value = "absen")
                         @Valid AbsenDTO absenDTO
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
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut
        Long idUserzz = Long.parseLong(request.getAttribute("USR_ID",1).toString());

        List<Absen> listAbsen = absenRepo.findByAbsenOutAndUserzIdUser(null,idUserzz);
        Absen absen = listAbsen.get(0);
        absen.setAbsenOut(new Date());
        absenRepo.save(absen);

        return "index_1";
    }

    @GetMapping("/default")
    public String getAbsensi(Model model, WebRequest request)
    {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model,request);//untuk set session ke attribut


        Long idUserzz = Long.parseLong(request.getAttribute("USR_ID",1).toString());
        List<Absen> listAbsen = absenRepo.findByAbsenOutAndUserzIdUser(null,idUserzz);
        String flag = "OUT";
        if(listAbsen.size()==0)
        {
            flag = "IN";
        }
        model.addAttribute("absen",new AbsenDTO());
        model.addAttribute("flag",flag);

        return "authz/tombolabsen";
    }
}