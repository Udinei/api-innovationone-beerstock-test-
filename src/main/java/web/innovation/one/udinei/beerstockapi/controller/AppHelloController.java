package web.innovation.one.udinei.beerstockapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class AppHelloController {

    @GetMapping
    public String getHelloWord() {
        return "Hello, Digital Innovation One!";
    }
}
