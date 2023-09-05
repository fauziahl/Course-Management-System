package com.juaracoding.TA.repo;

import com.juaracoding.TA.model.ClassGroup;
import com.juaracoding.TA.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Long>{

    Page<Student> findByIsDelete(Pageable page , byte byteIsDelete);

    List<Student> findByIsDelete(byte byteIsDelete);
    Page<Student> findByIsDeleteAndNameContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Student> findByIsDeleteAndStudentId(Pageable page , byte byteIsDelete, Long values);

    @Query(value = "SELECT cg.ClassGroupCode FROM MstClassGroup cg JOIN MstStudent s ON s.grade = cg.Grade WHERE cg.Grade = :grade", nativeQuery = true)
    List<ClassGroup> findClassGroup(Integer grade);
}
