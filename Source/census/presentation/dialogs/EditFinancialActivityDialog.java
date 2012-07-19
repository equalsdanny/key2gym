/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package census.presentation.dialogs;

import census.business.*;
import census.business.api.BusinessException;
import census.business.api.SecurityException;
import census.business.api.ValidationException;
import census.business.dto.AttendanceDTO;
import census.business.dto.ClientDTO;
import census.business.dto.ItemDTO;
import census.business.dto.OrderDTO;
import census.presentation.CensusFrame;
import census.presentation.util.ItemListCellRenderer;
import census.presentation.util.ItemsTableModel;
import census.presentation.util.ItemsTableModel.Column;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;

/**
 * This dialog allows user to view and edit a financial activity. It implements
 * the following features:
 * 
 * <ul>
 * 
 * <il> Basic information </il>
 * <il> Payment information
 * <il> Purchases
 * 
 * </ul>
 * 
 * Session variables:
 * <ul>
 * 
 * <li> financialActivityId - the ID of financial activity to be shown and edited </li>
 * <li> fullPaymentForced - if true, the dialog won't exit with RESULT_OK, if the 
 * user did not record full payment.

 * </ul>
 * 
 * A transaction is required to be active, and a session to be open, upon the 
 * dialog's creation. 
 * 
 * This dialog supports hot swapping. The session variables can be set and reset 
 * after the <code>setVisible(true)</code> was called.
 * 
 * @author Danylo Vashchilenko
 */
public class EditFinancialActivityDialog extends CensusDialog {

    /**
     * Constructs from a parent frame.
     * 
     * @param parent the frame to use when positioning itself
     */
    public EditFinancialActivityDialog(JFrame parent) {
        super(parent, true);
        finanancialActivitiesService = OrdersService.getInstance();
        clientsService = ClientsService.getInstance();
        attendancesService = AttendancesService.getInstance();
        itemsService = ItemsService.getInstance();

        initComponents();

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

        financialActivity = new OrderDTO();
        itemsTableScrollPane = new javax.swing.JScrollPane();
        itemsTable = new javax.swing.JTable();
        itemsComboBox = new javax.swing.JComboBox();
        addItemButton = new javax.swing.JButton();
        removeItemButton = new javax.swing.JButton();
        basicInformationPanel = new javax.swing.JPanel();
        subjectLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        subjectTextField = new javax.swing.JTextField();
        dateTextField = new javax.swing.JTextField();
        paymentPanel = new javax.swing.JPanel();
        totalLabel = new javax.swing.JLabel();
        totalTextField = new javax.swing.JTextField();
        paidLabel = new javax.swing.JLabel();
        paidTextField = new javax.swing.JTextField();
        paymentLabel = new javax.swing.JLabel();
        paymentTextField = new javax.swing.JTextField();
        dueLabel = new javax.swing.JLabel();
        dueTextField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("Title.FinancialActivity")); // NOI18N
        setResizable(false);

        Column[] itemsTableColumns = new Column[] {
            Column.TITLE,
            Column.PRICE
        };

        itemsTableModel = new ItemsTableModel(itemsTableColumns);
        itemsTable.setModel(itemsTableModel);
        itemsTableScrollPane.setViewportView(itemsTable);

        itemsComboBox.setRenderer(new ItemListCellRenderer());

