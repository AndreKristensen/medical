package no.ask.medical.web;

import java.util.ArrayList;

import no.ask.medical.domain.Patient;
import no.ask.medical.service.MedicalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MedicalController {

	@Autowired
	private MedicalService service;

	@RequestMapping("/")
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
		try {

			ArrayList<Patient> patients = new ArrayList<Patient>();
			patients.add(service.readPatient(id));
			model.addAttribute("patients", patients);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	

	@RequestMapping("/patient/id/{id}/{firstname}/{lastname}")
	public String getUpdatePateient(Model model, @PathVariable Long id,@PathVariable String firstname,@PathVariable String lastname) {
		try {
			service.updatePatient(id, firstname, lastname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	
	

	@RequestMapping("/patient/delete/id/{id}")
	public String getDeletePateient(Model model, @PathVariable Long id) {
		try {
			service.deletePatient(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}
	

	@RequestMapping("/addPatient")
	public String getAddPatient() {

		// try{
		//
		// service.addPatient("Andre", "Kristensen");
		// service.addPatient("Donal", "Duck");
		// service.addPatient("Mikke", "Mus");
		// service.addPatient("Daniel", "N");
		// service.addPatient("Minni", "Mus");
		// }catch (Exception e){
		// e.printStackTrace();
		// }
		return "index";
	}

}
