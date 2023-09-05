package com.juaracoding.TA.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DemoDTO {


    private Long idDemo;

    /*
        validasi field namaDemo not null not empty
     */
    @NotNull
    @NotEmpty
    @NotBlank
    private String namaDemo;

    private byte[] gambar;

    public byte[] getGambar() {
        return gambar;
    }

    public void setGambar(byte[] gambar) {
        this.gambar = gambar;
    }

    public Long getIdDemo() {
        return idDemo;
    }

    public void setIdDemo(Long idDemo) {
        this.idDemo = idDemo;
    }

    public String getNamaDemo() {
        return namaDemo;
    }

    public void setNamaDemo(String namaDemo) {
        this.namaDemo = namaDemo;
    }
}
