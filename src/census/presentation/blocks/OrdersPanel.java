/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package census.presentation.blocks;

import census.business.CashService;
import census.business.OrdersService;
import census.business.SessionsService;
import census.business.StorageService;
import census.business.api.SecurityException;
import census.business.dto.OrderDTO;
import census.presentation.actions.CensusAction;
import census.presentation.actions.EditOrderAction;
import census.presentation.util.OrdersTableCellRenderer;
import census.presentation.util.OrdersTableModel;
import census.presentation.util.OrdersTableModel.Column;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.table.TableColumn;
import org.joda.time.DateMidnight;

/**
 *
 * @author Danylo Vashchilenko
 */
public class OrdersPanel extends javax.swing.JPanel {
    private ResourceBundle bundle = ResourceBundle.getBundle("census/presentation/resources/Strings");

    /**
     * Creates new form OrdersPanel
     */
    public OrdersPanel() {
        financialActivitiesService = OrdersService.getInstance();
        cashService = CashService.getInstance();

        initComponents();

        observer = new CustomObserver();
        observer.update(null, null);

        financialActivitiesTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    new EditOrderAction().actionPerformed(new ActionEvent(this, 0, CensusAction.ACTION_CONTEXT));
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cashLabel = new javax.swing.JLabel();
        cashTextField = new javax.swing.JTextField();
        totalLabel = new javax.swing.JLabel();
        totalTextField = new javax.swing.JTextField();
        financialActivitiesTableScrollPane = new javax.swing.JScrollPane();
        financialActivitiesTable = new javax.swing.JTable();

        cashLabel.setText(bundle.getString("Label.Cash")); // NOI18N

        cashTextField.setEditable(false);

        totalLabel.setText(bundle.getString("Label.Total")); // NOI18N

        totalTextField.setEditable(false);

        /*
        * This renderer highlights FAs wo/ full payments
        */
        financialActivitiesTable.setDefaultRenderer(String.class, new OrdersTableCellRenderer());

        /*
        * Columns for FAs table
        */
        Column[] сolumns =
        new Column[] {
            Column.ID,
            Column.SUBJECT,
            Column.TOTAL,
            Column.PAID
        };

        financialActivitiesTableModel = new OrdersTableModel(сolumns);
        financialActivitiesTable.setModel(financialActivitiesTableModel);
        int[] widths = new int[]{50,200,50,50};
        TableColumn column = null;
        for (int i = 0; i < widths.length; i++) {
            column = financialActivitiesTable.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
        financialActivitiesTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        financialActivitiesTable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                financialActivitiesTableFocusLost(evt);
            }
        });
        financialActivitiesTableScrollPane.setViewportView(financialActivitiesTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(financialActivitiesTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalLabel)
                    .addComponent(totalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashLabel)
                    .addComponent(cashTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(financialActivitiesTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void financialActivitiesTableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_financialActivitiesTableFocusLost
        financialActivitiesTable.clearSelection();
    }//GEN-LAST:event_financialActivitiesTableFocusLost

    private void updateGUI() throws SecurityException {
        List<OrderDTO> financialActivities = financialActivitiesService.findAllByDate(date);
        financialActivitiesTableModel.setFinancialActivities(financialActivities);

        BigDecimal total = financialActivitiesService.getTotalForDate(date);
        BigDecimal cash = cashService.getCashByDate(date);

        totalTextField.setText(total.toPlainString());
        cashTextField.setText(cash.toPlainString());
    }
    
    public OrderDTO getSelectedOrder() {
        int index = financialActivitiesTable.getSelectedRow();
        
        if(index == -1) {
            return null;
        }
        
        return financialActivitiesTableModel.getFinancialActivityAt(index);
    }

    public void setDate(DateMidnight date) throws SecurityException {
        this.date = date;
        updateGUI();
    }

    public DateMidnight getDate() {
        return date;

    }
    /*
     * Presentation
     */
    private DateMidnight date;
    private OrdersTableModel financialActivitiesTableModel;
    private CustomObserver observer;
    /*
     * Business
     */
    private OrdersService financialActivitiesService;
    private CashService cashService;

    private class CustomObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            if (o == null) {
                StorageService.getInstance().addObserver(this);
                return;
            }

            if (SessionsService.getInstance().hasOpenSession()) {
                try {
                    updateGUI();
                } catch (SecurityException ex) {
                    // TODO: process exception
                }
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cashLabel;
    private javax.swing.JTextField cashTextField;
    private javax.swing.JTable financialActivitiesTable;
    private javax.swing.JScrollPane financialActivitiesTableScrollPane;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JTextField totalTextField;
    // End of variables declaration//GEN-END:variables
}
