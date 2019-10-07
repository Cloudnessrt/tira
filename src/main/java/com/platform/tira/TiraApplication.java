package com.platform.tira;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling//开启定时器
@MapperScan("com.platform.tira.mapper.temp")
@EnableTransactionManagement //如果mybatis中service实现类中加入事务注解，需要此处添加该注解
public class TiraApplication {

    public static void main(String[] args) {
        SpringApplication.run(TiraApplication.class, args);
    }

}
