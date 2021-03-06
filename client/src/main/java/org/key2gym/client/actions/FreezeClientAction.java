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
package org.key2gym.client.actions;

import java.awt.event.ActionEvent;

import org.key2gym.business.api.BusinessException;
import org.key2gym.business.api.SecurityViolationException;
import org.key2gym.business.api.ValidationException;
import org.key2gym.client.dialogs.AbstractDialog;
import org.key2gym.client.dialogs.FreezeClientDialog;
import org.key2gym.client.dialogs.PickClientDialog;

/**
 *
 * @author Danylo Vashchilenko
 */
public class FreezeClientAction extends BasicAction {

    public FreezeClientAction() {
        setText(getString("Text.Freeze"));
    }

    @Override
    public void onActionPerformed(ActionEvent e) throws BusinessException, ValidationException, SecurityViolationException {

        PickClientDialog pickClientDialog = new PickClientDialog(getFrame());
        pickClientDialog.setVisible(true);

        if (pickClientDialog.getResult().equals(AbstractDialog.Result.CANCEL)) {
            return;
        }

        FreezeClientDialog freezeClientDialog = new FreezeClientDialog(getFrame());
        freezeClientDialog.setClientId(pickClientDialog.getClientId());
        freezeClientDialog.setVisible(true);

        if (freezeClientDialog.getResult().equals(AbstractDialog.Result.CANCEL)) {
            return;
        }
    }
}
