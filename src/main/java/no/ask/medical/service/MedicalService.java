package no.ask.medical.service;

import javax.transaction.Transactional;

import no.ask.medical.domain.Patient;
import no.ask.medical.domain.repository.PatientsRepository;
import no.ask.medical.security.annotations.PEP;
import no.ask.medical.security.annotations.PEPConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicalService {

	@Autowired
	private PatientsRepository repo;

	@PEP(action = PEPConstants.READ)
	public Iterable<Patient> readAllPatients() {
		return repo.findAll();
	}
	
	@PEP(action = PEPConstants.READ)
	public Patient readPatient(Long id) {
		return repo.findOne(id);
	}
	
	@Transactional
	@PEP(action = PEPConstants.CREATE)
	public void addPatient(String firstname, String lastname) {
		repo.save(new Patient(firstname, lastname));
	}

	@Transactional
	@PEP(action = PEPConstants.UPDATE)
	public void updatePatient(Long id, String firstname, String lastname) {
		repo.save(new Patient(id, firstname, lastname));
	}
	
	@Transactional
	@PEP(action = PEPConstants.DELETE)
	public void deletePatient(Long id) {
		repo.delete(id);
	}
}
