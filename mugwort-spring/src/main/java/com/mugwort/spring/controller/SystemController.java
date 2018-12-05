//package com.mugwort.spring.controller;
//
//import com.mugwort.spring.advice.MismatchException;
//import com.mugwort.spring.dao.SystemRepository;
//import com.mugwort.spring.model.SystemBean;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(value = "/api/systems")
//public class SystemController {
//    @Autowired
//    private SystemRepository systemRepository;
//
//    @GetMapping
//    public Iterable findAll() {
//        return systemRepository.findAll();
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public SystemBean create(@RequestBody SystemBean bean) {
//        return systemRepository.save(bean);
//    }
//
//    @PutMapping("/{id}")
//    //方法的说明
//    @ApiOperation(value = "更新系统操作人员信息")
//    //定义请求参数
//    @ApiImplicitParams({@ApiImplicitParam(paramType = "update", dataTypeClass = SystemBean.class, name = "bean", required = true),
//            @ApiImplicitParam(paramType = "query", dataTypeClass = Long.class, name = "id", required = true)})
//    public SystemBean updateSystemBean(@RequestBody SystemBean bean, @PathVariable Long id) {
//        if (bean.getId() != id) {
//            throw new MismatchException("", new Throwable());
//        }
//        systemRepository.findById(id);
//        return systemRepository.save(bean);
//    }
//}
