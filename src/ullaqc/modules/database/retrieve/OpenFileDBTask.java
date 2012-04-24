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
package ullaqc.modules.database.retrieve;

import java.sql.ResultSet;
import java.text.NumberFormat;
import ullaqc.data.Dataset;
import ullaqc.data.DatasetType;
import ullaqc.data.PeakListRow;
import ullaqc.data.impl.datasets.SimpleBasicDataset;
import ullaqc.data.impl.peaklists.SimplePeakListRowOther;
import ullaqc.taskcontrol.Task;
import ullaqc.taskcontrol.TaskStatus;
import ullaqc.database.Database;
import ullaqc.util.GUIUtils;

/**
 *
 * @author scsandra
 */
public class OpenFileDBTask implements Task {

    private String name;
    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private double progress;
    private Database db;

    public OpenFileDBTask(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public String getTaskDescription() {
        return "Opening DB File... ";
    }

    public double getFinishedPercentage() {
        return progress;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void cancel() {
        status = TaskStatus.CANCELED;
    }

    public void run() {
        try {
            this.db = new Database();
            this.openFile();
        } catch (Exception e) {
            status = TaskStatus.ERROR;
            e.printStackTrace();
            return;
        }
    }

    public void openFile() {
        status = TaskStatus.PROCESSING;
        try {
            if (status == TaskStatus.PROCESSING) {
                // Create dataset
                Dataset dataset = new SimpleBasicDataset(this.name);
                dataset.setType(DatasetType.DBBASIC);
                dataset.addColumnName("Dataset name");
                dataset.addColumnName("Date");
                dataset.addColumnName("RT");
                dataset.addColumnName("Trace");
                dataset.addColumnName("Height");
                dataset.addColumnName("Area");
                dataset.addColumnName("Height/Area");
                dataset.addColumnName("S/N");
                dataset.addColumnName("Detection Flags");
                dataset.addColumnName("Acq.Time");
                dataset.addColumnName("Noise");
                dataset.addColumnName("Comments");

                // Datasets
                ResultSet result = db.query("SELECT * FROM data WHERE MoleculeName = '" + name + "'");
                while (result.next()) {
                    PeakListRow row = new SimplePeakListRowOther();
                    ResultSet result2 = db.query("SELECT * FROM dataset WHERE ID = '" + result.getString("DatasetID") + "'");
                    if (result2.next()) {
                        row.setPeak("Date", result2.getString("Date"));
                        row.setPeak("Dataset name", result2.getString("FileName"));
                    }
                    row.setPeak("Trace", result.getString("Trace"));
                    row.setPeak("RT", result.getString("RT"));
                    row.setPeak("Height", result.getString("Height"));
                    row.setPeak("Area", result.getString("Area"));
                    row.setPeak("Height/Area", result.getString("HeightArea"));
                    row.setPeak("S/N", result.getString("SignalToNoise"));
                    row.setPeak("Detection Flags", result.getString("Flags"));
                    row.setPeak("Acq.Time", result.getString("AcqTime"));
                    row.setPeak("Noise", result.getString("Noise"));
                    if (result.getString("Comments") != null) {
                        row.setPeak("Comments", result.getString("Comments"));
                    }

                    dataset.addRow(row);
                }              
                dataset.sortRowsByDate();
                addStatistics(dataset);
                db.close();
                GUIUtils.showNewTable(dataset, true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        progress = 1f;
        status = TaskStatus.FINISHED;
    }

    public static Dataset getDataset(String datasetName) {
        try {
            Database db = new Database();
            // Create dataset
            Dataset dataset = new SimpleBasicDataset(datasetName);
            dataset.setType(DatasetType.DBBASIC);
            dataset.addColumnName("Dataset name");
            dataset.addColumnName("Date");
            dataset.addColumnName("RT");
            dataset.addColumnName("Trace");
            dataset.addColumnName("Height");
            dataset.addColumnName("Area");
            dataset.addColumnName("Height/Area");
            dataset.addColumnName("S/N");
            dataset.addColumnName("Detection Flags");
            dataset.addColumnName("Acq.Time");
            dataset.addColumnName("Noise");
            dataset.addColumnName("Comments");

            // Datasets
            ResultSet result = db.query("SELECT * FROM data WHERE MoleculeName = '" + datasetName + "'");
            while (result.next()) {
                PeakListRow row = new SimplePeakListRowOther();
                ResultSet result2 = db.query("SELECT * FROM dataset WHERE ID = '" + result.getString("DatasetID") + "'");
                if (result2.next()) {
                    row.setPeak("Date", result2.getString("Date"));
                    row.setPeak("Dataset name", result2.getString("FileName"));
                }
                row.setPeak("Trace", result.getString("Trace"));
                row.setPeak("RT", result.getString("RT"));
                row.setPeak("Height", result.getString("Height"));
                row.setPeak("Area", result.getString("Area"));
                row.setPeak("Height/Area", result.getString("HeightArea"));
                row.setPeak("S/N", result.getString("SignalToNoise"));
                row.setPeak("Detection Flags", result.getString("Flags"));
                row.setPeak("Acq.Time", result.getString("AcqTime"));
                row.setPeak("Noise", result.getString("Noise"));
                if (result.getString("Comments") != null) {
                    row.setPeak("Comments", result.getString("Comments"));
                }

                dataset.addRow(row);
            }            
            db.close();
            dataset.sortRowsByDate();
            return dataset;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void addStatistics(Dataset dataset) {
        //mean
        double RTMean = 0, HeightMean = 0, AreaMean = 0, HAMean = 0, SNMean = 0, NoiseMean = 0;
        for (PeakListRow row : dataset.getRows()) {
            RTMean += Double.parseDouble((String) row.getPeak("RT"));
            HeightMean += Double.parseDouble((String) row.getPeak("Height"));
            AreaMean += Double.parseDouble((String) row.getPeak("Area"));
            HAMean += Double.parseDouble((String) row.getPeak("Height/Area"));
            SNMean += Double.parseDouble((String) row.getPeak("S/N"));
            NoiseMean += Double.parseDouble((String) row.getPeak("Noise"));
        }

        RTMean /= dataset.getNumberRows();
        HeightMean /= dataset.getNumberRows();
        AreaMean /= dataset.getNumberRows();
        HAMean /= dataset.getNumberRows();
        SNMean /= dataset.getNumberRows();
        NoiseMean /= dataset.getNumberRows();
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);

        NumberFormat format2 = NumberFormat.getNumberInstance();
        format2.setMaximumFractionDigits(0);

        NumberFormat format3 = NumberFormat.getNumberInstance();
        format3.setMaximumFractionDigits(3);

        PeakListRow row = new SimplePeakListRowOther();

        row.setPeak("RT",  format.format(RTMean));
        row.setPeak("Height", format2.format(HeightMean));
        row.setPeak("Area", format3.format(AreaMean));
        row.setPeak("Height/Area", format3.format(HAMean));
        row.setPeak("S/N", format3.format(SNMean));
        row.setPeak("Noise", format3.format(NoiseMean));
        dataset.addRow(row);
    }
}
