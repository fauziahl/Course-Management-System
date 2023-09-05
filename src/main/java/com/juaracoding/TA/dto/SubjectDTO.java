package com.juaracoding.TA.dto;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 10:03 PM
@Last Modified 8/30/2023 10:03 PM
Version 1.0
*/

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SubjectDTO {
    private Long subjectId;

    @NotNull(message = "SUBJECT TIDAK BOLEH NULL")
    @NotEmpty(message = "SUBJECT KELAS BOLEH KOSONG")
    @NotBlank(message = "SUBJECT KELAS BOLEH BLANK")
    private String subject;

    private List<TeacherDTO> subjectTeacherList;

    public List<TeacherDTO> getSubjectTeacherList() {
        return subjectTeacherList;
    }

    public void setSubjectTeacherList(List<TeacherDTO> subjectTeacherList) {
        this.subjectTeacherList = subjectTeacherList;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
