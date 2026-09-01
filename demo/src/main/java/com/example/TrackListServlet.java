package com.example;

import com.example.model.Artist;
import com.example.model.Track;
import com.example.services.ITrackService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TrackListServlet extends BaseServlet {

    private ITrackService trackService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.trackService = getBean(ITrackService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        List<Track> tracks = trackService.getAllTracks();

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Tracks</title></head><body>");
            out.println("<h1>All Tracks</h1>");
            out.println("<p><a href='" + req.getContextPath() + "/tracks/create'>Create new track</a> | ");
            out.println("<a href='" + req.getContextPath() + "/tracks/delete'>Delete track</a> | ");
            out.println("<a href='" + req.getContextPath() + "/artists'>View artists</a></p>");
            out.println("<table border='1' cellpadding='6'>");
            out.println("<tr><th>ID</th><th>Title</th><th>Genre</th><th>Duration (s)</th>"
                    + "<th>Album</th><th>Artists</th></tr>");
            for (Track t : tracks) {
                StringBuilder artistNames = new StringBuilder();
                for (Artist a : t.getArtists()) {
                    if (artistNames.length() > 0) artistNames.append(", ");
                    artistNames.append(escape(a.getName()));
                }
                out.println("<tr><td>" + t.getId() + "</td><td>" + escape(t.getTitle())
                        + "</td><td>" + escape(t.getGenre()) + "</td><td>" + t.getDuration()
                        + "</td><td>" + escape(t.getAlbumTitle()) + "</td><td>" + artistNames + "</td></tr>");
            }
            out.println("</table></body></html>");
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}