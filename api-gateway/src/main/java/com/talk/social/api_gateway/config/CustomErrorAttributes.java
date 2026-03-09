package com.talk.social.api_gateway.config;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, options);
        
        if ((int) map.get("status") == 429) {
            map.put("message", "Upload limit exceeded. Please wait a moment.");
            map.put("code", "RATE_LIMIT_EXCEEDED");
            // In 2026, we often add a standard 'retryAfter' hint
            map.put("retryAfterSeconds", 5); 
        }
        return map;
    }
}