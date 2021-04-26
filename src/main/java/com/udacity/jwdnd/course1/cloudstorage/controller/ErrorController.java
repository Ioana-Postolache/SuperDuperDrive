package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

// adapted from: https://www.baeldung.com/custom-error-page-spring-mvc
@Controller
public class ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @RequestMapping(value = "errors", method = RequestMethod.GET)
    public String renderErrorPage(HttpServletRequest httpRequest, Model model) {

        String error;
        int httpErrorCode = getErrorCode(httpRequest);
        String endpoint = String.valueOf(httpRequest.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI));
        Exception ex = ((Exception) httpRequest.getAttribute(RequestDispatcher.ERROR_EXCEPTION));

        switch (httpErrorCode) {
            case 400: {
                error = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                error = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                error = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                error = "Http Error Code: 500. Internal Server Error";
                break;
            }
            default:
                error = "There was an error";
        }
        LOGGER.error(String.format("Error code %s was thrown from %s. Exception was: %s", httpErrorCode, endpoint, ex));
        model.addAttribute("error", error);
        return "error";
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
}
