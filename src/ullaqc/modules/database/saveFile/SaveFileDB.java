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

import ullaqc.data.ParameterSet;
import ullaqc.desktop.Desktop;
import ullaqc.desktop.UllaQCMenu;
import ullaqc.main.UllaQCCore;
import ullaqc.main.UllaQCModule;
import ullaqc.taskcontrol.Task;
import ullaqc.taskcontrol.TaskListener;
import ullaqc.taskcontrol.TaskStatus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import ullaqc.data.Dataset;

/**
 *
 * @author scsandra
 */
public class SaveFileDB implements UllaQCModule, TaskListener, ActionListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Desktop desktop;

    public void initModule() {

        this.desktop = UllaQCCore.getDesktop();
        desktop.addMenuItem(UllaQCMenu.DATABASE, "Save File..",
                "Save file into the database", KeyEvent.VK_S, this, null, null);

    }

    public void taskStarted(Task task) {
        logger.info("Running Save File into DB");
    }

    public void taskFinished(Task task) {
        if (task.getStatus() == TaskStatus.FINISHED) {
            logger.info("Finished Save File into DB on " + ((SaveFileDBTask) task).getTaskDescription());
        }

        if (task.getStatus() == TaskStatus.ERROR) {

            String msg = "Error while Save File into DB on .. " + ((SaveFileDBTask) task).getErrorMessage();
            logger.severe(msg);
            desktop.displayErrorMessage(msg);

        }
    }

    public void actionPerformed(ActionEvent e) {
        runModule();
    }

    public ParameterSet getParameterSet() {
        return null;
    }

    public void setParameters(ParameterSet parameterValues) {
    }

    public String toString() {
        return "Save File into DB";
    }

    public Task[] runModule() {

        // prepare a new group of tasks
        Dataset[] datasets = UllaQCCore.getDesktop().getSelectedDataFiles();

        Task tasks[] = new SaveFileDBTask[datasets.length];
        for (int i = 0; i < datasets.length; i++) {
            tasks[i] = new SaveFileDBTask(datasets[i]);
        }

        UllaQCCore.getTaskController().addTasks(tasks);

        return tasks;


    }
}
