package com.blog.miniblog.config;

import com.blog.miniblog.controller.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebSecurity extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
                .excludePathPatterns("/v1/user/signup")
                .excludePathPatterns("/v1/user/login");;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()//.anyRequest().authenticated()
                .antMatchers(HttpMethod.POST, "/v1/user/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/user/login").permitAll()
                //.anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
