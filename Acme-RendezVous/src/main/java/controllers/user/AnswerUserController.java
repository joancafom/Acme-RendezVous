
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
import services.AnswerService;
import services.RendezVousService;
import services.UserService;
import controllers.AbstractController;
import domain.Answer;
import domain.RendezVous;
import domain.User;

@Controller
@RequestMapping("/answer/user")
public class AnswerUserController extends AbstractController {

	/* Services */
	@Autowired
	private AnswerService		answerService;

	@Autowired
	private RendezVousService	rendezVousService;

	@Autowired
	private UserService			userService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int userId, @RequestParam final int rendezVousId) {

		ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);
		final User user = this.userService.findOne(userId);
		final User viewer = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(rendezVous);
		Assert.notNull(user);

		Assert.isTrue(user.getAttendedRendezVouses().contains(rendezVous));

		//If we are under age
		if (viewer.getAge() < 18)
			Assert.isTrue(!rendezVous.getIsForAdults());

		final Collection<Answer> answers = this.answerService.findAllByRendezVousAndUser(rendezVous, user);

		result = new ModelAndView("answer/list");
		result.addObject("answers", answers);
		result.addObject("rendezVous", rendezVous);
		result.addObject("user", user);
		result.addObject("actorWS", "user/");

		return result;
	}
}
