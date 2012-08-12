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
package census.presentation.actions;

import census.business.SessionsService;
import census.business.StorageService;
import census.presentation.dialogs.CensusDialog;
import census.presentation.dialogs.EditClientDialog;
import census.presentation.dialogs.PickClientDialog;
import java.awt.event.ActionEvent;
import java.beans.Beans;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 *
 * @author Danylo Vashchilenko
 */
public class EditClientAction extends CensusAction implements Observer {

    private ResourceBundle bundle;

    public EditClientAction() {
        if (!Beans.isDesignTime()) {
            update(null, null);
        }
        bundle  = ResourceBundle.getBundle("census/presentation/resources/Strings");
        setText(bundle.getString("Text.Find"));
        setIcon(new ImageIcon(getClass().getResource("/census/presentation/resources/search64.png")));

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StorageService storageService = null;

        try {
            storageService = StorageService.getInstance();
            storageService.beginTransaction();

            final PickClientDialog pickClientDialog = new PickClientDialog(getFrame());
            pickClientDialog.setVisible(true);

            if (pickClientDialog.getResult().equals(CensusDialog.RESULT_EXCEPTION)) {
                throw pickClientDialog.getException();
            }

            if (pickClientDialog.getResult().equals(CensusDialog.RESULT_CANCEL)) {
                storageService.rollbackTransaction();
                return;
            }

            final EditClientDialog editClientDialog = new EditClientDialog(getFrame());
            new Thread() {

                @Override
                public void run() {
                    editClientDialog.setClientId(pickClientDialog.getClientId());
                }
            }.start();
            editClientDialog.setVisible(true);

            if (editClientDialog.getResult().equals(CensusDialog.RESULT_EXCEPTION)) {
                throw editClientDialog.getException();
            }

            if (editClientDialog.getResult().equals(CensusDialog.RESULT_CANCEL)) {
                storageService.rollbackTransaction();
                return;
            }

            storageService.commitTransaction();

        } catch (RuntimeException ex) {
            Logger.getLogger(this.getClass().getName()).error("RuntimeException", ex);
            JOptionPane.showMessageDialog(getFrame(), bundle.getString("Message.ProgramEncounteredError"), bundle.getString("Title.Error"), JOptionPane.ERROR_MESSAGE);
            if(StorageService.getInstance().isTransactionActive()) {
                StorageService.getInstance().rollbackTransaction();
            }
            return;
        }
    }

    @Override
    public final void update(Observable o, Object arg) {
        if (o == null) {
            SessionsService.getInstance().addObserver(this);
        }
        Boolean open = SessionsService.getInstance().hasOpenSession();
        setEnabled(open);
    }
}
