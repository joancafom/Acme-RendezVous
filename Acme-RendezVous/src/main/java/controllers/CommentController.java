
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import domain.Comment;

@Controller
@RequestMapping("/comment")
public class CommentController extends AbstractController {

	/* Services */
	@Autowired
	private CommentService	commentService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int commentId) {
		ModelAndView res;
		final Comment comment = this.commentService.findOne(commentId);
		Assert.notNull(comment);

		final Collection<Comment> comments = comment.getReplies();

		res = new ModelAndView("comment/list");
		res.addObject("actorWS", "");
		res.addObject("comments", comments);

		return res;

	}
}
