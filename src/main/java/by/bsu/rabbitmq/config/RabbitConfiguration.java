package by.bsu.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

@EnableRabbit
@Configuration
public class RabbitConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory =
//                new CachingConnectionFactory(rabbitMQPropertiesAsAWhole().getProperty("rabbitmq.vmware.localhost"));
//        connectionFactory.setUsername(rabbitMQPropertiesAsAWhole().getProperty("rabbitmq.username"));
//        connectionFactory.setPassword(rabbitMQPropertiesAsAWhole().getProperty("rabbitmq.password"));
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
        return new Queue(rabbitMQPropertiesAsAWhole().getProperty("rabbitmq.queue"));
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(rabbitMQPropertiesAsAWhole().getProperty("rabbitmq.exchange"));
    }

    @Bean
    public Binding firstBinding(){
        return BindingBuilder.bind(firstQueue()).to(fanoutExchange());
    }

    @Bean (name = "rabbitMQProperties")
    public Properties rabbitMQPropertiesAsAWhole() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("rabbitmq.properties"));
        Properties properties;

        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load rabbitmq.properties", e);
        }

        return properties;
    }
}
