package com.example;

import com.example.services.IArtistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class ArtistCreateServlet extends BaseServlet {

    private IArtistService artistService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.artistService = getBean(IArtistService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        renderForm(resp, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String nationality = req.getParameter("nationality");

        if (name == null || name.isBlank()) {
            renderForm(resp, "Name is required.");
            return;
        }

        try {
            artistService.createArtist(name.trim(), nationality == null ? "" : nationality.trim());
            resp.sendRedirect(req.getContextPath() + "/artists");
        } catch (IllegalArgumentException e) {
            renderForm(resp, e.getMessage());
        }
    }

    private void renderForm(HttpServletResponse resp, String errorMessage) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Create Artist</title></head><body>");
            out.println("<h1>Create Artist</h1>");
            if (errorMessage != null) {
                out.println("<p style='color:red;'>" + errorMessage + "</p>");
            }
            out.println("<form method='post'>");
            out.println("Name: <input type='text' name='name'/><br/>");
            out.println("Nationality: <input type='text' name='nationality'/><br/>");
            out.println("<input type='submit' value='Create'/>");
            out.println("</form>");
            out.println("<p><a href='" + resp.encodeURL("../artists") + "'>Back to artist list</a></p>");
            out.println("</body></html>");
        }
    }
}