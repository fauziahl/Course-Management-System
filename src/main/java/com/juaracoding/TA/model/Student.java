package com.juaracoding.TA.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MstStudent")
public class Student implements Serializable {

	private static final long serialversionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "StudentId")
	private Long studentId;

	@Column(name = "Name", length = 25, nullable = false)
	private String name;

	@Column(name = "Grade", nullable = false)
	private Integer grade;

	@Column(name = "SchoolName", length = 50, nullable = false)
	private String schoolName;

	@Column(name = "PhoneNumber", length = 13, nullable = false, unique = true)
	private String phoneNumber;

	@Column(name = "Email", unique = true)
	private String email;

	@Column(name = "CreatedBy",nullable = false)
	private Integer createdBy = 1;

	@Column(name = "CreatedDate",nullable = false)
	private Date createdDate = new Date();

	@Column(name = "ModifiedBy")
	private Integer modifiedBy ;

	@Column(name = "ModifiedDate")
	private Date modifiedDate;

	@Column(name = "IsDelete")
	private Byte isDelete = 1;

	//JOIN CLASS GROUP
	@ManyToOne
	@JoinColumn(name = "ClassGroupId")
	private ClassGroup classGroup;

    /*
        MODEL 02
     */

	public ClassGroup getClassGroup() {
		return classGroup;
	}

	public void setClassGroup(ClassGroup classGroup) {
		this.classGroup = classGroup;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Byte getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Byte isDelete) {
		this.isDelete = isDelete;
	}
}
