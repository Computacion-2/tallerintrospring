package servlet;

import model.Artist;
import model.Track;
import service.ArtistService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/artists")
public class ArtistServlet extends HttpServlet {

    private ArtistService artistService;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.artistService = context.getBean(ArtistService.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<Artist> artists = artistService.getArtists();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Artistas</title></head><body>");
        out.println("<p><a href='" + request.getContextPath() + "/artists'><b>Gestion de Artistas</b></a> | <a href='" + request.getContextPath() + "/tracks'><b>Gestion de Canciones</b></a></p>");
        out.println("<hr>");
        out.println("<h1>Gestion de Artistas</h1>");

        // a y b: Formulario y Lista
        out.println("<h2>Crear Artista</h2>");
        out.println("<form action='" + request.getContextPath() + "/artists' method='POST'>");
        out.println("<input type='hidden' name='action' value='create'>");
        out.println("<label>ID: </label><input type='text' name='id' required><br><br>");
        out.println("<label>Nombre: </label><input type='text' name='name' required><br><br>");
        out.println("<label>Nacionalidad: </label><input type='text' name='nationality' required><br><br>");
        out.println("<button type='submit'>Guardar Artista</button>");
        out.println("</form>");

        out.println("<hr>");

        // c: Buscar por nombre
        out.println("<h2>Buscar Artista por Nombre</h2>");
        out.println("<form action='" + request.getContextPath() + "/artists' method='GET'>");
        out.println("<input type='text' name='searchName' placeholder='Nombre del artista' required>");
        out.println("<button type='submit'>Buscar</button>");
        out.println("</form>");

        String searchName = request.getParameter("searchName");
        if (searchName != null) {
            Artist found = artistService.getArtistByName(searchName);
            if (found != null) {
                out.println("<h3>Resultado de busqueda:</h3>");
                out.println("<p><b>ID:</b> " + found.getId() + " | <b>Nombre:</b> " + found.getName() + " | <b>Nacionalidad:</b> " + found.getNationality() + "</p>");
                out.println("<b>Canciones del artista:</b><ul>");
                for (Track t : found.getTracks()) {
                    out.println("<li>" + t.getTitle() + " (" + t.getDuration() + ") - Album: " + t.getAlbumTitle() + "</li>");
                }
                out.println("</ul>");
            } else {
                out.println("<p style='color:red;'>Artista no encontrado</p>");
            }
        }

        out.println("<hr>");

        // d: Eliminar por ID
        out.println("<h2>Eliminar Artista por ID</h2>");
        out.println("<form action='" + request.getContextPath() + "/artists' method='POST'>");
        out.println("<input type='hidden' name='action' value='delete'>");
        out.println("<input type='text' name='id' placeholder='ID del artista' required>");
        out.println("<button type='submit'>Eliminar Artista</button>");
        out.println("</form>");

        out.println("<hr>");

        // a: Lista completa
        out.println("<h2>Lista de Todos los Artistas</h2>");
        out.println("<ul>");
        for (Artist a : artists) {
            out.println("<li><b>ID:</b> " + a.getId() + " | <b>Nombre:</b> " + a.getName() + " | <b>Nacionalidad:</b> " + a.getNationality() + "</li>");
        }
        out.println("</ul>");

        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            String id = request.getParameter("id");
            artistService.removeArtist(id);
        } else {
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String nationality = request.getParameter("nationality");
            artistService.addArtist(id, name, nationality);
        }

        response.sendRedirect(request.getContextPath() + "/artists");
    }
}