package com.wsgcya.coder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.wsgcya.**.dao"})
public class CoderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoderApplication.class, args);
	}
}
