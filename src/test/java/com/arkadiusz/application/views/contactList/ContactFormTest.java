package com.arkadiusz.application.views.contactList;

import com.arkadiusz.application.data.entity.Company;
import com.arkadiusz.application.data.entity.Contact;
import com.arkadiusz.application.data.entity.WorkScope;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ContactFormTest {
    private List<Company> companies;
    private List<WorkScope> workScopes;
    private Contact testContact;
    private Company company1;
    private Company company2;
    private WorkScope workScope1;
    private WorkScope workScope2;

    @Before 
    public void setupData() {
        companies = new ArrayList<>();
        company1 = new Company();
        company1.setName("PAI");
        company2 = new Company();
        company2.setName("Test");
        companies.add(company1);
        companies.add(company2);

        workScopes = new ArrayList<>();
        workScope1 = new WorkScope();
        workScope1.setName("Projekt");
        workScope2 = new WorkScope();
        workScope2.setName("Elektryka");
        workScopes.add(workScope1);
        workScopes.add(workScope2);

        testContact = new Contact();
        testContact.setFirstName("Jan");
        testContact.setLastName("Kowalski");
        testContact.setPhone("123 456 789");
        testContact.setWorkScope(workScope1);
        testContact.setCompany(company2);
    }

    @Test
    public void formFieldsPopulated(){
        ContactForm form = new ContactForm(companies, workScopes);
        form.setContact(testContact);

        Assert.assertEquals("Jan", form.firstName.getValue());
        Assert.assertEquals("Kowalski", form.lastName.getValue());
        Assert.assertEquals("123 456 789", form.phone.getValue());
        Assert.assertEquals(company2, form.company.getValue());
        Assert.assertEquals(workScope1, form.workScope.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {

        ContactForm form = new ContactForm(companies, workScopes);
        Contact contact = new Contact();
        form.setContact(contact);
        form.firstName.setValue("Adam");
        form.lastName.setValue("Nowak");
        form.company.setValue(company1);
        form.phone.setValue("505 174 656");
        form.workScope.setValue(workScope2);

        AtomicReference<Contact> savedContactRef = new AtomicReference<>(null);
        form.addListener(ContactForm.SaveEvent.class, e -> {
            savedContactRef.set(e.getContact());
        });
        form.saveButton.click();
        Contact savedContact = savedContactRef.get();

        //    a czy d

        Assert.assertEquals("Adam", savedContact.getFirstName());
        Assert.assertEquals("Nowak", savedContact.getLastName());
        Assert.assertEquals("505 174 656", savedContact.getPhone());
        Assert.assertEquals(company1, savedContact.getCompany());
        Assert.assertEquals(workScope2, savedContact.getWorkScope());
    }
}