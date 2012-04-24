package ullaqc.modules.plots;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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
public class Chart extends ChartPanel {

    XYSeriesCollection dataset;
    JFreeChart chart;

    public Chart() {
        super(null, true);
        dataset = new XYSeriesCollection();
        chart = ChartFactory.createXYLineChart(
				"",
				null,
				null,
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false);


        chart.setBackgroundPaint(Color.white);
        setChart(chart);


        //         Generate the graph

        // try {
        // ChartUtilities.saveChartAsJPEG(new File("C:chart.jpg"), chart, 500, 300);
        // } catch (IOException e) {
        //     System.err.println("Problem occurred creating chart.");
        // }
    }

    public void addSeries(List<Double> info, String name) {
        XYSeries series = new XYSeries(name);
        int cont = 1;
        for (Double d : info) {
            series.add(cont, d);
            cont++;
        }

        dataset.addSeries(series);
    }



    public void createChart() {
        final XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setBaseLinesVisible(true);
        renderer.setBaseShapesVisible(true);
        plot.setRenderer(renderer);
        chart.setBackgroundPaint(Color.white);
        plot.setOutlinePaint(Color.black);
    }
}
