package com.juaracoding.TA.controller;

import cn.apiclub.captcha.Captcha;
import com.juaracoding.TA.model.Userz;
import com.juaracoding.TA.utils.CaptchaUtils;
import com.juaracoding.TA.utils.MappingAttribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {

    private MappingAttribute mappingAttribute = new MappingAttribute();
    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    @GetMapping("/")
    public String pageTwo(Model model, WebRequest request)
    {
        Captcha captcha = CaptchaUtils.createCaptcha(150, 60);

        Userz users = new Userz();
        users.setHidden(captcha.getAnswer());
        users.setCaptcha("");
        users.setImage(CaptchaUtils.encodeBase64(captcha));
        model.addAttribute("usr",users);
        if(request.getAttribute("USR_ID",1)!=null)
        {
            mappingAttribute.setAttribute(model,objectMapper,request);
            return "index_1";
        }
        return "authz/authz_signin";
    }
}
