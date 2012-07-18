/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package census.presentation.blocks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author daniel
 */
public class CloseableTabComponent extends javax.swing.JPanel {
 
    private Set<ActionListener> actionListeners;

    /**
     * Creates new form CloseableTabComponent
     */
    public CloseableTabComponent() {
        actionListeners = new HashSet<>();
        initComponents();
    }
    
    public void setText(String text) {
        label.setText(text);
        label.validate();
    }
    
    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }
    
    public void removeActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        closeButton = new javax.swing.JButton();

        setOpaque(false);

        label.setText("Attendances for 16-05-1996");

        closeButton.setText("x");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(3, 3, 3)
                .addComponent(closeButton))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(label)
                .addComponent(closeButton))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        for(ActionListener actionListener : actionListeners) {
            actionListener.actionPerformed(new ActionEvent(this, 0, label.getText()));
        }
    }//GEN-LAST:event_closeButtonActionPerformed

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel label;
    // End of variables declaration//GEN-END:variables
}
