package com.diqin.gateway.repository;

import com.diqin.gateway.mapper.AccessLog;
import com.diqin.gateway.mapper.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @Auther: guobing
 * @Date: 2019-1-16 10:22
 * @Description:
 */
@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> ,JpaSpecificationExecutor<AccessLog> {

}
