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

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import org.key2gym.business.api.dtos.OrderLineDTO;

/**
 *
 * @author Danylo Vashchilenko
 */
public class OrderLinesTableModel extends AbstractTableModel {

    private ResourceBundle strings = ResourceBundle.getBundle("org/key2gym/client/resources/Strings");

    public enum Column {

        ITEM_TITLE, ITEM_PRICE, ITEM_ID, QUANTITY, DISCOUNT_PERCENT, DISCOUNT_TITLE, TOTAL
    };
    private List<OrderLineDTO> orderLines;
    private Column[] columns;

    public OrderLinesTableModel(Column[] columns) {
        this(columns, new LinkedList<OrderLineDTO>());
    }

    public OrderLinesTableModel(Column[] columns, List<OrderLineDTO> orderLines) {
        this.columns = columns;
        this.orderLines = orderLines;
    }

    public void setOrderLines(List<OrderLineDTO> orderLines) {
        this.orderLines = orderLines;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return orderLines.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columns[columnIndex].equals(Column.ITEM_TITLE)) {
            return strings.getString("Text.Item");
        } else if (columns[columnIndex].equals(Column.ITEM_PRICE)) {
            return strings.getString("Text.Price");
        } else if (columns[columnIndex].equals(Column.ITEM_ID)) {
            return strings.getString("Text.ItemID");
        } else if (columns[columnIndex].equals(Column.QUANTITY)) {
            return strings.getString("Text.Quantity");
        } else if (columns[columnIndex].equals(Column.DISCOUNT_PERCENT)) {
            return strings.getString("Text.DiscountPercent");
        } else if (columns[columnIndex].equals(Column.DISCOUNT_TITLE)) {
            return strings.getString("Text.Discount");
        } else if (columns[columnIndex].equals(Column.TOTAL)) {
            return strings.getString("Text.Total");
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        OrderLineDTO orderLine = orderLines.get(rowIndex);
        if (columns[columnIndex].equals(Column.ITEM_TITLE)) {
            return orderLine.getItemTitle();
        } else if (columns[columnIndex].equals(Column.ITEM_PRICE)) {
            return orderLine.getItemPrice().toPlainString();
        } else if (columns[columnIndex].equals(Column.ITEM_ID)) {
            return orderLine.getItemId().toString();
        } else if (columns[columnIndex].equals(Column.QUANTITY)) {
            return orderLine.getQuantity().toString();
        } else if (columns[columnIndex].equals(Column.DISCOUNT_PERCENT)) {
            return orderLine.getDiscountPercent().toString();
        } else if (columns[columnIndex].equals(Column.DISCOUNT_TITLE)) {
            return orderLine.getDiscountTitle();
        } else if (columns[columnIndex].equals(Column.TOTAL)) {
            return orderLine.getTotal().toPlainString();
        }
        throw new IllegalArgumentException();
    }
}
