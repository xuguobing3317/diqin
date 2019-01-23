package com.diqin.gateway.Schedule;

import com.diqin.gateway.service.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by weslie on 2019/1/23.
 */
@Component
@Configurable
@EnableScheduling
public class ScheduleTasks{

    @Autowired
    private AccessService accessService;

    //每1分钟执行一次
    @Scheduled(cron = "0 */1 *  * * * ")
    public void reportCurrentByCron(){
        accessService.doBatchAccess();
    }

}