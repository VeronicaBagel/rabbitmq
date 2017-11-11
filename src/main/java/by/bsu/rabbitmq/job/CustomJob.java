package by.bsu.rabbitmq.job;


import org.quartz.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class CustomJob extends QuartzJobBean {

    @Autowired
    private RabbitTemplate template;

    public final String COMMENT = "<div style='font:20px;'><p>Awesome movie</p></div>";

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        template.setExchange("fanout");
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String comment = dataMap.getString("comment");
        template.convertAndSend(comment);
    }

    public void setTemplate(RabbitTemplate template) {
        this.template = template;
    }
}
