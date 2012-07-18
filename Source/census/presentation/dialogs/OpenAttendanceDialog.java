/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package census.presentation.dialogs;

import census.business.AttendancesService;
import census.business.ClientsService;
import census.business.KeysService;
import census.business.api.BusinessException;
import census.business.api.ValidationException;
import census.business.dto.KeyDTO;
import census.presentation.CensusFrame;
import census.presentation.util.KeyListCellRenderer;
import java.beans.Beans;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;

/**
 *
 * @author daniel
 */
public class OpenAttendanceDialog extends CensusDialog {
    private ResourceBundle bundle = ResourceBundle.getBundle("census/presentation/resources/Strings");

    /**
     * Creates new form OpenAttendanceDialog
     */
    public OpenAttendanceDialog(JFrame parent) throws BusinessException {
        super(parent, true);

        financialActivityDialogRequested = false;
        clientLocked = false;

        /*
         * GUI
         */
        initComponents();
        setLocationRelativeTo(parent);

        if (!Beans.isDesignTime()) {
            List keys = KeysService.getInstance().getKeysAvailable();
            if (keys.isEmpty()) {
                throw new BusinessException(bundle.getString("Message.NoKeyIsAvailable"));
            }

            keysComboBox.setRenderer(new KeyListCellRenderer());
            keysComboBox.setModel(new DefaultComboBoxModel(keys.toArray()));
            keysComboBox.setSelectedIndex(0);

            /*
             * Update the form accordingly to the session
             */
            openFinancialActivityRadioButton.setSelected(isFinancialActivityDialogRequested());
            if (getClientId() != null) {
                idTextField.setText(getClientId().toString());
                clientIdRadioButton.doClick();
                if (isClientLocked()) {
                    clientIdRadioButton.setEnabled(false);
                    clientCardRadioButton.setEnabled(false);
                    anonymousRadioButton.setEnabled(false);
                    idTextField.setEditable(false);
                    cardTextField.setEditable(false);
                }
            } else if (getClientCard() != null) {
                cardTextField.setText(getClientCard().toString());
                clientCardRadioButton.doClick();
                if (isClientLocked()) {
                    clientIdRadioButton.setEnabled(false);
                    clientCardRadioButton.setEnabled(false);
                    anonymousRadioButton.setEnabled(false);
                    idTextField.setEditable(false);
                    cardTextField.setEditable(false);
                }
            } else {
                clientCardRadioButton.doClick();
            }
            //CensusFrame.getGlobalCensusExceptionListenersStack().push(new CensusDialogExceptionListener());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clientButtonGroup = new javax.swing.ButtonGroup();
        keyLabel = new javax.swing.JLabel();
        keysComboBox = new javax.swing.JComboBox();
        clientDetailsPanel = new javax.swing.JPanel();
        cardTextField = new javax.swing.JTextField();
        anonymousRadioButton = new javax.swing.JRadioButton();
        clientCardRadioButton = new javax.swing.JRadioButton();
        clientIdRadioButton = new javax.swing.JRadioButton();
        idTextField = new javax.swing.JTextField();
        additionalActionsPanel = new javax.swing.JPanel();
        openFinancialActivityRadioButton = new javax.swing.JCheckBox();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("Title.OpenAttendance")); // NOI18N
        setName("openAttendance");
        setResizable(false);

        keyLabel.setText(bundle.getString("Label.Key")); // NOI18N

        keysComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        clientDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("Text.ClientDetails"))); // NOI18N

        clientButtonGroup.add(anonymousRadioButton);
        anonymousRadioButton.setText(bundle.getString("Text.ClientWithNoMembership")); // NOI18N
        anonymousRadioButton.setActionCommand("anonymous");
        anonymousRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxesActionPerformed(evt);
            }
        });

        clientButtonGroup.add(clientCardRadioButton);
        clientCardRadioButton.setText(bundle.getString("Text.RegisteredClientWithTheFollowingCard")); // NOI18N
        clientCardRadioButton.setActionCommand("clientCard");
        clientCardRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxesActionPerformed(evt);
            }
        });

        clientButtonGroup.add(clientIdRadioButton);
        clientIdRadioButton.setText(bundle.getString("Text.RegisteredClientWithTheFollowingID")); // NOI18N
        clientIdRadioButton.setActionCommand("clientId");
        clientIdRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout clientDetailsPanelLayout = new javax.swing.GroupLayout(clientDetailsPanel);
        clientDetailsPanel.setLayout(clientDetailsPanelLayout);
        clientDetailsPanelLayout.setHorizontalGroup(
            clientDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(clientDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clientDetailsPanelLayout.createSequentialGroup()
                        .addGroup(clientDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(clientIdRadioButton)
                            .addGroup(clientDetailsPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(clientDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cardTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                    .addGroup(clientDetailsPanelLayout.createSequentialGroup()
                        .addGroup(clientDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(clientCardRadioButton)
                            .addComponent(anonymousRadioButton))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        clientDetailsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cardTextField, idTextField});

        clientDetailsPanelLayout.setVerticalGroup(
            clientDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, clientDetailsPanelLayout.createSequentialGroup()
                .addComponent(clientIdRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(clientCardRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cardTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(anonymousRadioButton)
                .addGap(32, 32, 32))
        );

        additionalActionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("Text.AdditionalActions"))); // NOI18N

        openFinancialActivityRadioButton.setText(bundle.getString("CheckBox.OpenFinancialActivity")); // NOI18N

        javax.swing.GroupLayout additionalActionsPanelLayout = new javax.swing.GroupLayout(additionalActionsPanel);
        additionalActionsPanel.setLayout(additionalActionsPanelLayout);
        additionalActionsPanelLayout.setHorizontalGroup(
            additionalActionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(additionalActionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(openFinancialActivityRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        additionalActionsPanelLayout.setVerticalGroup(
            additionalActionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(additionalActionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(openFinancialActivityRadioButton)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        okButton.setText(bundle.getString("Button.Ok")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(bundle.getString("Button.Cancel")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(additionalActionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(keyLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(keysComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(clientDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clientDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(keysComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(keyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(additionalActionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        Short attendanceId;
        Short selectedKeyId;
        Integer clientCard = null;
        Short clientId = null;
        Boolean isAnonymous;
        AttendancesService attendancesService;

        try {
            attendancesService = AttendancesService.getInstance();

            isAnonymous = anonymousRadioButton.isSelected();

            /*
             * GUI garantees that there is always a key selected.
             */
            selectedKeyId = ((KeyDTO) keysComboBox.getSelectedItem()).getId();

            if (isAnonymous) {
                /*
                 * Anonymous attendance
                 */
                try {
                    attendanceId = attendancesService.openAnonymousAttendance(selectedKeyId);
                } catch (ValidationException ex) {
                    throw new RuntimeException(ex);
                }

            } else {
                /*
                 * Local validation.
                 */
                String cardText = cardTextField.getText().trim();
                String idText = idTextField.getText().trim();

                if (clientCardRadioButton.isSelected()) {
                    try {
                        clientCard = new Integer(cardText);
                    } catch (NumberFormatException ex) {
                        throw new ValidationException(MessageFormat.format(bundle.getString("Message.FieldIsNotFilledInCorrectly.withFieldName"), new Object[]{bundle.getString("Text.Card")}));
                    }
                } else {
                    try {
                        clientId = new Short(idText);
                    } catch (NumberFormatException ex) {
                        throw new ValidationException(MessageFormat.format(bundle.getString("Message.FieldIsNotFilledInCorrectly.withFieldName"), new Object[]{bundle.getString("Text.ID")}));
                    }
                }

                if (clientCard != null) {
                    clientId = ClientsService.getInstance().findByCard(clientCard);
                    if (clientId == null) {
                        throw new ValidationException(bundle.getString("Message.EnsureCardIsValid"));
                    }
                }

                /*
                 * TODO: a validation exception can also be thrown when the
                 * key's ID is invalid, which should be wrapped into a runtime
                 * exception. however, we can not tell by design.
                 */
                attendanceId = attendancesService.openClientAttendance(clientId, selectedKeyId);
            }

        } catch (BusinessException ex) {
            CensusFrame.getGlobalCensusExceptionListenersStack().peek().processException(ex);
            return;
        } catch (ValidationException ex) {
            CensusFrame.getGlobalCensusExceptionListenersStack().peek().processException(ex);
            return;
        } catch (RuntimeException ex) {
            /*
             * The exception is unexpected. We got to shutdown the dialog for
             * the state of the transaction is now unknown.
             */
            setResult(RESULT_EXCEPTION);
            setException(ex);
            dispose();
            return;
        }

        setResult(CensusDialog.RESULT_OK);
        setAttendanceId(attendanceId);
        setAnonymous(isAnonymous);
        if (!isAnonymous) {
            setClientCard(clientCard);
            setClientId(clientId);
        }
        setFinancialActivityDialogRequested(openFinancialActivityRadioButton.isSelected());
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setResult(CensusDialog.RESULT_CANCEL);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void checkBoxesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxesActionPerformed
        /*
         * The code does some magic to simplify the GUI and make it more
         * intuitive.
         */

        if (clientCardRadioButton.isSelected()) {
            cardTextField.setEnabled(true);
            cardTextField.requestFocusInWindow();
        } else {
            cardTextField.setEnabled(false);
            cardTextField.setText("");
        }

        if (clientIdRadioButton.isSelected()) {
            idTextField.setEnabled(true);
            idTextField.requestFocusInWindow();
        } else {
            idTextField.setEnabled(false);
            idTextField.setText("");
        }
    }//GEN-LAST:event_checkBoxesActionPerformed

    @Override
    public void dispose() {
        //CensusFrame.getGlobalCensusExceptionListenersStack().pop();
        super.dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel additionalActionsPanel;
    private javax.swing.JRadioButton anonymousRadioButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField cardTextField;
    private javax.swing.ButtonGroup clientButtonGroup;
    private javax.swing.JRadioButton clientCardRadioButton;
    private javax.swing.JPanel clientDetailsPanel;
    private javax.swing.JRadioButton clientIdRadioButton;
    private javax.swing.JTextField idTextField;
    private javax.swing.JLabel keyLabel;
    private javax.swing.JComboBox keysComboBox;
    private javax.swing.JButton okButton;
    private javax.swing.JCheckBox openFinancialActivityRadioButton;
    // End of variables declaration//GEN-END:variables

    public Short getClientId() {
        return clientId;
    }

    public void setClientId(Short clientId) {
        this.clientId = clientId;
    }

    public Boolean isClientLocked() {
        return clientLocked;
    }

    public void setClientLocked(Boolean isClientLocked) {
        this.clientLocked = isClientLocked;
    }

    public Integer getClientCard() {
        return clientCard;
    }

    public void setClientCard(Integer clientCard) {
        this.clientCard = clientCard;
    }

    public Boolean isFinancialActivityDialogRequested() {
        return financialActivityDialogRequested;
    }

    public void setFinancialActivityDialogRequested(Boolean financialActivityDialogRequested) {
        this.financialActivityDialogRequested = financialActivityDialogRequested;
    }

    public Short getAttendanceId() {
        return attedanceId;
    }

    public void setAttendanceId(Short attedanceId) {
        this.attedanceId = attedanceId;
    }

    public Boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(Boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }
    private Short attedanceId;
    private Boolean isAnonymous;
    private Boolean financialActivityDialogRequested;
    private Integer clientCard;
    private Short clientId;
    private Boolean clientLocked;
}
