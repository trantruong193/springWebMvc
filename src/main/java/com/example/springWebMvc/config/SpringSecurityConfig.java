package com.example.springWebMvc.config;

import com.example.springWebMvc.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SpringSecurityConfig {
    private final UserDetailServiceImpl userDetailServiceImpl;
    private final DataSource dataSource;
    @Autowired
    public SpringSecurityConfig(UserDetailServiceImpl userDetailServiceImpl,
                                DataSource dataSource){
        this.dataSource = dataSource;
        this.userDetailServiceImpl = userDetailServiceImpl;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
//    @Bean
//    public  AuthenticationSuccessHandler handler(){
//        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
//        successHandler.setUseReferer(true);
//        return successHandler;
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http
                .csrf().disable()
                .authorizeHttpRequests()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/site/customer/**").authenticated()
                    .anyRequest().permitAll()
                .and()
                .formLogin()
                    .permitAll()
                    .loginPage("/login")
                    .defaultSuccessUrl("/site")
                    .successHandler((request, response, authentication) -> {
                        GrantedAuthority adm = new SimpleGrantedAuthority("ROLE_ADMIN");
                        GrantedAuthority cus = new SimpleGrantedAuthority("ROLE_CUSTOMER");
                        if(authentication.getAuthorities().contains(adm))
                            response.sendRedirect("/admin");
                        else if(authentication.getAuthorities().contains(cus))
                            response.sendRedirect("/site/customer");
                        else
                            response.sendRedirect("/site");
                    })
                .and()
                .logout()
                    .permitAll()
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/site")
                .and()
                .rememberMe().tokenRepository(persistentTokenRepository())
                .and()
                .exceptionHandling().accessDeniedPage("/errorPage");

        return http.build();
    }
}
