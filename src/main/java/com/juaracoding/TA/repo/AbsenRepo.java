package com.juaracoding.TA.repo;

import com.juaracoding.TA.model.Absen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsenRepo extends JpaRepository<Absen,Long> {


    List<Absen> findByAbsenOutAndUserzIdUser(String absenz, Long idUser);

}
