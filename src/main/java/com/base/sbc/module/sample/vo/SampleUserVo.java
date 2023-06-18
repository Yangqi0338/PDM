package com.base.sbc.module.sample.vo;

import com.base.sbc.config.common.annotation.UserAvatar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述： 用户信息
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.sample.vo.SampleUserVo
 * @email li_xianglin@126.com
 * @date 创建时间：2023-06-15 19:52
 */

@Data
@ApiModel("用户信息 SampleUserVo ")
public class SampleUserVo {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "用户名称")
    private String name;
    @ApiModelProperty(value = "用户编码")
    private String userCode;
    @ApiModelProperty(value = "头像")
    @UserAvatar("userId")
    private String avatar;
}
