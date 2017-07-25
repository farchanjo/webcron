package br.eti.archanjo.webcron.configs;

import br.eti.archanjo.webcron.constants.ExceptionConstants;
import br.eti.archanjo.webcron.constants.PathContants;
import br.eti.archanjo.webcron.filters.CsrfHeaderFilter;
import br.eti.archanjo.webcron.filters.CustomAuthenticationEntryPoint;
import br.eti.archanjo.webcron.providers.SecurityProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProvider provider;
    private final CsrfHeaderFilter csrfHeaderFilter;

    @Autowired
    public SecurityConfig(SecurityProvider provider, CsrfHeaderFilter csrfHeaderFilter) {
        this.provider = provider;
        this.csrfHeaderFilter = csrfHeaderFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/webjars/**", "/js/**", "/css/**", "/views/**", "/*").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .addFilterAfter(csrfHeaderFilter, CsrfFilter.class)
                .formLogin()
                .failureHandler(
                        (request, response, exception) -> response.sendError(400, ExceptionConstants.PASSWORD_DOES_NOT_MATCH)
                )
                .successHandler(
                        (request, response, authentication) -> response.sendError(200, "logged")
                )
                .loginPage(PathContants.API + PathContants.LOGIN).passwordParameter("password").usernameParameter("emailorusername").permitAll()
                .and()
                .logout().logoutUrl(PathContants.API + PathContants.LOGOUT).permitAll()
                .invalidateHttpSession(true)
                .logoutSuccessHandler(
                        (request, response, authentication) -> response.sendError(200, "logout ok")
                )
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAuthenticationEntryPoint()).authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .headers().xssProtection().block(true)
                .and()
                .frameOptions().sameOrigin()
                .and()
                .csrf()
                .ignoringAntMatchers(PathContants.API + PathContants.LOGIN + "/**", PathContants.API + PathContants.LOGOUT + "/**")
                .csrfTokenRepository(csrfTokenRepository());
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider);
    }

}