
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

import security.LoginService;
import utilities.AbstractTest;
import domain.Category;
import domain.Manager;
import domain.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ServiceServiceTest extends AbstractTest {

	// System Under Test

	@Autowired
	private ServiceService	serviceService;

	// Fixtures

	@Autowired
	private ManagerService	managerService;

	@PersistenceContext
	private EntityManager	entityManager;

	@Autowired
	private CategoryService	categoryService;


	// Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-2-005: List and Edit Services
	 * 1. Log in to the system as a Manager
	 * 2. List his/her Services
	 * 3. Select a Service from the list
	 * 4. Edit the Service
	 * 4. List his/her Services and check the changes
	 * 
	 * Involved REQs: 2, 5.2
	 * 
	 * Test Cases (16; 2+ 14-):
	 * 
	 * + 1) A Manager logs in, list his/her Services, selects one and modifies some data correctly (name, description and picture)
	 * Then he faithfully checks that the changes appear in the listing.
	 * 
	 * - 2) A User logs in, selects one Service and tries to modify it using correct data (name, description and picture)
	 * 
	 * - 3) An unauthenticated actor selects one Service and tries to modify it using correct data (name, description and picture)
	 * 
	 * - 4) A Manager logs in, list his/her Services, selects one and tries to modify it with incorrect data (name = null)
	 * 
	 * - 5) A Manager logs in, list his/her Services, selects one and tries to modify it with incorrect data (name = " ")
	 * 
	 * - 6) A Manager logs in, list his/her Services, selects one and tries to modify it with incorrect data (description = null)
	 * 
	 * - 7) A Manager logs in, list his/her Services, selects one and tries to modify it with incorrect data (description = " ")
	 * 
	 * + 8) A Manager logs in, list his/her Services, selects one with a picture and removes it (picture = null)
	 * Then he faithfully checks that the changes appear in the listing.
	 * 
	 * - 9) A Manager logs in, list his/her Services, selects one and tries to modify it with incorrect data (name with XSS)
	 * 
	 * - 10) A Manager logs in, list his/her Services, selects one and tries to modify it with incorrect data (description with XSS)
	 * 
	 * - 11) A Manager logs in, list his/her Services, selects one and tries to modify it with incorrect data (picture with XSS)
	 * 
	 * - 12) A Manager logs in, list his/her Services, selects one and tries to update "isCanceled" property (from true -> false)
	 * 
	 * - 13) A Manager logs in, list his/her Services, selects one and tries to update "isCanceled" property (from false -> true)
	 * 
	 * - 14) A Manager logs in, selects one Service from another Manager and tries to modify it using correct data (description)
	 * 
	 * - 15) A Manager logs in and tries to update a null Service
	 * 
	 * - 16) A Manager logs in, list his/her Services and tries to edit a cancelled Service.
	 */

	@Test
	public void driverListEditService() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the beanName of the Service to edit.
		// testingData[i][2] -> the name of the Service.
		// testingData[i][3] -> the description of the Service.
		// testingData[i][4] -> the picture URL of the Service.
		// testingData[i][5] -> isCanceled property of Service.
		// testingData[i][6] -> String with the names of the parameters we want to update, separated by blank spaces
		// testingData[i][7] -> the expected exception.

		final Object testingData[][] = {

			{
				"manager3", "service1", "Test Service 1", "Test Description 1", "https://goo.gl/FbAHdJ", false, "name description picture", null
			}, {
				"user1", "service1", "Test Service 2", "Test Description 2", "https://goo.gl/FbAHdJ", false, "name description picture", IllegalArgumentException.class
			}, {
				null, "service1", "Test Service 3", "Test Description 3", "https://goo.gl/FbAHdJ", false, "name description picture", IllegalArgumentException.class
			}, {
				"manager3", "service2", null, null, null, false, "name", ConstraintViolationException.class
			}, {
				"manager3", "service2", " ", null, null, false, "name", ConstraintViolationException.class
			}, {
				"manager2", "service3", null, null, null, false, "description", ConstraintViolationException.class
			}, {
				"manager2", "service3", null, " ", null, false, "description", ConstraintViolationException.class
			}, {
				"manager3", "service2", null, null, null, false, "picture", null
			}, {
				"manager2", "service3", "<script>alert('Hacked!');</script>", null, null, false, "name", ConstraintViolationException.class
			}, {
				"manager2", "service3", null, "<script>alert('Hacked!');</script>", null, false, "description", ConstraintViolationException.class
			}, {
				"manager3", "service1", null, null, "<script>alert('Hacked!');</script>", false, "picture", ConstraintViolationException.class
			}, {
				"manager2", "service4", null, null, null, false, "isCanceled", IllegalArgumentException.class
			}, {
				"manager2", "service5", null, null, null, true, "isCanceled", IllegalArgumentException.class
			}, {
				"manager1", "service1", null, "Test Description 14", null, false, "description", IllegalArgumentException.class
			}, {
				"manager1", null, null, null, null, false, "", IllegalArgumentException.class
			}, {
				"manager2", "service4", "Test Service 16", null, null, false, "name", IllegalArgumentException.class
			}
		};

		domain.Service service = null;
		domain.Service serviceCopy;

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			if (testingData[i][1] != null) {
				service = this.serviceService.findOne(this.getEntityId((String) testingData[i][1]));
				serviceCopy = this.serviceService.copy(service);
			} else {
				service = null;
				serviceCopy = null;
			}

			final String changes = (String) testingData[i][6];

			if (changes.contains("name"))
				serviceCopy.setName((String) testingData[i][2]);

			if (changes.contains("description"))
				serviceCopy.setDescription((String) testingData[i][3]);

			if (changes.contains("picture"))
				serviceCopy.setPicture((String) testingData[i][4]);

			if (changes.contains("isCanceled"))
				serviceCopy.setIsCanceled((Boolean) testingData[i][5]);

			this.templateListEditService((String) testingData[i][0], serviceCopy, changes, (Class<?>) testingData[i][7]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-2-006: List and Create Services
	 * 1. Log in to the system as a Manager
	 * 2. List his/her Services
	 * 3. Create a Service
	 * 4. List his/her Services and check the changes
	 * 
	 * Involved REQs: 2, 5.2
	 * 
	 * Test Cases (13; 2+ 11-):
	 * 
	 * + 1) A Manager provides correct data and successfully creates a Service (name, description and picture)
	 * 
	 * - 2) An unauthenticated actor provides correct data tries to create a Service
	 * 
	 * - 3) A User provides correct data tries to create a Service
	 * 
	 * + 4) A Manager provides correct data and successfully creates a Service (name and description)
	 * 
	 * - 5) A Manager provides incorrect data and tries to create a Service (name = null)
	 * 
	 * - 6) A Manager provides incorrect data and tries to create a Service (name = " ")
	 * 
	 * - 7) A Manager provides incorrect data and tries to create a Service (description = null)
	 * 
	 * - 8) A Manager provides incorrect data and tries to create a Service (description = " ")
	 * 
	 * - 9) A Manager provides incorrect data and tries to create a Service (name with XSS)
	 * 
	 * - 10) A Manager provides incorrect data and tries to create a Service (description with XSS)
	 * 
	 * - 11) A Manager provides incorrect data and tries to create a Service (picture with XSS and hence not URL)
	 * 
	 * - 12) A Manager provides incorrect data and tries to create a Service (service = null)
	 * 
	 * - 13) A Manager provides incorrect data and tries to create a Service (isCanceled = true)
	 */

	@Test
	public void driverListCreateService() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the name of the Service.
		// testingData[i][2] -> the description of the Service.
		// testingData[i][3] -> the picture URL of the Service.
		// testingData[i][4] -> isCanceled property of Service.
		// testingData[i][5] -> the beanName of the Service to retrieve from the BD.
		// testingData[i][6] -> if we want to retrieve a Service from the BD or not.
		// testingData[i][7] -> the expected exception.

		final Object testingData[][] = {

			{
				"manager1", "Test Service 1", "Test Description 1", "https://goo.gl/FbAHdJ", false, null, false, null
			}, {
				null, "Test Service 2", "Test Description 2", "https://goo.gl/FbAHdJ", false, null, false, IllegalArgumentException.class
			}, {
				"user2", "Test Service 3", "Test Description 3", "https://goo.gl/FbAHdJ", false, null, false, IllegalArgumentException.class
			}, {
				"manager1", "Test Service 4", "Test Description 4", null, false, null, false, null
			}, {
				"manager2", null, "Test Description 5", "https://goo.gl/FbAHdJ", false, null, false, ConstraintViolationException.class
			}, {
				"manager2", " ", "Test Description 6", "https://goo.gl/FbAHdJ", false, null, false, ConstraintViolationException.class
			}, {
				"manager2", "Test Service 7", null, "https://goo.gl/FbAHdJ", false, null, false, ConstraintViolationException.class
			}, {
				"manager1", "Test Service 8", " ", "https://goo.gl/FbAHdJ", false, null, false, ConstraintViolationException.class
			}, {
				"manager1", "<script>alert('Hacked!');</script>", "Test Description 9", "https://goo.gl/FbAHdJ", false, null, false, ConstraintViolationException.class
			}, {
				"manager1", "Test Service 10", "<script>alert('Hacked!');</script>", "https://goo.gl/FbAHdJ", false, null, false, ConstraintViolationException.class
			}, {
				"manager2", "Test Service 11", "Test Description 11", "<script>alert('Hacked!');</script>", false, null, false, ConstraintViolationException.class
			}, {
				"manager2", null, null, null, null, null, true, IllegalArgumentException.class
			}, {
				"manager3", "Test Service 13", "Test Description 13", "https://goo.gl/FbAHdJ", true, null, false, IllegalArgumentException.class
			}
		};

		domain.Service service = null;

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			if ((Boolean) testingData[i][6])
				if (testingData[i][5] != null)
					service = this.serviceService.findOne(this.getEntityId((String) testingData[i][5]));
				else
					service = null;

			this.templateListCreateService((String) testingData[i][0], service, (Boolean) testingData[i][6], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Boolean) testingData[i][4], (Class<?>) testingData[i][7]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	// Test Templates

	protected void templateListCreateService(final String username, final domain.Service service, final Boolean useGiven, final String name, final String description, final String url, final Boolean isCanceled, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;
		final domain.Service serviceToSave;
		Manager manager = null;

		//1. Log in to the system
		this.authenticate(username);

		try {

			//2. List his/her Service (skipped if not a manager)
			final List<domain.Service> myServicesBefore = new ArrayList<domain.Service>();

			if (username != null && username.contains("manager")) {
				manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
				myServicesBefore.addAll(manager.getServices());
			}

			//3. Create a Service

			if (useGiven)
				serviceToSave = service;
			else {

				//Simulates the Actor inputing data
				serviceToSave = this.serviceService.create();
				serviceToSave.setName(name);
				serviceToSave.setDescription(description);
				serviceToSave.setPicture(url);
				serviceToSave.setIsCanceled(isCanceled);
			}

			final domain.Service savedService = this.serviceService.save(serviceToSave);

			//Force the Service to Flush
			this.serviceService.flush();

			Assert.isTrue(!savedService.equals(serviceToSave));

			//4. List his/her Services and check the changes
			final List<domain.Service> myServicesAfter = new ArrayList<domain.Service>(manager.getServices());

			Assert.isTrue(myServicesAfter.containsAll(myServicesBefore));
			Assert.isTrue(myServicesAfter.size() == myServicesBefore.size() + 1);
			Assert.isTrue(myServicesAfter.contains(savedService));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateListEditService(final String username, final domain.Service serviceToSave, final String changesPerformed, final Class<?> expected) {

		//v1.0 Implemented by JA

		Class<?> caught = null;
		Manager manager = null;

		//1. Log in to the system
		this.authenticate(username);

		try {

			//2. List his/her Service (skipped if not a manager)
			final List<domain.Service> myServicesBefore = new ArrayList<domain.Service>();

			//We use a String to Store the current State of the List, as it is a shallow copy and when 
			//modified later (after the save()) its content will also be updated! Representing the List as 
			//a String we can store its current state and compare it later, it won't change!
			String myServicesBeforeState = "";

			if (username != null && username.contains("manager")) {
				manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
				myServicesBefore.addAll(manager.getServices());
			}

			myServicesBeforeState = myServicesBefore.toString();

			//3. Select a Service from the list (given as a parameter)

			//4. Edit the Service (given with the editions)

			final domain.Service editedService = this.serviceService.save(serviceToSave);
			this.serviceService.flush();

			//Check the changes we have made

			if (changesPerformed.contains("name"))
				Assert.isTrue(editedService.getName().equals(serviceToSave.getName()));

			if (changesPerformed.contains("description"))
				Assert.isTrue(editedService.getDescription().equals(serviceToSave.getDescription()));

			if (changesPerformed.contains("picture"))
				Assert.isTrue((editedService.getPicture() == null && serviceToSave.getPicture() == null) || editedService.getPicture().equals(serviceToSave.getPicture()));

			if (changesPerformed.contains("isCanceled"))
				Assert.isTrue(editedService.getIsCanceled() == serviceToSave.getIsCanceled());

			//5. List his/her Services and check the changes
			final List<domain.Service> myServicesAfter = new ArrayList<domain.Service>(manager.getServices());
			final String myServicesAfterState = myServicesAfter.toString();

			Assert.isTrue(!myServicesAfterState.equals(myServicesBeforeState));
			Assert.isTrue(myServicesAfter.size() == myServicesBefore.size());
			Assert.isTrue(myServicesAfter.contains(editedService));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-2-003] Administrador cancela un Servicio.
	// 
	// Requisitos relacionados:
	//   � REQ 2: Managers manage services, for which the system must store a name, a
	//            description, and an optional picture.
	//   � REQ 6.1: An actor who is authenticated as an administrator must be able to
	//              cancel a service that he or she finds inappropriate. Such services
	//              cannot be requested for any rendezvous. They must be flagged
	//              appropriately when listed.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverCancelService() {

		// testingData[i][0] -> username del usuario loggeado.
		// testingData[i][1] -> servicio que va a marcarse como cancelado.
		// testingData[i][2] -> excepci�n que debe saltar.

		final Object testingData[][] = {
			{
				// 1 - (-) Un usuario no loggeado cancela un Servicio.
				null, "service1", IllegalArgumentException.class
			}, {
				// 2 - (-) Un Manager loggeado cancela un Servicio que es suyo.
				"manager3", "service1", IllegalArgumentException.class
			}, {
				// 3 - (+) Un Administrator loggeado cancela un Servicio.
				"admin", "service1", null
			}, {
				// 4 - (-) Un Administrator cancela un Servicio ya marcado como cancelado.
				"admin", "service2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			final Service serviceToCancel = this.serviceService.findOne(super.getEntityId((String) testingData[i][1]));

			if (i < 3)
				serviceToCancel.setIsCanceled(false);
			else
				serviceToCancel.setIsCanceled(true);

			this.startTransaction();

			this.templateCancelService((String) testingData[i][0], serviceToCancel, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}
	}

	protected void templateCancelService(final String username, final Service serviceToCancel, final Class<?> expected) {

		// 1. Loggearse (o como null)
		super.authenticate(username);

		Class<?> caught = null;

		try {

			// 2. Cancelar un Servicio

			this.serviceService.cancel(serviceToCancel);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * v1.0 - josembell
	 * 
	 * [UC-2-007] - List and Delete Services
	 * 
	 * REQ: 2, 4.2, 5.1, 5.2
	 */

	@Test
	public void driverListAndDeleteService() {
		final Object testingData[][] = {
			{
				/* + 1) Un manager elimina un Service creado por �l */
				"manager2", "service4", null
			}, {
				/* - 2) Un usuario no identificado elimina un Service */
				null, "service4", IllegalArgumentException.class
			}, {
				/* - 3) Un manager identificado intenta eliminar un Service null */
				"manager2", null, IllegalArgumentException.class
			}, {
				/* - 4) Un manager identificado intenta eliminar un Service que no le pertenece */
				"manager2", "service2", IllegalArgumentException.class
			}, {
				/* - 5) Un manager identificado intenta eliminar un Service que est� siendo usado */
				"manager3", "service1", IllegalArgumentException.class
			}, {
				/* - 6) Un usuario intenta eliminar un Service */
				"user1", "service1", IllegalArgumentException.class
			}, {
				/* - 7) Un admin intenta eliminar un Service */
				"admin", "service1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			domain.Service service = null;
			if (testingData[i][1] != null)
				service = this.serviceService.findOne(this.getEntityId((String) testingData[i][1]));

			//System.out.println("Test " + (i + 1));
			this.templateListAndDeleteService((String) testingData[i][0], service, (Class<?>) testingData[i][2]);
			//System.out.println("Test " + (i + 1)+" - OK");
		}
	}

	/* v1.0 - josembell */
	protected void templateListAndDeleteService(final String username, final Service service, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Loggearse como Manager */
		this.authenticate(username);

		try {
			/* 2. Listar todos los Services */
			final Collection<Service> allServices = this.serviceService.findAll();
			final int numAllServicesBefore = allServices.size();

			/* 3. Listar mis Services */
			Collection<Service> myServices = new HashSet<Service>();
			int numMyServicesBefore = 0;
			Manager manager = null;
			if (username != null) {
				manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
				if (manager != null) {
					myServices = manager.getServices();
					numMyServicesBefore = myServices.size();
				}
			}

			/* 4. Eliminar un Service -> el que entra por par�metros */
			this.serviceService.delete(service);

			/* 5. Asegurar que el Service ya no lo tiene el manager */
			Assert.isTrue(!manager.getServices().contains(service));
			final int numMyServicesNow = manager.getServices().size();
			Assert.isTrue(numMyServicesBefore == numMyServicesNow + 1);

			/* 6. Asegurar que el Service ya no est� disponible en todos los services */
			//Assert.isTrue(!allServices.contains(service));
			final int numAllServicesNow = this.serviceService.findAll().size();
			Assert.isTrue(numAllServicesBefore == numAllServicesNow + 1);

		} catch (final Throwable oopsIDidItAgain) {
			caught = oopsIDidItAgain.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	/*
	 * v1.0 - josembell
	 * 
	 * [UC-2-013] - Add a Category to a Service
	 */

	@Test
	public void driverAddCategoryToService() {
		final Object testingData[][] = {
			{
				/* + 1) Un manager a�ade una categoria a un service suyo */
				"manager2", "service3", "category1", null
			}, {
				/* - 2) Un usuario no identificado a�ade una categoria a un service */
				null, "service3", "category1", IllegalArgumentException.class
			}, {
				/* - 3) Un manager a�ade una categoria a un service que no es suyo */
				"manager1", "service3", "category1", IllegalArgumentException.class
			}, {
				/* - 4) Un manager a�ade una categoria a un service null */
				"manager2", null, "category1", IllegalArgumentException.class
			}, {
				/* - 5) Un manager a�ade una categoria null a un service */
				"manager2", "service3", null, IllegalArgumentException.class
			}, {
				/* - 6) Un usuario a�ade una categoria a un service */
				"user1", "service3", "category1", IllegalArgumentException.class
			}, {
				/* - 7) Un admin a�ade una categoria a un service */
				"admin", "service3", "category1", IllegalArgumentException.class
			}, {
				/* - 8) Un manager a�ade una categoria ya existente a un service */
				"manager2", "service3", "category2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			domain.Service service = null;
			Category category = null;

			if (testingData[i][1] != null)
				service = this.serviceService.findOne(this.getEntityId((String) testingData[i][1]));
			if (testingData[i][2] != null)
				category = this.categoryService.findOne(this.getEntityId((String) testingData[i][2]));

			//System.out.println("test " + (i + 1));
			this.templateAddCategoryToService((String) testingData[i][0], service, category, (Class<?>) testingData[i][3]);
		}
	}
	/* v1.0 - josembell */
	public void templateAddCategoryToService(final String username, final Service service, final Category category, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Loggearse como manager */
		this.authenticate(username);

		try {
			/* 2. Listar los services */
			Integer numServicesBefore = 0;
			if (username != null) {
				final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
				if (manager != null) {
					final Collection<domain.Service> myServices = manager.getServices();
					numServicesBefore = myServices.size();
				}

			}

			/* 3. Seleccionar un service y a�adirle una categoria -> entra por par�metros */
			this.serviceService.addCategory(service, category);

			/* 4. Listar los services */
			Integer numServicesNow = 0;
			if (username != null) {
				final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
				if (manager != null) {
					final Collection<domain.Service> myServices = manager.getServices();
					numServicesNow = myServices.size();
				}

			}

			Assert.isTrue(numServicesBefore == numServicesNow);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

	/*
	 * v1.0 - josembell
	 * 
	 * [UC-2-014] - Remove a Category from a Service
	 */

	@Test
	public void driverRemoveCategoryFromService() {
		final Object testingData[][] = {
			{
				/* + 1) Un manager quita una categoria de un service suyo */
				"manager2", "service3", "category2", null
			}, {
				/* - 2) Un usuario no identificado quita una categoria a un service */
				null, "service3", "category2", IllegalArgumentException.class
			}, {
				/* - 3) Un manager quita una categoria a un service que no es suyo */
				"manager1", "service5", "category2", IllegalArgumentException.class
			}, {
				/* - 4) Un manager quita una categoria a un service null */
				"manager2", null, "category1", IllegalArgumentException.class
			}, {
				/* - 5) Un manager quita una categoria null a un service */
				"manager2", "service3", null, IllegalArgumentException.class
			}, {
				/* - 6) Un usuario quita una categoria a un service */
				"user1", "service3", "category1", IllegalArgumentException.class
			}, {
				/* - 7) Un admin quita una categoria a un service */
				"admin", "service3", "category1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			domain.Service service = null;
			Category category = null;

			if (testingData[i][1] != null)
				service = this.serviceService.findOne(this.getEntityId((String) testingData[i][1]));
			if (testingData[i][2] != null)
				category = this.categoryService.findOne(this.getEntityId((String) testingData[i][2]));

			//System.out.println("test " + (i + 1));
			this.templateRemoveCategoryFromService((String) testingData[i][0], service, category, (Class<?>) testingData[i][3]);
		}

	}

	/* v1.0 - josembell */
	public void templateRemoveCategoryFromService(final String username, final Service service, final Category category, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Loggearse como manager */
		this.authenticate(username);

		try {
			/* 2. Listar los services */
			Integer numServicesBefore = 0;
			if (username != null) {
				final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
				if (manager != null) {
					final Collection<domain.Service> myServices = manager.getServices();
					numServicesBefore = myServices.size();
				}

			}

			/* 3. Seleccionar un service y a�adirle una categoria -> entra por par�metros */
			this.serviceService.removeCategory(service, category);

			/* 4. Listar los services */
			Integer numServicesNow = 0;
			if (username != null) {
				final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
				if (manager != null) {
					final Collection<domain.Service> myServices = manager.getServices();
					numServicesNow = myServices.size();
				}

			}

			Assert.isTrue(numServicesBefore == numServicesNow);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}

}
