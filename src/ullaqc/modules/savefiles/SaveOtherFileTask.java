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
package ullaqc.modules.savefiles;

import ullaqc.data.Dataset;
import ullaqc.data.impl.SimpleParameterSet;
import ullaqc.taskcontrol.Task;
import ullaqc.taskcontrol.TaskStatus;

/**
 *
 * @author scsandra
 */
public class SaveOtherFileTask implements Task {

    private Dataset dataset;
    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private String path;
    private WriteFile writer;
    private SimpleParameterSet parameters;

    public SaveOtherFileTask(Dataset dataset, SimpleParameterSet parameters, String path) {
        this.dataset = dataset;
        this.path = path;
        this.parameters = parameters;
        writer = new WriteFile();
    }

    public String getTaskDescription() {
        return "Saving Dataset... ";
    }

    public double getFinishedPercentage() {
        return 0.0f;
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
            status = TaskStatus.PROCESSING;
            if (parameters.getParameterValue(SaveOtherParameters.type).toString().matches(".*Excel.*")) {
                writer.WriteXLSFileBasicDataset(dataset, path);
            } else {
                writer.WriteCommaSeparatedBasicDataset(dataset, path);
            }

            status = TaskStatus.FINISHED;
        } catch (Exception e) {
            status = TaskStatus.ERROR;
        }
    }
}
