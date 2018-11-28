/*
package com.mugwort.spring;

import com.mugwort.spring.dao.SystemRepository;
import com.mugwort.spring.model.SystemBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaRepositories(value = "com.mugwort.spring.dao")
@EntityScan(value = "com.mugwort.spring.model")
public class Application implements CommandLineRunner {

    @Autowired
    DataSource dataSource;

    @Autowired
    SystemRepository systemRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        // add windows server
        SystemBean SystemBeanWindows = new SystemBean();
        SystemBeanWindows.setName("Windows Server 2012 R2");
        SystemBeanWindows.setLastaudit("2017-08-11");
        systemRepository.save(SystemBeanWindows);
        // add rhel
        SystemBean SystemBeanRhel = new SystemBean();
        SystemBeanRhel.setName("RHEL 7");
        SystemBeanRhel.setLastaudit("2017-07-21");
        systemRepository.save(SystemBeanRhel);
        // add solaris
        SystemBean SystemBeanSolaris = new SystemBean();
        SystemBeanSolaris.setName("Solaris 11");
        SystemBeanSolaris.setLastaudit("2017-08-13");
        systemRepository.save(SystemBeanSolaris);
        Iterable<SystemBean> systemlist = systemRepository.findAll();
        System.out.println("here are system count: " + systemlist.toString());
        for(SystemBean SystemBean:systemlist){
            System.out.println("Here is a system: " + SystemBean.toString());
        }
    }
}
*/
