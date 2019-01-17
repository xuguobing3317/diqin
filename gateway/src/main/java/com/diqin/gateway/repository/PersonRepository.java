package com.diqin.gateway.repository;

import com.diqin.gateway.mapper.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

/**
 * @Auther: guobing
 * @Date: 2019-1-16 10:22
 * @Description:
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, String>
        ,JpaSpecificationExecutor<Person> {

}
