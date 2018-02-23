
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
@RequestMapping("/user/user")
public class UserUserController extends AbstractController {

	/* Services */
	@Autowired
	private UserService			userService;
	@Autowired
	private RendezVousService	rendezVousService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<User> users = this.userService.findAll();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("userURI", "user/user/display.do?userId=");

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer userId) {
		ModelAndView result;
		final User principal = this.userService.findByUserAccount(LoginService.getPrincipal());
		final User user;

		if (userId == null)
			user = principal;
		else
			user = this.userService.findOne(userId);

		Assert.notNull(user);

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		final Collection<RendezVous> rendezVouses;

		if (principal.getAge() < 18)
			rendezVouses = this.rendezVousService.findAllNotAdultByUser(user);
		else
			rendezVouses = user.getAttendedRendezVouses();

		result.addObject("attendedRendezVouses", rendezVouses);
		result.addObject("actorWS", "user/");

		return result;
	}
}
