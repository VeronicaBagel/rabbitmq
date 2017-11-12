package by.bsu.rabbitmq.util;


import by.bsu.rabbitmq.model.CommentModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonTransformationsUtil {

    public static CommentModel parseStringToCommentModel(String request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CommentModel commentModel = mapper.readValue(request, CommentModel.class);
        return commentModel;
    }

    public static String setUpJsonLikeStringContent(String request) {
        return request.replaceAll("^\"|\"$", "")
                .replaceAll("'", "\"");
    }
}
