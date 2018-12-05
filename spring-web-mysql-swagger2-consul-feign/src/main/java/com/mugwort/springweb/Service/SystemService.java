package com.mugwort.springweb.Service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient 指定服务
@FeignClient(url = "http://localhost:8082",name = "service-system-bean")
public interface SystemService {


    @RequestMapping(value = "/api/systems", method = RequestMethod.GET)
    String helloSystem();
}
