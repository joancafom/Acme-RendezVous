
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdministratorService;
import services.RendezVousService;
import domain.Administrator;
import domain.RendezVous;

@Controller
@RequestMapping("/rendezVous/administrator")
public class RendezVousAdministratorController {

	/* Services */
	@Autowired
	private RendezVousService		rendezVousService;

	@Autowired
	private AdministratorService	administratorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
		final Collection<RendezVous> rendezVouses;

		Assert.notNull(administrator);

		result = new ModelAndView("rendezVous/list");

		rendezVouses = this.rendezVousService.findAll();

		result.addObject("actorWS", "administrator/");
		result.addObject("rendezVouses", rendezVouses);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rendezVousId) {

		ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		Assert.notNull(rendezVous);

		result = new ModelAndView("rendezVous/display");

		result.addObject("rendezVous", rendezVous);

		return result;
	}

}
