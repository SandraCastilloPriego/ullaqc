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
package ullaqc.main;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ullaqc.desktop.impl.MainWindow;
import ullaqc.desktop.impl.helpsystem.HelpImpl;
import ullaqc.taskcontrol.impl.TaskControllerImpl;

/**
 * Main client class
 *
 * @author Taken from MZmine2
 * http://mzmine.sourceforge.net/
 *
 */
public class UllaQCClient extends UllaQCCore implements Runnable {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Vector<UllaQCModule> moduleSet;
    // make UllaQCClient a singleton
    private static UllaQCClient client = new UllaQCClient();

    public UllaQCClient() {
    }

    public static UllaQCClient getInstance() {
        return client;
    }

    /**
     * Main method
     */
    public static void main(String args[]) {

        // create the GUI in the event-dispatching thread
        SwingUtilities.invokeLater(client);

    }

    /**
     * @see java.lang.Runnable#run()
     */
    @SuppressWarnings("unchecked")
    public void run() {

        // load configuration from XML
        Document configuration = null;
        MainWindow desktop = null;
        try {
            SAXReader reader = new SAXReader();
            configuration = reader.read(CONFIG_FILE);
            Element configRoot = configuration.getRootElement();

            // get the configured number of computation nodes
            int numberOfNodes;

            Element nodes = configRoot.element(NODES_ELEMENT_NAME);
            String numberOfNodesConfigEntry = nodes.attributeValue(LOCAL_ATTRIBUTE_NAME);
            if (numberOfNodesConfigEntry != null) {
                numberOfNodes = Integer.parseInt(numberOfNodesConfigEntry);
            } else {
                numberOfNodes = Runtime.getRuntime().availableProcessors();
            }

            logger.info("UllaQC starting with " + numberOfNodes + " computation nodes");

            logger.finer("Loading core classes");


            // create instances of core modules
            TaskControllerImpl taskControllerr = new TaskControllerImpl();
            //IOControllerImpl ioController=new IOControllerImpl();
            desktop = new MainWindow();
            help = new HelpImpl();

            // save static references to UllaQCCore
            UllaQCCore.taskController = taskControllerr;
            //GineuCore.ioController = ioController;
            UllaQCCore.desktop = desktop;

            logger.finer("Initializing core classes");
            desktop.initModule();
            taskControllerr.initModule();


            logger.finer("Loading modules");

            moduleSet = new Vector<UllaQCModule>();

            Iterator<Element> modIter = configRoot.element(MODULES_ELEMENT_NAME).
                    elementIterator(MODULE_ELEMENT_NAME);

            while (modIter.hasNext()) {
                Element moduleElement = modIter.next();
                String className = moduleElement.attributeValue(CLASS_ATTRIBUTE_NAME);
                loadModule(className);
            }

            UllaQCCore.initializedModules = moduleSet.toArray(new UllaQCModule[0]);

            // load module configuration
            loadConfiguration(CONFIG_FILE);



        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not parse configuration file " + CONFIG_FILE, e);
            System.exit(1);
        }

        // register the shutdown hook
        ShutDownHook shutDownHook = new ShutDownHook();
        Runtime.getRuntime().addShutdownHook(shutDownHook);

        // show the GUI
        logger.finest("Showing main window");
        desktop.setVisible(true);

        // show the welcome message
        desktop.setStatusBarText("Welcome to UllaQC!");

    }

    /*public ProjectManager getProjectManager() {
    return projectManager;
    }*/
    public UllaQCModule loadModule(String moduleClassName) {

        try {

            logger.finest("Loading module " + moduleClassName);

            // load the module class
            Class moduleClass = Class.forName(moduleClassName);

            // create instance
            UllaQCModule moduleInstance = (UllaQCModule) moduleClass.newInstance();

            // init module
            moduleInstance.initModule();

            // add to the module set
            moduleSet.add(moduleInstance);

            return moduleInstance;

        } catch (Exception e) {
            logger.log(Level.SEVERE,
                    "Could not load module " + moduleClassName, e);
            return null;
        }

    }

    /**
     * Shutdown hook - invoked on JRE shutdown. This method saves current
     * configuration to XML.
     *
     */
    private class ShutDownHook extends Thread {

        public void start() {
            saveConfiguration(CONFIG_FILE);
        }
    }
}
