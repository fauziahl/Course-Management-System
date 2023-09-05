package com.juaracoding.TA.repo;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 10:02 PM
@Last Modified 8/30/2023 10:02 PM
Version 1.0
*/

import com.juaracoding.TA.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepo extends JpaRepository<Subject,Long> {
    Page<Subject> findByIsDelete(Pageable page , byte byteIsDelete);

    List<Subject> findByIsDelete(byte byteIsDelete);
    Page<Subject> findByIsDeleteAndSubjectContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Subject> findByIsDeleteAndSubjectId(Pageable page , byte byteIsDelete, Long values);
}
