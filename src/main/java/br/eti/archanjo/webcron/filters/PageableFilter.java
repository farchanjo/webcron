package br.eti.archanjo.webcron.filters;

import br.eti.archanjo.webcron.configs.PropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by fabricio on 17/02/16.
 **/
@Component
@Order(999)
public class PageableFilter implements Filter {

    private final PropertiesConfig config;

    @Autowired
    public PageableFilter(PropertiesConfig config) {
        this.config = config;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new CustomQueryParamNormalizer(config.getHttplimits().getLimit(), config.getHttplimits().getPage(), (HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {

    }
}