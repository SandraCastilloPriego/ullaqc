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
package ullaqc.desktop.impl;

import ullaqc.data.Dataset;

import ullaqc.desktop.Desktop;
import ullaqc.main.UllaQCCore;
import ullaqc.util.GUIUtils;
import ullaqc.util.components.DragOrderedJList;
import ullaqc.util.dialogs.ExitCode;
import ullaqc.util.dialogs.ParameterSetupDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class implements a selector of data sets
 *
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */
public class ItemSelector extends JPanel implements ActionListener,
        MouseListener, ListSelectionListener {

    public static final String DATA_FILES_LABEL = "Dataset Files";
    private DragOrderedJList DatasetFiles;
    private List<Dataset> DatasetFilesModel = new ArrayList<Dataset>();
    private DefaultListModel DatasetNamesModel = new DefaultListModel();
    private JPopupMenu dataFilePopupMenu;
    private int copies = 0;
    private NameChangeParameter parameterName;

    /**
     * Constructor
     */
    public ItemSelector(Desktop desktop) {


        // Create panel for raw data objects
        JPanel rawDataPanel = new JPanel();
        JLabel rawDataTitle = new JLabel(DATA_FILES_LABEL);

        DatasetFiles = new DragOrderedJList(DatasetNamesModel);
        DatasetFiles.setCellRenderer(new ItemSelectorListRenderer());
        DatasetFiles.addMouseListener(this);
        DatasetFiles.addListSelectionListener(this);
        JScrollPane rawDataScroll = new JScrollPane(DatasetFiles);

        rawDataPanel.setLayout(new BorderLayout());
        rawDataPanel.add(rawDataTitle, BorderLayout.NORTH);
        rawDataPanel.add(rawDataScroll, BorderLayout.CENTER);
        rawDataPanel.setMinimumSize(new Dimension(150, 10));



        // Add panels to a split and put split on the main panel
        setPreferredSize(new Dimension(200, 10));
        setLayout(new BorderLayout());
        add(rawDataPanel, BorderLayout.CENTER);

        dataFilePopupMenu = new JPopupMenu();
        GUIUtils.addMenuItem(dataFilePopupMenu, "Show Dataset", this, "SHOW_DATASET");
        GUIUtils.addMenuItem(dataFilePopupMenu, "Remove", this, "REMOVE_FILE");

        this.parameterName = new NameChangeParameter();

    }

    void addSelectionListener(ListSelectionListener listener) {
        DatasetFiles.addListSelectionListener(listener);
    }

    public ExitCode setupParameters() {
        try {
            ParameterSetupDialog nameDialog = new ParameterSetupDialog("Change Name", parameterName);
            nameDialog.setVisible(true);
            return nameDialog.getExitCode();
        } catch (Exception exception) {
            return ExitCode.CANCEL;
        }
    }

    public void setupInfoDialog(Dataset data) {
        try {
            InfoDataIF dialog = new InfoDataIF();
            dialog.setData(data);
            UllaQCCore.getDesktop().getDesktopPane().add(dialog);
            UllaQCCore.getDesktop().getDesktopPane().validate();
            dialog.setVisible(true);
        } catch (Exception exception) {
        }
    }

    // Implementation of action listener interface
    public void actionPerformed(ActionEvent e) {
        Runtime.getRuntime().freeMemory();
        String command = e.getActionCommand();
        if (command.equals("REMOVE_FILE")) {
            removeData();
        }

        if (command.equals("SHOW_DATASET")) {
            showData();
        }

    }

    private void showData() {
        Dataset[] selectedFiles = getSelectedDatasets();
        for (Dataset file : selectedFiles) {
            if (file != null) {
                GUIUtils.showNewTable(file, false);
            }
        }
    }

    private void removeData() {
        Dataset[] selectedFiles = getSelectedDatasets();

        for (Dataset file : selectedFiles) {
            if (file != null) {
                DatasetFilesModel.remove(file);
                DatasetNamesModel.removeElement(file.getDatasetName());
            }
        }
    }

    public void removeData(Dataset file) {
        if (file != null) {
            DatasetFilesModel.remove(file);
            DatasetNamesModel.removeElement(file.getDatasetName());
        }

    }

    /**
     * Returns selected raw data objects in an array
     */
    public Dataset[] getSelectedDatasets() {

        Object o[] = DatasetFiles.getSelectedValues();

        Dataset res[] = new Dataset[o.length];

        for (int i = 0; i < o.length; i++) {
            for (Dataset dataset : DatasetFilesModel) {
                if (dataset.getDatasetName().compareTo((String) o[i]) == 0) {
                    res[i] = dataset;
                }
            }
        }

        return res;

    }

    public void mouseClicked(MouseEvent e) {

        if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1)) {
            showData();
        }

    }

    public void mouseEntered(MouseEvent e) {
        // ignore
        }

    public void mouseExited(MouseEvent e) {
        // ignore
        }

    public void mousePressed(MouseEvent e) {

        if (e.isPopupTrigger()) {
            if (e.getSource() == DatasetFiles) {
                dataFilePopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }

    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            if (e.getSource() == DatasetFiles) {
                dataFilePopupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    public void valueChanged(ListSelectionEvent event) {

        Object src = event.getSource();

        // Update the highlighting of peak list list in case raw data list
        // selection has changed and vice versa.
        if (src == DatasetFiles) {
            DatasetFiles.revalidate();
        }

    }

    public void addNewFile(Dataset dataset) {
        for (int i = 0; i < DatasetNamesModel.getSize(); i++) {
            if (dataset.getDatasetName().matches(DatasetNamesModel.getElementAt(i).toString())) {
                dataset.setDatasetName(dataset.getDatasetName() + "-Version:" + ++copies);
            }
        }
        this.DatasetFilesModel.add(dataset);
        DatasetNamesModel.addElement(dataset.getDatasetName());
        this.DatasetFiles.revalidate();
        this.revalidate();  
    }
}
