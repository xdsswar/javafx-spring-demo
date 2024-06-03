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

package xss.it.jfx.events;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

/**
 * @author XDSSWAR
 * Created on 04/18/2024
 */
public class StartUpEvent extends ApplicationEvent {

    public StartUpEvent(Stage stage) {
        super(stage);
    }

    public Stage getStage(){
        return (Stage) getSource();
    }
}
