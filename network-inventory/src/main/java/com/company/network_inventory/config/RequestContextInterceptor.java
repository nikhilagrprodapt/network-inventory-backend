package com.company.network_inventory.config;

import com.company.network_inventory.util.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class RequestContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Optional: user can pass actor name from Postman header: X-Actor: Nikhil
        String actor = request.getHeader("X-Actor");
        if (actor == null || actor.isBlank()) actor = "SYSTEM";

        RequestContext.setActor(actor);
        RequestContext.setRequestId(UUID.randomUUID().toString());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        RequestContext.clear();
    }
}
