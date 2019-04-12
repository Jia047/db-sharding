package com.jia.dbsharding.service;

import com.jia.dbsharding.entity.DBEntity;

public interface DBService {

    void insertName(String name);

    DBEntity select(Integer id);
}
