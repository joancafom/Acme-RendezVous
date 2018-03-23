
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
import services.QuestionService;
import services.RendezVousService;
import services.UserService;
import controllers.AbstractController;
import domain.Question;
import domain.RendezVous;
import domain.User;

@Controller
@RequestMapping("/question/user")
public class QuestionUserController extends AbstractController {

	// Services -------------------

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private UserService			userService;

	@Autowired
	private RendezVousService	rendezVousService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int questionId) {

		ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final Question question = this.questionService.findOne(questionId);

		Assert.notNull(question);
		Assert.isTrue(question.getRendezVous().getCreator().equals(user));

		result = new ModelAndView("question/display");

		result.addObject("question", question);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezVousId) {

		ModelAndView res;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		Assert.notNull(rendezVous);
		Assert.isTrue(rendezVous.getCreator().equals(user));

		final Question question = this.questionService.create(rendezVous);
		res = this.createEditModelAndView(question);

		res.addObject("rendezVousId", rendezVousId);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(@Valid final Question question, final BindingResult binding) {

		ModelAndView res = null;

		if (binding.hasErrors())
			res = this.createEditModelAndView(question);
		else
			try {
				this.questionService.save(question);
				res = new ModelAndView("redirect:/rendezVous/user/display.do?rendezVousId=" + question.getRendezVous().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(question, "question.commit.error");
			}

		return res;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int questionId) {
		final ModelAndView res;
		final Question question = this.questionService.findOne(questionId);

		Assert.notNull(question);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		Assert.isTrue(user.getCreatedRendezVouses().contains(question.getRendezVous()));

		res = new ModelAndView("question/delete");
		res.addObject("question", question);

		return res;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ModelAndView delete(@Valid final Question question, final BindingResult binding) {
		ModelAndView res = new ModelAndView("question/delete");
		final int rendezVousId = question.getRendezVous().getId();

		if (binding.hasErrors())
			res.addObject("question", question);
		else
			try {
				this.questionService.delete(question);
				res = new ModelAndView("redirect:/rendezVous/user/display.do?rendezVousId=" + rendezVousId);
			} catch (final Throwable oops) {
				res.addObject("messageCode", "question.commit.error");
				res.addObject("question", question);
			}
		return res;
	}

	// Ancillary Methods -------------

	protected ModelAndView createEditModelAndView(final Question question) {

		ModelAndView result;
		result = this.createEditModelAndView(question, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Question question, final String message) {

		final ModelAndView result;
		result = new ModelAndView("question/edit");
		result.addObject("rendezVousId", question.getRendezVous().getId());
		result.addObject("question", question);
		result.addObject("message", message);

		return result;
	}

}
