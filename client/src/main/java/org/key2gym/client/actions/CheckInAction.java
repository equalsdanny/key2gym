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

import javax.swing.ImageIcon;

import org.joda.time.DateMidnight;
import org.key2gym.business.api.BusinessException;
import org.key2gym.business.api.SecurityViolationException;
import org.key2gym.business.api.ValidationException;
import org.key2gym.business.api.services.OrdersService;
import org.key2gym.client.ContextManager;
import org.key2gym.client.dialogs.AbstractDialog;
import org.key2gym.client.dialogs.CheckInDialog;
import org.key2gym.client.dialogs.EditOrderDialog;

/**
 *
 * @author Danylo Vashchilenko
 */
public final class CheckInAction extends BasicAction {

    public CheckInAction() {
        setText(getString("Text.Entry"));
        setIcon(new ImageIcon(getClass().getResource("/org/key2gym/client/resources/open.png")));
    }

    @Override
    public void onActionPerformed(ActionEvent e) throws BusinessException, ValidationException, SecurityViolationException {

        OrdersService ordersService = ContextManager.lookup(OrdersService.class);

        /*
         * Opens the CheckIn dialog.
         */
        CheckInDialog checkInDialog = new CheckInDialog(getFrame());
        checkInDialog.setVisible(true);
        
        if (checkInDialog.getResult().equals(AbstractDialog.Result.CANCEL)) {
            return;
        }

        /*
         * If requested, opens an order.
         */
        if (checkInDialog.isOrderRequested()) {

            Integer orderId = null;
            EditOrderDialog editOrderDialog = new EditOrderDialog(getFrame());

            try {
                if (checkInDialog.isAnonymous()) {
                    orderId = ordersService.findForAttendanceById(checkInDialog.getAttendanceId());
                } else {
                    orderId = ordersService.findByClientIdAndDate(checkInDialog.getClientId(), new DateMidnight(), true);
                }
            } catch (ValidationException ex) {
                throw new RuntimeException(ex);
            }

            editOrderDialog.setOrderId(orderId);
            editOrderDialog.setFullPaymentForced(false);
            editOrderDialog.setVisible(true);
        }
    }
}
