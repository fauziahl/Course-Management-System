package com.juaracoding.TA.dto;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 11:11 AM
@Last Modified 8/30/2023 11:11 AM
Version 1.0
*/


import javax.validation.constraints.*;

public class ClassGroupDTO {
    private Long classGroupId;

    @NotNull(message = "GRADE TIDAK BOLEH NULL")
    @Min(7)
    @Max(12)
    private Integer grade;

    @NotNull(message = "KODE KELAS TIDAK BOLEH NULL")
    @NotEmpty(message = "KODE KELAS BOLEH KOSONG")
    @NotBlank(message = "KODE KELAS BOLEH BLANK")
    private String classGroupCode;

    private String description;

    public Long getClassGroupId() {
        return classGroupId;
    }

    public void setClassGroupId(Long classGroupId) {
        this.classGroupId = classGroupId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getClassGroupCode() {
        return classGroupCode;
    }

    public void setClassGroupCode(String classGroupCode) {
        this.classGroupCode = classGroupCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
