package com.smartcampus.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartcampus.dto.CompanyDto;
import com.smartcampus.dto.CompanyEligibilityDto;
import com.smartcampus.dto.EligibleStudentDto;
import com.smartcampus.dto.StudentDto;
import com.smartcampus.dto.StudentUpdateDto;
import com.smartcampus.entity.Company;
import com.smartcampus.entity.Student;
import com.smartcampus.repository.CompanyRepository;
import com.smartcampus.repository.StudentRepository;
import com.smartcampus.service.SmartCampusService;

@Service
public class SmartCampusServiceImpl implements SmartCampusService {

	@Autowired
	private StudentRepository studentrepo;

	@Autowired
	private CompanyRepository companyrepo;

	@Override
	public Student addStudent(StudentDto stddto) {

		Student student = new Student();

		student.setName(stddto.getName());
		student.setBranch(stddto.getBranch());
		student.setCgpa(stddto.getCgpa());
		student.setNo_of_backlog(stddto.getNo_of_backlog());
		student.setEmail(stddto.getEmail());

		List<Company> eligibleCompanies = companyrepo.findEligibleCompanies(
				student.getBranch(),
				student.getCgpa(),
				student.getNo_of_backlog());

		student.setCompanies(eligibleCompanies);

		if (studentrepo.existsByEmail(student.getEmail())) {
			throw new RuntimeException("Email already exists!");
		}

		return studentrepo.save(student);
	}

	@Override
	public Company addCompany(CompanyDto compdto) {

		Company comp = new Company();

		comp.setName(compdto.getName());
		comp.setBacklogcriteria(compdto.getBacklogcriteria());
		comp.setBranchcriteria(compdto.getBranchcriteria());
		comp.setCgpacriteria(compdto.getCgpacriteria());

		Company savedCompany = companyrepo.save(comp);

		List<Student> students = studentrepo.findAll();

		for (Student student : students) {

			List<Company> companies = companyrepo.findEligibleCompanies(
					student.getBranch(),
					student.getCgpa(),
					student.getNo_of_backlog());

			student.setCompanies(companies);
			studentrepo.save(student);
		}

		return savedCompany;
	}

	@Override
	public List<StudentDto> getStudent() {

		List<Student> std = studentrepo.findAll();
		List<StudentDto> lstddto = new ArrayList<>();

		for (Student s : std) {

			StudentDto stddto = new StudentDto();

			stddto.setName(s.getName());
			stddto.setBranch(s.getBranch());
			stddto.setCgpa(s.getCgpa());
			stddto.setNo_of_backlog(s.getNo_of_backlog());
			stddto.setEmail(s.getEmail());

			lstddto.add(stddto);
		}

		return lstddto;
	}

	@Override
	public List<Company> getCompany() {

		List<Company> clist = companyrepo.findAll();

		return clist;
	}

	public String getStudentAndCompanies(int studentId) {

		Student student = studentrepo.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));

		String result = "Student Name: " + student.getName()
				+ "\nStudent Branch: " + student.getBranch()
				+ "\nCompanies: ";

		for (Company company : student.getCompanies()) {
			result += company.getName() + " ";
		}

		return result;
	}

	public String updateStudent(String email, StudentUpdateDto stddto) {

		Student existing = studentrepo.findByEmail(email);

		existing.setName(stddto.getName());
		existing.setBranch(stddto.getBranch());
		existing.setCgpa(stddto.getCgpa());
		existing.setNo_of_backlog(stddto.getNo_of_backlog());

		List<Company> eligibleCompanies = companyrepo.findEligibleCompanies(
				existing.getBranch(),
				existing.getCgpa(),
				existing.getNo_of_backlog());

		existing.setCompanies(eligibleCompanies);

		studentrepo.save(existing);

		return "student updated";
	}

	public String updateCompany(String name, CompanyDto compdto) {

		Company comp = companyrepo.findByName(name);

		comp.setName(compdto.getName());
		comp.setBacklogcriteria(compdto.getBacklogcriteria());
		comp.setBranchcriteria(compdto.getBranchcriteria());
		comp.setCgpacriteria(compdto.getCgpacriteria());

		companyrepo.save(comp);

		String msg = "COMPANY UPDATED!";

		return msg;
	}
	public String deleteCompany(String name) {

		Company comp = companyrepo.findByName(name);

		List<Student> students = studentrepo.findAll();

		for (Student student : students) {

			student.getCompanies().remove(comp);
			studentrepo.save(student);
		}

		companyrepo.delete(comp);

		return "COMPANY DELETED!";
	}

	@Transactional(readOnly = true)
	public List<EligibleStudentDto> getEligibleStudents(String companyName) {

		Company company = companyrepo.findByName(companyName);

		List<Student> students = studentrepo.findAll();

		List<EligibleStudentDto> eligibleStudents = new ArrayList<>();

		for (Student student : students) {

			if (student.getCompanies().contains(company)) {

				EligibleStudentDto dto = new EligibleStudentDto();

				dto.setName(student.getName());
				dto.setBranch(student.getBranch());
				dto.setEmail(student.getEmail());

				eligibleStudents.add(dto);
			}
		}

		return eligibleStudents;
	}

	@Transactional(readOnly = true)
	public CompanyEligibilityDto getCompanyEligibility(String companyName) {

		Company company = companyrepo.findByName(companyName);

		CompanyEligibilityDto dto = new CompanyEligibilityDto();

		dto.setCompanyName(company.getName());

		int total = 0;
		int ce = 0;
		int etc = 0;
		int mech = 0;

		List<Student> students = studentrepo.findAll();

		for (Student student : students) {

			if (student.getCompanies() != null
					&& student.getCompanies().contains(company)) {

				total++;

				if ("CE".equalsIgnoreCase(student.getBranch())) {
					ce++;
				}

				else if ("MECH".equalsIgnoreCase(student.getBranch())) {
					mech++;
				}

				else if ("ETC".equalsIgnoreCase(student.getBranch())) {
					etc++;
				}
			}
		}

		dto.setTotalEligibleStudents(total);
		dto.setCeStudents(ce);
		dto.setMechStudents(mech);
		dto.setEtcStudents(etc);

		return dto;
	}
}
