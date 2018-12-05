package com.mugwort.springweb.Service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient 指定服务
@FeignClient(value = "service-system-bean")
public interface BeanService {

    @RequestMapping(value = "/api/systems", method = RequestMethod.GET)
    String helloSystem();
}
