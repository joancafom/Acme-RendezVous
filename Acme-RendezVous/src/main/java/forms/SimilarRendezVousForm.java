
package forms;

import javax.validation.constraints.NotNull;

public class SimilarRendezVousForm {

	private int	rendezVous;
	private int	id;


	@NotNull
	public int getRendezVous() {
		return this.rendezVous;
	}

	public void setRendezVous(final int rendezVous) {
		this.rendezVous = rendezVous;
	}

	@NotNull
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
