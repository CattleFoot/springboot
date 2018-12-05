package com.mugwort.springweb.controller;

import com.mugwort.springweb.dao.SystemRepository;
import com.mugwort.springweb.model.SystemBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/systems")
public class SystemController {
    @Autowired
    private SystemRepository systemRepository;

    @GetMapping
    public Iterable findAll() {
        return systemRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SystemBean create(@RequestBody SystemBean bean) {
        return systemRepository.save(bean);
    }

    @PutMapping("/{id}")
    public SystemBean updateSystemBean(@RequestBody SystemBean bean, @PathVariable Long id) {
        if (bean.getId() != id) {
            return null;
        }
        return systemRepository.save(bean);
    }
}
