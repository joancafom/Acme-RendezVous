
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import controllers.AbstractController;
import domain.Comment;

@Controller
@RequestMapping("/comment/administrator")
public class CommentAdministratorController extends AbstractController {

	/* Services */
	@Autowired
	private CommentService	commentService;


	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int commentId) {

		ModelAndView res;
		final Comment comment = this.commentService.findOne(commentId);

		Assert.notNull(comment);

		res = new ModelAndView("redirect:/rendezVous/administrator/display.do?rendezVousId=" + comment.getRendezVous().getId());

		try {
			this.commentService.delete(comment);
		} catch (final Throwable oops) {
			res.addObject("messageCode", "comment.commit.error");
		}

		return res;
	}
}
