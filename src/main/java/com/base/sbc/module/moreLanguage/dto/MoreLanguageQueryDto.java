package com.base.sbc.module.moreLanguage.dto;

import com.base.sbc.config.common.base.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 17:16:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MoreLanguageQueryDto extends Page {

    @NotBlank(message = "国家语言不能为空")
    @ApiModelProperty(value = "查询国家语言条件标签Id")
    private String countryLanguageId;

}
