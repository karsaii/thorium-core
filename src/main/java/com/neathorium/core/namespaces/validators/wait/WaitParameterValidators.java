package com.neathorium.core.namespaces.validators.wait;

import com.neathorium.core.namespaces.validators.CoreFormatter;
import com.neathorium.core.records.wait.tasks.common.WaitTaskCommonData;
import com.neathorium.core.records.wait.WaitTimeData;

import static org.apache.commons.lang3.StringUtils.isBlank;

public interface WaitParameterValidators {
    static String validateWaitTimeData(WaitTimeData timeData) {
        var message = CoreFormatter.isNullMessageWithName(timeData, "TimeData");
        if (isBlank(message)) {
            message += (
                CoreFormatter.isNullMessageWithName(timeData.clock, "TimeData clock") +
                CoreFormatter.isNullMessageWithName(timeData.interval, "TimeData interval") +
                CoreFormatter.isNullMessageWithName(timeData.duration, "TimeData duration")
            );
        }

        return CoreFormatter.getNamedErrorMessageOrEmpty("validateWaitTimeData", message);
    }

    static <T, U, V> String validateUntilParameters(WaitTaskCommonData<T, U, V> taskData, WaitTimeData timeData) {
        return CoreFormatter.getNamedErrorMessageOrEmpty(
            "validateUntilParameters",
            isValidWaitTaskCommonData(taskData) + validateWaitTimeData(timeData)
        );
    }

    static <T, U, V> String isValidWaitTaskCommonData(WaitTaskCommonData<T, U, V> data) {
        var message = CoreFormatter.isNullMessageWithName(data, "Wait Task Common Data");
        if (isBlank(message)) {
            message += (
                CoreFormatter.isNullMessageWithName(data.function, "Function") +
                CoreFormatter.isNullMessageWithName(data.exitCondition, "Exit Condition")
            );
        }

        return CoreFormatter.getNamedErrorMessageOrEmpty("validateUntilParameters", message);
    }
}
