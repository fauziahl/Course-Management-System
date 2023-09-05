package com.juaracoding.TA.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AuthorizeAccessModulKeys implements Serializable {

    private static final long serialversionUID = 1L;

    @Column(name = "AccessID")
    private Long accessId;

    @Column(name = "ModulID")
    private Long modulId;

    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    public Long getModulId() {
        return modulId;
    }

    public void setModulId(Long modulId) {
        this.modulId = modulId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorizeAccessModulKeys that)) return false;

        if (!getAccessId().equals(that.getAccessId())) return false;
        return getModulId().equals(that.getModulId());
    }

    @Override
    public int hashCode() {
        int result = getAccessId().hashCode();
        result = 31 * result + getModulId().hashCode();
        return result;
    }
}