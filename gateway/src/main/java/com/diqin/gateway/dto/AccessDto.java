package com.diqin.gateway.dto;

import com.diqin.gateway.enums.AccessStateEnum;
import lombok.Data;

import javax.persistence.Access;

/**
 * Created by weslie on 2019/1/23.
 */
@Data
public class AccessDto {

    private String userId;

    private Long personId;

    private String accessTime;

    private String remark = "系统默认";

    private AccessStateEnum accessStateEnum = AccessStateEnum.SYSTEM;

}
