
package controllers.user;

import java.util.Collection;

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
import controllers.AbstractController;
import domain.RendezVous;
import domain.User;

@Controller
@RequestMapping("/rendezVous/user")
public class RendezVousUserController extends AbstractController {

	/* Services */
	@Autowired
	private RendezVousService	rendezVousService;

	@Autowired
	private UserService			userService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final String show) {
		ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final Collection<RendezVous> rendezVouses;
		final User me = user;

		Assert.notNull(user);

		result = new ModelAndView("rendezVous/list");
		boolean own = false;

		if (show.equals("mine")) {
			rendezVouses = user.getCreatedRendezVouses();
			own = true;

		} else if (show.equals("attended"))
			rendezVouses = user.getAttendedRendezVouses();
		else if (show.equals("all") && user.getAge() < 18)
			rendezVouses = this.rendezVousService.findAllNotAdult();
		else
			rendezVouses = this.rendezVousService.findAll();

		result.addObject("own", own);
		result.addObject("me", me);
		result.addObject("actorWS", "user/");
		result.addObject("rendezVouses", rendezVouses);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rendezVousId) {

		ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);
		final boolean hasRSVP = user.getAttendedRendezVouses().contains(rendezVous);
		final boolean own = rendezVous.getCreator().equals(user);

		Assert.notNull(rendezVous);

		if (user.getAge() < 18)
			Assert.isTrue(!rendezVous.getIsForAdults());

		result = new ModelAndView("rendezVous/display");

		result.addObject("rendezVous", rendezVous);
		result.addObject("hasRSVP", hasRSVP);
		result.addObject("own", own);
		result.addObject("actorWS", "user/");

		return result;
	}
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int rendezVousId) {
		final ModelAndView res;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		res = new ModelAndView("rendezVous/cancel");
		res.addObject("rendezVous", rendezVous);

		return res;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public ModelAndView cancel(RendezVous rendezVous, final BindingResult binding) {
		ModelAndView res = null;

		rendezVous = this.rendezVousService.reconstruct(rendezVous, binding);

		res = new ModelAndView("rendezVous/cancel");

		if (binding.hasErrors())
			res.addObject("rendezVous", rendezVous);
		else
			try {
				this.rendezVousService.cancelRSVP(rendezVous);
				res = new ModelAndView("redirect:list.do?show=all");
			} catch (final Throwable oops) {
				res.addObject("message", "rendezVous.commit.error");
				res.addObject("rendezVous", rendezVous);
			}
		return res;
	}

	@RequestMapping(value = "/rsvp", method = RequestMethod.GET)
	public ModelAndView rsvp(@RequestParam final int rendezVousId) {
		final ModelAndView res;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		res = new ModelAndView("rendezVous/rsvp");
		res.addObject("rendezVous", rendezVous);

		return res;
	}

	@RequestMapping(value = "/rsvp", method = RequestMethod.POST)
	public ModelAndView rsvp(RendezVous rendezVous, final BindingResult binding) {
		ModelAndView res = null;

		rendezVous = this.rendezVousService.reconstruct(rendezVous, binding);

		res = new ModelAndView("rendezVous/rsvp");

		if (binding.hasErrors())
			res.addObject("rendezVous", rendezVous);
		else
			try {
				this.rendezVousService.acceptRSVP(rendezVous);
				res = new ModelAndView("redirect:list.do?show=all");
			} catch (final Throwable oops) {
				res.addObject("message", "rendezVous.commit.error");
				res.addObject("rendezVous", rendezVous);
			}
		return res;
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
				result = new ModelAndView("redirect:list.do?show=mine");
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
				result = new ModelAndView("redirect:list.do?show=mine");
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
