
package forms;

import java.util.List;

public class RSVPForm {

	private int				rendezVous;
	private List<String>	answers;


	public List<String> getAnswers() {
		return this.answers;
	}

	public void setAnswers(final List<String> answers) {
		this.answers = answers;
	}

	public int getRendezVous() {
		return this.rendezVous;
	}

	public void setRendezVous(final int rendezVous) {
		this.rendezVous = rendezVous;
	}

}
