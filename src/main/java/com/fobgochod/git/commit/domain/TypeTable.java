package com.fobgochod.git.commit.domain;

import com.fobgochod.git.commit.settings.GitCommitHelperState;
import com.fobgochod.git.commit.view.settings.TypeEditor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *  TypeTable.java
 *
 * @author fobgochod
 * @date 2022/12/13 2:13
 */
public class TypeTable extends JBTable {

    private static final Logger log = Logger.getInstance(TypeTable.class);
    private static final int NAME_COLUMN = 0;
    private static final int VALUE_COLUMN = 1;
    private final MyTableModel myTableModel = new MyTableModel();


    private List<TypeItem> typeItems = new LinkedList<>();

    /**
     * instantiation AliasTable
     */
    public TypeTable() {
        setModel(myTableModel);
        TableColumn column = getColumnModel().getColumn(NAME_COLUMN);
        TableColumn valueColumn = getColumnModel().getColumn(VALUE_COLUMN);
        column.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                final String macroValue = getAliasValueAt(row);
                component.setForeground(macroValue.length() == 0
                        ? JBColor.RED
                        : isSelected ? table.getSelectionForeground() : table.getForeground());
                return component;
            }
        });
        setColumnSize(column, 150, 250, 150);
        setColumnSize(valueColumn, 550, 750, 550);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


    /**
     * Set  Something  ColumnSize
     */
    public static void setColumnSize(TableColumn column, int preferedWidth, int maxWidth, int minWidth) {
        column.setPreferredWidth(preferedWidth);
        column.setMaxWidth(maxWidth);
        column.setMinWidth(minWidth);
    }


    public String getAliasValueAt(int row) {
        return (String) getValueAt(row, VALUE_COLUMN);
    }


    public void addAlias() {
        final TypeEditor macroEditor = new TypeEditor("Add Type", "", "");
        if (macroEditor.showAndGet()) {
            final String name = macroEditor.getTitle();
            typeItems.add(new TypeItem(macroEditor.getTitle(), macroEditor.getDescription()));
            final int index = indexOfAliasWithName(name);
            log.assertTrue(index >= 0);
            myTableModel.fireTableDataChanged();
            setRowSelectionInterval(index, index);
        }
    }

    private boolean isValidRow(int selectedRow) {
        return selectedRow >= 0 && selectedRow < typeItems.size();
    }

    public void moveUp() {
        int selectedRow = getSelectedRow();
        int index1 = selectedRow - 1;
        if (selectedRow != -1) {
            Collections.swap(typeItems, selectedRow, index1);
        }
        setRowSelectionInterval(index1, index1);
    }


    public void moveDown() {
        int selectedRow = getSelectedRow();
        int index1 = selectedRow + 1;
        if (selectedRow != -1) {
            Collections.swap(typeItems, selectedRow, index1);
        }
        setRowSelectionInterval(index1, index1);
    }


    public void removeSelectedAliases() {
        final int[] selectedRows = getSelectedRows();
        if (selectedRows.length == 0) return;
        Arrays.sort(selectedRows);
        final int originalRow = selectedRows[0];
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            final int selectedRow = selectedRows[i];
            if (isValidRow(selectedRow)) {
                typeItems.remove(selectedRow);
            }
        }
        myTableModel.fireTableDataChanged();
        if (originalRow < getRowCount()) {
            setRowSelectionInterval(originalRow, originalRow);
        } else if (getRowCount() > 0) {
            final int index = getRowCount() - 1;
            setRowSelectionInterval(index, index);
        }
    }


    public void commit(GitCommitHelperState settings) {
        settings.getDateSettings().setTypeAliases(new LinkedList<>(typeItems));
    }

    public void resetDefaultAliases() {
        myTableModel.fireTableDataChanged();
    }

    public void reset(GitCommitHelperState settings) {
        obtainAliases(typeItems, settings);
        myTableModel.fireTableDataChanged();
    }


    private int indexOfAliasWithName(String name) {
        for (int i = 0; i < typeItems.size(); i++) {
            final TypeItem typeItem = typeItems.get(i);
            if (name.equals(typeItem.getTitle())) {
                return i;
            }
        }
        return -1;
    }

    private void obtainAliases(@NotNull List<TypeItem> aliases, GitCommitHelperState settings) {
        aliases.clear();
        aliases.addAll(settings.getDateSettings().getTypeAliases());
    }

    public boolean editAlias() {
        if (getSelectedRowCount() != 1) {
            return false;
        }
        final int selectedRow = getSelectedRow();
        final TypeItem typeItem = typeItems.get(selectedRow);
        final TypeEditor editor = new TypeEditor("Edit Type", typeItem.getTitle(), typeItem.getDescription());
        if (editor.showAndGet()) {
            typeItem.setTitle(editor.getTitle());
            typeItem.setDescription(editor.getDescription());
            myTableModel.fireTableDataChanged();
        }
        return true;
    }

    public boolean isModified(GitCommitHelperState settings) {
        final List<TypeItem> aliases = new LinkedList<>();
        obtainAliases(aliases, settings);
        return !aliases.equals(typeItems);
    }

    //==========================================================================//

    /**
     * EditValidator
     */
    private static class EditValidator implements TypeEditor.Validator {
        @Override
        public boolean isOK(String name, String value) {
            return !name.isEmpty() && !value.isEmpty();
        }
    }


    /**
     * MyTableModel
     */
    private class MyTableModel extends AbstractTableModel {
        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return typeItems.size();
        }

        @Override
        public Class getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final TypeItem pair = typeItems.get(rowIndex);
            switch (columnIndex) {
                case NAME_COLUMN:
                    return pair.getTitle();
                case VALUE_COLUMN:
                    return pair.getDescription();
            }
            log.error("Wrong indices");
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case NAME_COLUMN:
                    return "title";
                case VALUE_COLUMN:
                    return "description";
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
