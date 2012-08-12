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
package census.presentation.dialogs;

import census.business.SubscriptionsService;
import census.business.api.BusinessException;
import census.business.api.ValidationException;
import census.business.api.SecurityException;
import census.business.dto.SubscriptionDTO;
import census.presentation.CensusFrame;
import census.presentation.util.SubscriptionsTableModel;
import census.presentation.util.SubscriptionsTableModel.Column;
import java.beans.Beans;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

/**
 *
 * @author Danylo Vashchilenko
 */
public class ManageSubscriptionsDialog extends CensusDialog {
    private ResourceBundle bundle = ResourceBundle.getBundle("census/presentation/resources/Strings");

    private Boolean isNew;
    private SubscriptionsTableModel subscriptionsTableModel;
    private List<SubscriptionDTO> subscriptions;

    /**
     * Creates new form ManageSubscriptionsDialog
     */
    public ManageSubscriptionsDialog(JFrame parent) {
        super(parent, true);
        subscriptions = SubscriptionsService.getInstance().getAllSubscriptions();
        
        initComponents();


        if (!Beans.isDesignTime()) {

            /*
             * Listens to the table to know when to enable the Edit button
             */
            subscriptionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (subscriptionCancelButton.isEnabled()) {
                        return;
                    }
                    // There has to be exactly one selected item for editing to take place
                    if (subscriptionsTable.getSelectedRowCount() == 1) {
                        editButton.setEnabled(true);
                    } else {
                        editButton.setEnabled(false);
                    }
                }
            });

            subscriptionApplyButton.setEnabled(false);
            subscriptionCancelButton.setEnabled(false);
        }

        setLocationRelativeTo(parent);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        subscriptionsTableScollPane = new javax.swing.JScrollPane();
        subscriptionsTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        editingPanel = new javax.swing.JPanel();
        subscriptionPanel = new census.presentation.blocks.SubscriptionPanel();
        subscriptionApplyButton = new javax.swing.JButton();
        subscriptionCancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("Title.ManageSubscriptions")); // NOI18N
        setResizable(false);

        Column[] columns = new Column[]{Column.TITLE, Column.PRICE, Column.UNITS, Column.TIME_RANGE, Column.TERM_DAYS, Column.TERM_MONTHS, Column.TERM_YEARS};
        subscriptionsTableModel = new SubscriptionsTableModel(columns);
        subscriptionsTableModel.setSubscriptions(subscriptions);
        subscriptionsTable.setModel(subscriptionsTableModel);
        subscriptionsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        int[] widths = new int[]{200,50,50,137,26,26,26};
        TableColumn column = null;
        for (int i = 0; i < columns.length; i++) {
            column = subscriptionsTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
        subscriptionsTableScollPane.setViewportView(subscriptionsTable);

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/census/presentation/resources/plus32.png"))); // NOI18N
        addButton.setText(bundle.getString("Button.Add")); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(bundle.getString("Button.Cancel")); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/census/presentation/resources/edit32.png"))); // NOI18N
        editButton.setText(bundle.getString("Button.Edit")); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/census/presentation/resources/remove32.png"))); // NOI18N
        removeButton.setText(bundle.getString("Button.Remove")); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        getRootPane().setDefaultButton(okButton);
        okButton.setText(bundle.getString("Button.Ok")); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        editingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Editing"));

        subscriptionPanel.setSubscription(null);

        subscriptionApplyButton.setText(bundle.getString("Button.Apply")); // NOI18N
        subscriptionApplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subscriptionApplyButtonActionPerformed(evt);
            }
        });

        subscriptionCancelButton.setText(bundle.getString("Button.Cancel")); // NOI18N
        subscriptionCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subscriptionCancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editingPanelLayout = new javax.swing.GroupLayout(editingPanel);
        editingPanel.setLayout(editingPanelLayout);
        editingPanelLayout.setHorizontalGroup(
            editingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editingPanelLayout.createSequentialGroup()
                .addComponent(subscriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editingPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(subscriptionApplyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subscriptionCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        editingPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {subscriptionApplyButton, subscriptionCancelButton});

        editingPanelLayout.setVerticalGroup(
            editingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, editingPanelLayout.createSequentialGroup()
                .addComponent(subscriptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subscriptionApplyButton)
                    .addComponent(subscriptionCancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subscriptionsTableScollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(editButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(39, 39, 39))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(okButton)
                            .addComponent(cancelButton)))
                    .addComponent(subscriptionsTableScollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        isNew = true;
        subscriptionPanel.setSubscription(new SubscriptionDTO());

        subscriptionsTable.getSelectionModel().clearSelection();

        subscriptionApplyButton.setEnabled(true);
        subscriptionCancelButton.setEnabled(true);
        cancelButton.setEnabled(false);
        okButton.setEnabled(false);
        addButton.setEnabled(false);
        removeButton.setEnabled(false);
        editButton.setEnabled(false);
    }//GEN-LAST:event_addButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, bundle.getString("Message.AreYouSureYouWantToCancelChanges"), bundle.getString("Title.Confirmation"), JOptionPane.YES_NO_OPTION)) {
            return;
        }

        setResult(RESULT_CANCEL);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        isNew = false;
        subscriptionPanel.setSubscription(subscriptions.get(subscriptionsTable.getSelectedRow()));

        subscriptionApplyButton.setEnabled(true);
        subscriptionCancelButton.setEnabled(true);
        cancelButton.setEnabled(false);
        okButton.setEnabled(false);
        addButton.setEnabled(false);
        removeButton.setEnabled(false);
        editButton.setEnabled(false);
    }//GEN-LAST:event_editButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, bundle.getString("Message.AreYouSureYouWantToCancelChanges"), bundle.getString("Title.Confirmation"), JOptionPane.YES_NO_OPTION)) {
            return;
        }

        SubscriptionsService subscriptionsService = SubscriptionsService.getInstance();

        for (int index : subscriptionsTable.getSelectedRows()) {
            try {
                try {
                    subscriptionsService.removeSubscription(subscriptions.get(index).getId());
                } catch (ValidationException ex) {
                    throw new RuntimeException(ex);
                } catch(BusinessException ex) {
                    CensusFrame.getGlobalCensusExceptionListenersStack().peek().processException(ex);
                } catch(SecurityException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (RuntimeException ex) {
                /*
                 * The exception is unexpected. We got to shutdown the dialog
                 * for the state of the transaction is now unknown.
                 */
                setResult(EditOrderDialog.RESULT_EXCEPTION);
                setException(ex);
                dispose();
                return;
            }
        }

        subscriptions = subscriptionsService.getAllSubscriptions();
        subscriptionsTableModel.setSubscriptions(subscriptions);
    }//GEN-LAST:event_removeButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setResult(RESULT_OK);
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void subscriptionApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subscriptionApplyButtonActionPerformed
        if(!subscriptionPanel.isFormValid()) {
            return;
        }
        
        try {
            if (isNew) {
                SubscriptionsService.getInstance().addSubscription(subscriptionPanel.getSubscription());
            } else {
                SubscriptionsService.getInstance().updateSubscription(subscriptionPanel.getSubscription());
            }
        } catch(SecurityException ex) {
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
            setResult(EditOrderDialog.RESULT_EXCEPTION);
            setException(ex);
            dispose();
            return;
        }

        subscriptionPanel.setSubscription(null);
        subscriptions = SubscriptionsService.getInstance().getAllSubscriptions();
        subscriptionsTableModel.setSubscriptions(subscriptions);

        subscriptionApplyButton.setEnabled(false);
        subscriptionCancelButton.setEnabled(false);
        cancelButton.setEnabled(true);
        okButton.setEnabled(true);
        addButton.setEnabled(true);
        removeButton.setEnabled(true);
        editButton.setEnabled(true);
    }//GEN-LAST:event_subscriptionApplyButtonActionPerformed

    private void subscriptionCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subscriptionCancelButtonActionPerformed
        subscriptionPanel.setSubscription(null);

        subscriptionApplyButton.setEnabled(false);
        subscriptionCancelButton.setEnabled(false);
        cancelButton.setEnabled(true);
        okButton.setEnabled(true);
        addButton.setEnabled(true);
        removeButton.setEnabled(true);
        editButton.setEnabled(true);
    }//GEN-LAST:event_subscriptionCancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel editingPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton subscriptionApplyButton;
    private javax.swing.JButton subscriptionCancelButton;
    private census.presentation.blocks.SubscriptionPanel subscriptionPanel;
    private javax.swing.JTable subscriptionsTable;
    private javax.swing.JScrollPane subscriptionsTableScollPane;
    // End of variables declaration//GEN-END:variables
}
