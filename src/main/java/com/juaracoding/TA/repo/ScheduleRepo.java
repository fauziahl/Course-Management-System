package com.juaracoding.TA.repo;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 9/1/2023 2:05 AM
@Last Modified 9/1/2023 2:05 AM
Version 1.0
*/

import com.juaracoding.TA.model.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepo extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByIsDelete(Pageable page , byte byteIsDelete);
    List<Schedule> findByIsDelete(byte byteIsDelete);
//    Page<Schedule> findByIsDeleteAndNameContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Schedule> findByIsDeleteAndScheduleId(Pageable page , byte byteIsDelete, Long values);

}
