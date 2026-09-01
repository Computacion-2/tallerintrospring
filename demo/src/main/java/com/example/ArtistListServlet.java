package com.example;

import com.example.model.Artist;
import com.example.services.IArtistService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ArtistListServlet extends BaseServlet {

    private IArtistService artistService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.artistService = getBean(IArtistService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        List<Artist> artists = artistService.getAllArtists();

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Artists</title></head><body>");
            out.println("<h1>All Artists</h1>");
            out.println("<p><a href='" + req.getContextPath() + "/artists/create'>Create new artist</a> | ");
            out.println("<a href='" + req.getContextPath() + "/artists/search'>Search by name</a> | ");
            out.println("<a href='" + req.getContextPath() + "/artists/delete'>Delete artist</a> | ");
            out.println("<a href='" + req.getContextPath() + "/tracks'>View tracks</a></p>");
            out.println("<table border='1' cellpadding='6'>");
            out.println("<tr><th>ID</th><th>Name</th><th>Nationality</th><th># Tracks</th></tr>");
            for (Artist a : artists) {
                out.println("<tr><td>" + a.getId() + "</td><td>" + escape(a.getName())
                        + "</td><td>" + escape(a.getNationality())
                        + "</td><td>" + a.getTracks().size() + "</td></tr>");
            }
            out.println("</table></body></html>");
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}