/*
 * Copyright 2012 Danylo Vashchilenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.key2gym.client;

import org.key2gym.business.api.BusinessException;
import org.key2gym.business.api.ValidationException;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.FocusManager;
import javax.swing.JOptionPane;
import org.key2gym.business.api.SecurityViolationException;
import org.key2gym.client.util.NotificationException;

/**
 *
 * @author Danylo Vashchilenko
 */
public class UserExceptionHandler {
    
    private UserExceptionHandler() {
        strings = ResourceBundle.getBundle("org/key2gym/client/resources/Strings");
    }

    public Component getComponent() {
        return FocusManager.getCurrentManager().getFocusedWindow();
    }
    
    public void processException(Exception ex) {
        Integer messageType;
        if (ex instanceof NotificationException) {
            messageType = JOptionPane.INFORMATION_MESSAGE;
        } else if (ex instanceof ValidationException || ex instanceof SecurityViolationException) {
            messageType = JOptionPane.WARNING_MESSAGE;
        } else if (ex instanceof BusinessException) {
            messageType = JOptionPane.ERROR_MESSAGE;
        } else {
            throw new RuntimeException("Unexpexted exception type.", ex);
        }
        JOptionPane.showMessageDialog(getComponent(), ex.getMessage(), strings.getString("Title.Message"), messageType);
    }
    
    private ResourceBundle strings;
    
    /**
     * Singleton instance.
     */
    private static UserExceptionHandler instance;
    
    /**
     * Returns an instance of this class.
     * 
     * @return an instance of this class 
     */
    public static UserExceptionHandler getInstance() {
        if(instance == null) {
            instance = new UserExceptionHandler();
        }
        
        return instance;
    }
}