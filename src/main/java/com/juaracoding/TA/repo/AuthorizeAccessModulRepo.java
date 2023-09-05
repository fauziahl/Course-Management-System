package com.juaracoding.TA.repo;

import com.juaracoding.TA.model.AuthorizeAccessModul;
import com.juaracoding.TA.model.AuthorizeAccessModulKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorizeAccessModulRepo extends JpaRepository<AuthorizeAccessModul, AuthorizeAccessModulKeys> {


    @Query("SELECT a FROM AuthorizeAccessModul a " +
            "JOIN a.access c " +
            "JOIN a.modul d  WHERE c.accessId = :accessIdz AND a.isDelete = 1 ")
    List<AuthorizeAccessModul> findByAccessAccessId(@Param("accessIdz") Long accessIdz);

//    @Query("SELECT a FROM AuthorizeAccessModul a " +
//            "JOIN a.access b " +
//            "JOIN a.modul c  WHERE b.accessID =: accessId AND a.isDelete = 1 GROUP BY a.access")
//    public List<AuthorizeAccessModul> getByAccessID(Long accessId);
}
