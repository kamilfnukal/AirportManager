package cz.fi.muni.pa165.airport_manager.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/rest").permitAll()
                .antMatchers("/rest/stewards").permitAll()
                .antMatchers("/rest/stewards/{{\\d+}}").permitAll()
                .antMatchers("/rest/stewards/available").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable();
        http.cors().and().csrf().disable();
    }

}
