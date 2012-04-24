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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import ullaqc.data.Dataset;
import ullaqc.data.ParameterSet;
import ullaqc.data.impl.SimpleParameterSet;
import ullaqc.desktop.Desktop;
import ullaqc.desktop.UllaQCMenu;
import ullaqc.main.UllaQCCore;
import ullaqc.main.UllaQCModule;
import ullaqc.taskcontrol.Task;
import ullaqc.taskcontrol.TaskListener;
import ullaqc.taskcontrol.TaskStatus;
import ullaqc.util.dialogs.ExitCode;
import ullaqc.util.dialogs.ParameterSetupDialog;

/**
 *
 * @author scsandra
 */
public class SaveOtherFile implements UllaQCModule, TaskListener, ActionListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Desktop desktop;
    private Dataset[] Datasets;
    private SimpleParameterSet parameters;

    public void initModule() {
        this.parameters = new SaveOtherParameters();
        this.desktop = UllaQCCore.getDesktop();
        desktop.addMenuItem(UllaQCMenu.FILE, "Save Files..",
                "Save a new set of files", KeyEvent.VK_O, this, null, null);
    }

    public void taskStarted(Task task) {
        logger.info("Running Save Dataset");
    }

    public void taskFinished(Task task) {
        if (task.getStatus() == TaskStatus.FINISHED) {
            logger.info("Finished Save Dataset" + ((SaveOtherFileTask) task).getTaskDescription());
        }

        if (task.getStatus() == TaskStatus.ERROR) {

            String msg = "Error while save Dataset on .. " + ((SaveOtherFileTask) task).getErrorMessage();
            logger.severe(msg);
            desktop.displayErrorMessage(msg);

        }
    }

    public ExitCode setupParameters() {
        try {
            ParameterSetupDialog dialog = new ParameterSetupDialog("Parameters", parameters);
            dialog.setVisible(true);
            return dialog.getExitCode();
        } catch (Exception exception) {
            return ExitCode.CANCEL;
        }
    }

    public ParameterSet getParameterSet() {
        return parameters;
    }

    public void setParameters(ParameterSet parameterValues) {
        parameters = (SimpleParameterSet) parameterValues;
    }

    @Override
    public String toString() {
        return "Save Dataset";
    }

    public Task[] runModule() {

        // prepare a new group of tasks
        Datasets = UllaQCCore.getDesktop().getSelectedDataFiles();

        String path = (String) parameters.getParameterValue(SaveOtherParameters.Otherfilename);
        Task tasks[] = new SaveOtherFileTask[Datasets.length];
        for (int i = 0; i < Datasets.length; i++) {
            String newpath = path;
            if (i > 0) {
                newpath = path.substring(0, path.length() - 4) + String.valueOf(i) + path.substring(path.length() - 4);
            }
            tasks[i] = new SaveOtherFileTask(Datasets[i], parameters, newpath);
        }

        UllaQCCore.getTaskController().addTasks(tasks);

        return tasks;

    }

    public void actionPerformed(ActionEvent arg0) {
        ExitCode exitCode = setupParameters();
        if (exitCode != ExitCode.OK) {
            return;
        }

        runModule();
    }
}
