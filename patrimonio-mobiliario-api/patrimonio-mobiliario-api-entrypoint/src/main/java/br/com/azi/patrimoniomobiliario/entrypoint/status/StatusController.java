package br.com.azi.patrimoniomobiliario.entrypoint.status;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/status")
public class StatusController {

    @GetMapping
    public String status() {
        return "{\"status\" : \"up\" }";
    }
}
