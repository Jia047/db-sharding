package com.jia.dbsharding.mapper;

import com.jia.dbsharding.entity.DBEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface DBMapper {

    @Insert("INSERT INTO master_db.user (name) values (#{name})")
    public void insertName(@Param("name") String name);

    @Select("SELECT id, name FROM slave_db.user WHERE id = #{id}")
    public DBEntity selectName(@Param("id") Integer id);


}
