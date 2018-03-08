
package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ServiceRequestService;
import services.ServiceService;
import controllers.AbstractController;
import domain.RendezVous;
import domain.Service;
import domain.ServiceRequest;

@Controller
@RequestMapping("/serviceRequest/user")
public class ServiceRequestUserController extends AbstractController {

	/* Services */
	@Autowired
	private ServiceRequestService	serviceRequestService;

	@Autowired
	private ServiceService			serviceService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int serviceId) {

		//v1.0 - Implemented by JA

		final ModelAndView res;
		final Service service;
		final ServiceRequest serviceRequest;

		service = this.serviceService.findOne(serviceId);

		Assert.notNull(service);

		serviceRequest = this.serviceRequestService.create(service);

		res = this.createEditModelAndView(serviceRequest);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ServiceRequest serviceRequest, final BindingResult binding) {

		//v1.0 - Implemented by JA
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(serviceRequest);
		else
			try {
				this.serviceRequestService.save(serviceRequest);
				res = new ModelAndView("redirect:/service/user/list.do");
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(serviceRequest, "serviceRequest.commit.error");
			}

		return res;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int serviceRequestId) {

		//v1.0 - Implemented by JA
		ModelAndView res;

		final ServiceRequest serviceRequest = this.serviceRequestService.findOne(serviceRequestId);

		Assert.notNull(serviceRequest);

		try {

			res = new ModelAndView("redirect:/rendezVous/user/display.do?rendezVousId=" + serviceRequest.getRendezVous().getId());
			this.serviceRequestService.delete(serviceRequest);

		} catch (final Throwable oops) {
			res = new ModelAndView("redirect:/rendezVous/user/display.do?rendezVousId=" + serviceRequest.getRendezVous().getId());
		}

		return res;

	}
	//Ancillary Methods

	protected ModelAndView createEditModelAndView(final ServiceRequest serviceRequest) {

		//v1.0 - Implemented by JA

		ModelAndView res;
		res = this.createEditModelAndView(serviceRequest, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(final ServiceRequest serviceRequest, final String message) {

		//v1.0 - Implemented by JA

		ModelAndView res;
		final Collection<RendezVous> availableRendezVouses;

		availableRendezVouses = this.serviceService.getRendezVousesCreatedNotUsingService(serviceRequest.getService());

		res = new ModelAndView("serviceRequest/edit");
		res.addObject("serviceRequest", serviceRequest);
		res.addObject("message", message);
		res.addObject("availableRendezVouses", availableRendezVouses);

		return res;
	}
}
