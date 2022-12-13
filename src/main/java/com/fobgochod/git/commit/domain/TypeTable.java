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
 * TypeTable.java
 *
 * @author fobgochod
 * @date 2022/12/13 2:13
 */
public class TypeTable extends JBTable {

    private static final Logger log = Logger.getInstance(TypeTable.class);
    private static final int TITLE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private final MyTableModel myTableModel = new MyTableModel();
    private final List<TypeRow> typeRows = new LinkedList<>();

    /**
     * instantiation AliasTable
     */
    public TypeTable() {
        setModel(myTableModel);
        TableColumn titleColumn = getColumnModel().getColumn(TITLE_COLUMN);
        TableColumn descriptionColumn = getColumnModel().getColumn(DESCRIPTION_COLUMN);
        descriptionColumn.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                final String titleValue = getTitleValueAt(row);
                component.setForeground(titleValue.length() == 0 ? JBColor.RED : isSelected ? table.getSelectionForeground() : table.getForeground());
                return component;
            }
        });
        setColumnSize(titleColumn, 150, 250, 150);
        setColumnSize(descriptionColumn, 550, 750, 550);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


    /**
     * Set  Something  ColumnSize
     */
    public static void setColumnSize(TableColumn column, int preferredWidth, int maxWidth, int minWidth) {
        column.setPreferredWidth(preferredWidth);
        column.setMaxWidth(maxWidth);
        column.setMinWidth(minWidth);
    }


    public String getTitleValueAt(int row) {
        return (String) getValueAt(row, TITLE_COLUMN);
    }

    public void addRow() {
        final TypeEditor rowEditor = new TypeEditor("Add Type", "", "");
        if (rowEditor.showAndGet()) {
            final String name = rowEditor.getTitle();
            typeRows.add(new TypeRow(rowEditor.getTitle(), rowEditor.getDescription()));
            final int index = indexOfRowWithName(name);
            log.assertTrue(index >= 0);
            myTableModel.fireTableDataChanged();
            setRowSelectionInterval(index, index);
        }
    }

    public void removeRow() {
        final int[] selectedRows = getSelectedRows();
        if (selectedRows.length == 0) return;
        Arrays.sort(selectedRows);
        final int originalRow = selectedRows[0];
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            final int selectedRow = selectedRows[i];
            if (isValidRow(selectedRow)) {
                typeRows.remove(selectedRow);
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

    public boolean editRow() {
        if (getSelectedRowCount() != 1) {
            return false;
        }
        final int selectedRow = getSelectedRow();
        final TypeRow typeRow = typeRows.get(selectedRow);
        final TypeEditor editor = new TypeEditor("Edit Type", typeRow.getTitle(), typeRow.getDescription());
        if (editor.showAndGet()) {
            typeRow.setTitle(editor.getTitle());
            typeRow.setDescription(editor.getDescription());
            myTableModel.fireTableDataChanged();
        }
        return true;
    }

    public void moveUp() {
        int selectedRow = getSelectedRow();
        int index = selectedRow - 1;
        if (selectedRow != -1) {
            Collections.swap(typeRows, selectedRow, index);
        }
        setRowSelectionInterval(index, index);
    }


    public void moveDown() {
        int selectedRow = getSelectedRow();
        int index = selectedRow + 1;
        if (selectedRow != -1) {
            Collections.swap(typeRows, selectedRow, index);
        }
        setRowSelectionInterval(index, index);
    }

    public void resetRow() {
        myTableModel.fireTableDataChanged();
    }

    private boolean isValidRow(int selectedRow) {
        return selectedRow >= 0 && selectedRow < typeRows.size();
    }

    public void commit(GitCommitHelperState state) {
        state.setTypeRows(typeRows);
    }

    public void reset(GitCommitHelperState state) {
        obtainRows(typeRows, state);
        myTableModel.fireTableDataChanged();
    }


    private int indexOfRowWithName(String name) {
        for (int i = 0; i < typeRows.size(); i++) {
            final TypeRow typeRow = typeRows.get(i);
            if (name.equals(typeRow.getTitle())) {
                return i;
            }
        }
        return -1;
    }

    private void obtainRows(@NotNull List<TypeRow> typeRows, GitCommitHelperState settings) {
        typeRows.clear();
        typeRows.addAll(settings.getTypeRows());
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
        public int getRowCount() {
            return typeRows.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case TITLE_COLUMN:
                    return "title";
                case DESCRIPTION_COLUMN:
                    return "description";
            }
            return null;
        }

        @Override
        public Class<String> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final TypeRow row = typeRows.get(rowIndex);
            switch (columnIndex) {
                case TITLE_COLUMN:
                    return row.getTitle();
                case DESCRIPTION_COLUMN:
                    return row.getDescription();
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }
    }
}
