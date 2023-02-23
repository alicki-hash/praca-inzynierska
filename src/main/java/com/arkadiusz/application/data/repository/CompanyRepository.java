package com.arkadiusz.application.data.repository;

import com.arkadiusz.application.data.entity.Company;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
