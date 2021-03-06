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
package org.key2gym.client.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.jdesktop.beansbinding.Converter;
import org.key2gym.business.api.ValidationException;

/**
 *
 * @author Danylo Vashchilenko
 */
public class IntegerToStringConverter extends Converter<Integer, String> {

    private String fieldName;
    private boolean canBeEmpty;
    private ResourceBundle strings = ResourceBundle.getBundle("org/key2gym/client/resources/Strings");

    public IntegerToStringConverter(String fieldName, boolean canBeEmpty) {
        this.fieldName = fieldName;
        this.canBeEmpty = canBeEmpty;
    }

    @Override
    public Integer convertReverse(String value) {
        value = value.trim();

        try {
            if (value.isEmpty()) {
                if (!canBeEmpty) {
                    throw new NumberFormatException();
                } else {
                    return null;
                }
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(new ValidationException(MessageFormat.format(strings.getString("Message.FieldIsNotFilledInCorrectly.withFieldName"), new Object[]{fieldName})));
        }
    }

    @Override
    public String convertForward(Integer value) {
        return value == null ? "" : value.toString();
    }
}
