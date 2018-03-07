
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceService;
import controllers.AbstractController;
import domain.Service;

@Controller
@RequestMapping("/service/user")
public class ServiceUserController extends AbstractController {

	/* Services */
	@Autowired
	private ServiceService	serviceService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		//v1.0 - Implemented by JA

		final ModelAndView res;
		final Collection<Service> services = new ArrayList<Service>();

		services.addAll(this.serviceService.findAll());

		res = new ModelAndView("service/list");
		res.addObject("services", services);

		return res;
	}
}
