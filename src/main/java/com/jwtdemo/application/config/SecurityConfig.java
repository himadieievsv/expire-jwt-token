package com.jwtdemo.application.config;

import com.jwtdemo.application.auth.DemoAuthenticationFilter;
import com.jwtdemo.application.auth.DemoAuthenticationProvider;
import com.jwtdemo.application.service.JwtService;
import com.jwtdemo.domain.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/login").permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().denyAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .enableSessionUrlRewriting(false)
                .and()
                .addFilterAfter(demoAuthenticationFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public DemoAuthenticationProvider defaultAuthProvider(JwtService jwtService, UserService userService) {
        return new DemoAuthenticationProvider(jwtService, userService);
    }

    private DemoAuthenticationFilter demoAuthenticationFilter() throws Exception {
        var filter = new DemoAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }
}
