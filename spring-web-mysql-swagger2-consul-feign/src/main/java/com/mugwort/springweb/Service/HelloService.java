package com.mugwort.springweb.Service;

import com.mugwort.springweb.model.Student;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

//@FeignClient 指定服务
@FeignClient(value = "battcn-feign-hello")
public interface HelloService {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/hello")
    Student findStudentByName(@RequestParam("name") String name, @RequestHeader(name = "token", required = false) String token);

}
