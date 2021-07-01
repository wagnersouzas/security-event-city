package com.devsuperior.bds04.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private Environment environment;

    private JwtTokenStore tokenStore;

    private static final String[] PUBLIC = {"/oauth/token", "/h2-console/**"};

    private static final String[] PATH_GET = {"/cities/**", "/events/**"};

    private static final String[] PATH_POST_CLIENT = {"/events/**"};

    @Autowired
    public ResourceServerConfig(JwtTokenStore tokenStore,
                                Environment environment) {
        this.tokenStore = tokenStore;
        this.environment = environment;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources){
        resources.tokenStore(tokenStore);
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {

        //para liberar o H2 console
        if(Arrays.asList(environment.getActiveProfiles()).contains("teste")){
            http.headers().frameOptions().disable();
        }

        http.authorizeRequests()
            .antMatchers(PUBLIC).permitAll()
            .antMatchers(HttpMethod.GET, PATH_GET).permitAll()
            .antMatchers(HttpMethod.POST, PATH_POST_CLIENT).hasAnyRole("CLIENT", "ADMIN")
            .anyRequest().hasAnyRole("ADMIN");
    }
}
