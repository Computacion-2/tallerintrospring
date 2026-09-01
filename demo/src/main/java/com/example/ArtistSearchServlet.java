package com.example;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.services.IArtistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

public class ArtistSearchServlet extends BaseServlet {

    private IArtistService artistService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.artistService = getBean(IArtistService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Search Artist</title></head><body>");
            out.println("<h1>Search Artist by Name</h1>");
            out.println("<form method='get'>");
            out.println("Name: <input type='text' name='name' value='" + escape(name) + "'/>");
            out.println("<input type='submit' value='Search'/>");
            out.println("</form>");

            // Only attempt a lookup if the form was actually submitted with a value —
            // otherwise every first visit to this page would show a "not found" error.
            if (name != null && !name.isBlank()) {
                try {
                    Artist artist = artistService.getArtistWithTracksByName(name.trim());
                    out.println("<h2>" + escape(artist.getName()) + "</h2>");
                    out.println("<p>ID: " + artist.getId() + " | Nationality: "
                            + escape(artist.getNationality()) + "</p>");
                    out.println("<h3>Tracks (" + artist.getTracks().size() + ")</h3>");
                    out.println("<ul>");
                    for (Track t : artist.getTracks()) {
                        out.println("<li>" + escape(t.getTitle()) + " — " + escape(t.getGenre())
                                + " (" + t.getDuration() + "s) — Album: " + escape(t.getAlbumTitle()) + "</li>");
                    }
                    out.println("</ul>");
                } catch (NoSuchElementException e) {
                    out.println("<p style='color:red;'>" + escape(e.getMessage()) + "</p>");
                }
            }

            out.println("<p><a href='" + req.getContextPath() + "/artists'>Back to artist list</a></p>");
            out.println("</body></html>");
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}