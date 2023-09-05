package com.juaracoding.TA.dto;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/29/2023 5:26 PM
@Last Modified 8/29/2023 5:26 PM
Version 1.0
*/


import com.juaracoding.TA.model.Subject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class TeacherDTO {
    private Long teacherId;

    @NotNull(message = "NAMA TIDAK BOLEH NULL")
    @NotEmpty(message = "NAMA TIDAK BOLEH KOSONG")
    @NotBlank(message = "NAMA TIDAK BOLEH BLANK")
    private String name;

    @NotNull(message = "TELPON TIDAK BOLEH NULL")
    @NotEmpty(message = "TELPON TIDAK BOLEH KOSONG")
    @NotBlank(message = "TELPON TIDAK BOLEH BLANK")
    private String phoneNumber;

    private String email;

    private List<SubjectDTO> subjectTeacherList;

    //JOIN SUBJECT
    public List<SubjectDTO> getSubjectTeacherList() {
        return subjectTeacherList;
    }

    public void setSubjectTeacherList(List<SubjectDTO> subjectTeacherList) {
        this.subjectTeacherList = subjectTeacherList;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
