package com.jia.dbsharding.entity;

import lombok.Data;

@Data
public class DBEntity {
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
