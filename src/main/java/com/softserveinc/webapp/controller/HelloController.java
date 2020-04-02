package com.softserveinc.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {
    @GetMapping
    public String doHelloWorld() {
        return " <!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>" +
                "<p> Hello, %username%. </p>" +
                "<p> This is CRUD API for managing user entities.</p> " +
                "<p> Documentation can be found " +
                " <a href=/static/docs/index.html>here</a></p> " +
                "</body>\n" +
                "</html>\n";
    }
}
