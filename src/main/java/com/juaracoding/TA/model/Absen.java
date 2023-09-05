package com.juaracoding.TA.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TrxAbsen")
public class Absen implements Serializable {
    private static final long serialversionUID = 1L;
    @Id
    @Column(name = "IDAbsen")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAbsen;

    @Column(name = "AbsenIn")
    private Date absenIn;
    @Column(name = "AbsenOut")
    private Date absenOut;

    @ManyToOne
    @JoinColumn(name = "IDUser")
    private Userz userz;

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

    public Userz getUserz() {
        return userz;
    }

    public void setUserz(Userz userz) {
        this.userz = userz;
    }
}
