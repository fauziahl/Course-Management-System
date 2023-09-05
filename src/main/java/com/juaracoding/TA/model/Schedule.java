package com.juaracoding.TA.model;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 9/1/2023 1:34 AM
@Last Modified 9/1/2023 1:34 AM
Version 1.0
*/

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Schedule")
public class Schedule implements Serializable {

    private static final long serialversionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScheduleId")
    private Long scheduleId;

    @Column(name = "SessionDate", nullable = false)
    private String sessionDate;

    @Column(name = "StartTime")
    private String startTime;

    @Column(name = "EndTime", nullable = false)
    private String endTime;

    //JOIN CLASS GROUP
    @ManyToOne
    @JoinColumn(name = "ClassGroupId", nullable = false)
    private ClassGroup classGroup;

    //JOIN SUBJECT
    @ManyToOne
    @JoinColumn(name = "SubjectId", nullable = false)
    private Subject subject;

    //JOIN TEACHER
    @ManyToOne
    @JoinColumn(name = "TeacherId", nullable = false)
    private Teacher teacher;

    //JOIN CLASSROOM
    @ManyToOne
    @JoinColumn(name = "ClassroomId", nullable = false)
    private Classroom classroom;

    @Column(name = "Status")
    private String status;

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

    /*
        MODEL 08
     */

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
