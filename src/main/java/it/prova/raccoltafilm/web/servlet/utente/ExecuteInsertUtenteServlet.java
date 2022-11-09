package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.model.Film;
import it.prova.raccoltafilm.model.Ruolo;
import it.prova.raccoltafilm.model.Utente;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RuoloService;
import it.prova.raccoltafilm.service.UtenteService;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteInsertUtenteServlet
 */
@WebServlet("/admin/ExecuteInsertUtenteServlet")
public class ExecuteInsertUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UtenteService utenteService;
	private RuoloService ruoloService;

	public ExecuteInsertUtenteServlet() {
		this.utenteService = MyServiceFactory.getUtenteServiceInstance();
		this.ruoloService = MyServiceFactory.getRuoloServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// estraggo input
		String usernameParam = request.getParameter("username");
		String nomeParam = request.getParameter("nome");
		String cognomeParam = request.getParameter("cognome");
		String passwordParam = request.getParameter("password");
		String confermaPasswordParam = request.getParameter("confermapassword");
		String[] idRuoliParam = request.getParameterValues("ruoli");

		// preparo un bean (che mi serve sia per tornare in pagina
		// che per inserire) e faccio il binding dei parametri
		Utente utenteInstance = new Utente(usernameParam, passwordParam, nomeParam, cognomeParam, new Date());

		List<Ruolo> ruoliChecked = new ArrayList<>();

		try {
			
			if(idRuoliParam != null) {
				for(String item : idRuoliParam) {
					Ruolo ruoloInstance = null;
					if(NumberUtils.isCreatable(item))
						ruoloInstance = ruoloService.caricaSingoloElemento(Long.parseLong(item));
					if(ruoloInstance != null)
						ruoliChecked.add(ruoloInstance);
				}
			}
			// se la validazione non risulta ok
			if (!(UtilityForm.validateUtenteBean(utenteInstance)) || StringUtils.isBlank(confermaPasswordParam)
					|| !passwordParam.equals(confermaPasswordParam)) {
				request.setAttribute("insert_utente_attr", utenteInstance);
				// questo mi serve per la checkbox di ruoli in pagina
				request.setAttribute("ruoli_list_attribute", ruoloService.listAll());
				
				List<Long> idRuoliChecked = new ArrayList<>();
				for (Ruolo ruoloItem : ruoliChecked) {
					idRuoliChecked.add(ruoloItem.getId());
				}
				request.setAttribute("ruoliChecked", idRuoliChecked);
				
				request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
				request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
				return;
			}

			// se sono qui i valori sono ok quindi posso creare l'oggetto da inserire
			// occupiamoci delle operazioni di business
			if (idRuoliParam != null) {
				for (String item : idRuoliParam) {
					if (NumberUtils.isCreatable(item))
						utenteService.aggiungiRuolo(utenteInstance,
								ruoloService.caricaSingoloElemento(Long.parseLong(item)));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			try {
				request.setAttribute("ruoli_list_attribute", ruoloService.listAll());
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			request.getRequestDispatcher("/utente/insert.jsp").forward(request, response);
			return;
		}

		// andiamo ai risultati
		// uso il sendRedirect con parametro per evitare il problema del double save on
		// refresh
		response.sendRedirect("/raccoltafilm/admin/ExecuteListUtenteServlet?operationResult=SUCCESS");
	}

}
