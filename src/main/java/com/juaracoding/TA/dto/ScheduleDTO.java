package com.juaracoding.TA.dto;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 9/1/2023 2:06 AM
@Last Modified 9/1/2023 2:06 AM
Version 1.0
*/

import com.juaracoding.TA.model.Classroom;
import com.juaracoding.TA.model.Subject;
import com.juaracoding.TA.model.Teacher;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class ScheduleDTO {
    private Long scheduleId;

    private String sessionDate;

    private String startTime;

    @DateTimeFormat(pattern = "HH:MM")
    private String endTime;

    @DateTimeFormat(pattern = "HH:MM")
    private String status;

    private ClassGroupDTO classGroup;

    private SubjectDTO subject;

    private TeacherDTO teacher;

    private ClassroomDTO classroom;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ClassGroupDTO getClassGroup() {
        return classGroup;
    }

    public void setClassGroup(ClassGroupDTO classGroup) {
        this.classGroup = classGroup;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherDTO teacher) {
        this.teacher = teacher;
    }

    public ClassroomDTO getClassroom() {
        return classroom;
    }

    public void setClassroom(ClassroomDTO classroom) {
        this.classroom = classroom;
    }
}
