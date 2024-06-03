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

import lombok.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import xss.it.jfx.ui.FxView;

import java.io.IOException;

/**
 * @author XDSSWAR
 * Created on 04/18/2024
 */
@Component
public class StartUpListener implements ApplicationListener<StartUpEvent> {
    /**
     * Handles the StartUpEvent by creating and displaying the login window.
     *
     * @param event The StartUpEvent instance.
     */
    @Override
    public void onApplicationEvent(@NonNull StartUpEvent event) {
        try {
            new FxView(event.getStage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
