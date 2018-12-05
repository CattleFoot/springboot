package com.mugwort.spring.controller;

import com.mugwort.spring.mapper.BookMapper;
import com.mugwort.spring.model.BookBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;

    @Autowired
    BookMapper bookMapper;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("appName", "abbbbbb");
        return "home";
    }

    @GetMapping("/books")
    public String getAllBook(Model model) {
        model.addAttribute("appName", bookMapper.getAll());
        return "home";
    }
}
