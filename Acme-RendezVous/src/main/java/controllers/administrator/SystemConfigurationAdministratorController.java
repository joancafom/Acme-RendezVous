
package controllers.administrator;

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

@Controller
@RequestMapping("/systemConfiguration/administrator")
public class SystemConfigurationAdministratorController extends AbstractController {

	/* Services */
	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {

		// v1.0 - Implemented by JA

		ModelAndView res;

		//Abstract Controller implements by itself the properties of SystemConfiguration, so no need to retrieve

		res = new ModelAndView("systemConfiguration/display");

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		// v1.0 - Implemented by JA

		final ModelAndView res;

		//We can only edit the current one

		SystemConfiguration currentSC;

		if (this.systemConfigurationService.getCurrentSystemConfiguration() != null)
			currentSC = this.systemConfigurationService.getCurrentSystemConfiguration();
		else
			currentSC = this.systemConfigurationService.create();

		res = this.createEditModelAndView(currentSC);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SystemConfiguration sysConfig, final BindingResult binding) {

		// v1.0 - Implemented by JA

		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(sysConfig);
		else
			try {
				this.systemConfigurationService.save(sysConfig);
				res = new ModelAndView("redirect:/systemConfiguration/administrator/display.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(sysConfig, "systemConfiguration.commit.error");
			}

		return res;
	}

	//Ancillary Methods
	protected ModelAndView createEditModelAndView(final SystemConfiguration sysConfig) {

		// v1.0 - Implemented by JA

		ModelAndView res;

		res = this.createEditModelAndView(sysConfig, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(final SystemConfiguration sysConfig, final String message) {

		// v1.0 - Implemented by JA

		final ModelAndView res;

		res = new ModelAndView("systemConfiguration/edit");

		res.addObject("systemConfiguration", sysConfig);
		res.addObject("message", message);

		return res;
	}
}