        addItemButton.setText("+");
        addItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemButtonActionPerformed(evt);
            }
        });

        removeItemButton.setText("-");
        removeItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeItemButtonActionPerformed(evt);
            }
        });

        basicInformationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("Text.BasicInformation"))); // NOI18N

        subjectLabel.setText(bundle.getString("Label.Subject")); // NOI18N

        dateLabel.setText(bundle.getString("Label.Date")); // NOI18N

        subjectTextField.setEditable(false);

        dateTextField.setEditable(false);

        javax.swing.GroupLayout basicInformationPanelLayout = new javax.swing.GroupLayout(basicInformationPanel);
        basicInformationPanel.setLayout(basicInformationPanelLayout);
        basicInformationPanelLayout.setHorizontalGroup(
            basicInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicInformationPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(basicInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(subjectLabel)
                    .addComponent(dateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(subjectTextField))
                .addContainerGap())
        );
        basicInformationPanelLayout.setVerticalGroup(
            basicInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(basicInformationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(basicInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectLabel)
                    .addComponent(subjectTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(basicInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateLabel)
                    .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        paymentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("Text.Payment"))); // NOI18N

        totalLabel.setText(bundle.getString("Label.Total")); // NOI18N

        totalTextField.setEditable(false);
        totalTextField.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        totalTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        paidLabel.setText(bundle.getString("Label.Paid")); // NOI18N

        paidTextField.setEditable(false);
        paidTextField.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        paidTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        paymentLabel.setText(bundle.getString("Label.Payment")); // NOI18N

        paymentTextField.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        paymentTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        paymentTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                paymentTextFieldFocusGained(evt);
            }
        });

        dueLabel.setText(bundle.getString("Label.Due")); // NOI18N

        dueTextField.setEditable(false);
        dueTextField.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        dueTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        javax.swing.GroupLayout paymentPanelLayout = new javax.swing.GroupLayout(paymentPanel);
        paymentPanel.setLayout(paymentPanelLayout);
        paymentPanelLayout.setHorizontalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paymentLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dueLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paidLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(totalLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dueTextField)
                    .addComponent(paymentTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paidTextField)
                    .addComponent(totalTextField))
                .addContainerGap())
        );
        paymentPanelLayout.setVerticalGroup(
            paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalLabel)
                    .addComponent(totalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paidLabel)
                    .addComponent(paidTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentLabel)
                    .addComponent(paymentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(basicInformationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(paymentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(itemsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(removeItemButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(itemsTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(itemsTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(itemsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addItemButton)
                            .addComponent(removeItemButton)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(basicInformationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateGUI(boolean softReset) {
        try {
            financialActivity = finanancialActivitiesService.getById(financialActivityId);
        } catch (ValidationException ex) {
            throw new RuntimeException(ex);
        }

        /*
         * Total
         */
        totalTextField.setText(financialActivity.getTotal().toPlainString());

        /*
         * Paid
         */
        paidTextField.setText(financialActivity.getPayment().toPlainString());

        /*
         * Due
         */
        dueTextField.setForeground(financialActivity.getDue().compareTo(BigDecimal.ZERO) > 0 ? new Color(168, 0, 0) : new Color(98, 179, 0));
        dueTextField.setText(financialActivity.getDue().toPlainString());

        /*
         * Payment
         */
        if(!softReset) {
            paymentTextField.setText("0.00"); //NOI18N
            paymentTextField.requestFocusInWindow();
        }

        /*
         * Items list. It has to be reloaded for some items could have gone out
         * of stock since last update. However, we want to preserve the 
         * selected item for convinience.
         */
        List<ItemDTO> items;
        if(financialActivity.getClientId() == null) {
            items = itemsService.getPureItemsAvailable();
        } else {
            items = itemsService.getItemsAvailable();
        }
        itemsComboBox.setModel(new DefaultComboBoxModel(items.toArray()));
        
        /*
         * Items table
         */
        itemsTableModel.setItems(financialActivity.getItems());

        /*
         * Subject
         */
        String subject;
        if (financialActivity.getClientId() == null) {
            if (financialActivity.getAttendanceId() != null) {
                AttendanceDTO attendance;
                try {
                    attendance = attendancesService.getAttendanceById(financialActivity.getAttendanceId());
                } catch (SecurityException ex) {
                    throw new RuntimeException(ex);
                }
                subject = MessageFormat.format(bundle.getString("Text.Attendance.withIDAndKey"), 
                        new Object[] { 
                            attendance.getId(), 
                            attendance.getKeyTitle()
                        }
                );
            } else {
                subject = bundle.getString("Text.Other");
            }
        } else {
            ClientDTO client;
            try {
                client = clientsService.getById(financialActivity.getClientId());
            } catch (ValidationException ex) {
                throw new RuntimeException(ex);
            }
            subject = MessageFormat.format(bundle.getString("Text.Client.withFullNameAndID"), 
                    new Object[]{
                        client.getFullName(),
                        client.getId()
                    }
            );
        }
        subjectTextField.setText(subject);

        /*
         * Date
         */
        dateTextField.setText(financialActivity.getDate().toString("dd-MM-yyyy")); //NOI18N
    }
    
    /**
     * Processes an Add button click event
     * 
     * @param evt an optional ActionEvent 
     */
    private void addItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemButtonActionPerformed
        ItemDTO selectedItem = (ItemDTO)itemsComboBox.getSelectedItem();

        try {
            if (selectedItem == null) {
                throw new ValidationException(bundle.getString("Message.SelectItemFirst"));
            }
            Short itemId = ((ItemDTO) selectedItem).getId();

            finanancialActivitiesService.addPurchase(financialActivity.getId(), itemId);
        } catch (BusinessException | ValidationException | SecurityException ex) {
            CensusFrame.getGlobalCensusExceptionListenersStack().peek().processException(ex);
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

        // We do a GUI update with soft reset to reload data that might have changed.
        updateGUI(true);
    }//GEN-LAST:event_addItemButtonActionPerformed

    /**
     * Processes a Remove button click.
     * 
     * @param evt an optional ActionEvent 
     */
    private void removeItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeItemButtonActionPerformed
        /*
         * There can be several items selected, all of which need to be removed.
         */
        for (Integer index : itemsTable.getSelectedRows()) {
            
            Short itemId = ((ItemDTO) financialActivity.getItems().get(index)).getId();
            
            try {
                    finanancialActivitiesService.removePurchase(financialActivity.getId(), itemId);
            } catch (BusinessException|SecurityException ex) {
                CensusFrame.getGlobalCensusExceptionListenersStack().peek().processException(ex);
            } catch (ValidationException|RuntimeException ex) {
                /*
                 * The exception is unexpected. We got to shutdown the dialog
                 * for the state of the transaction is now unknown.
                 */
                setResult(RESULT_EXCEPTION);
                setException(new RuntimeException(ex));
                dispose();
                return;
            }
        }

        // Reloads the financial activity and updates GUI
        setFinancialActivityId(financialActivityId);
    }//GEN-LAST:event_removeItemButtonActionPerformed

    /**
     * Processes an OK button click
     * 
     * @param evt an optional event 
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

        try {
            BigDecimal newPayment = new BigDecimal(paymentTextField.getText().trim());

            if (isFullPaymentForced() && financialActivity.getDue().compareTo(newPayment) != 0) {
                throw new BusinessException(bundle.getString("Message.FullPaymenIsForced"));
            }

            finanancialActivitiesService.recordPayment(financialActivity.getId(), newPayment);
        } catch (NumberFormatException ex) {
            CensusFrame.getGlobalCensusExceptionListenersStack().peek().processException(new ValidationException(bundle.getString("Message.NewPaymentFieldContainInvalidValue")));
            return;
        } catch (BusinessException|ValidationException|SecurityException ex) {
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

        setResult(RESULT_OK);
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setResult(RESULT_CANCEL);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void paymentTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_paymentTextFieldFocusGained
        paymentTextField.setSelectionStart(0);
        paymentTextField.setSelectionEnd(paymentTextField.getDocument().getLength());
    }//GEN-LAST:event_paymentTextFieldFocusGained
    
    /**
     * Sets the financial activity's ID. This method causes all components to
     * be reloaded in order to correspond with the new financial activity.
     * 
     * @param financialActivityId the 
     * @see EditFinancialActivityDialog for details about hot swapping 
     */
    public void setFinancialActivityId(Short financialActivityId) {
        this.financialActivityId = financialActivityId;
        
        updateGUI(false);
    }
    
    public Boolean isFullPaymentForced() {
        return fullPaymentForced;
    }

    public void setFullPaymentForced(Boolean fullPaymentForced) {
        this.fullPaymentForced = fullPaymentForced;
    }

    public Short getFinancialActivityId() {
        return financialActivityId;
    }
    
    /*
     * Presentation
     */
    private ItemsTableModel itemsTableModel;
    private Short financialActivityId;
    private Boolean fullPaymentForced;
    private ResourceBundle bundle = ResourceBundle.getBundle("census/presentation/resources/Strings");
    
    /*
     * Business
     */
    private OrdersService finanancialActivitiesService;
    private ClientsService clientsService;
    private AttendancesService attendancesService;
    private ItemsService itemsService;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItemButton;
    private javax.swing.JPanel basicInformationPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JTextField dateTextField;
    private javax.swing.JLabel dueLabel;
    private javax.swing.JTextField dueTextField;
    private OrderDTO financialActivity;
    private javax.swing.JComboBox itemsComboBox;
    private javax.swing.JTable itemsTable;
    private javax.swing.JScrollPane itemsTableScrollPane;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel paidLabel;
    private javax.swing.JTextField paidTextField;
    private javax.swing.JLabel paymentLabel;
    private javax.swing.JPanel paymentPanel;
    private javax.swing.JTextField paymentTextField;
    private javax.swing.JButton removeItemButton;
    private javax.swing.JLabel subjectLabel;
    private javax.swing.JTextField subjectTextField;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JTextField totalTextField;
    // End of variables declaration//GEN-END:variables
}
