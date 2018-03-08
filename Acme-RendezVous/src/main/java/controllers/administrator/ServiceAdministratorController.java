
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import controllers.AbstractController;
import domain.Service;

@Controller
@RequestMapping("/service/administrator")
public class ServiceAdministratorController extends AbstractController {

	// Services ---------------------------

	@Autowired
	private ServiceService	serviceService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		// v1.0 - Implemented by Alicia

		final ModelAndView res;
		final Collection<Service> services = new ArrayList<Service>();

		services.addAll(this.serviceService.findAll());

		res = new ModelAndView("service/list");
		res.addObject("services", services);

		return res;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int serviceId) {

		// v1.0 - Implemented by Alicia

		ModelAndView res;
		final Service service = this.serviceService.findOne(serviceId);

		Assert.notNull(service);

		res = new ModelAndView("redirect:/service/administrator/list.do");

		try {
			this.serviceService.cancel(service);
		} catch (final Throwable oops) {
			res.addObject("message", "service.commit.error");
		}

		return res;
	}
}
