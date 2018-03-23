
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
import domain.Service;

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

	@Autowired
	private ServiceService	serviceService;

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

	// -------------------------------------------------------------------------------
	// [UC-2-009] Listar Category y actualizar Category.
	// 
	// Requisitos relacionados:
	//   · REQ 9: Services belong to categories, which may be organised into arbitrary
	//            hierarchies. A category is characterised by a name and a
	//            description.
	//   · REQ 11.1: An actor who is authenticated as an administrator must be able to
	//               manage the categories of services, which includes listing,
	//               creating, updating, deleting, and reorganising them in the
	//               category hierarchies.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverListAndUpdateCategory() {

		// testingData[i][0] -> username del actor loggeado.
		// testingData[i][1] -> categoría a editar.
		// testingData[i][2] -> nuevo nombre de la categoría.
		// testingData[i][3] -> nueva descripción de la categoría.
		// testingData[i][4] -> servicio a añadir a la categoría.
		// testingData[i][5] -> servicio a eliminar de la categoría.
		// testingData[i][6] -> excepción que debe saltar.

		final Object testingData[][] = {
			{
				// 1 - (+) Un administrador edita correctamente una categoría.
				"admin", "category2", "myNewName", "myNewDescription", "service1", "service3", null
			}, {
				// 2 - (-) Un usuario no autentificado edita una categoría.
				null, "category2", "myNewName", "myNewDescription", "service1", "service3", IllegalArgumentException.class
			}, {
				// 3 - (-) Un usuario edita una categoría.
				"user1", "category2", "myNewName", "myNewDescription", "service1", "service3", IllegalArgumentException.class
			}, {
				// 4 - (-) Un administrador edita una categoría null.
				"admin", null, "myNewName", "myNewDescription", "service1", "service3", IllegalArgumentException.class
			}, {
				// 5 - (-) Un administrador actualiza el nombre de una categoría a null.
				"admin", "category2", null, "myNewDescription", "service1", "service3", IllegalArgumentException.class
			}, {
				// 6 - (-) Un administrador actualiza el nombre de una categoría a blanco.
				"admin", "category2", " ", "myNewDescription", "service1", "service3", ConstraintViolationException.class
			}, {
				// 7 - (-) Un administrador actualiza la descripción de una categoría a null.
				"admin", "category2", "myNewName", null, "service1", "service3", ConstraintViolationException.class
			}, {
				// 8 - (-) Un administrador actualiza la descripción de una categoría a blanco.
				"admin", "category2", "myNewName", "", "service1", "service3", ConstraintViolationException.class
			}, {
				// 9 - (-) Un administrador añade a una categoría un servicio cancelado.
				"admin", "category2", "myNewName", "myNewDescription", "service4", "service3", IllegalArgumentException.class
			}, {
				// 10 - (-) Un administrador actualiza el nombre de una categoría a un nombre ya existente entre las categorías con esa parentCategory.
				"admin", "category2", "Movies", "myNewDescription", "service1", "service3", IllegalArgumentException.class
			}, {
				// 11 - (-) Un administrador actualiza el nombre de una categoría añadiendo código malicioso.
				"admin", "category2", "<script>alert('Hacked!');</script>", "myNewDescription", "service1", "service3", ConstraintViolationException.class
			}, {
				// 12 - (-) Un administrador actualiza la descripción de una categoría añadiendo código malicioso.
				"admin", "category2", "myNewName", "<script>alert('Hacked!');</script>", "service1", "service3", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Category categoryToUpdate = null;
			Service serviceToAdd = null;
			Service serviceToRemove = null;

			if (testingData[i][1] != null)
				categoryToUpdate = this.categoryService.findOne(super.getEntityId((String) testingData[i][1]));

			final Category categoryCopy = categoryToUpdate;

			if (testingData[i][4] != null)
				serviceToAdd = this.serviceService.findOne(super.getEntityId((String) testingData[i][4]));

			if (testingData[i][5] != null)
				serviceToRemove = this.serviceService.findOne(super.getEntityId((String) testingData[i][5]));

			this.startTransaction();

			this.templateListAndUpdateCategory((String) testingData[i][0], categoryCopy, (String) testingData[i][2], (String) testingData[i][3], serviceToAdd, serviceToRemove, (Class<?>) testingData[i][6]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	protected void templateListAndUpdateCategory(final String username, final Category categoryToUpdate, final String newName, final String newDescription, final Service serviceToAdd, final Service serviceToRemove, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Loggearse como usuario (o como null)
		super.authenticate(username);

		try {

			// 2. Listar las categorías disponibles
			List<Category> categoriesBefore = null;

			if (username != null && username.contains("admin"))
				categoriesBefore = new ArrayList<Category>(this.categoryService.findAll());

			// 3. Editar los campos de la categoría

			if (categoryToUpdate != null) {
				categoryToUpdate.setName(newName);
				categoryToUpdate.setDescription(newDescription);

				categoryToUpdate.getServices().add(serviceToAdd);
				serviceToAdd.getCategories().add(categoryToUpdate);

				categoryToUpdate.getServices().remove(serviceToRemove);
				serviceToRemove.getCategories().remove(categoryToUpdate);
			}

			this.categoryService.save(categoryToUpdate);

			// Flush
			this.categoryService.flush();

			Assert.isTrue(categoriesBefore.size() == this.categoryService.findAll().size());

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	
	/*
	 * v1.0 - josembell
	 * 
	 * [UC-2-010] - List and Delete categories
	 */

	@Test
	public void driverListAndDeleteCategory() {
		final Object testingData[][] = {
			{
				/* + 1) Un admin elimina una categoria correctamente */
				"admin", "category1", null
			}, {
				/* - 2) Un usuario no identificado elimina una categorya */
				null, "category1", IllegalArgumentException.class
			}, {
				/* - 3) Un admin elimina una categoria null */
				"admin", null, IllegalArgumentException.class
			}, {
				/* - 4) Un usuario elimina una categoria */
				"user1", "category1", IllegalArgumentException.class
			}, {
				/* - 5) Un manager elimina una categoria */
				"manager1", "category1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Category category = null;
			if (testingData[i][1] != null)
				category = this.categoryService.findOne(this.getEntityId((String) testingData[i][1]));

			//System.out.println("Test " + (i + 1));
			this.templateListAndDeleteCategory((String) testingData[i][0], category, (Class<?>) testingData[i][2]);
			//System.out.println("Test " + (i + 1) + " - OK");
		}
	}

	/* v1.0 - josembell */
	protected void templateListAndDeleteCategory(final String username, final Category category, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Loggearse como admin */
		this.authenticate(username);
		try {
			/* 2. Listar las categorias */
			Collection<Category> categories = this.categoryService.findAll();
			final int sizeBefore = categories.size();

			/* 3. Seleccionar una y eliminarla -> entra por parámetros */
			this.categoryService.delete(category);

			/* 4. Comprobar que el tamaño es menor */
			categories = this.categoryService.findAll();
			final int sizeNow = categories.size();

			Assert.isTrue(sizeNow < sizeBefore);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - josembell
	 * 
	 * [UC-2-011] - Reorganise Categories Hierarchies
	 */
	@Test
	public void driverReorganiseCategory() {
		final Object testingData[][] = {
			{
				/* + 1) Un admin cambia el parentCategory de una category */
				"admin", "category1", "category5", null
			}, {
				/* + 2) Un admin cambia una categoria a root */
				"admin", "category2", null, null
			}, {
				/* - 3) Un usuario no identificado reorganiza una category */
				null, "category1", "category5", IllegalArgumentException.class
			}, {
				/* - 4) Un usuario regoraniza una categoria */
				"user1", "category1", "category5", IllegalArgumentException.class
			}, {
				/* - 5) Un manager reorganiza una categoria */
				"manager1", "category1", "category5", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Category category = null;
			Category parentCategory = null;
			if (testingData[i][1] != null)
				category = this.categoryService.findOne(this.getEntityId((String) testingData[i][1]));
			if (testingData[i][2] != null)
				parentCategory = this.categoryService.findOne(this.getEntityId((String) testingData[i][2]));

			//System.out.println("Test " + i);
			this.templateReorganiseCategory((String) testingData[i][0], category, parentCategory, (Class<?>) testingData[i][3]);
			//System.out.println("Test " + i + "- ok");
		}

	}

	/* v1.0 - josembell */
	protected void templateReorganiseCategory(final String username, final Category category, final Category parentCategory, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Loggearse como admin */
		this.authenticate(username);

		try {
			/* 2. Listar las categorias */
			Collection<Category> categories = this.categoryService.findAll();
			final int numCategoriesBefore = categories.size();

			/* 3. Elegir una y reorganizarla */
			category.setParentCategory(parentCategory);
			this.categoryService.reorganise(category);

			/* 4. Comprobar el tamaño */
			categories = this.categoryService.findAll();
			final int numCategoriesNow = categories.size();

			Assert.isTrue(numCategoriesNow == numCategoriesBefore);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}
}
