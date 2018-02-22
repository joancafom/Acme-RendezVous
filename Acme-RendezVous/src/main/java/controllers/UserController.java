
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import forms.UserRegisterForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	/* Services */
	@Autowired
	private UserService	userService;


	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		final ModelAndView result;
		final UserRegisterForm rf = new UserRegisterForm();
		result = this.createEditModelAndView(rf);

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
