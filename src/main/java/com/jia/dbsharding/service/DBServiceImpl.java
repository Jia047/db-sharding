package com.jia.dbsharding.service;

import com.jia.dbsharding.DbShardingApplication;
import com.jia.dbsharding.config.RoutingDataSource;
import com.jia.dbsharding.entity.DBEntity;
import com.jia.dbsharding.mapper.DBMapper;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DBServiceImpl implements DBService{

    private final DBMapper mapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public DBServiceImpl(DBMapper dbMapper, RedisTemplate<String, Object> redisTemplate){
        this.mapper = dbMapper;
        this.redisTemplate = redisTemplate;
    }

    @RoutingDataSource("masterDB")
    @Override
    public void insertName(String name) {
        System.out.println("insert");
        mapper.insertName(name);
    }

//    @RoutingDataSource("slaveDB")
    @RoutingDataSource("masterDB")
    @Override
    public DBEntity select(Integer id) {
        String idString = String.valueOf(id);
        if(redisTemplate.hasKey(idString)){
            DBEntity dbEntity = (DBEntity)redisTemplate.opsForValue().get(idString);
            System.out.println("缓存中存在，已取出：" + dbEntity.toString());
            return dbEntity;
        }else {
            DBEntity entity = mapper.selectName(id);
            redisTemplate.opsForValue().set(String.valueOf(entity.getId()), entity);
            System.out.println("缓存中没有，已放入缓存：" + entity.toString());
            return  entity;
        }
    }
}
