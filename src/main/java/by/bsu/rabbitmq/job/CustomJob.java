package by.bsu.rabbitmq.job;


import by.bsu.rabbitmq.constant.ParameterConst;
import org.quartz.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CustomJob extends QuartzJobBean {
    private RabbitTemplate template;
    @Autowired
    public void setTemplate(RabbitTemplate template) {
        this.template = template;
    }

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        template.setExchange("fanout");
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String comment = dataMap.get(ParameterConst.PARAMETER_COMMENT).toString();
        template.convertAndSend(comment);
    }


}
