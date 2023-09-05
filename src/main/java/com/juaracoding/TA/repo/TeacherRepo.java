package com.juaracoding.TA.repo;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/29/2023 5:29 PM
@Last Modified 8/29/2023 5:29 PM
Version 1.0
*/

import com.juaracoding.TA.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    Page<Teacher> findByIsDelete(Pageable page , byte byteIsDelete);

    List<Teacher> findByIsDelete(byte byteIsDelete);
    Page<Teacher> findByIsDeleteAndNameContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Teacher> findByIsDeleteAndTeacherId(Pageable page , byte byteIsDelete, Long values);
}
