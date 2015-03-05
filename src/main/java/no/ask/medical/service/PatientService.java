package no.ask.medical.service;

import javax.transaction.Transactional;

import no.ask.medical.domain.Patient;
import no.ask.medical.domain.repository.DepartmentRepository;
import no.ask.medical.domain.repository.PatientsRepository;
import no.ask.medical.security.annotations.PEP;
import no.ask.medical.security.annotations.PEPActions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientService {

	@Autowired
	private PatientsRepository repo;

	@Autowired
	private DepartmentRepository depRepo;

	@PEP(action = PEPActions.READ)
	public Iterable<Patient> readAllPatients() {
		return repo.findAll();
	}

	@PEP(action = PEPActions.READ)
	public Patient readPatient(Long id) {
		return repo.findOne(id);
	}

	@Transactional
	@PEP(action = PEPActions.CREATE)
	public void createPatient(String firstname, String lastname) {
		repo.save(new Patient(firstname, lastname));
	}

	@Transactional
	@PEP(action = PEPActions.CREATE)
	public void createPatientInLocation(Long locationId, String firstname, String lastname) {
		Patient entity = new Patient(firstname, lastname);
		entity.getDepartments().add(depRepo.findOne(locationId));
		repo.save(entity);
	}

	@Transactional
	@PEP(action = PEPActions.UPDATE)
	public void updatePatient(Long id, String firstname, String lastname) {
		repo.save(new Patient(id, firstname, lastname));
	}

	@Transactional
	@PEP(action = PEPActions.DELETE)
	public void deletePatient(Long id) {
		repo.delete(id);
	}
}
