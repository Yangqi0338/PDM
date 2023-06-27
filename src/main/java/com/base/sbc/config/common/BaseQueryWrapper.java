package com.base.sbc.config.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

/**
 * @author 卞康
 * @date 2023/6/12 19:36:07
 * @mail 247967116@qq.com
 */
public class BaseQueryWrapper<T> extends QueryWrapper<T> {
    public QueryWrapper<T> notEmptyEq(String column, Object val) {
        return this.eq(!StringUtils.isEmpty(val), column, val);
    }

    public QueryWrapper<T> notEmptyLike(String column, Object val) {
        return this.like(!StringUtils.isEmpty(val), column, val);
    }

    public QueryWrapper<T> between(String column, String[] strings) {
        if (strings != null && strings.length > 0) {
            this.ge(!StringUtils.isEmpty(strings[0]), column, strings[0]);
            if (strings.length > 1) {
                return this.and(i -> i.le(!StringUtils.isEmpty(strings[1]), column, strings[1]));
            }
        }
        return this;
    }
}
