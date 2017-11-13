package by.bsu.rabbitmq.job;


import by.bsu.rabbitmq.config.RabbitConfiguration;
import by.bsu.rabbitmq.constant.ParameterConst;
import org.quartz.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class CustomJob extends QuartzJobBean {
    private RabbitTemplate template;
    @Autowired
    public void setTemplate(RabbitTemplate template) {
        this.template = template;
    }

    private Properties rabbitProperties;
    @Autowired
    @Qualifier ("rabbitMQProperties")
    public void setRabbitConfiguration(Properties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        template.setExchange(rabbitProperties.getProperty("rabbitmq.exchange"));
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String comment = dataMap.get(ParameterConst.PARAMETER_COMMENT).toString();
        template.convertAndSend(comment);
    }
}
