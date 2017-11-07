package by.bsu.rabbitmq.controller;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RabbitMQController {
    @Autowired
    private RabbitTemplate template;

    @RequestMapping("/")
    String home() {
        return "Empty mapping";
    }

    @PostMapping("/comment")
    String emit() {
        template.setExchange("exchange-example-3");
        template.convertAndSend("Fanout message");
        return "Emit to exchange-example-3";
    }
}