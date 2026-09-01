package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class BaseServlet extends HttpServlet {

    protected WebApplicationContext ctx;

    @Override
    public void init() throws ServletException {
        this.ctx = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        if (this.ctx == null) {
            throw new ServletException(
                "Spring WebApplicationContext not found — check web.xml listener order.");
        }
    }

    protected <T> T getBean(Class<T> type) {
        return ctx.getBean(type);
    }
}