package com.glroland.stress;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class HostHeaderFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException
    {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader(ServiceUtils.HEADER_HOST_IP, ServiceUtils.getHostIp());
        
        filterChain.doFilter(request, response);
    }
}