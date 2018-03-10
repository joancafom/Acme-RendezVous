
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ManagerService;
import domain.Manager;
import forms.ManagerRegisterForm;

@Controller
@RequestMapping("/manager")
public class ManagerController extends AbstractController {

	/* V1.0 - josembell */

	/* Services */
	@Autowired
	private ManagerService	managerService;


	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		final ModelAndView result;
		final ManagerRegisterForm rf = new ManagerRegisterForm();
		result = this.createEditModelAndView(rf);

		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final ManagerRegisterForm managerRegisterForm, final BindingResult binding) {
		ModelAndView result;

		final Manager manager = this.managerService.reconstruct(managerRegisterForm, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(managerRegisterForm);
		else
			try {
				Assert.isTrue(managerRegisterForm.getTermsAndConditions() == true);
				Assert.isTrue(managerRegisterForm.getPassword().equals(managerRegisterForm.getRepeatedPassword()));
				this.managerService.save(manager);

				result = new ModelAndView("welcome/index");

			} catch (final DataIntegrityViolationException oops) {
				result = this.createEditModelAndView(managerRegisterForm, "manager.username.duplicated");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(managerRegisterForm, "manager.commit.error");

			}

		return result;

	}

	protected ModelAndView createEditModelAndView(final ManagerRegisterForm managerRegisterForm) {
		ModelAndView result;
		result = this.createEditModelAndView(managerRegisterForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ManagerRegisterForm managerRegisterForm, final String message) {
		final ModelAndView result;
		result = new ModelAndView("manager/register");
		result.addObject("managerRegisterForm", managerRegisterForm);
		result.addObject("message", message);
		return result;
	}

}
