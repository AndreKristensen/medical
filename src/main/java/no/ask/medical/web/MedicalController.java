package no.ask.medical.web;

import no.ask.medical.service.MedicalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MedicalController {

	@Autowired
	private MedicalService service;

	@RequestMapping("/")
	public String getIndex() {
		try{
			
		System.out.println(service.readAllPatients("jasd", 2));
		}catch (Exception e){
			
		}
		return "index";
	}

}
