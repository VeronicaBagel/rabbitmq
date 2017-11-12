package by.bsu.rabbitmq.job;

import by.bsu.rabbitmq.constant.ParameterConst;
import by.bsu.rabbitmq.util.JsonTransformationsUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.quartz.SchedulerException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@PropertySource ("classpath:azure.properties")
public class Worker {
    private Logger logger = Logger.getLogger(Worker.class);

    @Value("${url.request.path}")
    private String requestUrl;

    @Value("${url.response.path}")
    private String responseUrl;

    @Value("${settings.contentType}")
    private String contentType;

    @Value("${settings.charset}")
    private String charset;

    @RabbitListener(queues = {"messages"})
    public void worker(String message) throws IOException, SchedulerException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put(ParameterConst.PARAMETER_COMMENT, message);

        CloseableHttpResponse response = callAzureFunctions(jsonRequest);
        String stringResponse = EntityUtils.toString(response.getEntity());

        logger.info("Processed comment: " + stringResponse);
        System.out.println(stringResponse);

        callResponseProcessing(stringResponse);
    }

    private CloseableHttpResponse callAzureFunctions(JSONObject jsonObject) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(requestUrl);

        StringEntity entity = new StringEntity(jsonObject.toString(), charset);
        entity.setContentType(contentType);
        httpPost.setEntity(entity);

        CloseableHttpResponse response = client.execute(httpPost);

        return response;
    }

    private void callResponseProcessing(String stringResponse) throws IOException {
        String jsonLikeResponse = JsonTransformationsUtil.setUpJsonLikeStringContent(stringResponse);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(responseUrl);

        StringEntity entity = new StringEntity(jsonLikeResponse, charset);
        entity.setContentType(contentType);
        httpPost.setEntity(entity);

        client.execute(httpPost);
    }

}
