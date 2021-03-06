
package controllers.user;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
import services.AnswerService;
import services.CategoryService;
import services.CommentService;
import services.QuestionService;
import services.RendezVousService;
import services.UserService;
import controllers.AbstractController;
import domain.Answer;
import domain.Category;
import domain.Question;
import domain.RendezVous;
import domain.User;
import forms.RSVPForm;
import forms.SimilarRendezVousForm;

@Controller
@RequestMapping("/rendezVous/user")
public class RendezVousUserController extends AbstractController {

	/* Services */
	@Autowired
	private RendezVousService	rendezVousService;

	@Autowired
	private UserService			userService;

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private CommentService		commentService;

	@Autowired
	private CategoryService		categoryService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final String show, @RequestParam(required = false) final Integer categoryId) {
		ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Collection<RendezVous> rendezVouses = null;
		final User me = user;

		Assert.notNull(user);

		result = new ModelAndView("rendezVous/list");
		boolean own = false;

		if (show.equals("mine")) {
			rendezVouses = user.getCreatedRendezVouses();
			result.addObject("hasRendezVouses", true);
			own = true;

		} else if (show.equals("attended")) {
			rendezVouses = user.getAttendedRendezVouses();
			result.addObject("hasRendezVouses", true);
		} else if (show.equals("all") && user.getAge() < 18) {
			rendezVouses = this.rendezVousService.findAllNotAdult();
			result.addObject("hasRendezVouses", true);
		} else if (show.equals("category")) {
			Collection<Category> categories = null;

			if (categoryId != null) {

				final Category category = this.categoryService.findOne(categoryId);
				Assert.notNull(category);
				if (user.getAge() < 18)
					rendezVouses = this.rendezVousService.findAllNotAdultByCategory(category);
				else
					rendezVouses = this.rendezVousService.findAllByCategory(category);

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
			rendezVouses = this.rendezVousService.findAll();
			result.addObject("hasRendezVouses", true);
		}

		result.addObject("own", own);
		result.addObject("me", me);
		result.addObject("actorWS", "user/");
		result.addObject("rendezVouses", rendezVouses);

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int rendezVousId) {

		ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		Assert.notNull(rendezVous);

		final boolean hasRSVP = user.getAttendedRendezVouses().contains(rendezVous);
		final boolean own = rendezVous.getCreator().equals(user);

		if (user.getAge() < 18)
			Assert.isTrue(!rendezVous.getIsForAdults());

		result = new ModelAndView("rendezVous/display");

		result.addObject("rendezVous", rendezVous);
		result.addObject("rootComments", this.commentService.findRootCommentsByRendezVous(rendezVous));
		result.addObject("hasRSVP", hasRSVP);
		result.addObject("own", own);
		result.addObject("actorWS", "user/");

		return result;
	}
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam final int rendezVousId) {
		final ModelAndView res;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		Assert.notNull(rendezVous);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		Assert.isTrue(!rendezVous.getIsDeleted());
		Assert.isTrue(rendezVous.getOrgDate().after(new Date()));
		Assert.isTrue(rendezVous.getAttendants().contains(user));

		res = new ModelAndView("rendezVous/cancel");
		res.addObject("rendezVous", rendezVous);

		return res;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public ModelAndView cancel(RendezVous rendezVous, final BindingResult binding) {
		ModelAndView res = null;

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		rendezVous = this.rendezVousService.reconstruct(rendezVous, binding);
		final Collection<Answer> answers = this.answerService.findAllByUserAndRendezVous(user, rendezVous);

		res = new ModelAndView("rendezVous/cancel");

		if (binding.hasErrors())
			res.addObject("rendezVous", rendezVous);
		else
			try {
				for (final Answer a : answers)
					this.answerService.delete(a);
				this.rendezVousService.cancelRSVP(rendezVous);
				res = new ModelAndView("redirect:list.do?show=all");
			} catch (final Throwable oops) {
				res.addObject("message", "rendezVous.commit.error");
				res.addObject("rendezVous", rendezVous);
			}
		return res;
	}

	@RequestMapping(value = "/rsvp", method = RequestMethod.GET)
	public ModelAndView rsvp(@RequestParam final int rendezVousId) {
		final ModelAndView res;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);
		final RSVPForm rsvpForm = new RSVPForm();

		Assert.notNull(rendezVous);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		Assert.isTrue(!rendezVous.getIsDeleted());
		Assert.isTrue(rendezVous.getOrgDate().after(new Date()));
		Assert.isTrue(!rendezVous.getAttendants().contains(user));
		if (rendezVous.getIsForAdults())
			Assert.isTrue(user.getAge() >= 18);

		rsvpForm.setRendezVous(rendezVousId);

		res = new ModelAndView("rendezVous/rsvp");
		res.addObject("rsvpForm", rsvpForm);

		final Collection<String> answers = new LinkedList<String>();
		final Collection<Question> questions = this.questionService.findAllOrderedByRendezVous(rendezVous);

		for (@SuppressWarnings("unused")
		final Question q : questions)
			answers.add(new String());

		res.addObject("answersBlank", answers);
		res.addObject("questions", questions);

		return res;
	}
	@RequestMapping(value = "/rsvp", method = RequestMethod.POST)
	public ModelAndView rsvp(final RSVPForm rendezVous, final BindingResult binding) {
		ModelAndView res = null;
		RendezVous result;

		result = this.rendezVousService.reconstruct(rendezVous, binding);
		final List<Answer> answers = this.answerService.reconstuct(rendezVous, binding);

		res = new ModelAndView("rendezVous/rsvp");

		if (binding.hasErrors()) {
			res.addObject("rsvpForm", rendezVous);
			res.addObject("questions", this.questionService.findAllOrderedByRendezVous(result));
			res.addObject("message", "rendezVous.emptyInput.error");
		} else
			try {
				for (final Answer a : answers)
					//Assert.isTrue(a.getText() != "" || a.getText() == null);
					this.answerService.save(a);
				this.rendezVousService.acceptRSVP(result);

				res = new ModelAndView("redirect:display.do?rendezVousId=" + rendezVous.getRendezVous());
			} catch (final Throwable oops) {
				res.addObject("message", "rendezVous.commit.error");
				res.addObject("rsvpForm", rendezVous);
			}
		return res;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.create();
		//final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		result = this.createEditModelAndView(rendezVous);

		result.addObject("toEdit", true);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int rendezVousId) {
		ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(user.getCreatedRendezVouses().contains(rendezVous));
		Assert.isTrue(rendezVous.getIsFinal() == false);
		Assert.isTrue(rendezVous.getIsDeleted() == false);
		Assert.isTrue(rendezVous.getOrgDate().after(new Date()));
		Assert.isTrue(!rendezVous.getIsFinal());

		result = this.createEditModelAndView(rendezVous);

		result.addObject("toEdit", true);

		return result;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int rendezVousId) {
		ModelAndView result;
		final RendezVous rendezVous = this.rendezVousService.findOne(rendezVousId);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(user.getCreatedRendezVouses().contains(rendezVous));

		result = this.createEditModelAndView(rendezVous);
		result.addObject("toDelete", true);

		return result;

	}

	@RequestMapping(value = "/createLink", method = RequestMethod.GET)
	public ModelAndView createLink(@RequestParam final int rendezVousId) {
		final ModelAndView result;

		final RendezVous rendezVousParent = this.rendezVousService.findOne(rendezVousId);
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(user.getCreatedRendezVouses().contains(rendezVousParent));

		final SimilarRendezVousForm rendezVous = new SimilarRendezVousForm();
		rendezVous.setId(rendezVousId);

		result = this.createEditModelAndView(rendezVous);
		result.addObject("toAddLink", true);

		return result;

	}

	@RequestMapping(value = "/deleteLink", method = RequestMethod.GET)
	public ModelAndView deleteLink(@RequestParam final int parentRendezVousId, @RequestParam final int similarRendezVousId) {
		final ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		final RendezVous parentRendezVous = this.rendezVousService.findOne(parentRendezVousId);
		final RendezVous similarRendezVous = this.rendezVousService.findOne(similarRendezVousId);

		Assert.isTrue(user.getCreatedRendezVouses().contains(parentRendezVous));
		Assert.isTrue(parentRendezVous.getSimilarRendezVouses().contains(similarRendezVous));

		final SimilarRendezVousForm rendezVous = new SimilarRendezVousForm();
		rendezVous.setId(parentRendezVousId);
		rendezVous.setRendezVous(similarRendezVousId);

		result = this.createEditModelAndView(rendezVous);
		result.addObject("toDeleteLink", true);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(RendezVous rendezVous, final BindingResult binding) {
		ModelAndView result;

		rendezVous = this.rendezVousService.reconstructRendezVous(rendezVous, binding);

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(rendezVous);
			result.addObject("toEdit", true);
		} else
			try {
				this.rendezVousService.save(rendezVous);
				result = new ModelAndView("redirect:list.do?show=mine");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(rendezVous, "rendezVous.commit.error");
				result.addObject("toEdit", true);
			}

		return result;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final RendezVous rendezVous, final BindingResult binding) {
		ModelAndView result;

		try {
			Assert.isTrue(!rendezVous.getIsFinal());
			this.rendezVousService.virtualDelete(rendezVous);
			result = new ModelAndView("redirect:list.do?show=mine");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(rendezVous, "rendezVous.commit.error");
			result.addObject("toDelete", true);
		}

		return result;

	}

	@RequestMapping(value = "/editLink", method = RequestMethod.POST, params = "save")
	public ModelAndView saveLink(@Valid final SimilarRendezVousForm rendezVous, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(rendezVous);
			result.addObject("toAddLink", true);
		} else
			try {
				final RendezVous sRV = this.rendezVousService.getSimilarRendezVousByForm(rendezVous);
				Assert.notNull(sRV);
				final RendezVous pRV = this.rendezVousService.getParentRendezVousByForm(rendezVous);
				Assert.notNull(pRV);
				this.rendezVousService.addSimilarRendezVous(pRV, sRV);
				result = new ModelAndView("redirect:display.do?rendezVousId=" + pRV.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(rendezVous, "rendezVous.commit.error");
				result.addObject("toAddLink", true);
			}

		return result;

	}

	@RequestMapping(value = "/deleteLink", method = RequestMethod.POST, params = "delete")
	public ModelAndView deleteLink(@Valid final SimilarRendezVousForm rendezVous, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(rendezVous);
			result.addObject("toDeleteLink", true);
		} else
			try {
				final RendezVous sRV = this.rendezVousService.getSimilarRendezVousByForm(rendezVous);
				final RendezVous pRV = this.rendezVousService.getParentRendezVousByForm(rendezVous);
				this.rendezVousService.deleteSimilarRendezVous(pRV, sRV);
				result = new ModelAndView("redirect:display.do?rendezVousId=" + pRV.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(rendezVous, "rendezVous.commit.error");
				result.addObject("toDeleteLink", true);
			}

		return result;

	}

	protected ModelAndView createEditModelAndView(final RendezVous rendezVous) {
		ModelAndView result;
		result = this.createEditModelAndView(rendezVous, null);
		return result;
	}

	private ModelAndView createEditModelAndView(final RendezVous rendezVous, final String message) {
		final ModelAndView result;
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		result = new ModelAndView("rendezVous/edit");
		result.addObject("rendezVous", rendezVous);
		result.addObject("message", message);

		if (user.getAge() < 18)
			result.addObject("under18", true);

		return result;
	}
	protected ModelAndView createEditModelAndView(final SimilarRendezVousForm rendezVous) {
		ModelAndView result;
		result = this.createEditModelAndView(rendezVous, null);
		return result;
	}

	private ModelAndView createEditModelAndView(final SimilarRendezVousForm rendezVous, final String message) {
		final ModelAndView result;
		final RendezVous parentRendezVous = this.rendezVousService.findOne(rendezVous.getId());
		result = new ModelAndView("rendezVous/edit");
		result.addObject("rendezVousForm", rendezVous);
		result.addObject("message", message);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Collection<RendezVous> rendezVouses;
		if (user.getAge() >= 18)
			rendezVouses = this.rendezVousService.findAll();
		else
			rendezVouses = this.rendezVousService.findAllNotAdult();

		for (final RendezVous rv : parentRendezVous.getSimilarRendezVouses())
			rendezVouses.remove(rv);
		rendezVouses.remove(parentRendezVous);

		result.addObject("rendezVouses", rendezVouses);

		return result;
	}

}
