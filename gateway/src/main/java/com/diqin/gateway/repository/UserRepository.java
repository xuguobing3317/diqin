package com.diqin.gateway.repository;

import com.diqin.gateway.mapper.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Auther: guobing
 * @Date: 2019-1-16 10:22
 * @Description:
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUserName(String userName);

}
