/*
 * Copyright 2007-2012 VTT Biotechnology
 * This file is part of Guineu.
 *
 * Guineu is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Guineu is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Guineu; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 */
package ullaqc.modules.plots;

import java.util.List;
import org.jfree.chart.labels.XYZToolTipGenerator;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

public class PlotToolTipGenerator implements XYZToolTipGenerator {
        List<String> info;
        PlotToolTipGenerator(List<String> info) {
                this.info = info;
        }

        public String generateToolTip(XYZDataset dataset, int series, int item) {
                if (dataset instanceof XYZDataset) {
                        return null;
                } else {
                        return null;
                }
        }

        public String generateToolTip(XYDataset dataset, int series, int item) {
                if (dataset instanceof XYDataset) {
                        return this.info.get(item);
                } else {
                        return null;
                }
        }
}
