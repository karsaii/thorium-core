package com.github.karsaii.core.constants;

import com.github.karsaii.core.extensions.boilers.StringSet;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.MethodData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

public abstract class CoreDataConstants {
    public static final Data<Boolean> NULL_BOOLEAN = DataFactoryFunctions.getInvalidBooleanWithNameAndMessage("nullBoolean", "nullBoolean data" + CoreFormatterConstants.END_LINE);
    public static final Data<Boolean> DATA_PARAMETER_WAS_NULL = DataFactoryFunctions.getInvalidBooleanWithNameAndMessage("dataParameterWasNull", "Data parameter" + CoreFormatterConstants.WAS_NULL);
    public static final Data<Boolean> PARAMETERS_NULL_BOOLEAN = DataFactoryFunctions.getInvalidBooleanWithNameAndMessage("parametersNullBoolean", "Parameters" + CoreFormatterConstants.WAS_NULL);
    public static final Data<Boolean> NO_STEPS = DataFactoryFunctions.getBoolean(true, "noSteps", "No Steps were provided" + CoreFormatterConstants.END_LINE);

    public static final Data<Integer> NULL_INTEGER = DataFactoryFunctions.getWithNameAndMessage(0, false, "nullInteger", "nullInteger data.");
    public static final Data<Integer> NO_ELEMENTS_FOUND_DATA_FALSE_OR_NULL = DataFactoryFunctions.getWithNameAndMessage(0, false, "noElementsFoundDataFalseOrNull", "No elements were found, data was false or null" + CoreFormatterConstants.END_LINE);

    public static final Data<MethodData> NULL_METHODDATA = DataFactoryFunctions.getWithNameAndMessage(CoreConstants.STOCK_METHOD_DATA, false, "nullMethodData", "Null methodData data.");

    public static final Data<Object> NULL_OBJECT = DataFactoryFunctions.getWithNameAndMessage(CoreConstants.STOCK_OBJECT, false, "nullObject", "null object data.");

    public static final Data<Object[]> NULL_PARAMETER_ARRAY = DataFactoryFunctions.getWithNameAndMessage(CoreConstants.EMPTY_OBJECT_ARRAY, false, "nullParameterArray", "Null Parameter Array.");

    public static final Data<String> DATA_WAS_NULL_OR_FALSE_STRING = DataFactoryFunctions.getString(CoreFormatterConstants.EMPTY, "dataWasNullOrFalseString", CoreFormatterConstants.DATA_NULL_OR_FALSE);
    public static final Data<String> NULL_STRING = DataFactoryFunctions.getWithNameAndMessage(CoreFormatterConstants.EMPTY, false, "nullString", "nullString data.");

    public static final Data<StringSet> NULL_STRING_SET = DataFactoryFunctions.getWithNameAndMessage(CoreConstants.NULL_STRING_SET, false, "nullStringSet", "Null String Set data" + CoreFormatterConstants.END_LINE);

    public static final Data<Void> NULL_VOID = DataFactoryFunctions.getWithNameAndMessage(null, false, "nullVoid", "Void data" + CoreFormatterConstants.END_LINE);

    public static final Data<Void> VOID_TASK_RAN_SUCCESSFULLY = DataFactoryFunctions.getWithMessageAndExceptionDefaults(null, true, "runVoidTaskCore", "Void task successful" + CoreFormatterConstants.END_LINE);
}
