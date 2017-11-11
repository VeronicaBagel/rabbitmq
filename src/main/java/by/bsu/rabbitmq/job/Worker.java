package by.bsu.rabbitmq.job;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Component
@PropertySource("classpath:config.properties")
public class Worker {
    @Value("${url.path}")
    private String url;

    @Value("${parameter.comment}")
    private String parameterComment;

    @Value("${settings.contentType}")
    private String contentType;

    @Value("${settings.charset}")
    private String charset;

    @RabbitListener(queues = {"queue3", "queue4"})
    public void worker(String message) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put(parameterComment, message);

        StringEntity entity = new StringEntity(jsonObj.toString(), charset);
        entity.setContentType(contentType);
        httpPost.setEntity(entity);

        CloseableHttpResponse response = client.execute(httpPost);

        String stringResponse = EntityUtils.toString(response.getEntity());

        System.out.println(stringResponse);

//        return stringResponse;
    }

}
