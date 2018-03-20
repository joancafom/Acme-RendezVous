
package controllers;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.CommentService;
import services.RendezVousService;
import domain.Category;
import domain.RendezVous;

@Controller
@RequestMapping("/rendezVous")
public class RendezVousController extends AbstractController {

	/* Services */
	@Autowired
	private RendezVousService	rendezVousService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private CategoryService		categoryService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rendezVousId) {

		ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		Assert.notNull(rendezVous);
		Assert.isTrue(!rendezVous.getIsForAdults());

		result = new ModelAndView("rendezVous/display");

		result.addObject("rendezVous", rendezVous);
		result.addObject("rootComments", this.commentService.findRootCommentsByRendezVous(rendezVous));

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer categoryId, @RequestParam(required = true) final String show) {

		Assert.isTrue(show.equals("all") || show.equals("category"));

		ModelAndView result;
		Collection<RendezVous> rendezVouses = new HashSet<RendezVous>();

		result = new ModelAndView("rendezVous/list");

		if (show.equals("category")) {
			Collection<Category> categories = null;

			if (categoryId != null) {

				final Category category = this.categoryService.findOne(categoryId);
				Assert.notNull(category);
				rendezVouses = this.rendezVousService.findAllNotAdultByCategory(category);
				categories = category.getChildCategories();
				result.addObject("hasCategories", true);
				result.addObject("hasRendezVouses", true);
				result.addObject("category", category);

			} else {
				categories = this.categoryService.findRootCategories();
				result.addObject("hasCategories", true);
				result.addObject("hasRendezVouses", false);
			}

			result.addObject("categories", categories);

		} else {
			rendezVouses = this.rendezVousService.findAllNotAdult();
			result.addObject("hasCategories", false);
			result.addObject("hasRendezVouses", true);
		}

		result.addObject("rendezVouses", rendezVouses);
		result.addObject("own", false);
		result.addObject("actorWS", "");

		return result;
	}
}
