package com.example.contactapplication;

import data.ContactData;
import data.ContactInfo;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DialogController {
    @FXML
    public TextField firstNameInput;
    @FXML
    public TextField lastNameInput;
    @FXML
    public TextField phoneNumberInput;
    @FXML
    public TextField notesInput;


    public ContactInfo addNewContact(){
        String firstName = firstNameInput.getText().trim();
        String lastName = lastNameInput.getText().trim();
        String phoneNumber = phoneNumberInput.getText().trim();
        String notes = notesInput.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()|| phoneNumber .isEmpty() || notes.isEmpty()){
            return null;
        }else {
            ContactInfo newContact = new ContactInfo(firstName, lastName, phoneNumber, notes);
            ContactData.getInstance().addContact(newContact);
            return newContact;
        }
    }

    public ContactInfo editContact(ContactInfo contactInfo){

        String firstName = firstNameInput.getText().trim();
        String lastName = lastNameInput.getText().trim();
        String phoneNumber = phoneNumberInput.getText().trim();
        String notes = notesInput.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()|| phoneNumber .isEmpty() || notes.isEmpty()){
            return null;
        }else {
            int help = ContactData.getInstance().getContacts().indexOf(contactInfo);
            ContactData.getInstance().removeContact(contactInfo);
            ContactInfo newContact = new ContactInfo(firstName, lastName, phoneNumber, notes);
            ContactData.getInstance().editContact(newContact, help);
            return newContact;
        }
    }



}
