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
package ullaqc.util.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import org.jfree.ui.OverlayLayout;

/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 *
 * Simple table cell renderer that renders only JComponents
 */
public class ComponentCellRenderer implements TableCellRenderer,
        ListCellRenderer {

        private boolean createTooltips;
        private Font font;

        /**
         */
        public ComponentCellRenderer() {
                this(false, null);
        }

        /**
         * @param font
         */
        public ComponentCellRenderer(Font font) {
                this(false, font);
        }

        /**
         * @param font
         */
        public ComponentCellRenderer(boolean createTooltips) {
                this(createTooltips, null);
        }

        /**
         * @param font
         */
        public ComponentCellRenderer(boolean createTooltips, Font font) {
                this.createTooltips = createTooltips;
                this.font = font;
        }

        /**
         * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
         *      java.lang.Object, boolean, boolean, int, int)
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

                JPanel newPanel = new JPanel(new OverlayLayout());
                Color bgColor;

                if (isSelected) {
                        bgColor = table.getSelectionBackground();
                } else {
                        bgColor = table.getBackground();
                }

                newPanel.setBackground(bgColor);

                if (hasFocus) {
                        Border border = null;
                        if (isSelected) {
                                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
                        }
                        if (border == null) {
                                border = UIManager.getBorder("Table.focusCellHighlightBorder");
                        }
                        newPanel.setBorder(border);
                }

                if (value != null) {

                        if (value instanceof JComponent) {

                                newPanel.add((JComponent) value);

                        } else {

                                JLabel newLabel = new JLabel(value.toString());

                                if (font != null) {
                                        newLabel.setFont(font);
                                } else if (table.getFont() != null) {
                                        newLabel.setFont(table.getFont());
                                }

                                newPanel.add(newLabel);
                        }

                        if (createTooltips) {
                                newPanel.setToolTipText(value.toString());
                        }

                }

                return newPanel;

        }

        /**
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
         *      java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean hasFocus) {

                JPanel newPanel = new JPanel(new OverlayLayout());
                Color bgColor;

                if (isSelected) {
                        bgColor = list.getSelectionBackground();
                } else {
                        bgColor = list.getBackground();
                }

                newPanel.setBackground(bgColor);

                if (hasFocus) {
                        Border border = null;
                        if (isSelected) {
                                border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
                        }
                        if (border == null) {
                                border = UIManager.getBorder("List.focusCellHighlightBorder");
                        }
                        newPanel.setBorder(border);
                }

                if (value != null) {

                        if (value instanceof JComponent) {

                                newPanel.add((JComponent) value);

                        } else {

                                JLabel newLabel = new JLabel(value.toString());

                                if (font != null) {
                                        newLabel.setFont(font);
                                } else if (list.getFont() != null) {
                                        newLabel.setFont(list.getFont());
                                }

                                newPanel.add(newLabel);
                        }
                }

                return newPanel;

        }
}
