package com.biao.job.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Scheduled是Spring框架集成的定时任务能力，使用简单方便，因此适合单体服务场景。但使用过可以知道，存在一下瓶颈：
 *
 * 1、无法适配分布式环境下，即使在定时任务中业务逻辑中加分布式锁，但对代码侵袭性太强；
 *
 * 2、无法动态修改定时任务的执行规则，需要重新发布服务上线；
 *
 * 3、需要开发手动编写日志来获取定时任务的元数据信息，譬如执行时间这种功能。
 *
 * 4、其他一些高可用的能力，分片、告警等；
 */
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
