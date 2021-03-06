package no.ask.medical.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Patient {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String firstname;

	private String lastname;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Department> departments = new ArrayList<Department>();
	
	public Patient() {
	    // TODO Auto-generated constructor stub
    }
	
	public Patient(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname = lastname;
	}

	
	public Patient(Long id, String firstname, String lastname) {
	    this.id = id;
	    this.firstname = firstname;
		this.lastname = lastname;
    }

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Long getId() {
		return id;
	}

	@Override
    public String toString() {
	    return "Patient [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + "]";
    }
	
	public List<Department> getDepartments() {
	    return departments;
    }
}
