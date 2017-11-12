package by.bsu.rabbitmq.model;


import java.io.Serializable;

public class CommentModel implements Serializable {
    private String commentContent;

    public CommentModel() {
    }

    public CommentModel(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Override
    public String toString() {
        return '{' +
                "'commentContent':'" + commentContent + "'" +
                '}';
    }
}
