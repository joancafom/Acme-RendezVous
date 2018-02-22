
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.RendezVousService;
import services.UserService;
import domain.RendezVous;
import domain.User;
import forms.UserRegisterForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	/* Services */
	@Autowired
	private UserService			userService;
	@Autowired
	private RendezVousService	rendezVousService;


	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		final ModelAndView result;
		final UserRegisterForm rf = new UserRegisterForm();
		result = this.createEditModelAndView(rf);

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		final Collection<User> users = this.userService.findAll();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("userURI", "user/display.do?userId=");

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		ModelAndView result;
		final User user = this.userService.findOne(userId);
		Assert.notNull(user);

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		final Collection<RendezVous> rendezVouses = this.rendezVousService.findAllNotAdultByUser(user);
		result.addObject("attendedRendezVouses", rendezVouses);
		result.addObject("actorWS", "");

		return result;
	}
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final UserRegisterForm userRegisterForm, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(userRegisterForm);
		else
			try {
				Assert.isTrue(userRegisterForm.getTermsAndConditions() == true);
				Assert.isTrue(userRegisterForm.getPassword().equals(userRegisterForm.getRepeatedPassword()));
				this.userService.save(userRegisterForm);

				result = new ModelAndView("welcome/index");

			} catch (final DataIntegrityViolationException oops) {
				result = this.createEditModelAndView(userRegisterForm, "user.username.duplicated");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(userRegisterForm, "user.commit.error");

			}

		return result;

	}
	protected ModelAndView createEditModelAndView(final UserRegisterForm userRegisterForm) {
		ModelAndView result;
		result = this.createEditModelAndView(userRegisterForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final UserRegisterForm userRegisterForm, final String message) {
		final ModelAndView result;
		result = new ModelAndView("user/register");
		result.addObject("userRegisterForm", userRegisterForm);
		result.addObject("message", message);
		return result;
	}

}
