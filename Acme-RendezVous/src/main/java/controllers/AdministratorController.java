/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	// Services ------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


	// Display Dashboard ---------------------------------------

	@RequestMapping("/display-dashboard")
	public ModelAndView dashboard() {
		final ModelAndView res;

		res = new ModelAndView("administrator/display-dashboard");

		res.addObject("avgCreatedRendezVousesPerUser", this.administratorService.getAvgCreatedRendezVousesPerUser());
		res.addObject("stdDeviationCreatedRendezVousesPerUser", this.administratorService.getStdDeviationCreatedRendezVousesPerUser());
		res.addObject("usersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses", this.administratorService.getUsersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses());
		res.addObject("avgUsersPerRendezVous", this.administratorService.getAvgUsersPerRendezVous());
		res.addObject("stdDeviationUsersPerRendezVous", this.administratorService.getStdDeviationUsersPerRendezVous());
		res.addObject("avgRSVPPerUser", this.administratorService.getAvgRSVPPerUser());
		res.addObject("stdDeviationRSVPPerUser", this.administratorService.getStdDeviationRSVPPerUser());
		res.addObject("topTenMoreRSVP", this.administratorService.getTopTenMoreRSVP());
		res.addObject("avgAnnouncementsPerRendezVous", this.administratorService.getAvgAnnouncementsPerRendezVous());
		res.addObject("stdAnnouncementsPerRendezVous", this.administratorService.getStdAnnouncementsPerRendezVous());
		res.addObject("avgQuestionsPerRendezVous", this.administratorService.getAvgQuestionsPerRendezVous());
		res.addObject("stdQuestionsPerRendezVous", this.administratorService.getStdQuestionsPerRendezVous());
		res.addObject("avgRepliesPerComment", this.administratorService.getAvgRepliesPerComment());
		res.addObject("stdRepliesPerComment", this.administratorService.getStdRepliesPerComment());
		res.addObject("avgAnswersPerRendezVous", this.administratorService.getAvgAnswersPerRendezVous());
		res.addObject("stdAnswersPerRendezVous", this.administratorService.getStdAnswersPerRendezVous());
		res.addObject("rendezVousAbove75", this.administratorService.getRendezVousAbove75AvgAnnouncements());
		res.addObject("rendezVousAboveAvgPlus10", this.administratorService.getRendezVousAboveAvgPlus10SimilarRendezVouses());
		res.addObject("bestSellingServices", this.administratorService.bestSellingServices());
		res.addObject("managersMoreServicesThanAverage", this.administratorService.managersMoreServicesThanAverage());
		res.addObject("managersWithMoreServicesCancelled", this.administratorService.managersWithMoreServicesCancelled());

		// RendezVous 2.0 - Level B
		// v1.0 - Implemented by Alicia
		res.addObject("avgCategoriesPerRendezVous", this.administratorService.getAvgCategoriesPerRendezVous());
		res.addObject("avgRatioServicesPerCategory", this.administratorService.getAvgRatioServicesPerCategory());
		res.addObject("avgServicesRequestedPerRendezVous", this.administratorService.getAvgServicesRequestedPerRendezVous());
		res.addObject("minServicesRequestedPerRendezVous", this.administratorService.getMinServicesRequestedPerRendezVous());
		res.addObject("maxServicesRequestedPerRendezVous", this.administratorService.getMaxServicesRequestedPerRendezVous());
		res.addObject("stdServicesRequestedPerRendezVous", this.administratorService.getStdServicesRequestedPerRendezVous());
		res.addObject("topSellingServices", this.administratorService.topSellingServices());

		return res;
	}
}
