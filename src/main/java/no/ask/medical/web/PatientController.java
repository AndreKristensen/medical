package no.ask.medical.web;

import java.util.ArrayList;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import no.ask.medical.domain.Patient;
import no.ask.medical.exception.PEPException;
import no.ask.medical.security.filter.PEPFilter;
import no.ask.medical.service.PatientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PatientController {

	private static final Logger log = LoggerFactory.getLogger(PatientController.class);

	@Autowired
	private PatientService service;

	@RequestMapping({ "/patients", "/" })
	public String getIndex(Model model) {
		try {

			ArrayList<Patient> patients = new ArrayList<Patient>();
			Iterable<Patient> readAllPatients = service.readAllPatients();
			for (Patient patient : readAllPatients) {
				patients.add(patient);
			}
			model.addAttribute("patients", patients);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}

	@RequestMapping("/patient/id/{id}")
	public String getPateient(Model model, @PathVariable Long id) {
		ArrayList<Patient> patients = new ArrayList<Patient>();
		patients.add(service.readPatient(id));
		model.addAttribute("patients", patients);
		return "index";
	}

	@RequestMapping("/patient/id/{id}/{firstname}/{lastname}")
	public String getUpdatePateient(Model model, @PathVariable Long id, @PathVariable String firstname, @PathVariable String lastname) {
		service.updatePatient(id, firstname, lastname);
		return "index";
	}

	@RequestMapping("/patient/delete/id/{id}")
	public String getDeletePateient(Model model, @PathVariable Long id) {
		service.deletePatient(id);
		return "index";
	}

	@RequestMapping("/patient/create")
	public String createPatient() {
		service.createPatient("Test", "test");
		return "index";
	}

	@ExceptionHandler(PEPException.class)
	public ModelAndView handleAllException(Exception ex, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.error(auth.getName() + " - " + request.getMethod() + " - " + request.getRequestURI() + " - " + request.getRequestedSessionId());
		ModelAndView model = new ModelAndView("error");
		model.addObject("message", ex.getMessage());

		return model;

	}

}
