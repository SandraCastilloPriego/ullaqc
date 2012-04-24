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
import ullaqc.data.PeakListRow;
import ullaqc.data.DatasetType;
import ullaqc.data.impl.datasets.SimpleBasicDataset;
import ullaqc.data.impl.peaklists.SimplePeakListRowOther;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author scsandra
 */
public class LCMSParserMassLynx {

    private String datasetPath;
    private SimpleBasicDataset dataset;
    private float progress;

    public LCMSParserMassLynx(String datasetPath) {
        progress = 0.1f;
        this.datasetPath = datasetPath;
        this.dataset = new SimpleBasicDataset(this.getDatasetName());
        this.dataset.setType(DatasetType.BASIC);
        progress = 0.5f;
        fillData();
        progress = 1.0f;
    }

    public String getDatasetName() {
        Pattern pat = Pattern.compile("\\\\");
        Matcher matcher = pat.matcher(datasetPath);
        int index = 0;
        while (matcher.find()) {
            index = matcher.start();
        }
        String n = datasetPath.substring(index + 1, datasetPath.length() - 4);
        return n;
    }

    public float getProgress() {
        return progress;
    }

    public void fillData() {
        try {
            FileReader fr = new FileReader(new File(datasetPath));
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            String head = null;
            String[] header = null;
            String compound = "";
            PeakListRow lipid = null;
            int contRow = 0;
            int contLipids = 0;
            String date = null;
            while ((line = (br.readLine())) != null) {
                if (!line.isEmpty()) {
                    if (line.matches("^Printed.*")) {
                        date = line.substring(line.indexOf("Printed") + 7);
                        dataset.setDatasetName(dataset.getDatasetName());
                        dataset.setDate(date);
                    }
                    if (line.matches("^Compound.*|^Sample Name.*")) {
                        compound = line;
                        compound = compound.substring(compound.indexOf(":") + 1);
                        br.readLine();
                        head = br.readLine();
                        header = head.split("\t");
                        contRow = 0;
                        contLipids++;

                        setExperimentsName(header);

                        continue;
                    }
                    if (compound != null) {
                        if (contLipids == 1) {
                            lipid = new SimplePeakListRowOther();
                        } else if (contLipids > 1) {
                            lipid = (PeakListRow) this.dataset.getRow(contRow);
                        }
                        if (head != null && !head.isEmpty()) {
                            getData(lipid, line, header, compound);
                        }
                        if (contLipids == 1) {
                            this.dataset.addRow(lipid);
                        }

                    }
                    contRow++;
                }

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData(PeakListRow lipid, String line, String[] header, String compound) {
        try {
            String[] sdata = line.split("\t");

            for (int i = 0; i < sdata.length; i++) {
                try {
                    if (!header[i].isEmpty()) {
                        String name = header[i];
                        lipid.setPeak(name, sdata[i].toString());
                    }
                } catch (Exception e) {
                }

            }
        } catch (Exception exception) {
        }
    }

    public Dataset getDataset() {
        return this.dataset;
    }

    private void setExperimentsName(String[] header) {
        try {
            for (int i = 0; i < header.length; i++) {
                if (!header[i].isEmpty()) {
                    this.dataset.addColumnName(header[i]);
                }
            }

            this.dataset.addColumnName("Comments");
        } catch (Exception exception) {
        }
    }
}
