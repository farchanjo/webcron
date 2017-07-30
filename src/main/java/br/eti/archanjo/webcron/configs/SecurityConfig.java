package br.eti.archanjo.webcron.configs;

import br.eti.archanjo.webcron.constants.ExceptionConstants;
import br.eti.archanjo.webcron.constants.PathContants;
import br.eti.archanjo.webcron.filters.CustomAuthenticationEntryPoint;
import br.eti.archanjo.webcron.providers.SecurityProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProvider provider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(SecurityProvider provider,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.provider = provider;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/webjars/**", "/js/**", "/css/**", "/views/**", "/*").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .failureHandler(
                        (request, response, exception) -> response.sendError(400, ExceptionConstants.PASSWORD_DOES_NOT_MATCH)
                )
                .successHandler(
                        (request, response, authentication) -> response.sendError(200, "logged")
                )
                .loginPage(PathContants.LOGIN).passwordParameter("password").usernameParameter("emailorusername").permitAll()
                .and()
                .logout().logoutUrl(PathContants.LOGOUT).permitAll()
                .logoutSuccessHandler(
                        (request, response, authentication) -> response.sendError(200, "logout ok")
                )
                .and()
                .rememberMe()
                .and()
                .exceptionHandling().accessDeniedHandler(customAuthenticationEntryPoint).authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .headers().xssProtection().block(true)
                .and()
                .frameOptions().sameOrigin()
                .and()
                .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider);
    }

}