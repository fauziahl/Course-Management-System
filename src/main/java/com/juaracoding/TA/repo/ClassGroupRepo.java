package com.juaracoding.TA.repo;
/*
IntelliJ IDEA 2023.1.2 (Community Edition)
Build #IC-231.9011.34, built on May 16, 2023
@Author Asus a.k.a. Fauziah Latifah
Java Developer
Created on 8/30/2023 11:09 AM
@Last Modified 8/30/2023 11:09 AM
Version 1.0
*/

import com.juaracoding.TA.model.ClassGroup;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClassGroupRepo extends JpaRepository<ClassGroup,Long> {

    Page<ClassGroup> findByIsDelete(Pageable page , byte byteIsDelete);

    List<ClassGroup> findByIsDelete(byte byteIsDelete);
    ClassGroup findByIsDeleteAndClassGroupCodeContainsIgnoreCase(byte byteIsDelete, String classCode);
    Page<ClassGroup> findByIsDeleteAndClassGroupCodeContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<ClassGroup> findByIsDeleteAndClassGroupId(Pageable page , byte byteIsDelete, Long values);

    List<ClassGroup> findByGrade(Integer grade);

//    @Query(value = "SELECT cg.ClassGroupCode FROM MstClassGroup cg JOIN MstStudent s ON s.grade = cg.Grade WHERE cg.Grade = :grade")
//    List<ClassGroup> findClassGroup(Integer grade);
}
