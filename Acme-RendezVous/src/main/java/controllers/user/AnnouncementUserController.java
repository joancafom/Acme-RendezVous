
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.AnnouncementService;
import services.RendezVousService;
import services.UserService;
import controllers.AbstractController;
import domain.Announcement;
import domain.RendezVous;
import domain.User;

@Controller
@RequestMapping("/announcement/user")
public class AnnouncementUserController extends AbstractController {

	/* Services */
	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private UserService			userService;

	@Autowired
	private RendezVousService	rendezVousService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezVousId) {

		ModelAndView res;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final RendezVous rendezVous;

		rendezVous = this.rendezVousService.findOne(rendezVousId);

		Assert.notNull(rendezVous);
		Assert.isTrue(rendezVous.getCreator().equals(user));

		final Announcement announcement = this.announcementService.create(rendezVous);
		res = this.createEditModelAndView(announcement);

		res.addObject("rendezVousId", rendezVousId);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(final Announcement announcement, final BindingResult binding) {

		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(announcement);
		else
			try {
				this.announcementService.save(announcement);
				res = new ModelAndView("redirect:/rendezVous/user/display.do?rendezVousId=" + announcement.getRendezVous().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(announcement, "announcement.commit.error");
			}

		return res;

	}
	//Ancillary Methods

	protected ModelAndView createEditModelAndView(final Announcement announcement) {

		ModelAndView result;
		result = this.createEditModelAndView(announcement, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Announcement announcement, final String message) {

		final ModelAndView result;
		result = new ModelAndView("announcement/edit");
		result.addObject("announcement", announcement);
		result.addObject("message", message);

		return result;
	}

}
