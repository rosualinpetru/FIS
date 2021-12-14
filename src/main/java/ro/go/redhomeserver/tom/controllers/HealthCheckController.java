package ro.go.redhomeserver.tom.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthCheckController {
    @GetMapping("/healthcheck")
    public HttpStatus healthCheck() {
        return HttpStatus.OK;
    }
}
