package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户登录返回的数据格式")
public class UserLoginVO implements Serializable {

    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("微信用户openid")
    private String openid;
    @ApiModelProperty("jwt令牌")
    private String token;

}
