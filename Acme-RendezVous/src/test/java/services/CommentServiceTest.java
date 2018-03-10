
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Comment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CommentServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private CommentService	commentService;


	//Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * Req to Test: 6.1
	 * An actor who is authenticated as an administrator must be able to
	 * remove a comment that he or she thinks is inappropriate
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

}
