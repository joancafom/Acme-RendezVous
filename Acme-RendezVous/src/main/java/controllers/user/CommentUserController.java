
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.CommentService;
import services.RendezVousService;
import services.UserService;
import controllers.AbstractController;
import domain.Comment;
import domain.RendezVous;
import domain.User;

@Controller
@RequestMapping("/comment/user")
public class CommentUserController extends AbstractController {

	/* Services */
	@Autowired
	private CommentService		commentService;

	@Autowired
	private UserService			userService;

	@Autowired
	private RendezVousService	rendezVousService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int rendezVousId) {

		ModelAndView res;
		final User writter = this.userService.findByUserAccount(LoginService.getPrincipal());
		final RendezVous rv;

		rv = this.rendezVousService.findOne(rendezVousId);

		Assert.notNull(writter);
		Assert.notNull(rv);
		Assert.isTrue(rv.getAttendants().contains(writter));

		final Comment comment = this.commentService.create(rv);
		res = this.createEditModelAndView(comment);

		res.addObject("rendezVousId", rendezVousId);

		return res;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(final Comment prunedComment, final BindingResult binding) {

		ModelAndView res;

		final Comment comment = this.commentService.reconstructCreate(prunedComment, binding);
		if (binding.hasErrors())
			res = this.createEditModelAndView(comment);
		else
			try {
				this.commentService.save(comment);
				res = new ModelAndView("redirect:/rendezVous/user/display.do?rendezVousId=" + comment.getRendezVous().getId());
			} catch (final Throwable oops) {
				res = this.createEditModelAndView(comment, "comment.commit.error");
			}

		return res;

	}
	//Ancillary Methods

	protected ModelAndView createEditModelAndView(final Comment comment) {

		ModelAndView result;
		result = this.createEditModelAndView(comment, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Comment comment, final String message) {

		final ModelAndView result;
		result = new ModelAndView("comment/edit");
		result.addObject("comment", comment);
		result.addObject("message", message);

		return result;
	}

}
