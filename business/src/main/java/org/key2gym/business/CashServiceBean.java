package org.key2gym.business;

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


import java.math.BigDecimal;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import org.joda.time.DateMidnight;
import org.key2gym.business.api.*;
import org.key2gym.business.api.dtos.CashAdjustmentDTO;
import org.key2gym.business.api.remote.CashServiceRemote;
import org.key2gym.persistence.CashAdjustment;

/**
 *
 * @author Danylo Vashchilenko
 */
@Stateless
@Remote(CashServiceRemote.class)
@DeclareRoles({SecurityRoles.MANAGER, SecurityRoles.JUNIOR_ADMINISTRATOR, SecurityRoles.SENIOR_ADMINISTRATOR})
@RolesAllowed({SecurityRoles.MANAGER, SecurityRoles.JUNIOR_ADMINISTRATOR, SecurityRoles.SENIOR_ADMINISTRATOR})
public class CashServiceBean extends BasicBean implements CashServiceRemote {

    @Override
    public BigDecimal getCashByDate(DateMidnight date) throws SecurityViolationException {

        if (date == null) {
            throw new NullPointerException("The date is null."); //NOI18N
        }

        if (!date.equals(DateMidnight.now())) {
            if (!callerHasRole(SecurityRoles.MANAGER)) {
                throw new SecurityViolationException(getString("Security.Access.Denied"));
            }
        } else {
            if (!callerHasAnyRole(SecurityRoles.JUNIOR_ADMINISTRATOR, SecurityRoles.SENIOR_ADMINISTRATOR)) {
                throw new SecurityViolationException(getString("Security.Access.Denied"));
            }
        }

        BigDecimal cash = (BigDecimal) entityManager.createNamedQuery("OrderEntity.sumPaymentsForDateRecorded") //NOI18N
                .setParameter("dateRecorded", date.toDate()) //NOI18N
                .getSingleResult();

        /*
         * The sum aggregate function returns null, when there is not 
         * any orders.
         */
        if (cash == null) {
            cash = BigDecimal.ZERO;
        }

        CashAdjustment adjustment = entityManager.find(CashAdjustment.class, date.toDate());

        if (adjustment != null) {
            cash = cash.add(adjustment.getAmount());
        }

        return cash;
    }

    @Override
    public void recordCashAdjustment(CashAdjustmentDTO cashAdjustment) throws ValidationException, SecurityViolationException {

        if (!callerHasRole(SecurityRoles.MANAGER)) {
            throw new SecurityViolationException(getString("Security.Operation.Denied"));
        }

        if (cashAdjustment == null) {
            throw new NullPointerException("The cashAdjustment is null."); //NOI18N
        }

        if (cashAdjustment.getDate() == null) {
            throw new NullPointerException("The cashAdjustment.getDate() is null."); //NOI18N
        }

        if (cashAdjustment.getAmount() == null) {
            throw new NullPointerException("The cashAdjustment.getAmount() is null."); //NOI18N
        }

        validateAmount(cashAdjustment.getAmount());


        if (cashAdjustment.getNote() == null) {
            throw new NullPointerException("The cashAdjustment.getNote() is null."); //NOI18N
        }

        CashAdjustment entityCashAdjustment = entityManager.find(CashAdjustment.class,
								 cashAdjustment.getDate().toDate(), LockModeType.OPTIMISTIC);

        if (entityCashAdjustment == null) {
            entityCashAdjustment = new CashAdjustment();
            entityCashAdjustment.setDateRecorded(cashAdjustment.getDate().toDate());
            entityCashAdjustment.setAmount(cashAdjustment.getAmount());
            entityCashAdjustment.setNote(cashAdjustment.getNote());

            entityManager.persist(entityCashAdjustment);
        } else {
            entityCashAdjustment.setDateRecorded(cashAdjustment.getDate().toDate());
            entityCashAdjustment.setAmount(cashAdjustment.getAmount());
            entityCashAdjustment.setNote(cashAdjustment.getNote());
        }

        entityManager.flush();
    }

    @Override
    public CashAdjustmentDTO getAdjustmentByDate(DateMidnight date) throws SecurityViolationException {

        if (!callerHasRole(SecurityRoles.MANAGER)) {
            throw new SecurityViolationException(getString("Security.Access.Denied"));
        }

        if (date == null) {
            throw new NullPointerException("The date is null."); //NOI18N
        }

        CashAdjustment cashAdjustmentEntity = entityManager.find(CashAdjustment.class, date.toDate());

        if (cashAdjustmentEntity == null) {

            cashAdjustmentEntity = new CashAdjustment();
            cashAdjustmentEntity.setAmount(BigDecimal.ZERO);
            cashAdjustmentEntity.setDateRecorded(date.toDate());
            cashAdjustmentEntity.setNote("");

            entityManager.persist(cashAdjustmentEntity);
        }

        CashAdjustmentDTO cashAdjustmentDTO = new CashAdjustmentDTO();

        cashAdjustmentDTO.setAmount(cashAdjustmentEntity.getAmount());
        cashAdjustmentDTO.setDate(new DateMidnight(cashAdjustmentEntity.getDateRecorded()));
        cashAdjustmentDTO.setNote(cashAdjustmentEntity.getNote());

        return cashAdjustmentDTO;
    }

    /**
     * Validates a money amount.
     * 
     * @param value the amount to validate
     * @throws ValidationException if the amount is invalid
     */
    public void validateAmount(BigDecimal value) throws ValidationException {
        if (value == null) {
            throw new NullPointerException("The amount is null."); //NOI18N
        }

        if (value.scale() > 2) {
            throw new ValidationException(getString("Invalid.Money.TwoDigitsAfterDecimalPointMax"));
        }

        value = value.setScale(2);

        if (value.precision() > 6) {
            throw new ValidationException(getString("Invalid.CashAdjustment.LimitReached"));
        }
    }
    @PersistenceContext(unitName = "PU")
    private EntityManager entityManager;
}
