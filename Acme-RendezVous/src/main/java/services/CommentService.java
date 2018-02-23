
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

	public Comment create(final RendezVous rv) {

		final Comment res = new Comment();

		final User writter = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(rv);
		Assert.isTrue(writter.getAttendedRendezVouses().contains(rv));

		res.setRendezVous(rv);
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

		return this.commentRepository.save(comment);

	}

	public void delete(final Comment comment) {

		Assert.notNull(comment);

		this.commentRepository.delete(comment);

	}

	//Other Business Methods

	public Comment reconstructCreate(final Comment prunedComment, final BindingResult binding) {

		Comment res;
		final User writter = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(prunedComment.getId() == 0);

		res = prunedComment;

		res.setUser(writter);

		this.validator.validate(res, binding);

		return res;
	}
}
