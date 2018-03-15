
package services;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.Comment;
import domain.RendezVous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CommentServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private CommentService		commentService;
	@Autowired
	private RendezVousService	rendezVousService;
	@Autowired
	private UserService			userService;


	//Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-008: Delete a Comment
	 * 1. Log in as an Admin
	 * 2. List all RendezVouses
	 * 3. Select one RendezVous
	 * 4. Select one Comment and delete it
	 * 5. Display the rendezVous
	 * 
	 * Involved REQs: 6.1
	 * 
	 * Test Cases (5; 2+ 3-):
	 * 
	 * + 1) An administrator provides a correct comment with no replies that is removed
	 * 
	 * + 2) An administrator provides a correct comment with replies that is removed
	 * 
	 * - 3) An unauthenticated actor provides a correct comment and tries to remove it
	 * 
	 * - 4) A user provides a correct comment and tries to remove it
	 * 
	 * - 5) An administrator provides a null comment an tries to remove it
	 */
	@Test
	public void driverDeleteComment() {

		final Object testingData[][] = {
			{
				"admin", "comment2", null
			}, {
				"admin", "comment1", null
			}, {
				null, "comment1", IllegalArgumentException.class
			}, {
				"user1", "comment1", IllegalArgumentException.class
			}, {
				"admin", null, IllegalArgumentException.class
			}
		};

		Comment testComment;

		for (int i = 0; i < testingData.length; i++) {
			if (testingData[i][1] != null)
				testComment = this.commentService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				testComment = null;

			this.templateDeleteComment((String) testingData[i][0], testComment, (Class<?>) testingData[i][2]);
		}

	}
	//Templates

	protected void templateDeleteComment(final String username, final Comment comment, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			this.commentService.delete(comment);

			Assert.isNull(this.commentService.findOne(comment.getId()));

			if (comment.getParentComment() != null)
				Assert.isTrue(!comment.getParentComment().getReplies().contains(comment));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	/*
	 * v1.0 - josembell
	 * 
	 * [CU-007] - Comentar en un RendezVous
	 * 
	 * REQ: 5.6
	 */
	@Test
	public void driverCreateComment() {
		final Object testingData[][] = {
			{
				/* + 1) Un usuario crea un comentario válido para un rendezVous válido */
				"user1", 3, "rendezVous2", null, "This is a test", "http://economipedia.com/wp-content/uploads/2015/11/test-de-estr%C3%A9s.png", null
			}, {
				/* + 2) Un usuario responde a un comentario válido para un rendezVous válido */
				"user1", 3, "rendezVous2", "comment3", "This is a test", "http://economipedia.com/wp-content/uploads/2015/11/test-de-estr%C3%A9s.png", null
			}, {
				/* - 3) Un usuario no identificado intenta crear un comentario */
				null, 0, "rendezVous2", null, "This is a test", "whttp://economipedia.com/wp-content/uploads/2015/11/test-de-estr%C3%A9s.png", IllegalArgumentException.class
			}, {
				/* - 4) Un usuario identificado intenta crear un comentario en un RendezVous null */
				"user1", 3, null, null, "This is a test", "http://economipedia.com/wp-content/uploads/2015/11/test-de-estr%C3%A9s.png", IllegalArgumentException.class
			}, {
				/* - 5) Un usuario identificado intenta crear un comentario en un RendezVous al que no asiste */
				"user1", 3, "rendezVous4", null, "This is a test", "http://economipedia.com/wp-content/uploads/2015/11/test-de-estr%C3%A9s.png", IllegalArgumentException.class
			}, {
				/* - 6) Un usuario identificado intenta crear un comentario sin texto */
				"user1", 3, "rendezVous2", null, null, "http://economipedia.com/wp-content/uploads/2015/11/test-de-estr%C3%A9s.png", ConstraintViolationException.class
			}, {
				/* - 7) un usuario identificado intenta crear un comentario con una imagen con url errónea */
				"user1", 3, "rendezVous2", null, "This is a test", "fail", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			RendezVous rendezVous = null;
			if (testingData[i][2] != null)
				rendezVous = this.rendezVousService.findOne(this.getEntityId((String) testingData[i][2]));
			else
				rendezVous = null;

			System.out.println("Test " + (i + 1));
			this.templateCreateComment((String) testingData[i][0], (Integer) testingData[i][1], rendezVous, (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			System.out.println("Test " + (i + 1) + " - OK");
		}
	}

	/* v1.0 - josembell */
	private void templateCreateComment(final String username, final Integer numAttendedRV, final RendezVous rendezVous, final String parentComment, final String text, final String picture, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Loggearte como usuario */
		this.authenticate(username);
		User user = null;

		try {
			if (username != null) {
				user = this.userService.findByUserAccount(LoginService.getPrincipal());

				/* 2. Listar los rendezVouses a los que asisto */
				final Collection<RendezVous> attendedRendezVouses = user.getAttendedRendezVouses();
				Assert.isTrue(attendedRendezVouses.size() == numAttendedRV);
			}

			/* 3. Seleccionar un rendezVous -> el que entra por parámetros */

			/* 4. Crear un comentario */
			Comment replied;
			int numReplies = 0;
			if (parentComment != null) {
				replied = this.commentService.findOne(this.getEntityId(parentComment));
				numReplies = replied.getReplies().size();
			} else
				replied = null;

			final Comment comment = this.commentService.create(rendezVous, replied);
			comment.setText(text);
			comment.setPicture(picture);

			/* -> Guardamos */

			this.commentService.save(comment);

			this.commentService.flush();

			/* -> Comprobamos que si era un reply, el numero de replies del replied está incrementado */
			if (replied != null) {
				final int numReplies2 = replied.getReplies().size();
				Assert.isTrue(numReplies + 1 == numReplies2);
			}
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
