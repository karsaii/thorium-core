package com.github.karsaii.core.constants;

import com.github.karsaii.core.namespaces.SystemIdentityFunctions;

public abstract class SystemIdentityConstants {
    public static final String HOST_NAME = SystemIdentityFunctions.getHostName();
    public static final String HOST_ADDRESS = SystemIdentityFunctions.isOSX() ? SystemIdentityFunctions.getOSXHostAddress() : SystemIdentityFunctions.getHostAddress();
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String PROPERTY_HOSTNAME = System.getenv("HOSTNAME");
    public static final String PROPERTY_COMPUTERNAME = System.getenv("COMPUTERNAME");
    public static final String MAC_OSX_NAME = "Mac OS X";
    public static final String UNKNOWN = "Unknown";
    public static final String ETHERNET_0 = "en0";
    public static final String INTERNET_URI = "google.com";
    public static final int INTERNET_PORT = 80;
}
