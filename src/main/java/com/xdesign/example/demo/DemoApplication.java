package com.xdesign.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) throws IOException {
		ApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
		applicationContext.getBean(MunroDataLoader.class).loadData();
	}

}
