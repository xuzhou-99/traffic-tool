package com.qingyan.traffictool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xuzhou
 */
@SpringBootApplication
@MapperScan("com.qingyan.traffictool.*")
public class TrafficToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrafficToolApplication.class, args);
    }

}
