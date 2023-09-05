package com.juaracoding.TA.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MapAuthorizeAccessModul",uniqueConstraints = @UniqueConstraint(columnNames = {
        "AccessID", "ModulID" }))
public class AuthorizeAccessModul implements Serializable {
    private static final long serialversionUID = 1L;
    @EmbeddedId
    private AuthorizeAccessModulKeys id;

    @ManyToOne
    @MapsId("accessId")
    @JoinColumn(name = "AccessID")
    private Access access;

    @ManyToOne
    @MapsId("modulId")
    @JoinColumn(name = "ModulID")
    private Modul modul;

    @Column(name = "CanAdd")
    private byte canAdd;
    @Column(name = "CanEdit")
    private byte canEdit;
    @Column(name = "CanDelete")
    private byte canDelete;
    @Column(name = "CanPrint")
    private byte canPrint;
    @Column(name ="CreatedDate" , nullable = false)
    private Date createdDate = new Date();

    @Column(name = "CreatedBy", nullable = false)
    private Integer createdBy=1;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;
    @Column(name = "ModifiedBy")
    private Integer modifiedBy;

    @Column(name = "IsDelete", nullable = false)
    private Byte isDelete = 1;

    public AuthorizeAccessModul() {
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Modul getModul() {
        return modul;
    }

    public void setModul(Modul modul) {
        this.modul = modul;
    }

    public AuthorizeAccessModulKeys getId() {
        return id;
    }

    public void setId(AuthorizeAccessModulKeys id) {
        this.id = id;
    }
    public byte getCanAdd() {
        return canAdd;
    }

    public void setCanAdd(byte canAdd) {
        this.canAdd = canAdd;
    }

    public byte getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(byte canEdit) {
        this.canEdit = canEdit;
    }

    public byte getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(byte canDelete) {
        this.canDelete = canDelete;
    }

    public byte getCanPrint() {
        return canPrint;
    }

    public void setCanPrint(byte canPrint) {
        this.canPrint = canPrint;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}