package com.neathorium.core.constants.wait;

import com.neathorium.core.namespaces.DataFactoryFunctions;
import com.neathorium.core.records.Data;

public abstract class WaitDataConstants {
    public static final Data<Void> SLEEP_START_DATA = DataFactoryFunctions.getInvalidWith(null, "sleep", "sleep");

}
