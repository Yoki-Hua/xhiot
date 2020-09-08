package com.xhwl.licensePlate.huawei.items;

public class IdCardItem {
	private String name;   		//姓名
	private String sex;    		//性别
	private String ethnicity; 	//民族
	private String birth;  		//出生日期
	private String address;		//住址
	private String number; 		//身份证号码

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSex() {
		return sex;
	}
	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}
	public String getEthnicity() {
		return ethnicity;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getBirth() {
		return birth;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getNumber() {
		return number;
	}
}
