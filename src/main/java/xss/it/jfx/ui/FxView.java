/*
 * Copyright © 2024. XTREME SOFTWARE SOLUTIONS
 *
 * All rights reserved. Unauthorized use, reproduction, or distribution
 * of this software or any portion of it is strictly prohibited and may
 * result in severe civil and criminal penalties. This code is the sole
 * proprietary of XTREME SOFTWARE SOLUTIONS.
 *
 * Commercialization, redistribution, and use without explicit permission
 * from XTREME SOFTWARE SOLUTIONS, are expressly forbidden.
 */

package xss.it.jfx.ui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.security.crypto.password.PasswordEncoder;
import xss.it.FxApplication;
import xss.it.Launcher;
import xss.it.backend.entity.User;
import xss.it.backend.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author XDSSWAR
 * Created on 06/03/2024
 */
public class FxView implements Initializable {
    @FXML
    private PasswordField passTxt;

    @FXML
    private Button printBtn;

    @FXML
    private Button registerBtn;

    @FXML
    private TextField userTxt;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public FxView(Stage stage) throws IOException {
        //Find the Bean we want by its class
        userService = FxApplication.findBean(UserService.class).orElse(null);
        passwordEncoder= FxApplication.findBean(PasswordEncoder.class).orElse(null);
        Parent parent = load("/xss/it/view.fxml", this);
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.getIcons().add(
                new Image(load("/xss/it/icon.png").toExternalForm())
        );
        stage.setTitle("Javafx Spring Boot Demo");
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerBtn.setOnAction(event -> register());
        printBtn.setOnAction(event -> printUsers());
    }


    private void register(){
        Task<User> task = new Task<>() {
            @Override
            protected User call() throws Exception {
                String user = userTxt.getText();
                String pass = passTxt.getText();
                if (check(user) && check(pass)) {
                    Optional<User> existOp = userService.findByUsername(user);
                    if (existOp.isEmpty()) {
                        User u = new User();
                        u.setUsername(user);
                        u.setPassword(passwordEncoder.encode(pass));
                        u.setAvatar(loadStream("/xss/it/icon.png").readAllBytes());
                        Optional<User> op = userService.save(u);
                        return op.orElse(null);
                    }
                    else {
                        System.out.println("User is already Registered!");
                    }
                }
                else {
                    System.out.println("Username and Password Required!");
                }
                return null;
            }
        };

        task.valueProperty().addListener((obs, o, user) -> {
            Platform.runLater(()->{
                //Update gui here
                userTxt.clear();
                passTxt.clear();
            });

            System.out.println(user);
        });

        FxApplication.EXECUTOR.submit(task);
    }

    private void printUsers(){
        Task<List<User>> task = new Task<>() {
            @Override
            protected List<User> call() {
                return userService.findAll();
            }
        };

        task.valueProperty().addListener((obs, o, list) -> {
            //Update UI
            Platform.runLater(()->{

            });
            for (User user : list) {
                System.out.println(user);
            }
        });

        FxApplication.EXECUTOR.submit(task);
    }


    private boolean check(String txt){
        return txt != null && !txt.isBlank() && !txt.isEmpty();
    }


    /**
     * Loads and returns the root element of a JavaFX scene graph from the specified FXML file.
     *
     * @param location   The location of the FXML file.
     * @param controller The controller object to associate with the FXML file. Can be null.
     * @return The root element of the loaded JavaFX scene graph.
     */
    public static Parent load(final String location, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(load(location));
        if (controller!=null){
            loader.setController(controller);
        }
        return loader.load();
    }

    /**
     * Loads a resource file from the specified location.
     *
     * @param location The location of the resource file.
     * @return The URL of the loaded resource.
     */
    public static URL load(final String location){
        return Launcher.class.getResource(location);
    }

    /**
     * Loads and returns an input stream for the resource file from the specified location.
     *
     * @param location The location of the resource file.
     * @return The input stream of the loaded resource.
     */
    public static InputStream loadStream(final String location){
        return Launcher.class.getResourceAsStream(location);
    }
}
