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
package ullaqc.util.components;

import ullaqc.data.Dataset;
import ullaqc.data.PeakListRow;
import ullaqc.data.datamodels.OtherDataModel;
import ullaqc.data.DatasetType;
import ullaqc.data.impl.datasets.SimpleBasicDataset;
import ullaqc.data.impl.peaklists.SimplePeakListRowOther;
import ullaqc.util.Tables.DataTableModel;

/**
 *
 * @author scsandra
 */
public class FileUtils {

    public static PeakListRow getPeakListRow(DatasetType type) {
        return new SimplePeakListRowOther();
    }

    public static Dataset getDataset(Dataset dataset, String Name) {
        Dataset newDataset = new SimpleBasicDataset(Name + dataset.getDatasetName());
        newDataset.setType(dataset.getType());
        return newDataset;
    }

    public static DataTableModel getTableModel(Dataset dataset) {
        DataTableModel model = new OtherDataModel(dataset);
        return model;
    }
}
