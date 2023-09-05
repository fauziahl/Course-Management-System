package com.juaracoding.TA.repo;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 7:20 PM
@Last Modified 8/30/2023 7:20 PM
Version 1.0
*/

import com.juaracoding.TA.model.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepo extends JpaRepository<Classroom,Long> {
    Page<Classroom> findByIsDelete(Pageable page , byte byteIsDelete);

    List<Classroom> findByIsDelete(byte byteIsDelete);
    Page<Classroom> findByIsDeleteAndClassroomContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Classroom> findByIsDeleteAndClassroomId(Pageable page , byte byteIsDelete, Long values);
}
