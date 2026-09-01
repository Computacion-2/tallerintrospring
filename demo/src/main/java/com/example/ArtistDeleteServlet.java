package com.example;

import com.example.services.IArtistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

public class ArtistDeleteServlet extends BaseServlet {

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
        String idParam = req.getParameter("id");

        Long id;
        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            renderForm(resp, "Please enter a valid numeric ID.");
            return;
        }

        try {
            artistService.deleteArtist(id);
            resp.sendRedirect(req.getContextPath() + "/artists");
        } catch (NoSuchElementException e) {
            renderForm(resp, e.getMessage());
        }
    }

    private void renderForm(HttpServletResponse resp, String errorMessage) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Delete Artist</title></head><body>");
            out.println("<h1>Delete Artist</h1>");
            if (errorMessage != null) {
                out.println("<p style='color:red;'>" + errorMessage + "</p>");
            }
            out.println("<form method='post'>");
            out.println("Artist ID: <input type='text' name='id'/><br/>");
            out.println("<input type='submit' value='Delete'/>");
            out.println("</form>");
            out.println("<p><a href='../artists'>Back to artist list</a></p>");
            out.println("</body></html>");
        }
    }
}