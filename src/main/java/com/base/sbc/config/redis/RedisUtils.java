
package com.base.sbc.config.redis;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *
 * @author 王赛超
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法                    不含通用方法
 * 针对所有的List 都是以l开头的方法
 */
@Component
public class RedisUtils extends RedisFunUtils {

	public RedisUtils() {
		setRedisTemplate(0);
	}
}
