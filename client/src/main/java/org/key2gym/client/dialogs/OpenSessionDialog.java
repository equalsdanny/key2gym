/*
 * Copyright 2012-2013 Danylo Vashchilenko
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
package org.key2gym.client.dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.key2gym.business.api.ValidationException;
import org.key2gym.client.ContextManager;
import org.key2gym.client.UserExceptionHandler;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

/**
 *
 * @author Danylo Vashchilenko
 */
public class OpenSessionDialog extends AbstractDialog {

    /**
     * Creates new form OpenSessionDialog
     */
    public OpenSessionDialog(JFrame parent) {
        super(parent, true);
        initComponents();
        buildDialog();
    }   
    
    private void initComponents() {

        usernameLabel = new JLabel(getString("Label.Username"));
        passwordLabel = new JLabel(getString("Label.Password"));
        
        usernameTextField = new JTextField(20);
        passwordPasswordField = new JPasswordField(20);

        okButton = new JButton(getOkAction());
        cancelButton = new JButton(getCancelAction());
        
        Dimension commonSize = cancelButton.getPreferredSize();
        
        cancelButton.setPreferredSize(commonSize);
        okButton.setPreferredSize(commonSize);

        getRootPane().setDefaultButton(okButton);
    }

    private void buildDialog() {
        setLayout(new FormLayout("4dlu, r:p, 3dlu, p:g, 4dlu", "4dlu, p, 3dlu, p, 4dlu, c:p, 4dlu"));
        
        add(usernameLabel,          CC.xy(2, 2));
        add(usernameTextField,      CC.xy(4, 2));
        
        add(passwordLabel,          CC.xy(2, 4));
        add(passwordPasswordField,  CC.xy(4, 4));
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        
        add(buttonsPanel,           CC.xywh(2, 6, 3, 1));
        
        setTitle(getString("Title.OpenSession")); // NOI18N
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
    }
    
    @Override
    protected void onOkActionPerformed(ActionEvent evt) {
        String username = usernameTextField.getText().trim();
        char[] password = passwordPasswordField.getPassword();

        try {
            ContextManager.getInstance().login(username, String.valueOf(password));
        } catch (ValidationException ex) {
            UserExceptionHandler.getInstance().processException(ex);
            return;
        }

        super.onOkActionPerformed(evt);
    }
    
    /*
     * Presentation
     */
    private JButton cancelButton;
    private JButton okButton;
    private JLabel passwordLabel;
    private JPasswordField passwordPasswordField;
    private JLabel usernameLabel;
    private JTextField usernameTextField;
}
