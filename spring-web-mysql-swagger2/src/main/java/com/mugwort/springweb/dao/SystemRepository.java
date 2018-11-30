package com.mugwort.springweb.dao;

import com.mugwort.springweb.model.SystemBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemRepository extends CrudRepository<SystemBean, Long> {
}
