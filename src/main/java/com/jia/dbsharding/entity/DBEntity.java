package com.jia.dbsharding.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DBEntity implements Serializable{
    private static final long serialVersionUID = -1L;

    private Integer id;
    private String name;

    @Override
    public String toString() {
        return "DBEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
