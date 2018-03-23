
package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ManagerService;
import services.ServiceService;
import controllers.AbstractController;
import domain.Manager;

@Controller
@RequestMapping("/service/manager")
public class ServiceManagerController extends AbstractController {

	/* Services */
	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private ManagerService	managerService;


	/* V1.0 - josembell */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final String show) {
		final ModelAndView result;
		final Collection<domain.Service> services;
		final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
		result = new ModelAndView("service/list");

		if (show == null) {
			services = manager.getServices();
			result.addObject("show", "own");

		} else {
			services = this.serviceService.findAll();
			result.addObject("show", "all");
		}
		result.addObject("services", services);
		result.addObject("actor", "manager/");

		return result;
	}

	/* V1.0 - josembell */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final domain.Service service = this.serviceService.create();
		final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(manager);

		result = this.createEditModelAndView(service);
		result.addObject("toEdit", true);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int serviceId) {
		ModelAndView result;
		final domain.Service service = this.serviceService.findOne(serviceId);
		final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(service);
		Assert.isTrue(service.getManager().equals(manager));
		Assert.isTrue(manager.getServices().contains(service));

		result = this.createEditModelAndView(service);
		result.addObject("toEdit", true);

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int serviceId) {
		ModelAndView result;
		final domain.Service service = this.serviceService.findOne(serviceId);
		final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(service);
		Assert.notNull(manager);
		Assert.isTrue(manager.getServices().contains(service));
		Assert.isTrue(service.getManager().equals(manager));

		result = this.createEditModelAndView(service);
		result.addObject("toDelete", true);
		if (service.getServiceRequests().isEmpty())
			result.addObject("able", true);
		else
			result.addObject("able", false);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(domain.Service service, final BindingResult binding) {
		ModelAndView result;

		service = this.serviceService.reconstruct(service, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(service);
			result.addObject("toEdit", true);
		} else
			try {
				this.serviceService.save(service);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(service, "service.commit.error");
				result.addObject("toEdit", true);
			}

		return result;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final domain.Service prunedService, final BindingResult binding) {
		ModelAndView result;

		final domain.Service service = this.serviceService.reconstruct(prunedService, binding);

		try {
			this.serviceService.delete(service);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(service, "service.commit.error");
		}

		return result;

	}

	protected ModelAndView createEditModelAndView(final domain.Service service) {
		ModelAndView result;
		result = this.createEditModelAndView(service, null);
		return result;
	}

	private ModelAndView createEditModelAndView(final domain.Service service, final String message) {
		ModelAndView result;
		result = new ModelAndView("service/edit");
		result.addObject("service", service);
		result.addObject("message", message);
		return result;
	}

}
