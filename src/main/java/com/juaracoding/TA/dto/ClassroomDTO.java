package com.juaracoding.TA.dto;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 7:22 PM
@Last Modified 8/30/2023 7:22 PM
Version 1.0
*/

import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ClassroomDTO {
    private Long classroomId;

    @NotNull(message = "CLASSROOM TIDAK BOLEH NULL")
    @NotEmpty(message = "CLASSROOM BOLEH KOSONG")
    @NotBlank(message = "CLASSROOM BOLEH BLANK")
//    @UniqueElements(message = "NOMOR ROOM SUDAH ADA")
    private String classroom;

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
