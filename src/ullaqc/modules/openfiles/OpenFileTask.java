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
package ullaqc.modules.openfiles;

import ullaqc.data.Dataset;
import ullaqc.data.impl.datasets.SimpleBasicDataset;
import ullaqc.taskcontrol.Task;
import ullaqc.taskcontrol.TaskStatus;
import ullaqc.util.GUIUtils;

/**
 *
 * @author scsandra
 */
public class OpenFileTask implements Task {

        private String fileDir;
        private TaskStatus status = TaskStatus.WAITING;
        private String errorMessage;
        private double progress;

        public OpenFileTask(String fileDir) {
                if (fileDir != null) {
                        this.fileDir = fileDir;
                }
        }

        public String getTaskDescription() {
                return "Opening File... ";
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
                                LCMSParserMassLynx parser = new LCMSParserMassLynx(fileDir);
                                progress = parser.getProgress();
                                Dataset dataset = (SimpleBasicDataset) parser.getDataset();
                                progress = parser.getProgress();
                                GUIUtils.showNewTable(dataset, true);
                        }
                } catch (Exception ex) {
                }

                progress = 1f;
                status = TaskStatus.FINISHED;
        }
}
