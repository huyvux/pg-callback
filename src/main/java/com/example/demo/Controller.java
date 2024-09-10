package com.example.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class Controller {
    @RequestMapping(value = "/withdraw/{paymentChannel}", method = { RequestMethod.GET, RequestMethod.POST} )
    public ResponseEntity<?> submitPaymentGatewayWithdrawPOSTCallback(@PathVariable String paymentChannel, HttpServletRequest request) {
        System.out.println(getHeadersInfo(paymentChannel, request).toString());
        System.out.println(getBodyInfo(paymentChannel, request).toString());
        return ResponseEntity.ok().body("success");
    }

    public Map<String, String> getHeadersInfo(String paymentGateway, HttpServletRequest httpServletRequest) {
        try {
            Map<String, String> map = new HashMap<>();
            Enumeration<?> headerNames = httpServletRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = httpServletRequest.getHeader(key);
                map.put(key, value);
            }

            return map;
        } catch (Exception ex) {
            return Map.of();
        }
    }

    public Map<String, String> getBodyInfo(String paymentGateway, HttpServletRequest httpServletRequest) {
        try {
            Map<String, String> requestMap = new HashMap<>();

            // get body content from parameter map
            httpServletRequest.getParameterMap().entrySet().forEach(value -> {
                requestMap.put(value.getKey(), value.getValue()[0]);
            });

            // get body content from input stream
            if(httpServletRequest.getInputStream().available() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = mapper.readValue(httpServletRequest.getInputStream(), Map.class);
                for (Map.Entry<String,String> entry : map.entrySet()) {
                    requestMap.put(entry.getKey(), entry.getValue());
                }
            }

            return requestMap;
        } catch (Exception ex) {
            return Map.of();
        }
    }
}