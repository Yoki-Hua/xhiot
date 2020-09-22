package com.xhwl.pojo;

import com.fasterxml.jackson.core.SerializableString;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author: coll man
 * @create: 2020-09-02 16:29
 */
@Data
public class PersonInfo implements Serializable {

    private String name;
    private String contact;
    private String identity;
    private String idNumber;
    private String project;
    private String homeAddress;
    private String loginPerson;
    private String emergencyContact;
    private String isBlack;
    private String gender;
    private String personType;
}
