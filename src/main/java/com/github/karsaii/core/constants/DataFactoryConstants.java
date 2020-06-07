package com.github.karsaii.core.constants;

import com.github.karsaii.core.records.MethodMessageData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

public abstract class DataFactoryConstants {
    public static final boolean DEFAULT_STATE = false;
    public static final String DEFAULT_EXCEPTION_MESSAGE = CoreFormatterConstants.NON_EXCEPTION_MESSAGE;
    public static final Exception DEFAULT_EXCEPTION = CoreConstants.EXCEPTION;
    public static final MethodMessageData DEFAULT_METHOD_MESSAGE_DATA = new MethodMessageData("getWith", "Default method message" + CoreFormatterConstants.END_LINE);
}
