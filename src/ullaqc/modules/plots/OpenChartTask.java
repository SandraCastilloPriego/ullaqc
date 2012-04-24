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

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JInternalFrame;
import ullaqc.data.Dataset;
import ullaqc.data.PeakListRow;
import ullaqc.data.impl.SimpleParameterSet;
import ullaqc.main.UllaQCCore;
import ullaqc.modules.database.retrieve.OpenFileDBTask;
import ullaqc.taskcontrol.Task;
import ullaqc.taskcontrol.TaskStatus;

/**
 *
 * @author scsandra
 */
public class OpenChartTask implements Task {

    private TaskStatus status = TaskStatus.WAITING;
    private String errorMessage;
    private double progress;
    private Chart chart;
    private Object[] param;
    private String name;
    private Dataset dataset;

    public OpenChartTask(SimpleParameterSet parameters, String name) {
        chart = new Chart();
        param = (Object[]) parameters.getParameterValue(ChartParameters.charts);
        this.name = name;
        dataset = OpenFileDBTask.getDataset(name);
    }

    public String getTaskDescription() {
        return "Creating chart... ";
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
        status = TaskStatus.PROCESSING;
        for (Object p : param) {
            List<Double> list = this.getList((String) p);            
            chart.addSeries(list, (String) p);
        }
        chart.createChart();

        JInternalFrame frame = new JInternalFrame(name, true, true, true, true);
        frame.setSize(new Dimension(700, 500));

        frame.add(chart);

        chart.setVisible(true);
        frame.setVisible(true);

        UllaQCCore.getDesktop().addInternalFrame(frame);

        status = TaskStatus.FINISHED;
    }

    private List<Double> getList(String p) {
        try {
            List<Double> list = new ArrayList<Double>();
            for (PeakListRow row : dataset.getRows()) {
               // if(row.getVar(""))
                if (p.equals("RT")) {
                    list.add(Double.valueOf((String) row.getPeak("RT")));
                } else if (p.equals("Height")) {
                    list.add(Double.valueOf((String) row.getPeak("Height")));
                } else if (p.equals("Area")) {
                    list.add(Double.valueOf((String) row.getPeak("Area")));
                } else if (p.equals("Height/Area")) {
                    list.add(Double.valueOf((String) row.getPeak("Height/Area")));
                } else if (p.equals("S/N")) {
                    list.add(Double.valueOf((String) row.getPeak("S/N")));
                } else if (p.equals("Noise")) {
                    list.add(Double.valueOf((String) row.getPeak("Noise")));
                }
            }
            return list;
        } catch (Exception e) {
            status = TaskStatus.ERROR;
            errorMessage = e.toString();
            return null;
        }

    }
}
