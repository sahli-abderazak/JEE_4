package web;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;

import dao.filmDaoImpl;
import dao.lFilmDao;
import metier.Film;



@WebServlet (name="cs",urlPatterns= {"/controleur","*.do"})
public class ControleurServlet extends HttpServlet {
	
	lFilmDao metier;
	 @Override
	public void init() throws ServletException {
		metier = new filmDaoImpl();
	}
	
	@Override
	protected void doGet(HttpServletRequest request,
			             HttpServletResponse response) 
			            throws ServletException, IOException {
		String path=request.getServletPath();
		if (path.equals("/index.do"))
		{
			request.getRequestDispatcher("films.jsp").forward(request,response);
		}
		else if (path.equals("/chercher.do"))
		{
			String motCle=request.getParameter("motCle");
			FilmModele model= new FilmModele();
			model.setMotCle(motCle);
			List<Film> films = metier.filmsParMC(motCle);
			model.setFilms(films);
			request.setAttribute("model", model);
			request.getRequestDispatcher("films.jsp").forward(request,response);
		}
		else if (path.equals("/saisie.do")  )
		{
			request.getRequestDispatcher("saisieFilm.jsp").forward(request,response);
		}
		else if (path.equals("/save.do")  && request.getMethod().equals("POST"))
		{
		    String nom=request.getParameter("nom");
			float rate = Float.parseFloat(request.getParameter("rate"));
			 String genre=request.getParameter("genre");
			Film f = metier.save(new Film(nom,rate,genre));
			request.setAttribute("film", f);
			request.getRequestDispatcher("confirmation.jsp").forward(request,response);
		}
		else if (path.equals("/supprimer.do"))
		{
		    Long id= Long.parseLong(request.getParameter("id"));
		    metier.deleteFilm(id);
		    response.sendRedirect("chercher.do?motCle=");
					
			//request.getRequestDispatcher("confirmation.jsp").forward(request,response);
		}
		else if (path.equals("/editer.do")  )
		{
			Long id= Long.parseLong(request.getParameter("id"));
		    Film f = metier.getFilm(id);
		    request.setAttribute("film", f);
			request.getRequestDispatcher("editerFilm.jsp").forward(request,response);
		}
		else if (path.equals("/update.do")  )
		{
			 Long id = Long.parseLong(request.getParameter("id"));
			 String nom=request.getParameter("nom");
			 float rate = Float.parseFloat(request.getParameter("rate"));
			 String genre=request.getParameter("genre");
			 Film f = new Film();
			 f.setIdFilm(id);
			f.setNomFilm(nom);
			 f.setRateFilm(rate);
			 f.setGenreFilm(genre);
			 
			 metier.updateFilm(f);
			 request.setAttribute("film", f);
			 request.getRequestDispatcher("confirmation.jsp").forward(request,response);
		}
		else
		{
			response.sendError(Response.SC_NOT_FOUND);		
		}	
	}
	
	@Override
	protected void doPost(HttpServletRequest request, 
						  HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
}
