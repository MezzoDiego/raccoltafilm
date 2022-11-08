package it.prova.raccoltafilm.web.servlet.regista;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.model.Regista;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.service.RegistaService;
import it.prova.raccoltafilm.utility.UtilityForm;

/**
 * Servlet implementation class ExecuteUpdateRegistaServlet
 */
@WebServlet("/ExecuteUpdateRegistaServlet")
public class ExecuteUpdateRegistaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private RegistaService registaService;

	public ExecuteUpdateRegistaServlet() {
		this.registaService = MyServiceFactory.getRegistaServiceInstance();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// estraggo input
		String parametroIdRegistaDaModificare = request.getParameter("idDaInviareComeParametro");
		String nomeInputParam = request.getParameter("nome");
		String cognomeInputParam = request.getParameter("cognome");
		String nickNameInputParam = request.getParameter("nickName");
		String dataDiNascitaInputParam = request.getParameter("dataDiNascita");
		String sessoInputParam = request.getParameter("sesso");

		// preparo un bean (che mi serve sia per tornare in pagina
		// che per inserire) e faccio il binding dei parametri
		Regista registaInstance = UtilityForm.createRegistaFromParamsConId(parametroIdRegistaDaModificare,
				nomeInputParam, cognomeInputParam, nickNameInputParam, dataDiNascitaInputParam, sessoInputParam);

		// se la validazione non risulta ok
		if (!UtilityForm.validateRegistaBean(registaInstance)) {

			request.setAttribute("bigliettoDaAggiornare", registaInstance);

			request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
			request.getRequestDispatcher("/biglietto/update.jsp").forward(request, response);
			return;
		}

		// se sono qui i valori sono ok quindi posso modificare l'oggetto
		// occupiamoci delle operazioni di business
		try {
			registaService.aggiorna(registaInstance);
			request.setAttribute("registi_list_attribute", registaService.listAllElements());
			request.setAttribute("successMessage", "Operazione effettuata con successo");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		// andiamo ai risultati
		response.sendRedirect("ExecuteListRegistaServlet?operationResult=SUCCESS");

	}

}
