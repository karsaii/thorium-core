package com.github.karsaii.core.constants.systemidentity;

import com.github.karsaii.core.namespaces.systemidentity.ConcreteSystemIdentityFunctions;

public abstract class ConcreteSystemIdentityConstants {
    public static final String HOST_NAME = ConcreteSystemIdentityFunctions.getHostName();
    public static final String HOST_ADDRESS = ConcreteSystemIdentityFunctions.isOSX() ? ConcreteSystemIdentityFunctions.getOSXHostAddress() : ConcreteSystemIdentityFunctions.getHostAddress();
}
