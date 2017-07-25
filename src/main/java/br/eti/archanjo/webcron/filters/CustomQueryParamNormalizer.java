package br.eti.archanjo.webcron.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * Created by fabricio on 11/02/16.
 **/
public class CustomQueryParamNormalizer extends HttpServletRequestWrapper {
    private Integer page;
    private Integer limit;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public CustomQueryParamNormalizer(int limit, int page, HttpServletRequest request) {
        super(request);
        this.limit = limit;
        this.page = page;
    }

    /**
     * @return Enumeration
     */
    @Override
    public Enumeration<String> getParameterNames() {
        HttpServletRequest request = (HttpServletRequest) getRequest();
        List<String> enumeration = Collections.list(request.getParameterNames());

        if (!enumeration.contains("limit")) {
            enumeration.add("limit");
        }

        if (!enumeration.contains("page")) {
            enumeration.add("page");
        }

        return Collections.enumeration(enumeration);
    }

    /**
     * @param name This gonna get the value from params.
     * @return String
     */
    @Override
    public String getParameter(String name) {
        HttpServletRequest request = (HttpServletRequest) getRequest();
        String parameter = request.getParameter(name);

        if (parameter == null && name.equals("page")) {
            parameter = this.page.toString();
        } else if (name.equals("limit")) {
            if (parameter == null) {
                parameter = this.limit.toString();
            } else if (Integer.valueOf(parameter) > limit) {
                parameter = this.limit.toString();
            }
        }

        return parameter;
    }

    /**
     * @param name This gonna get the values from params.
     * @return String[]
     */
    @Override
    public String[] getParameterValues(String name) {
        HttpServletRequest request = (HttpServletRequest) getRequest();
        String[] parameterValues = request.getParameterValues(name);

        if (parameterValues == null && name.equals("page")) {
            parameterValues = new String[]{this.page.toString()};
        } else if (name.equals("limit")) {
            if (parameterValues == null) {
                parameterValues = new String[]{this.limit.toString()};
            } else if (Integer.valueOf(parameterValues[0]) > limit) {
                parameterValues = new String[]{this.limit.toString()};
            }
        }

        return parameterValues;
    }

    /**
     * @return Map
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Enumeration<String> parameterNames = this.getParameterNames();

        Map<String, String[]> result = new HashMap<>();
        Collections.list(parameterNames)
                .forEach(p -> result.put(p, this.getParameterValues(p)));
        return result;
    }
}