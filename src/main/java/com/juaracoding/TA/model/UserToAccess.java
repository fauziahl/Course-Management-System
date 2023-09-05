package com.juaracoding.TA.model;


import javax.persistence.*;

@Entity
@Table(name = "MstUserToAccess")
public class UserToAccess {

    @Id
    @Column(name = "UserId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    @ManyToOne
    @JoinColumn(name = "AccessID")
    private Access access;

    @Column(name = "UserName")
    private String userName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
