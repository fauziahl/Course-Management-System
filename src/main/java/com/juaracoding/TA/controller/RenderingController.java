package com.juaracoding.TA.controller;


import com.juaracoding.TA.dto.RenderingOneDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/render")
public class RenderingController {

    @GetMapping(value = "/all")
    public String showAll(@ModelAttribute(value = "akses")RenderingOneDTO renderingOneDTO, Model model) {

        return "books/allBooks";
    }
}
