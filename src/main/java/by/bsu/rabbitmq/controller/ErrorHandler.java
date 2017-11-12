package by.bsu.rabbitmq.controller;

import by.bsu.rabbitmq.model.ErrorModel;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice ("error")
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    public String handleEmptyData(Exception ex) {
        ModelAndView model = new ModelAndView();
        model.addObject("exception", new ErrorModel(ex));
        return "error";
    }
}
