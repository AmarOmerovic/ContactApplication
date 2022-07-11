package com.example.contactapplication;

import data.ContactData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 600);
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        stage.setResizable(false);
        stage.setTitle("My contacts");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() throws Exception {
        super.init();
        try{
            ContactData.getInstance().loadContacts();
        }catch (IOException ignore){
            System.out.println(ignore.getMessage());
        }


    }

    @Override
    public void stop() throws Exception {
        super.stop();
        try {
            ContactData.getInstance().storeContacts();
        }catch (IOException ignore){
            System.out.println(ignore.getMessage());
        }
    }
}