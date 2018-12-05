package com.mugwort.springweb.controller;

import com.mugwort.springweb.Service.BeanService;
import com.mugwort.springweb.Service.HelloService;
import com.mugwort.springweb.Service.SystemService;
import com.mugwort.springweb.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {


    @Autowired
    HelloService helloService;
    @Autowired
    SystemService systemService;
    @Autowired
    BeanService beanService;

    @RequestMapping(value = "/helloSystem", method = RequestMethod.GET)
    public Student helloSystem(@RequestParam("name") String name, @RequestHeader(name = "token", required = false) String token) {
        System.out.println("feign consumer get hello");
        return helloService.findStudentByName(name, token);
    }

    @RequestMapping(value = "/systemCall", method = RequestMethod.GET)
    public String systemCall() {
        System.out.println("feign consumer get hello");
        return systemService.helloSystem();
    }

    @RequestMapping(value = "/beanCall", method = RequestMethod.GET)
    public String beanCall() {
        System.out.println("feign consumer get hello");
        return beanService.helloSystem();
    }
}
