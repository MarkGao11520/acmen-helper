package com.acmen.acmenhelper.exception;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chenjiaqiang on 2018/5/18.
 */
@Slf4j
public class ExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @Nullable Object o, Exception e) {
        log.error("{} Exception" , httpServletRequest.getRequestURI() , e);
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        // 因为使用的是jackson1.x，所以要使用MappingJacksonJsonView。否则可以使用MappingJackson2JsonView
        modelAndView.addObject("status" , ResponseCode.ERROR.getCode());
        modelAndView.addObject("msg" , "接口异常，详情查看服务端日志");
        modelAndView.addObject("data" , ex.toString());
        return modelAndView;
    }
}
