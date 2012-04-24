/*
 * Copyright 20010-2012 VTT Biotechnology
 * This file is part of UllaQC.
 *
 * UllaQC is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * UllaQC is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * UllaQC; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */
package ullaqc.util.Tables.impl;

import ullaqc.data.DatasetType;
import ullaqc.util.Tables.DataTable;
import ullaqc.util.Tables.DataTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Creates a table for showing the data sets. It implements DataTable.
 * 
 * @author scsandra
 */
public class PushableTable implements DataTable {

    protected DataTableModel model;
    JTable table;

    public PushableTable() {
    }

    public PushableTable(DataTableModel model) {
        this.model = model;
        table = this.tableRowsColor(model);
        setTableProperties();
    }

    /**
     * Changes the model of the table.
     *
     * @param model
     */
    public void createTable(DataTableModel model) {
        this.model = model;
        // Color of the cells
        table = this.tableRowsColor(model);
    }

    /**
     * Returns the table.
     *
     * @return Table
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Changes the color of the cells depending of determinates conditions.
     *
     * @param tableModel
     * @return table
     */
    protected JTable tableRowsColor(final DataTableModel tableModel) {
        JTable colorTable = new JTable(tableModel) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int Index_row, int Index_col) {
                Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
                try {
                    // Coloring conditions
                    if (isDataSelected(Index_row)) {
                        comp.setBackground(new Color(173, 205, 203));
                        if (comp.getBackground().getRGB() != new Color(173, 205, 203).getRGB()) {
                            this.repaint();
                        }
                    } else if (Index_row % 2 == 0 && !isCellSelected(Index_row, Index_col)) {
                        comp.setBackground(new Color(234, 235, 243));
                    } else if (isCellSelected(Index_row, Index_col)) {
                        comp.setBackground(new Color(173, 205, 203));
                        if (comp.getBackground().getRGB() != new Color(173, 205, 203).getRGB()) {
                            this.repaint();
                        }
                    } else {
                        comp.setBackground(Color.white);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return comp;
            }

            private boolean isDataSelected(int row) {
                try {
                    return ((Boolean) table.getValueAt(row, 0)).booleanValue();
                } catch (Exception e) {
                    return false;
                }
            }
        };


        return colorTable;
    }

    /**
     * Sets the properties of the table: selection mode, tooltips, actions with keys..
     *
     */
    public void setTableProperties() {
        Comparator<String> comparator = new Comparator<String>() {

            public int compare(String s1, String s2) {
                DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                Date date = null, date2 = null;
                try {
                    date = (Date) formatter.parse(s1);
                    date2 = (Date) formatter.parse(s2);
                    if (date.after(date2)) {
                        return 1;
                    } else {
                        return -1;
                    }
                } catch (ParseException ex) {
                    try {
                        formatter = new SimpleDateFormat("E MMM dd HH:mm:ss yyyy");
                        date = null;
                        date2 = null;
                        date = (Date) formatter.parse(s1);
                        date2 = (Date) formatter.parse(s2);
                        if (date.after(date2)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } catch (ParseException ex2) {
                        return s1.compareTo(s2);
                    }

                }
            }
        };

        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setColumnSelectionAllowed(true);

        // Sorting       
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        sorter.setComparator(2, comparator);
        table.setRowSorter(sorter);
        table.setUpdateSelectionOnSort(true);

        // Size
        table.setMinimumSize(new Dimension(300, 800));

        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumn column = column = table.getColumnModel().getColumn(table.getColumnCount() - 1);
        column.setPreferredWidth(250);
    }

    /**
     * Formating of the numbers in the table depening on the data set type.
     *
     * @param type Type of dataset @see ullaqc.data.DatasetType
     */
    public void formatNumbers(DatasetType type) {
        try {
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMinimumFractionDigits(7);
            int init = model.getFixColumns();

            for (int i = init; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(new NumberRenderer(format));
            }
        } catch (Exception e) {
        }

    }

    /**
     * Formating of the numbers in certaing column
     *
     * @param column Column where the numbers will be formated
     */
    public void formatNumbers(int column) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(7);
        table.getColumnModel().getColumn(column).setCellRenderer(new NumberRenderer(format));
    }

    /**
     * Push header
     *
     */
    class HeaderListener extends MouseAdapter {

        JTableHeader header;
        ButtonHeaderRenderer renderer;

        HeaderListener(JTableHeader header, ButtonHeaderRenderer renderer) {
            this.header = header;
            this.renderer = renderer;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int col = header.columnAtPoint(e.getPoint());
            renderer.setPressedColumn(col);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            renderer.setPressedColumn(-1); // clear
        }
    }

    /**
     * Button header
     *
     */
    class ButtonHeaderRenderer extends JButton implements TableCellRenderer {

        int pushedColumn;

        public ButtonHeaderRenderer() {
            pushedColumn = -1;
            setMargin(new Insets(0, 0, 0, 0));
        }

        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            setText((value == null) ? "" : value.toString());
            boolean isPressed = (column == pushedColumn);
            getModel().setPressed(isPressed);
            getModel().setArmed(isPressed);
            return this;
        }

        public void setPressedColumn(int col) {
            pushedColumn = col;
        }
    }

    /**
     * Number renderer
     *
     */
    class NumberRenderer
            extends DefaultTableCellRenderer {

        private NumberFormat formatter;

        public NumberRenderer() {
            this(NumberFormat.getNumberInstance());
        }

        public NumberRenderer(NumberFormat formatter) {
            super();
            this.formatter = formatter;
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public void setValue(Object value) {
            if ((value != null) && (value instanceof Number)) {
                value = formatter.format(value);
            }

            super.setValue(value);
        }
    }
}
