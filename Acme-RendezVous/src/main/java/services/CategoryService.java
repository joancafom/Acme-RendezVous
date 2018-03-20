
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CategoryRepository;
import domain.Category;

@Service
@Transactional
public class CategoryService {

	/* Repository */
	@Autowired
	private CategoryRepository	categoryRepository;


	/* Services */

	/* CRUD */

	/* v1.0 - josembell */
	public Category create(final Category parentCategory) {
		final Category category = new Category();

		category.setParentCategory(parentCategory);
		category.setChildCategories(new HashSet<Category>());
		category.setServices(new HashSet<domain.Service>());

		return category;
	}

	/* v1.0 - josembell */
	public Category findOne(final int categoryId) {
		return this.categoryRepository.findOne(categoryId);
	}

	/* v1.0 - josembell */
	public Collection<Category> findAll() {
		return this.categoryRepository.findAll();
	}

	/* v1.0 - josembell */
	public Category save(final Category category) {
		Collection<Category> sameLevelCategories = new HashSet<Category>();
		if (category.getParentCategory() != null)
			sameLevelCategories = category.getParentCategory().getChildCategories();
		else
			sameLevelCategories = this.findFirstLevelCategories();

		for (final Category c : sameLevelCategories)
			Assert.isTrue(!category.getName().equals(c.getName()));

		final Category saved = this.categoryRepository.save(category);
		if (category.getId() == 0 && category.getParentCategory() != null)
			category.getParentCategory().getChildCategories().add(saved);

		return saved;
	}

	/* v1.0 - josembell */
	public void delete(final Category category) {
		/* TODO: Implementar recursión para eliminar los childCategories */
		this.categoryRepository.delete(category);
	}

	/* v1.0 - josembell */
	public void flush() {
		this.categoryRepository.flush();
	}

	/* Other business methods */

	/* v1.0 - josembell */
	private Collection<Category> findFirstLevelCategories() {
		return this.categoryRepository.findFirstLevelCategories();
	}

}
