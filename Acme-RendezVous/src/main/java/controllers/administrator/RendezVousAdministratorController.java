
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AdministratorService;
import services.CommentService;
import services.RendezVousService;
import controllers.AbstractController;
import domain.Administrator;
import domain.RendezVous;

@Controller
@RequestMapping("/rendezVous/administrator")
public class RendezVousAdministratorController extends AbstractController {

	/* Services */
	@Autowired
	private RendezVousService		rendezVousService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private CommentService			commentService;


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

		result.addObject("actorWS", "administrator/");
		result.addObject("rootComments", this.commentService.findRootCommentsByRendezVous(rendezVous));
		result.addObject("rendezVous", rendezVous);

		return result;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView remove(@RequestParam final int rendezVousId) {
		final ModelAndView res;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		Assert.notNull(rendezVous);

		res = new ModelAndView("rendezVous/remove");
		res.addObject("rendezVous", rendezVous);

		return res;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public ModelAndView remove(RendezVous rendezVous, final BindingResult binding) {
		ModelAndView res = null;

		rendezVous = this.rendezVousService.reconstruct(rendezVous, binding);

		res = new ModelAndView("rendezVous/remove");

		if (binding.hasErrors())
			res.addObject("rendezVous", rendezVous);
		else
			try {
				this.rendezVousService.delete(rendezVous);
				res = new ModelAndView("redirect:list.do?show=all");
			} catch (final Throwable oops) {
				res.addObject("message", "rendezVous.commit.error");
				res.addObject("rendezVous", rendezVous);
			}
		return res;
	}

}
