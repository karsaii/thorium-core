package com.neathorium.core.constants;

import com.neathorium.core.extensions.namespaces.CoreUtilities;
import com.neathorium.core.namespaces.executor.ExecutorFunctionDataFactory;
import com.neathorium.core.records.executor.ExecuteParametersData;
import com.neathorium.core.records.executor.ExecutorFunctionData;
import com.neathorium.core.records.SimpleMessageData;
import com.neathorium.core.constants.validators.CoreFormatterConstants;
import com.neathorium.core.namespaces.validators.CoreFormatter;

public abstract class ExecutorConstants {
    public static final ExecuteParametersData DEFAULT_EXECUTION_DATA = new ExecuteParametersData(CommandRangeDataConstants.DEFAULT_RANGE, CoreUtilities::isAllDone, CoreFormatter::getExecutionEndMessage);
    public static final ExecutorFunctionData DEFAULT_EXECUTION_ENDED = ExecutorFunctionDataFactory.getWithExecuteParametersDataAndDefaultExitCondition(new SimpleMessageData(CoreFormatterConstants.EXECUTION_ENDED), DEFAULT_EXECUTION_DATA);
    //public static final ExecuteParametersData<> TWO_COMMANDS_STEP_EXECUTION = new ExecutionParametersData<>(ExecutorConstants.DEFAULT_EXECUTION_ENDED, Executor::executeCoreStepMessages, ExecutorConstants.TWO_COMMANDS_RANGE)
}
