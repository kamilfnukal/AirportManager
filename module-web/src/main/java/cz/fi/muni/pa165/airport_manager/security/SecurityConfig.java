package cz.fi.muni.pa165.airport_manager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/dashboard").hasAnyAuthority( "MANAGER", "ADMIN")
                .antMatchers("/airplane/new", "/airplane/create", "/airplane/{{\\d+}}/edit", "/airplane/update", "/airplane/{{\\d+}}/delete").hasAnyAuthority("ADMIN")
                .antMatchers("/steward/new", "/steward/create", "/steward/{{\\d+}}/edit", "/steward/update", "/steward/{{\\d+}}/delete").hasAnyAuthority("ADMIN")
                .antMatchers("/airport/new", "/airport/create", "/airport/{{\\d+}}/edit", "/airport/update", "/airport/{{\\d+}}/delete").hasAnyAuthority("ADMIN")
                .antMatchers("/flight/list", "/flight/dates", "/flight/{{\\d+}}/dates", "/flight/new", "/flight/create", "/flight/{{\\d+}}/edit", "/flight/update", "/flight/{{\\d+}}/delete").hasAnyAuthority("MANAGER")
                .antMatchers("/manager/m_profile", "/manager/m_update", "/manager/m_update_password").hasAnyAuthority("MANAGER")
                .antMatchers("/admin/a_profile", "/admin/a_update", "/admin/a_update_password").hasAnyAuthority("ADMIN")
                .antMatchers("/css/**", "/webjars/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin(form -> {
                    try {
                        form.defaultSuccessUrl("/dashboard")
                            .failureUrl("/login?error=true")
                            .loginPage("/login").permitAll()
                            .and()
                            .logout().permitAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/rest/stewards");
    }

}