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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.key2gym.business.api.BusinessException;
import org.key2gym.business.api.SecurityViolationException;
import org.key2gym.business.api.UserException;
import org.key2gym.business.api.ValidationException;
import org.key2gym.business.api.dtos.ClientDTO;
import org.key2gym.business.api.dtos.KeyDTO;
import org.key2gym.business.api.services.AttendancesService;
import org.key2gym.business.api.services.ClientsService;
import org.key2gym.business.api.services.KeysService;
import org.key2gym.client.ContextManager;
import org.key2gym.client.UserExceptionHandler;
import org.key2gym.client.colors.Palette;
import org.key2gym.client.panels.forms.ClientFormPanel;
import org.key2gym.client.panels.forms.ClientFormPanel.Column;
import org.key2gym.client.renderers.KeyListCellRenderer;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

/**
 *
 * @author Danylo Vashchilenko
 */
public class CheckInDialog extends AbstractDialog {

    /**
     * Creates new CheckInDialog
     */
    public CheckInDialog(JFrame parent) throws BusinessException, SecurityViolationException {
        super(parent, true);
        clientsService = ContextManager.lookup(ClientsService.class);
        
        buildDialog();

        registeredRadioButton.doClick();
    }

    /**
     * Builds the dialog by placing components on it.
     */
    private void buildDialog() throws BusinessException, SecurityViolationException {
        setContentPane(new JPanel());

        setLayout(new FormLayout("4dlu, f:[200, d]:g, 4dlu", "4dlu, p, 3dlu, p, 3dlu, p, 4dlu, p, 4dlu"));

        /*
         * Client panel
         */
        add(createClientPanel(), CC.xy(2, 2));

        /*
         * Keys panel
         */
        add(createKeysPanel(), CC.xy(2, 4));

        /*
         * Additional actions panel
         */
        add(createAdditionalActionsPanel(), CC.xy(2, 6));

        /*
         * Buttons panel
         */
        add(createButtonsPanel(), CC.xy(2, 8));

        /*
         * Smart Input (see issue #22).
         */
        addHotKey(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, KeyEvent.CTRL_DOWN_MASK), new CardFocusAction());

