package edu.sia.credigo.models;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/")
    public String helloWorld() {
        return "<html><body><h1>Hello, World!</h1></body></html>";
    }
}





