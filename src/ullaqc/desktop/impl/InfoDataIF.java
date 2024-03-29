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

/*
 * InfoDataIF.java
 *
 * Created on 01-Oct-2009, 09:46:15 
 */
package ullaqc.desktop.impl;

import ullaqc.data.Dataset;

/** *
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */
public class InfoDataIF extends javax.swing.JInternalFrame {

        private Dataset data;

        /** Creates new form InfoDataIF */
        public InfoDataIF() {
                initComponents();
        }

        /** This method is called from within the constructor to
         * initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoDataText = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(150, 150));
        setPreferredSize(new java.awt.Dimension(300, 300));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        infoDataText.setColumns(20);
        infoDataText.setRows(5);
        infoDataText.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                infoDataTextPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(infoDataText);

        jScrollPane2.setViewportView(jScrollPane1);

        getContentPane().add(jScrollPane2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void infoDataTextPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_infoDataTextPropertyChange
                this.data.setInfo(this.getInfo());
	}//GEN-LAST:event_infoDataTextPropertyChange

        private String getInfo() {
                return infoDataText.getText();
        }

        public void setData(Dataset data) {
                this.setTitle(data.getDatasetName());
                this.data = data;
                infoDataText.setText(data.getInfo());
        }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea infoDataText;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
