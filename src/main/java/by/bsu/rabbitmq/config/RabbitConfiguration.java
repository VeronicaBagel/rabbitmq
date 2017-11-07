package by.bsu.rabbitmq.config;

import org.apache.log4j.Logger;
import org.quartz.spi.JobFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@EnableRabbit
@Configuration
public class RabbitConfiguration {
    private final Logger logger = Logger.getLogger(RabbitConfiguration.class);
    private static final String CLOUDAMQP_URL = "amqp://guest:guest@localhost:5672/%2f";

    @Bean
    @Scope("singleton")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("localhost");
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return rabbitTemplate;
    }

//    @Bean
//    public JobFactory jobFactory(ApplicationContext applicationContext) {
//        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
//        jobFactory.setApplicationContext(applicationContext);
//        return jobFactory;
//    }

//    @Bean
//    public CustomJob myJob(){
//        CustomJob job = new CustomJob();
//        job.setTemplate(rabbitTemplate());
//        return job;
//    }

    @Bean
    public Queue firstQueue() {
        return new Queue("firstQueue");
    }

//    @Bean
//    public Queue myQueue2() {
//        return new Queue("query-example-3-2");
//    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanout");
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(firstQueue()).to(fanoutExchange());
    }
}
