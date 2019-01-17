package com.diqin.gateway.service;

import com.diqin.gateway.common.ResponseDto;
import com.diqin.gateway.dto.PersonDto;
import com.diqin.gateway.dto.PersonPageQueryDto;
import com.diqin.gateway.mapper.Person;
import com.diqin.gateway.repository.AccessLogRepository;
import com.diqin.gateway.repository.PersonRepository;
import com.diqin.gateway.utils.DateStyle;
import com.diqin.gateway.utils.DateUtils;
import freemarker.template.utility.DateUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 用户管理，主要用于用户相关业务
 *
 * @Auther: guobing
 * @Date: 2019-1-15 10:59
 * @Description:
 */
@Data
@Service
public class AccessService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AccessLogRepository accessLogRepository;

    private Logger log = LoggerFactory.getLogger(AccessService.class);

    public ResponseDto getPersonPage(PersonPageQueryDto queryDto,
                                     int pageSize, int pageNum) {

        Pageable pageable = new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "id");

        Page<Person> personPage = personRepository.findAll(new Specification<Person>() {
            @Override
            public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(queryDto.getUserId())) {
                    list.add(criteriaBuilder.equal(root.get("userId").as(String.class),
                            queryDto.getUserId()));
                }
                if (!StringUtils.isEmpty(queryDto.getAccessTime())) {
                    list.add(criteriaBuilder.equal(root.get("nextAccessTime").as(String.class),
                            queryDto.getAccessTime()));
                }
                if (!StringUtils.isEmpty(queryDto.getCreateTime())) {
                    list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(Date.class),
                            DateUtils.StringToDate(queryDto.getCreateTime() + " 00:00:00", DateStyle.YYYY_MM_DD_HH_MM_SS)));
                    list.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime").as(Date.class),
                            DateUtils.StringToDate(queryDto.getCreateTime() + " 23:59:59", DateStyle.YYYY_MM_DD_HH_MM_SS)));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);


        List<Person> personList = personPage.getContent();
        List<PersonDto> personDtoList = new ArrayList<>();
        personList.forEach((item) -> {
            PersonDto personDto = new PersonDto();
            BeanUtils.copyProperties(item, personDto);
            personDtoList.add(personDto);
        });
        Page<PersonDto> personDtoPage =
                new PageImpl<>(
                        personDtoList, pageable, personPage.getTotalElements()
                );

        return ResponseDto.doSuccess(personDtoPage);
    }


}
