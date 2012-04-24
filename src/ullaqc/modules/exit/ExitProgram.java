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

package ullaqc.modules.exit;

import ullaqc.data.ParameterSet;
import ullaqc.desktop.Desktop;
import ullaqc.desktop.UllaQCMenu;
import ullaqc.main.UllaQCCore;
import ullaqc.main.UllaQCModule;
import ullaqc.taskcontrol.impl.TaskControllerImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 *
 * @author scsandra
 */
public class ExitProgram implements UllaQCModule, ActionListener {
    private Desktop desktop;
    
        
    public void initModule() {

        this.desktop = UllaQCCore.getDesktop(); 
        desktop.addMenuSeparator(UllaQCMenu.FILE);
        desktop.addMenuItem(UllaQCMenu.FILE, "Exit..",
                "TODO write description", KeyEvent.VK_E, this, null, null);
    }

    public ParameterSet getParameterSet() {
        return null;
    }

    public void setParameters(ParameterSet parameterValues) {
        
    }

    public void actionPerformed(ActionEvent e) {      
        UllaQCCore.exitUllaQC();
    }
    
}
