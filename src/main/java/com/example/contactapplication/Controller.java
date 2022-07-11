package com.example.contactapplication;

import data.ContactData;
import data.ContactInfo;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    public TableView<ContactInfo> contacts;
    @FXML
    public TableColumn<ContactInfo, String> firstNameColumn;
    @FXML
    public TableColumn<ContactInfo, String> lastNameColumn;
    @FXML
    public TableColumn<ContactInfo, String> phoneNumberColumn;
    @FXML
    public TableColumn<ContactInfo, String> notesColumn;
    @FXML
    public BorderPane mainBorderPane;
    @FXML
    public ContextMenu contextMenu;
    @FXML
    public MenuItem newContactMenuItem;
    @FXML
    public MenuItem editContactMenuItem;
    @FXML
    public MenuItem deleteContactMenuItem;
    @FXML
    public MenuItem closeProgramMenuItem;
    @FXML
    public ChoiceBox<String> choiceBox;
    @FXML
    public TextField searchField;
    private FilteredList<ContactInfo> filteredList;


    public void initialize() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));



        contextMenu = new ContextMenu();

        newContactMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showNewContactDialog();
            }
        });

        editContactMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showEditContactDialog();
            }
        });

        deleteContactMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteContact();
            }
        });

        closeProgramMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to exit?\nPress OK to confirm, or cancel to Back out.");
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    Platform.exit();
                }
            }
        });
        contextMenu.getItems().addAll(newContactMenuItem,editContactMenuItem,deleteContactMenuItem,closeProgramMenuItem);




        choiceBox.getItems().addAll("First Name", "Last Name");
        choiceBox.setValue("First Name");

        filteredList = new FilteredList<>(ContactData.getInstance().getContacts(), p -> true);//Pass the data to a filtered list
        contacts.setItems(filteredList);
        if (ContactData.getInstance().getContacts().size() > 0){
            contacts.getSelectionModel().selectFirst();
        }


        if (filteredList.isEmpty()){
            searchField.setDisable(true);
        }else {
            searchField.setDisable(false);
        }



        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            switch (choiceBox.getValue())//Switch on choiceBox value
            {
                case "First Name":
                    filteredList.setPredicate(p -> p.getFirstName().toLowerCase().contains(newValue.toLowerCase().trim()));//filter table by first name
                    break;
                case "Last Name":
                    filteredList.setPredicate(p -> p.getLastName().toLowerCase().contains(newValue.toLowerCase().trim()));//filter table by last name
                    break;
            }
        });

        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal)
                -> {
            if (newVal != null) {
                searchField.setText("");
            }
        });


    }


    public void showNewContactDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("customPopUp.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.setTitle("New Item");
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            ContactInfo newItem = controller.addNewContact();
            if (newItem == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Make sure you have entered all needed information's below!");
                Optional<ButtonType> error = alert.showAndWait();
                if ((error.isPresent()) && (error.get() == ButtonType.OK)) {
                    contacts.getSelectionModel().selectFirst();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Contact created successfully!");
                alert.showAndWait();
                contacts.getSelectionModel().select(newItem);
                searchField.setDisable(false);
            }
        }

    }



    public void showEditContactDialog() {
        ContactInfo contact = contacts.getSelectionModel().getSelectedItem();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("customPopUp.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
            dialog.setTitle("Edit Item");
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        try {
            Alert remainder = new Alert(Alert.AlertType.INFORMATION);
            remainder.setHeaderText("Contact: " + contact.getFirstName() + " | " + contact.getLastName() + " | " + contact.getPhoneNumber() + " | " + contact.getNotes());
            remainder.setContentText(null);
            remainder.setTitle("Edit Contact");
            remainder.getButtonTypes().add(ButtonType.CANCEL);
            Optional<ButtonType> remainderResult = remainder.showAndWait();
            if (remainderResult.isPresent() && remainderResult.get() == ButtonType.OK) {
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    DialogController controller = fxmlLoader.getController();
                    ContactInfo editedContact = controller.editContact(contact);
                    if (editedContact == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Make sure you have entered all needed information's below!");
                        Optional<ButtonType> error = alert.showAndWait();
                        if ((error.isPresent()) && (error.get() == ButtonType.OK)) {
                            contacts.getSelectionModel().selectFirst();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Contact edited successfully!");
                        alert.showAndWait();
                        contacts.getSelectionModel().select(editedContact);
                    }
                }
            }
        }catch (NullPointerException ignore){

        }
    }

    public void deleteContact(){
        ContactInfo contact = contacts.getSelectionModel().getSelectedItem();
        try{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete item");
            alert.setHeaderText("Delete item: " + contact.getFirstName() + " | " + contact.getLastName() + " | " + contact.getPhoneNumber() + " | " + contact.getNotes());
            alert.setContentText("Are you sure?\nPress OK to confirm, or cancel to Back out.");
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                if (ContactData.getInstance().getContacts().contains(contact)){
                    if (ContactData.getInstance().removeContact(contact)){
                        Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                        confirmation.setHeaderText("Contact deleted successfully!");
                        confirmation.showAndWait();
                        contacts.getSelectionModel().selectFirst();
                        if (ContactData.getInstance().getContacts().size() == 0){
                            searchField.setDisable(true);
                        }
                    }
                }
            }
        }catch (NullPointerException ignore){

        }
    }
}
