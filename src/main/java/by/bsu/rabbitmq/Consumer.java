package by.bsu.rabbitmq;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class Consumer {
    
    private static final String URL_PATH =
            "https://rabbitmqtask.azurewebsites.net/api/tagsremoval?code=INhWkU5XlQMlPoRH144mXtBp82TQxoMoeYFrxO4fYabHIlQNULbOLQ==";

    @RabbitListener(queues = {"firstQueue"})
    public void consume(String message) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL_PATH);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("comment", "div"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = client.execute(httpPost);
        String entity = EntityUtils.toString(response.getEntity());

        System.out.println(entity);
        System.out.println(response.getStatusLine().getStatusCode());
    }
//
//    @RabbitListener(queues = "query-example-3-2")
//    public void worker2(String message) {
//        logger.info("accepted on worker 2 : " + message);
//    }

}