        setTitle(getString("Title.OpenAttendance")); // NOI18N
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
    }

    private JPanel createClientPanel() {
        JPanel panel = new JPanel(new FormLayout("d:g, 3dlu, d", "d, 3dlu, d, 10dlu, d, 10dlu, d, 4dlu"));
        panel.setBorder(BorderFactory.createTitledBorder(getString("Text.ClientDetails"))); // NOI18N
        clientButtonGroup = new ButtonGroup();

        registeredRadioButton = new JRadioButton();
        registeredRadioButton.setText(getString("Text.RegisteredClientWithTheFollowingCard")); // NOI18N
        registeredRadioButton.setActionCommand(ACTION_REGISTERED);
        registeredRadioButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                checkBoxesActionPerformed(evt);
            }
        });
        clientButtonGroup.add(registeredRadioButton);
        panel.add(registeredRadioButton, CC.xy(1, 1));

        cardTextField = new JTextField();
        cardTextField.setColumns(8);
        cardTextField.getDocument().addDocumentListener(new CardDocumentListener());
        panel.add(cardTextField, CC.xy(1, 3));

        findClientAction = new FindClientAction();
        panel.add(new JButton(findClientAction), CC.xy(3, 3));

        clientDetailsPanel = new ClientFormPanel(Arrays.asList(Column.FULL_NAME, Column.MONEY_BALANCE, Column.ATTENDANCES_BALANCE, Column.EXPIRATION_DATE));
        clientDetailsPanel.setBorder(BorderFactory.createTitledBorder(getString("Text.BillingInformation")));
        clientDetailsPanel.setEditable(false);
        panel.add(clientDetailsPanel, CC.xywh(1, 5, 3, 1));

        casualRadionButton = new JRadioButton();
        casualRadionButton.setText(getString("Text.ClientWithNoMembership")); // NOI18N
        casualRadionButton.setActionCommand(ACTION_CASUAL); // NOI18N
        casualRadionButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                checkBoxesActionPerformed(evt);
            }
        });
        clientButtonGroup.add(casualRadionButton);
        panel.add(casualRadionButton, CC.xy(1, 7));

        return panel;
    }

    private JPanel createKeysPanel() throws BusinessException, SecurityViolationException {
        JPanel panel = new JPanel(new FormLayout("l:d, 3dlu, f:d:g", "d"));

        JLabel label = new JLabel(getString("Label.Key")); // NOI18N;
        panel.add(label, CC.xy(1, 1));

        keysComboBox = new JComboBox();
        List keys = ContextManager.lookup(KeysService.class).getKeysAvailable();
        if (keys.isEmpty()) {
            throw new BusinessException(getString("Message.NoKeyIsAvailable"));
        }
        keysComboBox.setRenderer(new KeyListCellRenderer());
        keysComboBox.setModel(new DefaultComboBoxModel(keys.toArray()));
        keysComboBox.setSelectedIndex(0);
        panel.add(keysComboBox, CC.xy(3, 1));

        return panel;
    }

    private JPanel createAdditionalActionsPanel() {
        JPanel panel = new JPanel(new FormLayout("l:d:g", "d"));
        panel.setBorder(BorderFactory.createTitledBorder(getString("Text.AdditionalActions"))); // NOI18N

        openOrderCheckBox = new JCheckBox(getString("CheckBox.OpenOrder")); // NOI18N
        panel.add(openOrderCheckBox, CC.xy(1, 1));

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton okButton = new JButton(getOkAction());
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton(getCancelAction());

        Dimension commonSize = cancelButton.getPreferredSize();
        okButton.setPreferredSize(commonSize);

        panel.add(okButton);
        panel.add(cancelButton);

        return panel;
    }

    /**
     * Used to process document events on cardTextField.
     */
    private class CardDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            processChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            processChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        /**
         * Process the change in card text field. <p> The method tries to find a
         * client with the card provided. If one is found, the card and ID are
         * stored to the session variables. Then, the client details panel is
         * updated. If no client is found, resets the session variables and the
         * client details panel.
         */
        public void processChange() {
            String cardString = cardTextField.getText().trim();
            Integer card = null;

            /*
             * Used to know whether the current state of the field is valid.
             */
            boolean valid = true;

            try {
                card = Integer.parseInt(cardString);
            } catch (NumberFormatException ex) {
                valid = false;
            }

            if (valid) {
                Integer id;
                try {
                    id = clientsService.findByCard(card);
                } catch (SecurityViolationException ex) {
                    UserExceptionHandler.getInstance().processException(ex);
                    return;
                }

                if (id != null) {
                    /*
                     * The field is valid, so it saves the ID.
                     */
                    clientId = id;
                    valid = true;
                } else {
                    valid = false;
                }
            }

            if (!valid) {
                /*
                 * If the card field is invalid, but it was valid, reset the ID
                 * field and the client details panel.
                 */
                if (clientId != null) {
                    clientId = null;
                    clientDetailsPanel.setClient(null);
                }

                if (cardString.isEmpty()) {
                    cardTextField.setBackground(Color.WHITE);
                    cardTextField.setForeground(Color.BLACK);
                } else {
                    cardTextField.setBackground(Palette.ERROR_BACKGROUND);
                    cardTextField.setForeground(Palette.ERROR_FOREGROUND);
                }
            } else {

                try {
                    clientDetailsPanel.setClient(clientsService.getById(clientId));
                } catch (ValidationException ex) {
                    throw new RuntimeException(ex);
                } catch (SecurityViolationException ex) {
                    UserExceptionHandler.getInstance().processException(ex);
                    return;
                }

                cardTextField.setBackground(Palette.OK_BACKGROUND);
                cardTextField.setForeground(Palette.OK_FOREGROUND);
            }
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            /*
             * Updates the dialog according to the session variables. We can not
             * do it constructor for the caller then would not have a chance to
             * set the session variables.
             */
            if (clientId != null) {
                ClientDTO client;
                try {
                    client = clientsService.getById(clientId);
                    cardTextField.setText(client.getCard() == null ? "" : client.getCard().toString());
                    clientDetailsPanel.setClient(client);
                } catch (ValidationException ex) {
                    /*
                     * This is a bug. Normally we should roll back the whole
                     * transaction, but let's not do that, but let the user
                     * choose that client manually.
                     */
                    Logger.getLogger(CheckInDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch(SecurityViolationException ex) {
                    UserExceptionHandler.getInstance().processException(ex);
                }
            }
        }
        super.setVisible(visible);
    }

    /**
     * Used by Smart Input to focus the needed field.
     */
    protected class CardFocusAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            registeredRadioButton.doClick(0);
        }
    }

    /**
     * The action used to find a registered client.
     */
    protected class FindClientAction extends AbstractAction {

        public FindClientAction() {
            putValue(LARGE_ICON_KEY, new ImageIcon(getClass().getResource("/org/key2gym/client/resources/search16.png")));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            PickClientDialog dialog = new PickClientDialog(CheckInDialog.this);
            dialog.setVisible(true);

            if (dialog.getResult().equals(Result.CANCEL)) {
                return;
            }

            clientId = dialog.getClientId();
            ClientDTO client;
            try {
                client = clientsService.getById(clientId);
            } catch (ValidationException ex) {
                /*
                 * This is a bug.
                 */
                throw new RuntimeException(ex);
            } catch (SecurityViolationException ex) {
                return;
            }

            clientDetailsPanel.setClient(client);
            cardTextField.setText(client.getCard().toString());
        }
    }

    /**
     * The action used for Check In button.
     *
     * <p>
     *
     * Performs check in action without closing the dialog.
     */
    @Override
    protected void onOkActionPerformed(ActionEvent e) {

        Integer selectedKeyId;
        AttendancesService attendancesService = ContextManager.lookup(AttendancesService.class);

        try {

            anonymous = casualRadionButton.isSelected();

            /*
             * GUI garantees that there is always a key selected.
             */
            selectedKeyId = ((KeyDTO) keysComboBox.getSelectedItem()).getId();

            if (anonymous) {
                /*
                 * Anonymous attendance
                 */
                try {
                    attendanceId = attendancesService.checkInCasualClient(selectedKeyId);
                } catch (ValidationException ex) {
                    throw new RuntimeException(ex);
                }

            } else {
                /*
                 * The client ID should be known by this time. If not, the user
                 * did not specify the client.
                 */
                if (clientId == null) {
                    throw new ValidationException(getString("Message.SelectClientFirst"));
                }

                /*
                 * TODO: a validation exception can also be thrown when the
                 * key's ID is invalid, which is a bug so should be wrapped into
                 * a runtime exception. However, we can not tell by design.
                 */
                attendanceId = attendancesService.checkInRegisteredClient(clientId, selectedKeyId);
            }

        } catch (UserException ex) {
            UserExceptionHandler.getInstance().processException(ex);
            return;
        }

        /*
         * Finally, sets the rest of the session variables.
         */
        orderRequested = openOrderCheckBox.isSelected();

        super.onOkActionPerformed(e);
    }

    private void checkBoxesActionPerformed(ActionEvent evt) {
        /*
         * The code does some magic to simplify the GUI and make it more
         * intuitive.
         */

        if (registeredRadioButton.isSelected()) {

            cardTextField.setEnabled(true);
            cardTextField.requestFocusInWindow();

            findClientAction.setEnabled(true);
            clientDetailsPanel.setEnabled(true);

            if (openOrderCheckBox.isSelected()) {
                openOrderCheckBox.doClick();
            }

        } else {
            cardTextField.setEnabled(false);
            try {
                cardTextField.getDocument().remove(0, cardTextField.getText().length());
            } catch (BadLocationException ex) {
                /*
                 * This is a bug, but we won't terminate the dialog.
                 */
                Logger.getLogger(CheckInDialog.class.getName()).log(Level.SEVERE, null, ex);
            }

            clientDetailsPanel.setEnabled(false);
            findClientAction.setEnabled(false);

            if (!openOrderCheckBox.isSelected()) {
                openOrderCheckBox.doClick();
            }
        }
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean isAnonymous) {
        this.anonymous = isAnonymous;
    }

    public Boolean isOrderRequested() {
        return orderRequested;
    }

    public void setOrderRequested(Boolean openOrderRequested) {
        this.orderRequested = openOrderRequested;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getClientId() {
        return clientId;
    }
    private static final String ACTION_CASUAL = "casual";
    private static final String ACTION_REGISTERED = "registered";
    
    /*
     * Business
     */
    private ClientsService clientsService;
    /*
     * Session variables
     */
    private Integer attendanceId;
    private Boolean anonymous;
    private Boolean orderRequested;
    private Integer clientId;
    /*
     * Components
     */
    private ButtonGroup clientButtonGroup;
    private JRadioButton registeredRadioButton;
    private JTextField cardTextField;
    private ClientFormPanel clientDetailsPanel;
    private JRadioButton casualRadionButton;
    private JComboBox keysComboBox;
    private JCheckBox openOrderCheckBox;
    /*
     * Misc variables
     */
    private Action findClientAction;
}
