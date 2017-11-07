package by.bsu.rabbitmq.job;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class CustomJob extends QuartzJobBean {
//    @Autowired
    private RabbitTemplate template = new RabbitTemplate(new CachingConnectionFactory("localhost"));

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        template.setExchange("fanout");
        template.convertAndSend("<div style='border:none;'><p>Awesome movie</p></div>");
    }
}
