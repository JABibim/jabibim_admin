package com.jabibim.admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableScheduling //스케쥴러 기능 일단 정지
@SpringBootApplication
public class JabibimAdminApplication implements CommandLineRunner {

//    @Autowired
//    private Environment env;

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    public static void main(String[] args) {
        SpringApplication.run(JabibimAdminApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // ASCII 아트 출력
        System.out.println("==============================================================");
        System.out.println("             🚀🚀 JAB Admin Server Started 🚀🚀                        ");
        System.out.println("==============================================================");

        // 서버 포트 출력
//        String port = env.getProperty("server.port", "8080");
//        System.out.println("Server Port        : " + port);
//        String contextPath = env.getProperty("server.servlet.context-path", "/");
//        System.out.println("Context Path       : " + contextPath);

        // DB 연결 정보 출력
//        String dbUrl = env.getProperty("spring.datasource.url", "Not Configured");
//        String dbUsername = env.getProperty("spring.datasource.username", "Not Configured");
//        System.out.println("Database URL       : " + dbUrl);
//        System.out.println("Database Username  : " + dbUsername);

//        System.out.println("==============================================================");
    }
}
