/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package census.presentation.dialogs;

import census.business.AttendancesService;
import census.business.FinancialActivitiesService;
import census.business.KeysService;
import census.business.api.BusinessException;
import census.business.api.ValidationException;
import census.business.api.SecurityException;
import census.business.dto.AttendanceDTO;
import census.business.dto.KeyDTO;
import census.presentation.CensusFrame;
import census.presentation.util.KeyListCellRenderer;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import org.joda.time.DateMidnight;

/**
 *
 * @author Danylo Vashchilenko
 */
public class PickFinancialActivityDialog extends CensusDialog {
    private ResourceBundle bundle = ResourceBundle.getBundle("census/presentation/resources/Strings");
    
    /**
     * Creates new form PickFinancialActivityDialog
     * 
     * @param parent the frame the dialog should align with
     * @param session the session used by the dialog and its caller
     */
    public PickFinancialActivityDialog(JFrame parent) {
        super(parent, true);
        
        initComponents();
        
        attendancesComboBox.setRenderer(new KeyListCellRenderer());
        
        List<KeyDTO> keys = KeysService.getInstance().getKeysTaken();
        if(keys.isEmpty()) {
            attendanceRadioButton.setEnabled(false);
            clientRadioButton.doClick();
        } else {
            attendancesComboBox.setModel(new DefaultComboBoxModel(keys.toArray()));
            attendanceRadioButton.doClick();
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

        modeButtonGroup = new javax.swing.ButtonGroup();
        clientRadioButton = new javax.swing.JRadioButton();
        attendanceRadioButton = new javax.swing.JRadioButton();
        attendancesComboBox = new javax.swing.JComboBox();
        otherRadioButton = new javax.swing.JRadioButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("Title.PickFinancialActivity")); // NOI18N
        setResizable(false);

        modeButtonGroup.add(clientRadioButton);
        clientRadioButton.setText(bundle.getString("Text.Client")); // NOI18N
        clientRadioButton.setActionCommand("clientCard");
        clientRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonsActionPerformed(evt);
            }
        });

        modeButtonGroup.add(attendanceRadioButton);
        attendanceRadioButton.setText(bundle.getString("Text.Attendance")); // NOI18N
        attendanceRadioButton.setActionCommand("attendance");
        attendanceRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonsActionPerformed(evt);
            }
        });

        modeButtonGroup.add(otherRadioButton);
        otherRadioButton.setText(bundle.getString("Text.Other")); // NOI18N
        otherRadioButton.setActionCommand("other");
        otherRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioButtonsActionPerformed(evt);
            }
        });

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
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(clientRadioButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(attendanceRadioButton)
                            .addComponent(otherRadioButton))
                        .addGap(209, 209, 209))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(attendancesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(clientRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attendanceRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(attendancesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(otherRadioButton)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Process Cancel button click.
     * 
     * @param evt the action event, optional
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setResult(RESULT_CANCEL);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Process a radio button event.
     * 
     * @param evt the action event, optional
     */
    private void radioButtonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonsActionPerformed
        /*
         * The code does some magic to simplify the GUI and make it more
         * intuitive.
         */
        
        if(attendanceRadioButton.isSelected()) {
            attendancesComboBox.setEnabled(true);
            attendancesComboBox.requestFocusInWindow();
        } else {
            attendancesComboBox.setEnabled(false);
        }
    }//GEN-LAST:event_radioButtonsActionPerformed

    /**
     * Processes an OK button click.
     * 
     * @param evt the action event, optional 
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        /*
         * The API requires just to return a valid financial activity's ID
         * upon a sucessful completion.
         */
        try {
            
            if(clientRadioButton.isSelected()) {
                setResult(RESULT_OK);
                setClient(true);
                dispose();
                return;
                
            /*
             * The attendance's key is provided.
             */
            } else if(attendanceRadioButton.isSelected()) {
                /*
                 * GUI garantees that there is a key selected, 
                 * if the attendanceRadionButton is selected
                 */
                KeyDTO key = (KeyDTO)attendancesComboBox.getSelectedItem();
                try {
                    AttendancesService attendancesService = AttendancesService.getInstance();
                    FinancialActivitiesService financialActivitiesService = FinancialActivitiesService.getInstance();
                    
                    Short attendanceId = attendancesService.findOpenAttendanceByKey(key.getId());
                    AttendanceDTO attendanceDTO = attendancesService.getAttendanceById(attendanceId);
                    if(attendanceDTO.getClientId() == null) {
                        /*
                         * The API requires to pass only anonymous attendances to findForAttendanceById
                         */
                        financialActivityId = financialActivitiesService.findForAttendanceById(attendanceId);
                    } else {
                        financialActivityId = financialActivitiesService.findByClientIdAndDate(attendanceDTO.getClientId(), new DateMidnight(), true);
                    }
                /*
                 * All exceptions are unexpected, and, therefore, are bugs. 
                 */
                } catch (BusinessException|ValidationException|SecurityException ex) {
                    throw new RuntimeException(ex);
                }
            /*
             * A default financial activity is needed.
             */
            } else {
                financialActivityId = FinancialActivitiesService.getInstance().findCurrentDefault(true);
            }
        } catch(ValidationException ex) {
            CensusFrame.getGlobalCensusExceptionListenersStack().peek().processException(ex);
            return;
        } catch(RuntimeException ex) {
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
        setClient(false);
        setFinancialActivityId(financialActivityId);
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed


    private Short financialActivityId;
    private Boolean client;

    public Short getFinancialActivityId() {
        return financialActivityId;
    }

    public void setFinancialActivityId(Short financialActivityId) {
        this.financialActivityId = financialActivityId;
    }
    
    public Boolean isClient() {
        return client;
    }
    
    public void setClient(Boolean client) {
        this.client = client;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton attendanceRadioButton;
    private javax.swing.JComboBox attendancesComboBox;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton clientRadioButton;
    private javax.swing.ButtonGroup modeButtonGroup;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton otherRadioButton;
    // End of variables declaration//GEN-END:variables
}
