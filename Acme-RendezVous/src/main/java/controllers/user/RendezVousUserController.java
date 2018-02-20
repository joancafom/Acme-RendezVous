
package controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.RendezVousService;
import domain.RendezVous;

@Controller
@RequestMapping("/rendezVous/user")
public class RendezVousUserController {

	/* Services */
	@Autowired
	private RendezVousService	rendezVousService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.create();

		result = this.createEditModelAndView(rendezVous);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RendezVous rendezVous, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(rendezVous);
		else
			try {
				this.rendezVousService.save(rendezVous);
				result = new ModelAndView("redirect:");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(rendezVous, "rendezVous.commit.error");
			}

		return result;

	}

	protected ModelAndView createEditModelAndView(final RendezVous rendezVous) {
		ModelAndView result;
		result = this.createEditModelAndView(rendezVous, null);
		return result;
	}

	private ModelAndView createEditModelAndView(final RendezVous rendezVous, final String message) {
		final ModelAndView result;
		result = new ModelAndView("rendezVous/edit");
		result.addObject("rendezVous", rendezVous);
		result.addObject("message", message);

		return result;
	}

}
