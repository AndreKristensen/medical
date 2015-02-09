package no.ask.medical.service;

import no.ask.medical.annotations.PEP;

import org.springframework.stereotype.Component;

@Component
public class MedicalService {
	
	@PEP(actions = {"read"})
	public void test(String s, int i) {
		System.out.println("test");
		
	}
	
	@PEP(actions = {"read","delete"})
	public String test2(String s, int i) {
		return s;
	}
}
