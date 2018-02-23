
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezVousService;
import domain.RendezVous;

@Controller
@RequestMapping("/rendezVous")
public class RendezVousController extends AbstractController {

	/* Services */
	@Autowired
	private RendezVousService	rendezVousService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rendezVousId) {

		ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		Assert.notNull(rendezVous);
		Assert.isTrue(!rendezVous.getIsForAdults());

		result = new ModelAndView("rendezVous/display");

		result.addObject("rendezVous", rendezVous);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		final Collection<RendezVous> rendezVouses = this.rendezVousService.findAllNotAdult();
		Assert.notNull(rendezVouses);

		result = new ModelAndView("rendezVous/list");

		result.addObject("rendezVouses", rendezVouses);
		result.addObject("own", false);
		result.addObject("actorWS", "");

		return result;
	}
}
