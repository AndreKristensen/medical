package no.ask.medical.domain.repository;

import no.ask.medical.domain.Patient;

import org.springframework.data.repository.CrudRepository;

public interface  PatientsRepository extends CrudRepository<Patient, Long> {

}
