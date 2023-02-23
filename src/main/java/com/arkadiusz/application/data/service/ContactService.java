package com.arkadiusz.application.data.service;

import com.arkadiusz.application.data.entity.Company;
import com.arkadiusz.application.data.entity.Contact;
import com.arkadiusz.application.data.entity.WorkScope;
import com.arkadiusz.application.data.repository.CompanyRepository;
import com.arkadiusz.application.data.repository.ContactRepository;
import com.arkadiusz.application.data.repository.WorkScopeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final WorkScopeRepository workScopeRepository;

    public ContactService(ContactRepository contactRepository,
                          CompanyRepository companyRepository,
                          WorkScopeRepository workScopeRepository) {
        this.contactRepository = contactRepository;
        this.companyRepository = companyRepository;
        this.workScopeRepository = workScopeRepository;
    }

    public List<Contact> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return contactRepository.findAll();
        } else {
            return contactRepository.search(stringFilter);
        }
    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(Contact contact) {
        contactRepository.delete(contact);
    }

    public void saveContact(Contact contact) {
        if (contact == null) {
            System.err.println("Contact is null!");
            return;
        }
        contactRepository.save(contact);
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public List<WorkScope> findAllWorkScopes(){
        return workScopeRepository.findAll();
    }
}
