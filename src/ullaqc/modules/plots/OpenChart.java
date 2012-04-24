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
package ullaqc.modules.plots;

import ullaqc.data.ParameterSet;
import ullaqc.desktop.Desktop;
import ullaqc.desktop.UllaQCMenu;
import ullaqc.main.UllaQCCore;
import ullaqc.main.UllaQCModule;
import ullaqc.taskcontrol.Task;
import ullaqc.taskcontrol.TaskListener;
import ullaqc.taskcontrol.TaskStatus;
import ullaqc.util.dialogs.ExitCode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import ullaqc.data.impl.SimpleParameterSet;
import ullaqc.util.dialogs.ParameterSetupDialog;

/**
 *
 * @author scsandra
 */
public class OpenChart implements UllaQCModule, TaskListener, ActionListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Desktop desktop;
    private SimpleParameterSet parameters;

    public void initModule() {
        this.parameters = new ChartParameters();
        this.desktop = UllaQCCore.getDesktop();
        desktop.addMenuItem(UllaQCMenu.GRAPHICS, "Create chart..",
                "Create a plot from the data", KeyEvent.VK_O, this, null, null);

    }

    public void taskStarted(Task task) {
        logger.info("Running Create chart");
    }

    public void taskFinished(Task task) {
        if (task.getStatus() == TaskStatus.FINISHED) {
            logger.info("Finished Create chart on " + ((OpenChartTask) task).getTaskDescription());
        }

        if (task.getStatus() == TaskStatus.ERROR) {

            String msg = "Error while Create chart on .. " + ((OpenChartTask) task).getErrorMessage();
            logger.severe(msg);
            desktop.displayErrorMessage(msg);

        }
    }

    public void actionPerformed(ActionEvent e) {
        ExitCode exitCode = setupParameters();
        if (exitCode != ExitCode.OK) {
            return;
        }

        runModule();
    }

    public ExitCode setupParameters() {
        ParameterSetupDialog dialog = new ParameterSetupDialog("LCMS Table View parameters", parameters);
        dialog.setVisible(true);
        return dialog.getExitCode();
    }

    public ParameterSet getParameterSet() {
        return parameters;
    }

    public void setParameters(ParameterSet parameterValues) {
        this.parameters = (SimpleParameterSet) parameterValues;
    }

    public String toString() {
        return "Create charts";
    }

    public Task[] runModule() {

        // prepare a new group of tasks
        Object[] names = (Object[]) parameters.getParameterValue(ChartParameters.dbMolecules);

        // prepare a new group of tasks
        if (names.length > 0) {
            Task tasks[] = new OpenChartTask[names.length];
            for (int i = 0; i < names.length; i++) {
                tasks[i] = new OpenChartTask(parameters, (String) names[i]);
            }
            UllaQCCore.getTaskController().addTasks(tasks);
            return tasks;

        } else {
            return null;
        }



    }
}
