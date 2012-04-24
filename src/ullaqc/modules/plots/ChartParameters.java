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

import ullaqc.data.Parameter;
import ullaqc.data.ParameterType;
import ullaqc.data.impl.SimpleParameter;
import ullaqc.data.impl.SimpleParameterSet;
import ullaqc.database.Database;

public class ChartParameters extends SimpleParameterSet {

    static Object[] objectsMol = Database.getMolecules();
    static Object[] objects = {"RT", "Height", "Area", "Height/Area", "S/N", "Noise"};
    public static final Parameter dbMolecules = new SimpleParameter(
            ParameterType.MULTIPLE_SELECTION,
            "Molecules",
            "Select plots that you want to show",
            null, objectsMol);
    public static final Parameter charts = new SimpleParameter(
            ParameterType.MULTIPLE_SELECTION,
            "Parameters",
            "Select the parameters that you want to represent in the plot",
            null, objects);

    public ChartParameters() {
        super(new Parameter[]{dbMolecules, charts});
    }
}