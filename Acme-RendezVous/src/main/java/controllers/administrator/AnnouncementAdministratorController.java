
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AnnouncementService;
import controllers.AbstractController;
import domain.Announcement;

@Controller
@RequestMapping("/announcement/administrator")
public class AnnouncementAdministratorController extends AbstractController {

	/* Services */
	@Autowired
	private AnnouncementService	announcementService;


	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int announcementId) {

		ModelAndView res;
		final Announcement announcement = this.announcementService.findOne(announcementId);

		Assert.notNull(announcement);

		res = new ModelAndView("redirect:/rendezVous/administrator/display.do?rendezVousId=" + announcement.getRendezVous().getId());

		try {
			this.announcementService.delete(announcement);
		} catch (final Throwable oops) {
			res.addObject("message", "announcement.commit.error");
		}

		return res;
	}

}
