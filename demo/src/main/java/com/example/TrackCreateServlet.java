package com.example;

import com.example.model.Artist;
import com.example.services.IArtistService;
import com.example.services.ITrackService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TrackCreateServlet extends BaseServlet {

    private ITrackService trackService;
    private IArtistService artistService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.trackService = getBean(ITrackService.class);
        this.artistService = getBean(IArtistService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        renderForm(resp, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String title = req.getParameter("title");
        String genre = req.getParameter("genre");
        String durationParam = req.getParameter("duration");
        String albumTitle = req.getParameter("albumTitle");
        String[] artistIdParams = req.getParameterValues("artistIds"); // multi-select -> array

        if (title == null || title.isBlank()) {
            renderForm(resp, "Title is required.");
            return;
        }
        if (artistIdParams == null || artistIdParams.length == 0) {
            renderForm(resp, "Select at least one artist.");
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(durationParam);
        } catch (NumberFormatException e) {
            renderForm(resp, "Duration must be a whole number of seconds.");
            return;
        }

        List<Long> artistIds = new ArrayList<>();
        for (String s : artistIdParams) {
            artistIds.add(Long.parseLong(s)); // safe: values come from our own <option> tags, not free text
        }

        try {
            trackService.createTrack(title.trim(), genre == null ? "" : genre.trim(),
                    duration, albumTitle == null ? "" : albumTitle.trim(), artistIds);
            resp.sendRedirect(req.getContextPath() + "/tracks");
        } catch (NoSuchElementException | IllegalArgumentException e) {
            renderForm(resp, e.getMessage());
        }
    }

    private void renderForm(HttpServletResponse resp, String errorMessage) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        List<Artist> artists = artistService.getAllArtists();

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Create Track</title></head><body>");
            out.println("<h1>Create Track</h1>");
            if (errorMessage != null) {
                out.println("<p style='color:red;'>" + escape(errorMessage) + "</p>");
            }
            out.println("<form method='post'>");
            out.println("Title: <input type='text' name='title'/><br/>");
            out.println("Genre: <input type='text' name='genre'/><br/>");
            out.println("Duration (seconds): <input type='text' name='duration'/><br/>");
            out.println("Album Title: <input type='text' name='albumTitle'/><br/>");
            out.println("Artists (ctrl/cmd-click to select multiple):<br/>");
            out.println("<select name='artistIds' multiple size='6'>");
            for (Artist a : artists) {
                out.println("<option value='" + a.getId() + "'>" + escape(a.getName()) + "</option>");
            }
            out.println("</select><br/>");
            out.println("<input type='submit' value='Create'/>");
            out.println("</form>");
            out.println("<p><a href='" + resp.encodeURL("../tracks") + "'>Back to track list</a></p>");
            out.println("</body></html>");
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}