/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is Drombler.org. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2015 Drombler.org. All Rights Reserved.
 *
 * Contributor(s): .
 */
package org.drombler.acp.startup.main.impl.singleinstance;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.drombler.acp.startup.main.DromblerACPConfiguration;
import org.drombler.acp.startup.main.impl.BootServiceStarter;

/**
 *
 * @author puce
 */
public class SingleInstanceStarter implements BootServiceStarter {

    private static final String SINGLE_INSTANCE_PROPERTIES_FILE_NAME = "singleInstance.properties";
    private static final String PORT_PROPERTY_NAME = "port";

    private final DromblerACPConfiguration configuration;
    private ServerSocket serverSocket;

    public SingleInstanceStarter(DromblerACPConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getName() {
        return "Single Instance Application Starter";
    }

    @Override
    public boolean isActive() {
        return configuration.getApplicationConfig().isSingleInstanceApplication();
    }

    private static ApplicationInstanceListener subListener;

    /**
     * Randomly chosen, but static, high socket number
     */
//    public static final int SINGLE_INSTANCE_NETWORK_SOCKET = 44331;
    /**
     * Must end with newline
     */
    public static final String SINGLE_INSTANCE_SHARED_KEY = "$$NewInstance$$\n";

//    /**
//     * Registers this instance of the application.
//     *
//     * @return true if first instance, false if not.
//     */
//    public boolean registerInstance() {
//        // try to open network socket
//        // if success, listen to socket for new instance message, return true
//        // if unable to open, connect to existing and send new instance message, return false
//        try {
//            final ServerSocket serverSocket = new ServerSocket(SINGLE_INSTANCE_NETWORK_SOCKET, 10, getInetAddress());
//            System.out.println("Listening for application instances on socket " + SINGLE_INSTANCE_NETWORK_SOCKET);
//            Thread instanceListenerThread = new Thread(() -> listenForOtherInstances(serverSocket));
//            instanceListenerThread.start();
//            // listen
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//            // allow app to run on network error
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//        return true;
//    }
    @Override
    public boolean init() throws IOException {
        boolean initialized;
        Path userConfigDir = configuration.getUserDirPath().resolve(DromblerACPConfiguration.CONFIG_DIRECTORY);
        if (!Files.exists(userConfigDir)) {
            Files.createDirectories(userConfigDir);
        }
        Path singleInstancePropertiesPath = userConfigDir.resolve(SINGLE_INSTANCE_PROPERTIES_FILE_NAME);
        if (Files.exists(singleInstancePropertiesPath)) {
            Properties singleInstanceProperties = loadSingleInstanceProperties(singleInstancePropertiesPath);
            int port = Integer.parseInt(singleInstanceProperties.getProperty(PORT_PROPERTY_NAME));
            try {
                tryInitServerSocket(port);
                initialized = true;
            } catch (IOException ex) {
                notifySingleInstance(port);
                initialized = false;
            }
        } else {
            Files.createFile(singleInstancePropertiesPath);
            int port = initialInitServerSocket(configuration.getApplicationConfig().getDefaultSingleInstancePort());
            storeSingleInstanceProperties(singleInstancePropertiesPath, port);
            initialized = true;
        }
        return initialized;
    }

    private int initialInitServerSocket(int defaultPort) {
        int port = defaultPort;
        boolean retry = true;
        while (retry) {
            try {
                tryInitServerSocket(port);
                retry = false;
            } catch (IOException ex) {
                port++;
            }
        }
        return port;
    }

    private Properties loadSingleInstanceProperties(Path singleInstancePropertiesPath) throws IOException {
        Properties singleInstanceProperties = new Properties();
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(singleInstancePropertiesPath))) {
            singleInstanceProperties.load(bis);
        }
        return singleInstanceProperties;
    }

    private void storeSingleInstanceProperties(Path singleInstancePropertiesPath, int port) throws IOException {
        Properties singleInstanceProperties = new Properties();
        singleInstanceProperties.setProperty(PORT_PROPERTY_NAME, Integer.toString(port));
        try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(singleInstancePropertiesPath))) {
            singleInstanceProperties.store(bos, "");
        }
    }

    private void tryInitServerSocket(int port) throws IOException {
        this.serverSocket = new ServerSocket(port, 10, getInetAddress());
    }

    private static InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByName(null);
    }

    @Override
    public void startAndWait() {
        listenForOtherInstances();
    }

    private void listenForOtherInstances() {
        boolean socketClosed = false;
        while (!socketClosed) {
            if (serverSocket.isClosed()) {
                socketClosed = true;
            } else {
                try (Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()))) {
                    String message = in.readLine();
                    if (SINGLE_INSTANCE_SHARED_KEY.trim().equals(message.trim())) {
                        System.out.println("Shared key matched - new application instance found");
                        fireNewInstance();
                    }

                } catch (IOException e) {
                    socketClosed = true;
                }
            }
        }
    }

    public void notifySingleInstance(int port) {
        System.out.println("Port is already taken.  Notifying first instance.");
        try (Socket socket = new Socket(getInetAddress(), port);
                OutputStream out = socket.getOutputStream()) {
            out.write(SINGLE_INSTANCE_SHARED_KEY.getBytes());
            System.out.println("Successfully notified first instance.");
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            System.err.println("Error connecting to local port for single instance notification");
            e1.printStackTrace();
        }
    }

    public static void setApplicationInstanceListener(ApplicationInstanceListener listener) {
        subListener = listener;
    }

    private static void fireNewInstance() {
        if (subListener != null) {
            subListener.newInstanceCreated();
        }
    }

    @Override
    public void stop() throws Exception {
        serverSocket.close();
    }

}

//public class MyApplication {
//    public static void main(String[] args) {
//       if (!ApplicationInstanceManager.registerInstance()) {
//                    // instance already running.
//                    System.out.println("Another instance of this application is already running.  Exiting.");
//                    System.exit(0);
//       }
//       ApplicationInstanceManager.setApplicationInstanceListener(new ApplicationInstanceListener() {
//          public void newInstanceCreated() {
//             System.out.println("New instance detected...");
//             // this is where your handler code goes...
//          }
//       });
//    }
//}

