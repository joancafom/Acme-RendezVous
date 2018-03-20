
package services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CategoryServiceTest extends AbstractTest {

	// System Under Test

	@Autowired
	private CategoryService	categoryService;

	//Helping Services

	@PersistenceContext
	private EntityManager	entityManager;


	// Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-2-008: List and Create Categories
	 * 1. Log in to the system as Administrator
	 * 2. List the available categories
	 * 3. Create a new Category
	 * 4. List the available categories
	 * 
	 * Involved REQs: 9, 11.1
	 * 
	 * Test Cases ():
	 * 
	 * + 1) An Administrator logs in, retrieves the available categories and successfully creates a new one providing correct data (parentCategory = null --> root)
	 * 
	 * + 2) An Administrator logs in, retrieves the available categories and successfully creates a new one providing correct data (parentCategory = existing --> child)
	 * 
	 * - 3) A Manager logs in and tries to create a new category providing correct data (only administrators can)
	 * 
	 * - 4) An unauthenticated actor tries to create a new category providing correct data (only administrators can)
	 * 
	 * - 5) An Administrator logs in, retrieves the available categories and tries to create a new one with incorrect data (name = null)
	 * 
	 * - 6) An Administrator logs in, retrieves the available categories and tries to create a new one with incorrect data (name = " ")
	 * 
	 * - 7) An Administrator logs in, retrieves the available categories and tries to create a new one with incorrect data (description = null)
	 * 
	 * - 8) An Administrator logs in, retrieves the available categories and tries to create a new one with incorrect data (description = " ")
	 * 
	 * - 9) An Administrator logs in, retrieves the available categories and tries to create a new one with incorrect data (name not unique within the context of the same parent category)
	 * 
	 * + 10) An Administrator logs in, retrieves the available categories and creates a new one providing correct data (already existing name but not in the same context)
	 * 
	 * - 11) An Administrator logs in, retrieves the available categories and tries to create a new one with incorrect data (category = null)
	 * 
	 * - 12) An Administrator logs in, retrieves the available categories and tries to create a new one with incorrect data (name with XSS)
	 * 
	 * - 13) An Administrator logs in, retrieves the available categories and tries to create a new one with incorrect data (description with XSS)
	 */

	@Test
	public void driverListCreateCategory() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the name of the Category.
		// testingData[i][2] -> the description of the Category.
		// testingData[i][3] -> the beanName of the parentCategory of the Category.
		// testingData[i][4] -> a provided Category to save.
		// testingData[i][5] -> if we want to create using the given entity or not (the provided entity in [4]).
		// testingData[i][6] -> the expected exception.

		final Object testingData[][] = {
			{
				"admin", "Test Cat 1", "Test Description 1", null, null, false, null
			}, {
				"admin", "Test Cat 2", "Test Description 2", "category1", null, false, null
			}, {
				"manager1", "Test Cat 3", "Test Description 3", null, null, false, IllegalArgumentException.class
			}, {
				null, "Test Cat 4", "Test Description 4", null, null, false, IllegalArgumentException.class
			}, {
				"admin", null, "Test Description 5", null, null, false, IllegalArgumentException.class
			}, {
				"admin", "   ", "Test Description 6", null, null, false, ConstraintViolationException.class
			}, {
				"admin", "Test Cat 7", null, null, null, false, ConstraintViolationException.class
			}, {
				"admin", "Test Cat 8", "  ", null, null, false, ConstraintViolationException.class
			}, {
				"admin", "Leisure", "Test Description 9", null, null, false, IllegalArgumentException.class
			}, {
				"admin", "Leisure", "Test Description 10", "category4", null, false, null
			}, {
				"admin", null, null, null, null, true, IllegalArgumentException.class
			}, {
				"admin", "<script>alert('Hacked!');</script>", "Test Description 12", null, null, false, ConstraintViolationException.class
			}, {
				"admin", "Test Cat 13", "<script>alert('Hacked!');</script>", "category2", null, false, ConstraintViolationException.class
			}
		};

		Category category = null;

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			if (!(Boolean) testingData[i][5] && testingData[i][4] != null)
				category = this.categoryService.findOne(this.getEntityId((String) testingData[i][4]));
			else
				category = null;

			this.templateListCreateCategory((String) testingData[i][0], category, (Boolean) testingData[i][5], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][6]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	protected void templateListCreateCategory(final String username, final Category category, final Boolean useGiven, final String name, final String description, final String parentCategoryBeanName, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;
		final Category categoryToSave;

		//1. Log in to the system
		this.authenticate(username);

		try {

			//2. List the available categories (skipped if not an Admin)
			List<Category> categoriesBefore = null;

			if (username != null && username.contains("admin"))
				categoriesBefore = new ArrayList<Category>(this.categoryService.findAll());

			//3. Create a new Category

			Category parentCategory = null;

			if (parentCategoryBeanName != null)
				parentCategory = this.categoryService.findOne(this.getEntityId(parentCategoryBeanName));

			if (useGiven)
				categoryToSave = category;
			else {

				//Simulates the Actor inputing data
				categoryToSave = this.categoryService.create(parentCategory);
				categoryToSave.setName(name);
				categoryToSave.setDescription(description);
			}

			final Category savedCategory = this.categoryService.save(categoryToSave);

			//Force the Service to Flush
			this.categoryService.flush();

			Assert.isTrue(!savedCategory.equals(categoryToSave));

			if (parentCategory != null) {
				Assert.isTrue(parentCategory.getChildCategories().contains(savedCategory));
				Assert.isTrue(savedCategory.getParentCategory().equals(parentCategory));
			} else
				Assert.isTrue(savedCategory.getParentCategory() == null);

			//4. List the available categories
			final List<Category> categoriesAfter = new ArrayList<Category>(this.categoryService.findAll());

			Assert.isTrue(categoriesAfter.containsAll(categoriesBefore));
			Assert.isTrue(categoriesAfter.size() == categoriesBefore.size() + 1);
			Assert.isTrue(categoriesAfter.contains(savedCategory));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}
}
