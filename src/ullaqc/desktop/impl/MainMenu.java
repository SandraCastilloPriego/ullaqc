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
package ullaqc.desktop.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import ca.guydavis.swing.desktop.CascadingWindowPositioner;
import ca.guydavis.swing.desktop.JWindowsMenu;
import ullaqc.desktop.UllaQCMenu;
import ullaqc.main.UllaQCCore;
import javax.swing.ImageIcon;

/**
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 */
class MainMenu extends JMenuBar implements ActionListener {

    private JMenu fileMenu, databaseMenu, graphicsMenu;
    private JWindowsMenu windowsMenu;
    private JMenuItem hlpAbout;

    MainMenu() {

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        add(fileMenu);

        databaseMenu = new JMenu("Database");
        databaseMenu.setMnemonic(KeyEvent.VK_D);
        add(databaseMenu);

        /*filterMenu = new JMenu("Filter");
        filterMenu.setMnemonic(KeyEvent.VK_L);
        this.add(filterMenu);
*/
        graphicsMenu = new JMenu("Graphics");
        graphicsMenu.setMnemonic(KeyEvent.VK_A);
        this.add(graphicsMenu);


        JDesktopPane mainDesktopPane = ((MainWindow) UllaQCCore.getDesktop()).getDesktopPane();
        windowsMenu = new JWindowsMenu(mainDesktopPane);
        CascadingWindowPositioner positioner = new CascadingWindowPositioner(
                mainDesktopPane);
        windowsMenu.setWindowPositioner(positioner);
        windowsMenu.setMnemonic(KeyEvent.VK_W);
        this.add(windowsMenu);
    }

    public void addMenuItem(UllaQCMenu parentMenu, JMenuItem newItem) {
        switch (parentMenu) {
            case FILE:
                fileMenu.add(newItem);
                break;
            case DATABASE:
                databaseMenu.add(newItem);
                break;
           /* case FILTER:
                filterMenu.add(newItem);
                break;*/
            case GRAPHICS:
                graphicsMenu.add(newItem);
                break;

        }
    }

    public JMenuItem addMenuItem(UllaQCMenu parentMenu, String text,
            String toolTip, int mnemonic,
            ActionListener listener, String actionCommand, String icon) {

        JMenuItem newItem = new JMenuItem(text);
        if (listener != null) {
            newItem.addActionListener(listener);
        }
        if (actionCommand != null) {
            newItem.setActionCommand(actionCommand);
        }
        if (toolTip != null) {
            newItem.setToolTipText(toolTip);
        }
        if (mnemonic > 0) {
            newItem.setMnemonic(mnemonic);
        }

        if (icon != null) {
            newItem.setIcon(new ImageIcon(icon));
        }
        addMenuItem(parentMenu, newItem);
        return newItem;

    }

    public void addMenuSeparator(UllaQCMenu parentMenu) {
        switch (parentMenu) {
            case FILE:
                fileMenu.addSeparator();
                break;
            case DATABASE:
                databaseMenu.addSeparator();
                break;
          /*  case FILTER:
                filterMenu.addSeparator();
                break;*/
            case GRAPHICS:
                graphicsMenu.addSeparator();
                break;
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == hlpAbout) {
            MainWindow mainWindow = (MainWindow) UllaQCCore.getDesktop();
            mainWindow.showAboutDialog();
        }
    }
}
