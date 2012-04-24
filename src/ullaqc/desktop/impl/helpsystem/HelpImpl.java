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

package ullaqc.desktop.impl.helpsystem;

import java.io.File;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/ *
 */
public class HelpImpl {

    private UllaQCHelpSet hs;

    public HelpImpl() {
        try {

            // Construct help
            boolean test = false;

            File file = new File(System.getProperty("user.dir")
                    + File.separator + "UllaQC.jar");

            if (!file.exists()) {
                file = new File(System.getProperty("user.dir") + File.separator
                        + "dist" + File.separator + "UllaQC.jar");
                test = true;
            }

            if (!file.exists()) {
                return;
            }

            UllaQCHelpMap helpMap = new UllaQCHelpMap(test);

            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry entry = e.nextElement();
                String name = entry.getName();
                if ((name.endsWith("htm")) || (name.endsWith("html"))) {
                    helpMap.setTarget(name);
                }
            }

            helpMap.setTargetImage("topic.png");

            hs = new UllaQCHelpSet();
            hs.setLocalMap(helpMap);

            UllaQCTOCView myTOC = new UllaQCTOCView(hs, "TOC",
                    "Table Of Contents", helpMap, file);

            hs.setTitle("UllaQC");
            hs.addTOCView(myTOC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UllaQCHelpSet getHelpSet() {
        return hs;
    }

}
