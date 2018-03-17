
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Question;
import domain.RendezVous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class QuestionServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private QuestionService		questionService;

	@Autowired
	private RendezVousService	rendezVousService;

	//Helping Services
	@PersistenceContext
	private EntityManager		entityManager;


	// -------------------------------------------------------------------------------
	// [UC-016] Listar Questions y eliminar una Question relacionada con un
	// RendezVous.
	// 
	// Requisitos relacionados:
	//   · REQ 18: The creator of a rendezvous may associate a number of questions
	//             with it, each of which must be answered when a user RSVPs that
	//             rendezvous.
	//   · REQ 21.1: An actor who is authenticated as a user must be able to manage
	//               the questions that are associated with a rendezvous that he or
	//               she's created previously.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverListAndDeleteQuestion() {

		// testingData[i][0] -> username del usuario loggeado.
		// testingData[i][1] -> rendezVous de la question a eliminar.
		// testingData[i][2] -> question a eliminar.
		// testingData[i][3] -> excepción que debe saltar.

		final Object testingData[][] = {
			{
				// 1 - (+) Un usuario no loggeado lista las questions de un rendezVous
				null, "rendezVous2", null, null
			}, {
				// 2 - (-) Un usuario no loggeado elimina una question
				null, "rendezVous2", "question1", IllegalArgumentException.class
			}, {
				// 3 - (+) Un usuario lista questions de un rendezVous suyo y elimina una
				"user3", "rendezVous2", "question1", null
			}, {
				// 4 - (-) Un usuario lista questions de un rendezVous que no es suyo y elimina una 
				"user1", "rendezVous2", "question1", IllegalArgumentException.class
			}, {
				// 5 - (-) Un administrador elimina una question
				"admin", "rendezVous2", "question1", IllegalArgumentException.class
			}, {
				// 6 - (-) Un manager elimina una question
				"manager1", "rendezVous2", "question1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			final RendezVous rendezVous = this.rendezVousService.findOne(super.getEntityId((String) testingData[i][1]));

			this.startTransaction();

			this.templateListAndDeleteQuestion((String) testingData[i][0], (String) testingData[i][1], rendezVous, (String) testingData[i][2], (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}

	protected void templateListAndDeleteQuestion(final String username, final String rendezVousBean, final RendezVous rendezVous, final String questionBean, final Class<?> expected) {

		// 1. Loggearse como Usuario (o como null)
		super.authenticate(username);

		Class<?> caught = null;

		try {

			// 2. Listar todos las questions asociadas con un rendezVous

			final Collection<Question> allByRendezVous = this.questionService.findAllOrderedByRendezVous(rendezVous);

			if (!(username == null && expected == null)) {

				// 3. Eliminar una question del rendezVous

				final Question questionToDelete = this.questionService.findOne(super.getEntityId(questionBean));

				this.questionService.delete(questionToDelete);

				// Flush
				this.questionService.flush();

				// 4. Listar las questions y comprobar que no contienen la borrada

				final Collection<Question> newAllByRendezVous = this.questionService.findAllOrderedByRendezVous(rendezVous);

				Assert.isTrue(!newAllByRendezVous.containsAll(allByRendezVous));
				Assert.isTrue(!this.questionService.findAll().contains(questionToDelete));
			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
