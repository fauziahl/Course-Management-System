package com.juaracoding.TA.repo;

import com.juaracoding.TA.model.Akses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AksesRepo extends JpaRepository<Akses,Long> {

    Page<Akses> findByIsDelete(Pageable page , byte byteIsDelete);
    List<Akses> findByIsDelete(byte byteIsDelete);
    Page<Akses> findByIsDeleteAndNamaAksesContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    List<Akses> findByIsDeleteAndNamaAksesContainsIgnoreCase(byte byteIsDelete, String values);
    Page<Akses> findByIsDeleteAndIdAkses(Pageable page , byte byteIsDelete, Long values);
    List<Akses> findByIsDeleteAndIdAkses(byte byteIsDelete, Long values);
    
}
