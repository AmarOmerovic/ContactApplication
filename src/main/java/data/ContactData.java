package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class ContactData {

    private static final ContactData instance = new ContactData();
    private static final String fileName = "Contacts.txt";
    private ObservableList<ContactInfo> contacts;




    public static ContactData getInstance(){
        return instance;
    }


    public ObservableList<ContactInfo> getContacts() {
        return contacts;
    }


    public void addContact(ContactInfo contact){
        contacts.add(contact);
    }

    public void editContact(ContactInfo contact, int index){
        contacts.add(index, contact);
    }


    public void loadContacts() throws IOException{
        contacts = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader bufferedReader = Files.newBufferedReader(path);
        String help;

        try{
            while ((help = bufferedReader.readLine()) != null){
                String[] contactPieces = help.split("\t");
                String firstName = contactPieces[0];
                String lastName = contactPieces[1];
                String phoneNumber = contactPieces[2];
                String notes = contactPieces[3];

                ContactInfo contact = new ContactInfo(firstName, lastName, phoneNumber ,notes);
                contacts.add(contact);
            }
        }finally {
            bufferedReader.close();
        }
    }

    public void storeContacts() throws IOException{
        Path path = Paths.get(fileName);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path);

        try{
            Iterator<ContactInfo> iterator = contacts.iterator();
            while (iterator.hasNext()){
                ContactInfo temp = iterator.next();
                bufferedWriter.write(String.format("%s\t%s\t%s\t%s", temp.getFirstName(), temp.getLastName(), temp.getPhoneNumber(), temp.getNotes()));
                bufferedWriter.newLine();
            }
        }finally {
            bufferedWriter.close();
        }
    }

    public boolean removeContact(ContactInfo contact){
        return contacts.remove(contact);
    }
}
