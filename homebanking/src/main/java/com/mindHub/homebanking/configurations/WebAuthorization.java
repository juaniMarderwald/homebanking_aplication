package com.mindHub.homebanking.configurations;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()

                //Permisos para el ADMIN
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/api/**").hasAuthority("ADMIN")
                .antMatchers("/manager.html").hasAuthority("ADMIN")
                .antMatchers("/manager_addLoan.html").hasAuthority("ADMIN")
                .antMatchers("/h2-console/**").hasAuthority("ADMIN")


                //Permisos para el CLIENT
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/clients/current").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/transaction").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "/api/**").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/api/**").hasAuthority("CLIENT")

                .antMatchers("/api/**").hasAuthority("CLIENT")
                .antMatchers("/accounts.html").hasAuthority("CLIENT")
                .antMatchers("/account.html").hasAuthority("CLIENT")
                .antMatchers("/cards.html").hasAuthority("CLIENT")
                .antMatchers("/404.html").hasAuthority("CLIENT")
                .antMatchers("/criptomonedas.html").hasAuthority("CLIENT")
                .antMatchers("/ayuda.html").hasAuthority("CLIENT")
                .antMatchers("/terminos_de_uso.html").hasAuthority("CLIENT")
                .antMatchers("/create-cards.html").hasAuthority("CLIENT")
                .antMatchers("/transfers.html").hasAuthority("CLIENT")
                .antMatchers("/loan_application.html").hasAuthority("CLIENT")
                .antMatchers("/payment_service.html").hasAuthority("CLIENT")
                .antMatchers("/payment.html").hasAuthority("CLIENT")



                //Permisos para TODOS
                .antMatchers("/index.html").permitAll()
                .antMatchers("/inicio_sesion.html").permitAll()
                .antMatchers("/crear_usuario.html").permitAll();





        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());


    }

    private void clearAuthenticationAttributes(HttpServletRequest request){

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }
    }

}
