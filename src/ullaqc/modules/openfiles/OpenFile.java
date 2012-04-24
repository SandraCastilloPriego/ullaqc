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

import ullaqc.data.ParameterSet;
import ullaqc.desktop.Desktop;
import ullaqc.desktop.UllaQCMenu;
import ullaqc.desktop.impl.DesktopParameters;
import ullaqc.main.UllaQCCore;
import ullaqc.main.UllaQCModule;
import ullaqc.taskcontrol.Task;
import ullaqc.taskcontrol.TaskListener;
import ullaqc.taskcontrol.TaskStatus;
import ullaqc.util.dialogs.ExitCode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.logging.Logger;

/**
 *
 * @author scsandra
 */
public class OpenFile implements UllaQCModule, TaskListener, ActionListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Desktop desktop;
    private File[] FilePaths;

    public void initModule() {

        this.desktop = UllaQCCore.getDesktop();
        desktop.addMenuItem(UllaQCMenu.FILE, "Open Files..",
                "Open a new set of files", KeyEvent.VK_O, this, null, "icons/masslynx.png");

    }

    public void taskStarted(Task task) {
        logger.info("Running Open Files");
    }

    public void taskFinished(Task task) {
        if (task.getStatus() == TaskStatus.FINISHED) {
            logger.info("Finished Open File on " + ((OpenFileTask) task).getTaskDescription());
        }

        if (task.getStatus() == TaskStatus.ERROR) {

            String msg = "Error while Open File on .. " + ((OpenFileTask) task).getErrorMessage();
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
        DesktopParameters deskParameters = (DesktopParameters) UllaQCCore.getDesktop().getParameterSet();
        String lastPath = deskParameters.getLastOpenProjectPath();
        if (lastPath == null) {
            lastPath = "";
        }
        File lastFilePath = new File(lastPath);
        DatasetOpenDialog dialog = new DatasetOpenDialog(lastFilePath);
        dialog.setVisible(true);
        try {
            this.FilePaths = dialog.getCurrentDirectory();
        } catch (Exception e) {
        }
        return dialog.getExitCode();
    }

    public ParameterSet getParameterSet() {
        return null;
    }

    public void setParameters(ParameterSet parameterValues) {
    }

    public String toString() {
        return "Open Files";
    }

    public Task[] runModule() {

        // prepare a new group of tasks
        if (FilePaths != null) {
            Task tasks[] = new OpenFileTask[FilePaths.length];
            for (int i = 0; i < FilePaths.length; i++) {
                tasks[i] = new OpenFileTask(FilePaths[i].getAbsolutePath());
            }
            UllaQCCore.getTaskController().addTasks(tasks);

            return tasks;

        } else {
            return null;
        }

    }
}
