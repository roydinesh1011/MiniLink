package com.droy.sample.miniLink.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class is used for adding the CORS params to the response before sending back to client
 */
public class ResponseInterceptor implements HandlerInterceptor {

    /**
     * This method is used to add the CORS params onto the reponse
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
        // Logic to execute after the request is handled, but before the view is rendered
    }

}