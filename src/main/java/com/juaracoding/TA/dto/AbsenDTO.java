package com.juaracoding.TA.dto;

import java.util.Date;

public class AbsenDTO {


    private Long idAbsen;
    private Date absenIn;
    private Date absenOut;

    private UserDTO userz;

    public UserDTO getUserz() {
        return userz;
    }

    public void setUserz(UserDTO userz) {
        this.userz = userz;
    }

    public Long getIdAbsen() {
        return idAbsen;
    }

    public void setIdAbsen(Long idAbsen) {
        this.idAbsen = idAbsen;
    }

    public Date getAbsenIn() {
        return absenIn;
    }

    public void setAbsenIn(Date absenIn) {
        this.absenIn = absenIn;
    }

    public Date getAbsenOut() {
        return absenOut;
    }

    public void setAbsenOut(Date absenOut) {
        this.absenOut = absenOut;
    }
}
