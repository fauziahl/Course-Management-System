package com.juaracoding.TA.repo;

import com.juaracoding.TA.model.Pesanan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PesananRepo extends JpaRepository<Pesanan,Long> {
    Page<Pesanan> findByIsDelete(Pageable page , byte byteIsDelete);

    List<Pesanan> findByIsDelete(byte byteIsDelete);
    /*
        QUERY UNTUK REPORT
     */
    @Query("SELECT a FROM Pesanan a " +
            "JOIN a.pelanggan b " +
            "JOIN a.pembayaran c " +
            "JOIN a.paketLayanan d  WHERE (a.berat * d.hargaPerKilo)  = :totalHarga AND a.isDelete = 1 ")
    List<Pesanan> findByIsDelete(@Param("totalHarga") Double totalHarga);
    Page<Pesanan> findByIsDeleteAndIdPesanan(Pageable page , byte byteIsDelete, Long values);

    /*
        QUERY UNTUK SEARCHING
     */
    @Query("SELECT a FROM Pesanan a " +
            "JOIN a.pelanggan b " +
            "JOIN a.pembayaran c " +
            "JOIN a.paketLayanan d  WHERE (a.berat * d.hargaPerKilo)  = :totalHarga AND a.isDelete = 1 ")
    Page<Pesanan> findByIsDelete(Pageable page,@Param("totalHarga") Double totalHarga);

//    SELECT SUM(tp.berat * mpl.Harga)
//    FROM TrxPesanan tp
//    JOIN MstPelanggan mp ON tp.IDPelanggan = mp.IDPelanggan
//    JOIN MstPaketLayanan mpl ON tp.IDListHarga = mpl.IDListHarga
//    JOIN MstPembayaran mpb ON tp.IDPembayaran = mpb.IDPembayaran
//    WHERE DATENAME(month, GETDATE()) = DATENAME(month, tp.CreatedDate)
    @Query("SELECT SUM(a.berat * d.hargaPerKilo) FROM Pesanan a " +
            "JOIN a.pelanggan b " +
            "JOIN a.pembayaran c " +
            "JOIN a.paketLayanan d  WHERE DATENAME(month, GETDATE()) = DATENAME(month, a.createdDate) AND a.isDelete = 1")
    double calculationCurrentMonthReport();

    @Query("SELECT SUM(a.berat * d.hargaPerKilo) FROM Pesanan a " +
            "JOIN a.pelanggan b " +
            "JOIN a.pembayaran c " +
            "JOIN a.paketLayanan d  WHERE DATENAME(month, DATEADD(month,:numbr,GETDATE())) = DATENAME(month, a.createdDate) AND a.isDelete = 1")
    Double calculationDynamicReport(Double numbr);
}