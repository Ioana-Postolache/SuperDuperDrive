package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

// source: https://www.baeldung.com/custom-error-page-spring-mvc
@Controller
public class ErrorController {

    @RequestMapping(value = "errors", method = RequestMethod.GET)
    public String renderErrorPage(HttpServletRequest httpRequest, Model model) {

        String error;
        int httpErrorCode = getErrorCode(httpRequest);

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
        model.addAttribute("error", error);
        return "error";
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
}
