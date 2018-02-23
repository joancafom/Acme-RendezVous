
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import controllers.AbstractController;
import domain.RendezVous;
import domain.User;

@Controller
@RequestMapping("/user/administrator")
public class UserAdministratorController extends AbstractController {

	/* Services */
	@Autowired
	private UserService	userService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		ModelAndView result;
		final User user = this.userService.findOne(userId);
		Assert.notNull(user);

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		final Collection<RendezVous> rendezVouses;

		rendezVouses = user.getAttendedRendezVouses();

		result.addObject("attendedRendezVouses", rendezVouses);
		result.addObject("actorWS", "administrator/");

		return result;
	}
}
