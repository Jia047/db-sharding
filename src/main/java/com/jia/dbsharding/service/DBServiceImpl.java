package com.jia.dbsharding.service;

import com.jia.dbsharding.config.RoutingDataSource;
import com.jia.dbsharding.entity.DBEntity;
import com.jia.dbsharding.mapper.DBMapper;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBServiceImpl implements DBService{

    private final DBMapper mapper;

    @Autowired
    public DBServiceImpl(DBMapper dbMapper){
        this.mapper = dbMapper;
    }

    @RoutingDataSource("masterDB")
    @Override
    public void insertName(String name) {
        System.out.println("insert");
        mapper.insertName(name);
    }

    @RoutingDataSource("slaveDB")
    @Override
    public DBEntity select(Integer id) {
        System.out.println("select");
        return mapper.selectName(id);
    }
}
