package com.juaracoding.TA.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MenuHeaderDTO {


    private Long idMenuHeader;

    @NotEmpty
    @NotNull
    private String namaMenuHeader;

    @NotEmpty
    @NotNull
    private String deskripsiMenuHeader;


    public Long getIdMenuHeader() {
        return idMenuHeader;
    }

    public void setIdMenuHeader(Long idMenuHeader) {
        this.idMenuHeader = idMenuHeader;
    }

    public String getNamaMenuHeader() {
        return namaMenuHeader;
    }

    public void setNamaMenuHeader(String namaMenuHeader) {
        this.namaMenuHeader = namaMenuHeader;
    }

    public String getDeskripsiMenuHeader() {
        return deskripsiMenuHeader;
    }

    public void setDeskripsiMenuHeader(String deskripsiMenuHeader) {
        this.deskripsiMenuHeader = deskripsiMenuHeader;
    }
}