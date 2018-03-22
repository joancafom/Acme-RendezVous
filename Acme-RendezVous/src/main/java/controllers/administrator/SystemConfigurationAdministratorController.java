
package controllers.administrator;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigurationService;
import controllers.AbstractController;
import domain.SystemConfiguration;
import forms.SystemConfigurationForm;

@Controller
@RequestMapping("/systemConfiguration/administrator")
public class SystemConfigurationAdministratorController extends AbstractController {

	/* Services */
	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {

		// v2.0 - Implemented by JA

		ModelAndView res;

		//Abstract Controller implements by itself some properties like businessName and banner, so don't need to add them

		//Also, we have a method to get the current WelcomeMessages (as we don't have more than one active SystemConfigurations)

		res = new ModelAndView("systemConfiguration/display");
		res.addObject("welcomeMessages", this.systemConfigurationService.getWelcomeMessagesMap());

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		// v2.0 - Implemented by JA

		final ModelAndView res;

		//We can only edit the current one

		SystemConfiguration currentSC;
		final SystemConfigurationForm sCForm = new SystemConfigurationForm();

		if (this.systemConfigurationService.getCurrentSystemConfiguration() != null) {

			//We retrieve the current sysConfig

			currentSC = this.systemConfigurationService.getCurrentSystemConfiguration();
			final Map<String, String> welcomeMessages = this.systemConfigurationService.getWelcomeMessagesMap();

			sCForm.setWelcomeMessageEN(welcomeMessages.containsKey("en") ? welcomeMessages.get("en") : "");
			sCForm.setWelcomeMessageES(welcomeMessages.containsKey("es") ? welcomeMessages.get("es") : "");

		} else {

			//If for some reason there is no current sysConfig, we create a new one
			currentSC = this.systemConfigurationService.create();
			sCForm.setWelcomeMessageEN("");
			sCForm.setWelcomeMessageES("");
		}

		sCForm.setBannerURL(currentSC.getBannerURL());
		sCForm.setBusinessName(currentSC.getBusinessName());

		res = this.createEditModelAndView(sCForm);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SystemConfigurationForm sysConfigForm, final BindingResult binding) {

		// v2.0 - Implemented by JA

		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(sysConfigForm);
		else
			try {
				final SystemConfiguration sC = this.systemConfigurationService.reconstruct(sysConfigForm);
				this.systemConfigurationService.save(sC);
				res = new ModelAndView("redirect:/systemConfiguration/administrator/display.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(sysConfigForm, "systemConfiguration.commit.error");
			}

		return res;
	}

	//Ancillary Methods
	protected ModelAndView createEditModelAndView(final SystemConfigurationForm sysConfigForm) {

		// v2.0 - Implemented by JA

		ModelAndView res;

		res = this.createEditModelAndView(sysConfigForm, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(final SystemConfigurationForm sysConfigForm, final String message) {

		// v2.0 - Implemented by JA

		final ModelAndView res;

		res = new ModelAndView("systemConfiguration/edit");

		res.addObject("systemConfigurationForm", sysConfigForm);
		res.addObject("message", message);

		return res;
	}
}
