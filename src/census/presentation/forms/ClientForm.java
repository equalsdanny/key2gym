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
package census.presentation.forms;

import census.business.SessionsService;
import census.business.dto.ClientDTO;
import census.presentation.util.CardIntegerToStringConverter;
import census.presentation.util.CensusBindingListener;
import census.presentation.util.DateMidnightToStringConverter;
import census.presentation.util.MoneyBigDecimalToStringConverter;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;
import javax.swing.*;
import org.jdesktop.beansbinding.*;

/**
 *
 * @author Danylo Vashchilenko
 */
public class ClientForm extends JPanel {

    public ClientForm() {
        isPriviliged = SessionsService.getInstance().getPermissionsLevel().equals(SessionsService.PL_ALL);

        initComponents();
        buildForm();
        
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, KeyEvent.CTRL_DOWN_MASK), CardFocusAction.class.getName());
        getActionMap().put(CardFocusAction.class.getName(), new CardFocusAction());
    }

    /**
     * Initializes the components on this form.
     */
    private void initComponents() {

        /*
         * ID
         */
        idTextField = new JTextField();
        idTextField.setEditable(false);
        idTextField.setEnabled(false);

        /*
         * Full name
         */
        fullNameTextField = new JTextField();
        
        /*
         * Card
         */
        cardTextField = new JTextField();

        /*
         * Money balance
         */
        moneyBalanceTextField = new JTextField();
        moneyBalanceTextField.setEnabled(isPriviliged);


        /*
         * Attendances balance
         */
        attendancesBalanceTextField = new JSpinner();
        attendancesBalanceTextField.setModel(new SpinnerNumberModel(Short.valueOf((short) 0), Short.valueOf((short) 0), Short.valueOf((short) 999), Short.valueOf((short) 1)));
        attendancesBalanceTextField.setEnabled(isPriviliged);

        /*
         * Expiration date
         */
        expirationDateTextField = new JTextField();
        expirationDateTextField.setEnabled(isPriviliged);

        /*
         * Registration date
         */
        registrationDateTextField = new JTextField();
        registrationDateTextField.setEnabled(isPriviliged);

        /*
         * Note
         */
        noteScrollPane = new JScrollPane();
        noteTextArea = new JTextArea();
        noteTextArea.setColumns(20);
        noteTextArea.setRows(5);
        noteScrollPane.setViewportView(noteTextArea);
    }

    /**
     * Builds this from by placing the components on it.
     */
    private void buildForm() {
        FormLayout layout = new FormLayout("right:default, 3dlu, default:grow", "");
        ResourceBundle bundle = ResourceBundle.getBundle("census/presentation/resources/Strings");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, bundle, this);
        builder.defaultRowSpec(new RowSpec(RowSpec.FILL, Sizes.DEFAULT, RowSpec.NO_GROW));

        builder.appendI15d("Label.ID", idTextField);
        builder.nextLine();
        builder.appendI15d("Label.FullName", fullNameTextField);
        builder.nextLine();
        builder.appendI15d("Label.Card", cardTextField);
        builder.nextLine();
        builder.appendI15d("Label.MoneyBalance", moneyBalanceTextField);
        builder.nextLine();
        builder.appendI15d("Label.AttendancesBalance", attendancesBalanceTextField);
        builder.nextLine();
        builder.appendI15d("Label.ExpirationDate", expirationDateTextField);
        builder.nextLine();
        builder.appendI15d("Label.RegistrationDate", registrationDateTextField);
        builder.nextLine();
        
        JLabel label = new JLabel(bundle.getString("Label.Note"));
        label.setVerticalAlignment(SwingConstants.TOP);
        builder.append(label, noteScrollPane);
    }

    /**
     * Sets the form's client.
     *
     * @param newClient the new client
     */
    public void setClient(ClientDTO newClient) {
        this.client = newClient;

        if (bindingGroup == null) {
            censusBindingListener = new CensusBindingListener();

            bindingGroup = new BindingGroup();

            Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, client,
                    BeanProperty.create("id"), idTextField, BeanProperty.create("text"), "id");
            bindingGroup.addBinding(binding);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, client,
                    BeanProperty.create("fullName"), fullNameTextField, BeanProperty.create("text"), "fullName");
            bindingGroup.addBinding(binding);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, client,
                    BeanProperty.create("card"), cardTextField, BeanProperty.create("text"), "card");
            binding.setConverter(new CardIntegerToStringConverter("Card"));
            bindingGroup.addBinding(binding);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, client,
                    BeanProperty.create("note"), noteTextArea, BeanProperty.create("text"), "note");
            bindingGroup.addBinding(binding);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, client,
                    BeanProperty.create("registrationDate"), registrationDateTextField, BeanProperty.create("text"), "registrationDate");
            binding.setConverter(new DateMidnightToStringConverter("Registration Date", "dd-MM-yyyy"));
            bindingGroup.addBinding(binding);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, client,
                    BeanProperty.create("expirationDate"), expirationDateTextField, BeanProperty.create("text"), "expirationDate");
            binding.setConverter(new DateMidnightToStringConverter("Expiration Date", "dd-MM-yyyy"));
            bindingGroup.addBinding(binding);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, client,
                    BeanProperty.create("attendancesBalance"), attendancesBalanceTextField, BeanProperty.create("value"), "attendancesBalance");
            bindingGroup.addBinding(binding);

            binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_ONCE, client,
                    BeanProperty.create("moneyBalance"), moneyBalanceTextField, BeanProperty.create("text"), "moneyBalance");
            binding.setConverter(new MoneyBigDecimalToStringConverter("Money Balance"));
            bindingGroup.addBinding(binding);

            bindingGroup.addBindingListener(censusBindingListener);

            bindingGroup.bind();
        } else {

            censusBindingListener.getInvalidTargets().clear();

            /*
             * We take each binding and set the source object.
             */
            for (Binding binding : bindingGroup.getBindings()) {
                binding.unbind();
                binding.setSourceObject(client);
                binding.bind();
            }
        }
    }

    /**
     * Tries to save the form to the current client.
     *
     * @return true, if the form is valid and has been saved
     */
    public boolean trySave() {
        for (Binding binding : bindingGroup.getBindings()) {
            binding.saveAndNotify();
        }
        return censusBindingListener.getInvalidTargets().isEmpty();
    }
    
    private class CardFocusAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            cardTextField.requestFocusInWindow();
        }
        
    }
    /*
     * Business
     */
    private Boolean isPriviliged;
    private ClientDTO client;
    /*
     * Presentation
     */
    private BindingGroup bindingGroup;
    private CensusBindingListener censusBindingListener;
    private JSpinner attendancesBalanceTextField;
    private JTextField cardTextField;
    private JTextField expirationDateTextField;
    private JTextField fullNameTextField;
    private JTextField idTextField;
    private JTextField moneyBalanceTextField;
    private JScrollPane noteScrollPane;
    private JTextArea noteTextArea;
    private JTextField registrationDateTextField;
}
