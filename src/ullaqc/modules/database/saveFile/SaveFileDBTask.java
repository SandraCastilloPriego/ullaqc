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
package ullaqc.modules.database.saveFile;

import java.sql.ResultSet;
import java.util.Hashtable;
import ullaqc.data.Dataset;
import ullaqc.data.DatasetType;
import ullaqc.data.PeakListRow;
import ullaqc.database.Database;
import ullaqc.taskcontrol.Task;
import ullaqc.taskcontrol.TaskStatus;

/**
 *
 * @author scsandra
 */
public class SaveFileDBTask implements Task {

    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private double progress;
    private Database db;
    private Dataset dataset;

    public SaveFileDBTask(Dataset dataset) {
        db = new Database();
        this.dataset = dataset;
    }

    public String getTaskDescription() {
        return "Saving File into DB... ";
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
            this.openFile();
        } catch (Exception e) {
            status = TaskStatus.ERROR;
            errorMessage = e.toString();
            return;
        }
    }

    public void openFile() {
        status = TaskStatus.PROCESSING;
        try {
            if (status == TaskStatus.PROCESSING) {
              
                if (dataset.getType() == DatasetType.BASIC) {
                    Hashtable<String, String> table = new Hashtable<String, String>();
                    String datasetName = dataset.getDatasetName();
                    if (datasetName.contains("/")) {
                        datasetName = datasetName.substring(datasetName.lastIndexOf("/") + 1);
                    } else if (datasetName.contains("\\")) {
                        datasetName = datasetName.substring(datasetName.lastIndexOf("\\") + 1);
                    }

                    // Check whether this experiment is already in the database or not
                    ResultSet result = db.query("SELECT FileName FROM dataset WHERE Filename = '" + datasetName + "'");
                    if (!result.next()) {
                        // dataset table.
                        table.put("FileName", datasetName);
                        table.put("Date", dataset.getDate());
                        db.insert("dataset", table);

                        result = db.query("SELECT last_insert_rowid() FROM dataset ");
                        String index = String.valueOf(result.getInt(1));

                        // molecule table
                        for (PeakListRow row : dataset.getRows()) {
                            result = db.query("SELECT Name FROM molecules WHERE Name = '" + row.getPeak("Name") + "'");
                            if (!result.next()) {
                                table.clear();
                                table.put("Name", (String) row.getPeak("Name"));
                                db.insert("molecules", table);
                                table.clear();
                            }
                        }

                        // data table
                        for (PeakListRow row : dataset.getRows()) {
                            table.clear();
                            table.put("DatasetID", index);
                            table.put("MoleculeName", (String) row.getPeak("Name"));
                            table.put("RT", (String) row.getPeak("RT"));
                            table.put("Trace", (String) row.getPeak("Trace"));
                            table.put("Height", (String) row.getPeak("Height"));
                            table.put("Area", (String) row.getPeak("Area"));
                            table.put("HeightArea", (String) row.getPeak("Height/Area"));
                            table.put("SignalToNoise", (String) row.getPeak("S/N"));
                            table.put("Flags", (String) row.getPeak("Detection Flags"));
                            table.put("AcqTime", (String) row.getPeak("Acq.Time"));
                            table.put("Noise", (String) row.getPeak("Noise"));
                            if (row.getPeak("Comments") != null) {
                                table.put("Comments", (String) row.getPeak("Comments"));
                            }else{
                                table.put("Comments", " ");
                            }
                            db.insert("data", table);
                        }
                    }
                    db.close();
                } else if (dataset.getType() == DatasetType.DBBASIC) {
                    Hashtable<String, String> table = new Hashtable<String, String>();
                    for (PeakListRow row : dataset.getRows()) {
                        ResultSet result = db.query("SELECT ID FROM dataset WHERE Filename = '" + row.getPeak("Dataset name") + "'");
                        if (result.next()) {
                            table.put("RT", "'" + (String) row.getPeak("RT") + "'");
                            table.put("Trace", "'" + (String) row.getPeak("Trace") + "'");
                            table.put("Height", "'" + (String) row.getPeak("Height") + "'");
                            table.put("Area", "'" + (String) row.getPeak("Area") + "'");
                            table.put("HeightArea", "'" + (String) row.getPeak("Height/Area") + "'");
                            table.put("SignalToNoise", "'" + (String) row.getPeak("S/N") + "'");
                            table.put("Flags", "'" + (String) row.getPeak("Detection Flags") + "'");
                            table.put("AcqTime", "'" + (String) row.getPeak("Acq.Time") + "'");
                            table.put("Noise", "'" + (String) row.getPeak("Noise") + "'");

                            if (row.getPeak("Comments") != null) {
                                table.put("Comments", "'" + (String) row.getPeak("Comments") + "'");
                            }

                            String name = this.dataset.getDatasetName();
                            if (name.contains("-Version:")) {
                                name = dataset.getDatasetName().substring(0, dataset.getDatasetName().indexOf("-Version:"));
                            }
                            db.update("data", table, "DatasetID = '" + result.getInt(1) + "' AND MoleculeName = '" + name + "'");
                        }
                    }
                    db.close();
                }
              // db.query("DELETE FROM dataset");
               //  db.query("DELETE FROM data");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        progress = 1f;
        status = TaskStatus.FINISHED;
    }
}
