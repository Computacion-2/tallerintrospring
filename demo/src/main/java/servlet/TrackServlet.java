package servlet;

import model.Artist;
import model.Track;
import service.ArtistService;
import service.TrackService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/tracks")
public class TrackServlet extends HttpServlet {

    private TrackService trackService;
    private ArtistService artistService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.trackService = context.getBean(TrackService.class);
        this.artistService = context.getBean(ArtistService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<Track> tracks = trackService.getTracks();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Canciones</title></head><body>");
        out.println("<p><a href='" + request.getContextPath() + "/artists'><b>Gestion de Artistas</b></a> | <a href='" + request.getContextPath() + "/tracks'><b>Gestion de Canciones</b></a></p>");
        out.println("<hr>");
        out.println("<h1>Gestion de Canciones (Tracks)</h1>");

        out.println("<h2>Crear Cancion</h2>");
        out.println("<form action='" + request.getContextPath() + "/tracks' method='POST'>");
        out.println("<input type='hidden' name='action' value='create'>");
        out.println("<label>ID: </label><input type='text' name='id' required><br><br>");
        out.println("<label>Titulo: </label><input type='text' name='title' required><br><br>");
        out.println("<label>Genero: </label><input type='text' name='genre' required><br><br>");
        out.println("<label>Duracion: </label><input type='text' name='duration' required><br><br>");
        out.println("<label>Album: </label><input type='text' name='albumTitle' required><br><br>");
        out.println("<label>IDs de Artistas Autores (ej: 1,2): </label>");
        out.println("<input type='text' name='artistIds'><br><br>");
        out.println("<button type='submit'>Guardar Cancion</button>");
        out.println("</form>");

        out.println("<hr>");

        out.println("<h2>Eliminar Cancion por ID</h2>");
        out.println("<form action='" + request.getContextPath() + "/tracks' method='POST'>");
        out.println("<input type='hidden' name='action' value='delete'>");
        out.println("<input type='text' name='id' placeholder='ID de la cancion' required>");
        out.println("<button type='submit'>Eliminar Cancion</button>");
        out.println("</form>");

        out.println("<hr>");

        out.println("<h2>Lista de Canciones</h2>");
        out.println("<ul>");
        for (Track t : tracks) {
            StringBuilder autores = new StringBuilder();
            for (Artist a : t.getArtists()) {
                autores.append(a.getName()).append(" ");
            }
            out.println("<li><b>ID:</b> " + t.getId() + " | <b>Titulo:</b> " + t.getTitle() +
                        " | <b>Genero:</b> " + t.getGenre() + " | <b>Duracion:</b> " + t.getDuration() +
                        " | <b>Album:</b> " + t.getAlbumTitle() +
                        " | <b>Artistas:</b> [" + autores.toString().trim() + "]</li>");
        }
        out.println("</ul>");

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String id = request.getParameter("id");
            trackService.removeTrack(id);
        } else {
            String id = request.getParameter("id");
            String title = request.getParameter("title");
            String genre = request.getParameter("genre");
            String duration = request.getParameter("duration");
            String albumTitle = request.getParameter("albumTitle");
            String artistIdsParam = request.getParameter("artistIds");

            List<String> artistIds = null;
            if (artistIdsParam != null && !artistIdsParam.trim().isEmpty()) {
                artistIds = Arrays.asList(artistIdsParam.split(","));
            }

            trackService.addTrack(id, title, genre, duration, albumTitle, artistIds);
        }

        response.sendRedirect(request.getContextPath() + "/tracks");
    }


    
}