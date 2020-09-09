package com.xhwl.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PersonBodyDto implements Serializable {
    private PersonInfo[] personInfos;
}
