
package controllers.administrator;

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

import services.CategoryService;
import controllers.AbstractController;
import domain.Category;

@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController extends AbstractController {

	// Services ---------------------------

	@Autowired
	private CategoryService	categoryService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer rootCategoryId) {

		// v1.0 - Implemented by JA

		final ModelAndView res = new ModelAndView("category/list");

		if (rootCategoryId != null) {

			final Category rootCategory = this.categoryService.findOne(rootCategoryId);
			Assert.notNull(rootCategory);

			res.addObject("rootCategory", rootCategory);

		} else {

			final Collection<Category> rootCategories = this.categoryService.findRootCategories();

			res.addObject("rootCategories", rootCategories);
		}

		return res;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final Integer parentCategoryId) {

		// v1.0 - Implemented by JA

		ModelAndView res;
		final Category parentCategory;
		if (parentCategoryId != null) {
			parentCategory = this.categoryService.findOne(parentCategoryId);
			Assert.notNull(parentCategory);

		} else
			parentCategory = null;

		final Category category = this.categoryService.create(parentCategory);
		Assert.notNull(category);

		res = this.createEditModelAndView(category);

		return res;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {

		// v1.0 - Implemented by JA

		ModelAndView res;
		final Category category = this.categoryService.findOne(categoryId);
		Assert.notNull(category);

		res = this.createEditModelAndView(category);

		return res;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Category category, final BindingResult binding) {

		// v1.0 - Implemented by JA

		ModelAndView res;

		if (binding.hasErrors())
			res = this.createEditModelAndView(category);
		else
			try {

				final Category savedCategory = this.categoryService.save(category);
				String parentCategoryId = "";

				if (savedCategory.getParentCategory() != null)
					parentCategoryId = new Integer(savedCategory.getParentCategory().getId()).toString();

				res = new ModelAndView("redirect:list.do?rootCategoryId=" + parentCategoryId);

			} catch (final Throwable oops) {
				res = this.createEditModelAndView(category, "category.commit.error");
			}

		return res;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Category category, final BindingResult binding) {

		// v1.0 - Implemented by JA

		ModelAndView res;

		try {

			this.categoryService.delete(category);

			String parentCategoryId = "";
			if (category.getParentCategory() != null)
				parentCategoryId = new Integer(category.getParentCategory().getId()).toString();

			res = new ModelAndView("redirect:list.do?rootCategoryId=" + parentCategoryId);

		} catch (final Throwable oops) {
			res = this.createEditModelAndView(category, "category.commit.error");
		}

		return res;

	}

	//Ancillary Methods
	protected ModelAndView createEditModelAndView(final Category category) {

		// v1.0 - Implemented by JA

		ModelAndView res;

		res = this.createEditModelAndView(category, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(final Category category, final String message) {

		// v1.0 - Implemented by JA

		final ModelAndView res;

		res = new ModelAndView("category/edit");

		res.addObject("category", category);
		res.addObject("message", message);

		return res;
	}

}
