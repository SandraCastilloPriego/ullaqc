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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.OverlayLayout;

/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 * 
 * Progress bar with a text label displaying % of completion
 */
public class LabeledProgressBar extends JPanel {

        private JLabel label;
        private JProgressBar progressBar;

        public LabeledProgressBar() {

                setLayout(new OverlayLayout(this));

                label = new JLabel();
                label.setAlignmentX(0.5f);
                label.setFont(label.getFont().deriveFont(11f));
                add(label);

                progressBar = new JProgressBar(0, 100);
                progressBar.setBorderPainted(false);
                add(progressBar);

        }

        public LabeledProgressBar(double value) {
                this();
                setValue(value);
        }

        public void setValue(double value) {
                int percent = (int) (value * 100);
                progressBar.setValue(percent);
                label.setText(percent + "%");
        }

        public void setValue(double value, String text) {
                int percent = (int) (value * 100);
                progressBar.setValue(percent);
                label.setText(text);
        }
}
