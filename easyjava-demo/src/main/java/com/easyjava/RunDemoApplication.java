package com.easyjava;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description:
 * @Author: KunSpireUp
 * @Date: 3/27/2024 9:49 AM
 */
@SpringBootApplication
@MapperScan("com.easyjava.mappers")
public class RunDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RunDemoApplication.class, args);
	}
}
