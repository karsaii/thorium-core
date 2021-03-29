package com.github.karsaii.core.constants.wait;

import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.records.Data;

public abstract class WaitDataConstants {
    public static final Data<Void> SLEEP_START_DATA = DataFactoryFunctions.getInvalidWith(null, "sleep", "sleep");

}
