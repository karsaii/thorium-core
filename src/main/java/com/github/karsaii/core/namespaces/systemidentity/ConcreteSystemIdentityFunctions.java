package com.github.karsaii.core.namespaces.systemidentity;

import com.github.karsaii.core.constants.systemidentity.BasicSystemIdentityConstants;
import com.github.karsaii.core.extensions.namespaces.predicates.BasicPredicates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areNull;
import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface ConcreteSystemIdentityFunctions {
    static boolean isOSX() {
        return Objects.equals(BasicSystemIdentityConstants.OS_NAME, BasicSystemIdentityConstants.MAC_OSX_NAME);
    }

    static String getHostName() {
        var host = "";
        if (
            isOSX() &&
            areNull(BasicSystemIdentityConstants.PROPERTY_HOSTNAME, BasicSystemIdentityConstants.PROPERTY_COMPUTERNAME)
        ) {
            try {
                final var processBuilder = new ProcessBuilder("hostname");
                final var process = processBuilder.start();
                process.waitFor();

                if (BasicPredicates.isZero(process.exitValue())) {
                    try (
                        final var is = process.getInputStream();
                        final var isr = new InputStreamReader(is);
                        final var reader = new BufferedReader(isr)
                    ) {
                        host = reader.readLine();
                        if (isNotBlank(host)) {
                            return host;
                        }
                    }
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            } catch (IOException ignored) {
            }
        }
        host = isNotNull(BasicSystemIdentityConstants.PROPERTY_HOSTNAME) ? BasicSystemIdentityConstants.PROPERTY_HOSTNAME : BasicSystemIdentityConstants.PROPERTY_COMPUTERNAME;
        if (isBlank(host)) {
            try {
                host = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ignored) {
                host = BasicSystemIdentityConstants.UNKNOWN;
            }
        }

        return host;
    }

    static String getHostAddress() {
        var address = "";

        try(final var socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName(BasicSystemIdentityConstants.ROUTING_IP), BasicSystemIdentityConstants.ROUTING_PORT);
            address = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException ex) {
            try {
                address = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException exception) {
                address = BasicSystemIdentityConstants.UNKNOWN;
            }
        }

        return address;
    }

    static String getOSXHostAddress() {
        var address = "";
        try (final var socket = new Socket()) {
            socket.connect(
                new InetSocketAddress(BasicSystemIdentityConstants.INTERNET_URI, BasicSystemIdentityConstants.INTERNET_PORT)
            );
            address = socket.getLocalAddress().getHostAddress();
            if (isNotBlank(address)) {
                return address;
            }

            final  var addresses = NetworkInterface.getByName(BasicSystemIdentityConstants.ETHERNET_0).getInetAddresses();
            if (addresses.hasMoreElements()) {
                address = addresses.nextElement().getHostAddress();
                if (isNotBlank(address)) {
                    return address;
                }
            }
        } catch (IOException ignored) {
            address = getHostAddress();
        }

        return address;
    }
}
