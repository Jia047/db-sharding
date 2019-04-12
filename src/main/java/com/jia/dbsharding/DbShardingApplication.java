package com.jia.dbsharding;

import com.jia.dbsharding.service.DBService;
import org.aspectj.lang.annotation.AfterThrowing;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
@MapperScan("com.jia.dbsharding.mapper")
@Controller
public class DbShardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbShardingApplication.class, args);
    }

    @Autowired
    private DBService service;

    @GetMapping("/select")
    public String select(@RequestParam("id") Integer id){
        service.select(id);
        return "OK";
    }

    @GetMapping("/insert")
    public String insert(@RequestParam("name") String name){
        service.insertName(name);
        return "OK";
    }

}
