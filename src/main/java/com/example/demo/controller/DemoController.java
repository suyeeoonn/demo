package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import com.example.demo.model.domain.TestDB;
import com.example.demo.model.service.TestService; // 최상단 서비스 클래스 연동 추가


@Controller // 컨트롤러 어노테이션 명시
public class DemoController {

    // 클래스 하단 작성
    @Autowired
    TestService testService;

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("data", "반갑습니다.");
        return "hello";
    }

    @GetMapping("/testdb")
    public String getAllTestDBs(Model model) {
        // TestDB test = testService.findByName("홍길동");
        // model.addAttribute("data4", test);
        // System.out.println("데이터 출력 디버그 : " + test);
        List<TestDB> users = testService.findAll();
        model.addAttribute("users", users);
        return "testdb";
    }
}