package com.diqin.gateway.repository;

import com.diqin.gateway.dto.PersonDto;
import com.diqin.gateway.mapper.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: guobing
 * @Date: 2019-1-16 10:22
 * @Description:
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, String>
        ,JpaSpecificationExecutor<Person> {

    @Query("SELECT COUNT(*) AS _count, t.nextAccessTime AS accessTime FROM t_person t where t.userId=:userId GROUP BY t.nextAccessTime")
    List findCountByDate(@Param("userId") String userId);


    @Query("SELECT t FROM t_person t where t.userId=:userId and t.nextAccessTime >= :accessTime")
    List findList(@Param("userId") String userId, @Param("accessTime") String accessTime);



    @Query(value="SELECT new com.diqin.gateway.dto.PersonDto(c.userId, c.userName," +
            "c.age, c.mobile, c.address, c.weixin) FROM t_person c where c.userId=:userId",
            countQuery = "SELECT count(*) FROM t_person c where userId=:userId")
    public Page<PersonDto> findListAge(@Param("userId")String userId, Pageable pageable);

}
