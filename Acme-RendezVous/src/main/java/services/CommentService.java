
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import security.LoginService;
import domain.Comment;
import domain.RendezVous;
import domain.User;

@Service
@Transactional
public class CommentService {

	/* Repositories */

	@Autowired
	private CommentRepository	commentRepository;

	/* Services */

	@Autowired
	private UserService			userService;

	@Autowired
	private Validator			validator;


	/* CRUD Methods */

	public Comment create(final RendezVous rv, final Comment replied) {

		final Comment res = new Comment();

		final User writter = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(rv);
		Assert.isTrue(writter.getAttendedRendezVouses().contains(rv));

		res.setRendezVous(rv);
		if (replied != null)
			res.setParentComment(replied);
		res.setReplies(new ArrayList<Comment>());
		res.setUser(writter);

		//This is temporary and will be changed in the save method...
		res.setWrittenMoment(new Date());

		return res;
	}
	public Comment findOne(final int commentId) {
		return this.commentRepository.findOne(commentId);
	}

	public Collection<Comment> findAll() {
		return this.commentRepository.findAll();
	}

	public Comment save(final Comment comment) {

		Assert.notNull(comment);

		final User writter = this.userService.findByUserAccount(LoginService.getPrincipal());

		//Till level A, a comment cannot be modified once saved...
		Assert.isTrue(comment.getId() == 0);
		Assert.notNull(writter);
		Assert.isTrue(comment.getUser().equals(writter));
		Assert.isTrue(writter.getAttendedRendezVouses().contains(comment.getRendezVous()));

		final Comment res = this.commentRepository.save(comment);

		if (res.getParentComment() != null)
			res.getParentComment().getReplies().add(res);

		return res;

	}
	public void delete(final Comment comment) {

		Assert.notNull(comment);

		if (comment.getParentComment() != null)
			comment.getParentComment().getReplies().remove(comment);

		this.deleteWithReferences(comment);

	}

	//Other Business Methods

	private void deleteWithReferences(final Comment comment) {

		for (final Comment reply : comment.getReplies())
			this.deleteWithReferences(reply);

		this.commentRepository.delete(comment);

	}

	public Comment reconstructCreate(final Comment prunedComment, final BindingResult binding) {

		Comment res;
		final User writter = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(prunedComment.getId() == 0);

		res = prunedComment;

		res.setUser(writter);

		this.validator.validate(res, binding);

		return res;
	}

	public Collection<Comment> findRootCommentsByRendezVous(final RendezVous rendezVous) {
		return this.commentRepository.findRootCommentsByRendezVous(rendezVous.getId());
	}
}
