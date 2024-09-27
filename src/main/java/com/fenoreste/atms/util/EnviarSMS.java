package com.fenoreste.atms.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EnviarSMS {

    @Autowired
    private RestTemplate restTemplate;

    public String getDataFromUrl(String url) {
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Respuesta envio SMS: " + response);
        return response;
    }

}
