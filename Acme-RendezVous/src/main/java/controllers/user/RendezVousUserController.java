
package controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.RendezVousService;
import services.UserService;
import domain.RendezVous;
import domain.User;

@Controller
@RequestMapping("/rendezVous/user")
public class RendezVousUserController {

	/* Services */
	@Autowired
	private RendezVousService	rendezVousService;

	@Autowired
	private UserService			userService;


	@RequestMapping(value = "/listMine", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		result = new ModelAndView("rendezVous/list");
		result.addObject("rendezVouses", user.getCreatedRendezVouses());

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.create();

		result = this.createEditModelAndView(rendezVous);

		result.addObject("toEdit", true);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezVousId) {
		ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);
		result = this.createEditModelAndView(rendezVous);

		result.addObject("toEdit", true);

		return result;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int rendezVousId) {
		ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);
		result = this.createEditModelAndView(rendezVous);

		result.addObject("toDelete", true);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final RendezVous rendezVous, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(rendezVous);
			result.addObject("toEdit", true);
		} else
			try {
				this.rendezVousService.save(rendezVous);
				result = new ModelAndView("redirect:listMine.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(rendezVous, "rendezVous.commit.error");
				result.addObject("toEdit", true);
			}

		return result;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final RendezVous rendezVous, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(rendezVous);
			result.addObject("toDelete", true);
		} else
			try {

				this.rendezVousService.virtualDelete(rendezVous);
				result = new ModelAndView("redirect:listMine.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(rendezVous, "rendezVous.commit.error");
				result.addObject("toDelete", true);
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
