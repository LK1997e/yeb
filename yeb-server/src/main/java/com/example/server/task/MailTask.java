package com.example.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.server.pojo.Employee;
import com.example.server.pojo.MailConstants;
import com.example.server.pojo.MailLog;
import com.example.server.service.IEmployeeService;
import com.example.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MailTask {
    @Autowired
    private IMailLogService mailLogService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 邮件发送定时任务
     * 10s执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void maskTask() {
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>()
                .eq("status", 0).lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            //如果重试次数超过三次，更新状态为投递失败，不再重试
            if (MailConstants.MAX_TRY_COUNT <= mailLog.getCount()) {
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", MailConstants.FAILURE).eq("msgId", mailLog.getMsgId()));
            } else {
                mailLogService.update(new UpdateWrapper<MailLog>()
                        .set("count", mailLog.getCount() + 1)
                        .set("updateTime", LocalDateTime.now())
                        .set("tryTime", LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT))
                        .eq("msgId", mailLog.getMsgId()));
                Employee employee = employeeService.getEmployee(mailLog.getEid()).get(0);
                rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME, MailConstants.MAIL_ROUTING_KEY_NAME, employee
                        , new CorrelationData(mailLog.getMsgId()));
            }
        });
    }
}
