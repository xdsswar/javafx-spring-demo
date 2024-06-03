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

package xss.it;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import xss.it.jfx.events.StartUpEvent;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author XDSSWAR
 * Created on 06/03/2024
 */
@SpringBootApplication
public class FxApplication extends Application {

    private static String[] ARGS = null;

    /**
     * Optional containing the Spring application context, initialized as empty.
     */
    private static Optional<ConfigurableApplicationContext> SPRING_CONTEXT = Optional.empty();

    /**
     * ExecutorService for managing asynchronous tasks, initialized with a fixed thread pool of size 2.
     * Each thread in the pool is set as a daemon thread.
     */
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2, r->{
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread;
    });


    /**
     * The entry point of the Java application.
     * This method calls the launch method to start a JavaFX application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method is called after the application has been launched.
     * Override this method to create and set up the primary stage of the application.
     *
     * @param stage The primary stage for this application, onto which
     *              the application scene can be set.
     */
    @Override
    public void start(Stage stage) {
        //Fire event and pass the stage
        fireStartUpEvent(new StartUpEvent(stage));
    }

    /**
     * The initialization method for the application.
     * This method is called immediately after the application class is loaded and
     * constructed. An application can override this method to perform initialization
     * tasks before the application is shown.
     *
     * @throws Exception if an error occurs during initialization.
     */
    @Override
    public void init() throws Exception {
        super.init();
        ARGS = getParameters()
                .getRaw()
                .toArray(new String[0]);
        start();
    }

    /**
     * This method is called when the application should stop, and provides a
     * convenient place to prepare for application exit and destroy resources.
     *
     * @throws Exception if an error occurs during stopping the application.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        shutDown();
    }



    /**
     * Shuts down the application context if it is present.
     */
    public static void shutDown(){
        SPRING_CONTEXT.ifPresent(ConfigurableApplicationContext::close);
    }

    /**
     * Restart Context
     */
    public static void restart() {
        SPRING_CONTEXT.ifPresentOrElse(context->{
            ApplicationArguments args = context.getBean(ApplicationArguments.class);
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    shutDown();
                    SPRING_CONTEXT = Optional.ofNullable(
                            SpringApplication.run(FxApplication.class, args.getSourceArgs())
                    );
                    return null;
                }
            };
            EXECUTOR.submit(task);
        },()->{
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    SPRING_CONTEXT = Optional.ofNullable(
                            SpringApplication.run(FxApplication.class, getArgs())
                    );
                    return null;
                }
            };
            EXECUTOR.submit(task);
        });
    }

    /**
     * Check if Context is running
     * @return boolean
     */
    public static boolean isRunning() {
        return SPRING_CONTEXT.isPresent() && SPRING_CONTEXT.get().isRunning();
    }

    /**
     * Get the Args
     * @return Array
     */
    public static String[] getArgs() {
        return ARGS;
    }

    /**
     *  Find beans
     * @param type Class type
     * @return Optional
     */
    public static  <T> Optional<T> findBean(Class<T> type) {
        return SPRING_CONTEXT.map(context -> context.getBean(type));
    }

    /**
     * Fires a startup event and shows a window if the context is not present.
     * @param event The application event to be fired.
     */
    private void fireStartUpEvent(ApplicationEvent event){
        SPRING_CONTEXT.ifPresentOrElse(context -> context.publishEvent(event), ()->{
            //TODO: Implement some crap here if needed
        });
    }

    /**
     * Starts the Spring application context by running the main application class
     * with the provided command-line arguments.
     */
    private void start(){
        SPRING_CONTEXT = Optional.ofNullable(
                new SpringApplicationBuilder(FxApplication.class).run(getArgs())
        );
    }
}
