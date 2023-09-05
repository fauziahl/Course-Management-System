package com.juaracoding.TA.dto;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/29/2023 3:01 AM
@Last Modified 8/29/2023 3:01 AM
Version 1.0
*/

import javax.validation.constraints.*;

public class StudentDTO {
    private Long studentId;

    @NotNull(message = "NAMA TIDAK BOLEH NULL")
    @NotEmpty(message = "NAMA TIDAK BOLEH KOSONG")
    @NotBlank(message = "NAMA TIDAK BOLEH BLANK")
    private String name;

    @NotNull(message = "GRADE TIDAK BOLEH NULL")
    @Min(7)
    @Max(12)
    private Integer grade;

    @NotNull(message = "NAMA SEKOLAH TIDAK BOLEH NULL")
    @NotEmpty(message = "NAMA SEKOLAH TIDAK BOLEH KOSONG")
    @NotBlank(message = "NAMA SEKOLAH TIDAK BOLEH BLANK")
    private String schoolName;

    @NotNull(message = "TELPON TIDAK BOLEH NULL")
    @NotEmpty(message = "TELPON TIDAK BOLEH KOSONG")
    @NotBlank(message = "TELPON TIDAK BOLEH BLANK")
    private String phoneNumber;

    private String email;

    private ClassGroupDTO classGroup;

    public ClassGroupDTO getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroupDTO classGroup) {
        this.classGroup = classGroup;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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
