package by.bsu.rabbitmq.model;


public class ErrorModel {
    public final String msg;

    public ErrorModel(Exception exception) {
        this.msg = exception.getLocalizedMessage();
    }

    @Override
    public String toString() {
        return "ErrorModel{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
