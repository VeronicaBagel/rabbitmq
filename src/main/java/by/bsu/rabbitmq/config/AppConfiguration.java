package by.bsu.rabbitmq.config;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.HashMap;
import java.util.Map;

@EnableRabbit
@Configuration
public class AppConfiguration {
    @Bean
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

    @Bean
    public Queue firstQueue() {
        return new Queue("queue3");
    }

    @Bean
    public Queue secondQueue() {
        return new Queue("queue4");
    }


    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanout");
    }

    @Bean
    public Binding firstBinding(){
        return BindingBuilder.bind(firstQueue()).to(fanoutExchange());
    }


    @Bean
    public Binding secondBinding(){
        return BindingBuilder.bind(secondQueue()).to(fanoutExchange());
    }

}
