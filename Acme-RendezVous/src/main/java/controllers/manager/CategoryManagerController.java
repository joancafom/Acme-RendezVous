
package controllers.manager;

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
import services.CategoryService;
import services.ManagerService;
import services.ServiceService;
import controllers.AbstractController;
import domain.Category;
import domain.Manager;
import forms.AddCategoryForm;

@Controller
@RequestMapping("/category/manager")
public class CategoryManagerController extends AbstractController {

	// Services ---------------------------

	@Autowired
	private CategoryService	categoryService;

	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private ManagerService	managerService;


	/* v1.0 - josembell */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(@RequestParam final Integer serviceId) {
		ModelAndView res;
		final domain.Service service = this.serviceService.findOne(serviceId);
		Assert.notNull(service);
		final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(manager);
		Assert.isTrue(manager.getServices().contains(service));

		final AddCategoryForm form = new AddCategoryForm();
		form.setServiceId(serviceId);

		res = this.createEditModelAndView(form);
		res.addObject("categories", this.categoryService.findAllExceptAdded(service));

		return res;

	}

	/* v1.0 - josembell */

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView remove(@RequestParam final int categoryId, @RequestParam final int serviceId) {
		ModelAndView result;
		final Category category = this.categoryService.findOne(categoryId);
		Assert.notNull(category);
		final domain.Service service = this.serviceService.findOne(serviceId);
		Assert.notNull(service);
		final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(manager);
		Assert.isTrue(manager.getServices().contains(service));

		result = new ModelAndView("redirect:/service/manager/edit.do?serviceId=" + service.getId());

		try {
			this.serviceService.removeCategory(service, category);
		} catch (final Throwable oops) {
			result.addObject("message", "comment.commit.error");
		}

		return result;
	}

	/* v1.0 - josembell */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "add")
	public ModelAndView add(@Valid final AddCategoryForm form, final BindingResult binding) {
		ModelAndView res;
		if (binding.hasErrors())
			res = new ModelAndView("category/add");
		else
			try {

				final domain.Service service = this.serviceService.findOne(form.getServiceId());
				final Category category = this.categoryService.findOne(form.getCategoryId());

				this.serviceService.addCategory(service, category);

				res = new ModelAndView("redirect:/service/manager/edit.do?serviceId=" + service.getId());

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(form, "category.commit.error");
			}

		return res;

	}

	/*
	 * @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	 * public ModelAndView delete(final Category category, final BindingResult binding) {
	 * 
	 * // v1.0 - Implemented by JA
	 * 
	 * ModelAndView res;
	 * 
	 * try {
	 * 
	 * this.categoryService.delete(category);
	 * 
	 * String parentCategoryId = "";
	 * if (category.getParentCategory() != null)
	 * parentCategoryId = new Integer(category.getParentCategory().getId()).toString();
	 * 
	 * res = new ModelAndView("redirect:list.do?rootCategoryId=" + parentCategoryId);
	 * 
	 * } catch (final Throwable oops) {
	 * res = this.createEditModelAndView(category, "category.commit.error");
	 * res.addObject("toEdit", true);
	 * }
	 * 
	 * return res;
	 * 
	 * }
	 */
	//Ancillary Methods
	protected ModelAndView createEditModelAndView(final AddCategoryForm form) {

		// v1.0 - Implemented by JA

		ModelAndView res;

		res = this.createEditModelAndView(form, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(final AddCategoryForm form, final String message) {

		// v1.0 - Implemented by JA

		final ModelAndView res;

		res = new ModelAndView("category/add");

		res.addObject("addCategoryForm", form);
		res.addObject("message", message);

		return res;
	}

}
