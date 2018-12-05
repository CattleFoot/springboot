package com.mugwort.spring.mapper;

import com.mugwort.spring.model.BookBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookMapper {
    @Select(value = "select * from system_bean")
    List<BookBean> getAll();

    @Insert(value = "insert into system_bean(name,lastaudit) values (#{name},#{lastaudit})")
    void insert(BookBean bean);
}
