package no.ask.medical.service;

import no.ask.medical.aop.PEP;

import org.springframework.stereotype.Component;

@Component
public class MedicalService {
	
	@PEP(action = "read")
	public void test(String s, int i) {
		System.out.println("test");
		
	}
	
	@PEP(action = "read")
	public String test2(String s, int i) {
		return s;
	}
}
