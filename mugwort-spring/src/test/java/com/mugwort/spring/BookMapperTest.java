package com.mugwort.spring;

import com.mugwort.spring.mapper.BookMapper;
import com.mugwort.spring.model.BookBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookMapperTest {

    @Autowired
    private BookMapper bookMapper;
/*
    @Test
    public void testInsert() {
        bookMapper.insert(new BookBean("Windows 2012 R2", "2017-08-11"));
    }*/

    @Test
    public void testGetAll() {
        List<BookBean> beans = bookMapper.getAll();
        for (BookBean bean : beans) {
            System.out.println(bean);
        }
    }
}
