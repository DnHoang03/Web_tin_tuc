package com.web.springmvc.web_tin_tuc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WebTinTucApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebTinTucApplication.class, args);
    }

}
