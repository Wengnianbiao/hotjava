package com.biao.job.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CronTask {

    // 每分钟的第 10 秒执行
    @Scheduled(cron = "10 * * * * ?")
    public void execute() {
        // todo 业务逻辑
        System.out.println("Cron task executed at: " + formatDate(new Date()));
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return sdf.format(date);
    }
}
