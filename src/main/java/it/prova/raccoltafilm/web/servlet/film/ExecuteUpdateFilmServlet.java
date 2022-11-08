package it.prova.raccoltafilm.web.servlet.film;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.service.FilmService;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteUpdateFilmServlet
 */
@WebServlet("/ExecuteUpdateFilmServlet")
public class ExecuteUpdateFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FilmService filmService;

	public ExecuteUpdateFilmServlet() {
		this.filmService = MyServiceFactory.getFilmServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// estraggo input
		String parametroIdFilmDaModificare = request.getParameter("idDaInviareComeParametro");
		String titoloParam = request.getParameter("titolo");
		String genereParam = request.getParameter("genere");
		String dataPubblicazioneParam = request.getParameter("dataPubblicazione");
		String minutiDurataParam = request.getParameter("minutiDurata");
		String registaIdParam = request.getParameter("regista.id");

		// preparo un bean (che mi serve sia per tornare in pagina
		// che per inserire) e faccio il binding dei parametri
		Film filmInstance = UtilityForm.createFilmFromParamsConId(parametroIdFilmDaModificare,
				titoloParam, genereParam, minutiDurataParam, dataPubblicazioneParam, registaIdParam);

		// se la validazione non risulta ok
		if (!UtilityForm.validateFilmBean(filmInstance)) {

			request.setAttribute("filmDaAggiornare", filmInstance);

			try {
				request.setAttribute("registi_list_attribute", MyServiceFactory.getRegistaServiceInstance().listAllElements());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
			request.getRequestDispatcher("/film/update.jsp").forward(request, response);
			return;
		}

		// se sono qui i valori sono ok quindi posso modificare l'oggetto
		// occupiamoci delle operazioni di business
		try {
			filmService.aggiorna(filmInstance);
			request.setAttribute("film_list_attribute", filmService.listAllElements());
			request.setAttribute("successMessage", "Operazione effettuata con successo");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		// andiamo ai risultati
		response.sendRedirect("ExecuteListFilmServlet?operationResult=SUCCESS");
	}

}
