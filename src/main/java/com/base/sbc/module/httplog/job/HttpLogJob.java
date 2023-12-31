package com.base.sbc.module.httplog.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.base.sbc.module.httplog.entity.HttpLog;
import com.base.sbc.module.httplog.service.HttpLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author 卞康
 * @date 2023/6/1 10:43:58
 * @mail 247967116@qq.com
 */
@Component
@RequiredArgsConstructor
public class HttpLogJob {
    private final HttpLogService httpLogService;

    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private static final int MONTH = -1;

    /**
     * 每日凌晨12点执行，清理日志时间超过一个礼拜的日志
     */
    @Scheduled(cron = "0 0 0 * * ?")
    // 每分钟执行
    //@Scheduled(cron = "0 * * * * ?")
    public void deleteLogs() {
        LocalDateTime limit = LocalDateTime.now().plusWeeks(MONTH);
        String format = limit.format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
        httpLogService.remove(new QueryWrapper<HttpLog>().lt("create_date",format));
    }
}
