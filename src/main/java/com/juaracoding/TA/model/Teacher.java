package com.juaracoding.TA.model;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/29/2023 5:22 PM
@Last Modified 8/29/2023 5:22 PM
Version 1.0
*/

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MstTeacher")
public class Teacher implements Serializable {

    private static final long serialversionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TeacherId")
    private Long teacherId;

    @Column(name = "Name", length = 25, nullable = false)
    private String name;

    @Column(name = "PhoneNumber", length = 13, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "CreatedBy",nullable = false)
    private Integer createdBy = 1;

    @Column(name = "CreatedDate",nullable = false)
    private Date createdDate = new Date();

    @Column(name = "ModifiedBy")
    private Integer modifiedBy ;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    @Column(name = "IsDelete")
    private Byte isDelete = 1;

    //JOIN SUBJECT
    @ManyToMany
    @JoinTable(name = "MapTeacherSubject",
            joinColumns = {
                    @JoinColumn(name = "TeacherId",referencedColumnName = "TeacherId")}, inverseJoinColumns = {
            @JoinColumn (name = "SubjectId",referencedColumnName = "SubjectId")}, uniqueConstraints = @UniqueConstraint(columnNames = {
            "TeacherId", "SubjectId" }))
    private List<Subject> subjectTeacherList;

    /*
        MODEL 03
     */

    public List<Subject> getSubjectTeacherList() {
        return subjectTeacherList;
    }

    public void setSubjectTeacherList(List<Subject> subjectTeacherList) {
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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}
