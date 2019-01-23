package com.diqin.gateway.service;

import com.diqin.gateway.common.ResponseDto;
import com.diqin.gateway.dto.AccessDto;
import com.diqin.gateway.dto.AccessPageQueryDto;
import com.diqin.gateway.dto.PersonDto;
import com.diqin.gateway.dto.PersonPageQueryDto;
import com.diqin.gateway.enums.AccessStateEnum;
import com.diqin.gateway.enums.ResponseCodeEnum;
import com.diqin.gateway.mapper.AccessLog;
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
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;


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



    public ResponseDto doBatchAccess() {
        String currentDate = DateUtils.getDate(new Date());
        List<Person> personList = personRepository.findListByAccessTime(currentDate);
        personList.forEach(
                item -> {
                    AccessDto accessDto = new AccessDto();
                    accessDto.setUserId(item.getUserId());
                    accessDto.setPersonId(item.getId());
                    accessDto.setAccessTime(currentDate);
                    doAccess(accessDto);
                }
        );
        return ResponseDto.doSuccess();
    }


    @Transactional
    public ResponseDto doAccess(AccessDto accessDto) {
        String userId = accessDto.getUserId();
        long personId = accessDto.getPersonId();
        String accessTime = accessDto.getAccessTime();
        String remark = accessDto.getRemark();
        AccessStateEnum accessStateEnum = accessDto.getAccessStateEnum();

        Person person = personRepository.findOne(personId);
        if (null == person) {
            return ResponseDto.doRet(ResponseCodeEnum.PARAM_ERROR);
        }

        if (!person.getUserId().equals(userId)) {
            return ResponseDto.doRet(ResponseCodeEnum.DATA_AUTHOR_ERROR);
        }
        person.setRemark(remark);
        person.setLastAccessTime(accessTime);
        person.setNextAccessTime(getNextAccessTime(person));
        personRepository.saveAndFlush(person);


        AccessLog accessLog = new AccessLog();
        accessLog.setAccessTime(accessTime);
        accessLog.setPersonId(personId);
        accessLog.setState(accessStateEnum.getCode());
        accessLog.setCreateTime(new Date());
        accessLog.setUpdateTime(accessLog.getCreateTime());
        accessLog.setRemark(remark);
        accessLogRepository.save(accessLog);

        return ResponseDto.doSuccess();
    }


    String getNextAccessTime(Person person){
        String accessTime = person.getNextAccessTime();
        if (StringUtils.isEmpty(accessTime)) {
            accessTime = DateUtils.getDate(new Date());
        }
        Date _date = DateUtils.StringToDate(accessTime,DateStyle.YYYY_MM_DD);
        int count = person.getCount();
        String unit = person.getUnit();
        Date retDate;
        if (unit.toUpperCase().equals("M")) {
            retDate = DateUtils.addMonth(_date,count);
        } else {
            retDate = DateUtils.addDay(_date, count);
        }
        return DateUtils.DateToString(retDate, DateStyle.YYYY_MM_DD);

    }


    public ResponseDto getAccessPage(AccessPageQueryDto queryDto,
                                     int pageSize, int pageNum) {
        Pageable pageable = new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "createTime");

        Page<AccessLog> accessLogPage = accessLogRepository.findAll(new Specification<AccessLog>() {
            @Override
            public Predicate toPredicate(Root<AccessLog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(queryDto.getPersonId())) {
                    list.add(criteriaBuilder.equal(root.get("personId").as(Long.class),
                            queryDto.getPersonId()));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        }, pageable);


        return ResponseDto.doSuccess(accessLogPage);
    }

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


    public ResponseDto getPersonPage2(PersonPageQueryDto queryDto,
                                     int pageSize, int pageNum) {

        Pageable pageable = new PageRequest(pageNum, pageSize, Sort.Direction.DESC, "id");

        Page<PersonDto> personDtoPage =
                personRepository.findListPage(queryDto.getUserId(), queryDto.getAccessTime(), pageable);

         return ResponseDto.doSuccess(personDtoPage);
    }



    public ResponseDto getCount(String userId) {
        List countList = personRepository.findCountByDate(userId);
        List<Map> list = new ArrayList<>();
        countList.forEach(item -> {
            Object[] cells = (Object[]) item;
            String count = cells[0].toString();
            String accessTime = cells[1].toString();
            Map _map = new HashMap();
            _map.put("count",count);
            _map.put("accessTime",accessTime);
            list.add(_map);
        });

        return ResponseDto.doSuccess(list);

    }


    public ResponseDto findList(String userId, String accessTime) {
        List countList = personRepository.findList(userId,accessTime);
        return ResponseDto.doSuccess(countList);
    }


    public static void main(String[] args) {
        AccessService accessService = new AccessService();
        Person person = new Person();

        person.setUnit("M");
        person.setCount(3);
        person.setNextAccessTime("2019-01-23");
        System.out.println(accessService.getNextAccessTime(person));

    }




}
