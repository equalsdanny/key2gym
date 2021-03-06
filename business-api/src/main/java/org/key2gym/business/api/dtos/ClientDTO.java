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
package org.key2gym.business.api.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import org.joda.time.DateMidnight;

/**
 *
 * @author Danylo Vashchilenko
 */
public class ClientDTO implements Serializable {
    
    public ClientDTO() {
        
    }

    public ClientDTO(Integer id, String fullName, Integer card, DateMidnight registrationDate, BigDecimal moneyBalance, Integer attendancesBalance, DateMidnight expirationDate, String note) {
        this.id = id;
        this.fullName = fullName;
        this.card = card;
        this.registrationDate = registrationDate;
        this.moneyBalance = moneyBalance;
        this.attendancesBalance = attendancesBalance;
        this.expirationDate = expirationDate;
        this.note = note;
    }
  
    public Integer getCard() {
        return card;
    }

    public void setCard(Integer card) {
        this.card = card;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getAttendancesBalance() {
        return attendancesBalance;
    }

    public void setAttendancesBalance(Integer attendancesBalance) {
        this.attendancesBalance = attendancesBalance;
    }

    public DateMidnight getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateMidnight expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMoneyBalance() {
        return moneyBalance;
    }

    public void setMoneyBalance(BigDecimal moneyBalance) {
        this.moneyBalance = moneyBalance;
    }

    public DateMidnight getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(DateMidnight registrationDate) {
        this.registrationDate = registrationDate;
    }
    private Integer id;
    private String fullName;
    private DateMidnight registrationDate;
    private BigDecimal moneyBalance;
    private Integer attendancesBalance;
    private DateMidnight expirationDate;
    private String note;
    private Integer card;
}
