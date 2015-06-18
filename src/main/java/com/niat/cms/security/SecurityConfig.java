package com.niat.cms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author dunknown
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll()
            .and()
            .rememberMe()
                .tokenValiditySeconds(8 * 604800) //8 weeks
                .key("niat_cms_key")
            .and()
            .authorizeRequests()
                .antMatchers("/users.**").hasRole("ADMIN")
                .antMatchers("/users/*/setrole/*").hasRole("ADMIN")
                .antMatchers("/sortmain").hasAnyRole("ADMIN", "EDITOR")
                .antMatchers("/addmaterial.**").hasAnyRole("ADMIN", "EDITOR", "AUTHOR")
                .antMatchers("/todrafts").hasAnyRole("ADMIN", "EDITOR", "AUTHOR")
                .antMatchers("/drafts/**").hasAnyRole("ADMIN", "EDITOR", "AUTHOR")
                .antMatchers("/modertasks/**").hasAnyRole("ADMIN", "EDITOR", "CORRECTOR")
                .antMatchers("/moderate/**").hasAnyRole("ADMIN", "EDITOR", "CORRECTOR")
                .antMatchers("/favourites/**").authenticated()
                .antMatchers("/material/*/addtofavs").authenticated()
                .antMatchers("/material/*/unfav").authenticated()
                .anyRequest().permitAll();
    }
}
