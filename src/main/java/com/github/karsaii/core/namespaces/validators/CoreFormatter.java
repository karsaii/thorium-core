package com.github.karsaii.core.namespaces.validators;

import com.github.karsaii.core.constants.CommandRangeDataConstants;
import com.github.karsaii.core.extensions.DecoratedList;
import com.github.karsaii.core.extensions.interfaces.IEmptiable;
import com.github.karsaii.core.extensions.namespaces.BasicPredicateFunctions;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.extensions.namespaces.EmptiableFunctions;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.command.CommandRangeData;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.records.reflection.message.InvokeCommonMessageParametersData;
import com.github.karsaii.core.records.reflection.message.InvokeParameterizedMessageData;
import com.github.karsaii.core.constants.validators.CoreFormatterConstants;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areAnyBlank;
import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areAnyNull;
import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areNotBlank;
import static com.github.karsaii.core.extensions.namespaces.CoreUtilities.areNotNull;
import static com.github.karsaii.core.namespaces.DataFunctions.isFalse;
import static com.github.karsaii.core.namespaces.DataFunctions.isTrue;
import static com.github.karsaii.core.namespaces.validators.DataValidators.isInvalidOrFalse;
import static com.github.karsaii.core.namespaces.validators.DataValidators.isValidNonFalse;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public interface CoreFormatter {

    static String getNamedErrorMessageOrEmpty(String name, String message) {
        final var nameof = isNotBlank(name) ? name : "getNamedErrorMessageOrEmpty: (Name was empty.) ";
        return isNotBlank(message) ? nameof + CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }

    static String getOptionMessage(boolean status) {
        return status ? CoreFormatterConstants.OPTION_EMPTY : CoreFormatterConstants.OPTION_NOT;
    }

    static String isParameterMessage(boolean condition, String parameterName, String descriptor) {
        return condition ? parameterName + " parameter was " + descriptor + CoreFormatterConstants.END_LINE : CoreFormatterConstants.EMPTY;
    }

    static <T> String isNullMessageWithName(T object, String parameterName) {
        return isParameterMessage(NullableFunctions.isNull(object), parameterName, "null");
    }

    static <T> String isNullMessage(T object) {
        return isNullMessageWithName(object, "Object");
    }

    static <T> String isEmptyMessage(T[] object) {
        var message = isNullMessageWithName(object, "Objeect");
        if (isBlank(message)) {
            message += object.length < 1 ? "Object is empty" : CoreFormatterConstants.EMPTY;
        }

        return getNamedErrorMessageOrEmpty("isEmptyMessage: ", message);
    }

    static String isInvalidOrFalseMessageWithName(Data data, String parameterName) {
        var message = isParameterMessage(isInvalidOrFalse(data), parameterName, "false data");
        if (isNotBlank(message)) {
            message += data.message;
        }

        return getNamedErrorMessageOrEmpty("isInvalidOrFalseMessage: ", message);
    }

    static String isInvalidOrFalseMessage(Data data) {
        return isInvalidOrFalseMessageWithName(data, "data");
    }

    static String isInvalidOrFalseMessageE(ExecutionResultData data) {
        return isNullMessageWithName(data.result, "Result Object");
    }

    static String isFalseMessageWithName(Data data, String parameterName) {
        var message = isParameterMessage(isFalse(data), parameterName, "false data");
        if (isNotBlank(message)) {
            message += data.message;
        }

        return getNamedErrorMessageOrEmpty("isFalseMessageWithName: ", message);
    }

    static String isFalseMessage(Data data) {
        return isFalseMessageWithName(data, "data");
    }

    static String isTrueMessageWithName(Data data, String parameterName) {
        var message = isParameterMessage(isTrue(data), parameterName, "true data");
        if (isNotBlank(message)) {
            message += data.message;
        }

        return getNamedErrorMessageOrEmpty("isTrueMessageWithName: ", message);
    }

    static String isTrueMessage(Data data) {
        return isTrueMessageWithName(data, "data");
    }

    static String isBlankMessageWithName(String value, String parameterName) {
        return isParameterMessage(isBlank(value), parameterName, "blank, empty or null");
    }

    static String isBlankMessage(String value) {
        return isBlankMessageWithName(value, "String value");
    }

    static String isFalseMessageWithName(boolean condition, String parameterName) {
        return isParameterMessage(CoreUtilities.isFalse(condition), parameterName, "false");
    }

    static String isInvalidMessage(boolean condition, String parameterName) {
        return isParameterMessage(CoreUtilities.isFalse(condition), parameterName, "invalid");
    }

    static String getConditionStatusMessage(boolean key) {
        return key ? "is" : "isn't";
    }

    static String getConditionMessage(String elementName, String descriptor, boolean option) {
        final var name = "getConditionMessage: ";
        final var errorMessage = (
            isBlankMessageWithName(elementName, "Element name") +
            isBlankMessageWithName(descriptor, "Descriptor")
        );
        return name + (
            isNotBlank(errorMessage) ? (
                CoreFormatterConstants.PARAMETER_ISSUES_LINE + errorMessage
            ) : (CoreFormatterConstants.ELEMENT + getConditionStatusMessage(option) + " " + descriptor + CoreFormatterConstants.END_LINE)
        );
    }

    static String getElementValueMessage(String elementName, String descriptor, String value) {
        final var name = "getValueMessage: ";
        final var errorMessage = (
                isBlankMessageWithName(elementName, "Element name") +
                isBlankMessageWithName(descriptor, "Descriptor") +
                isNullMessageWithName(value, "Value")
        );
        return name + (
            isNotBlank(errorMessage) ? (
                CoreFormatterConstants.PARAMETER_ISSUES_LINE + errorMessage
            ) : (CoreFormatterConstants.ELEMENT + " " + elementName + " " + descriptor + " was (\"" + value +"\")"  + CoreFormatterConstants.END_LINE)
        );
    }

    static String getMethodFromMapMessage(String methodName, boolean status) {
        return "Method(" + methodName + ") " + getOptionMessage(status) + " found in map" + CoreFormatterConstants.END_LINE;
    }

    static String getWaitErrorMessage(String message, long timeout, long interval) {
        return CoreFormatterConstants.WAITING_FAILED + message + "Tried for " + timeout + " second(s) with " + interval + " milliseconds interval" + CoreFormatterConstants.END_LINE;
    }

    static String getWaitInterruptMessage(String message) {
        return CoreFormatterConstants.WAITING_FAILED + "Thread interruption occurred, exception message" + CoreFormatterConstants.COLON_NEWLINE + message;
    }

    static String getExecutionStepMessage(int index, String message) {
        return (index + 1) + ". " + message + CoreFormatterConstants.END_LINE;
    }

    static String getExecutionStatusInvalidMessage(ExecutionStateData data) {
        var message = isNullMessageWithName(data, "Execution State Data");
        if (isBlank(message)) {
            message += (
                isNullMessageWithName(data.executionMap, "Execution Map") +
                isNullMessageWithName(data.indices, "Indices")
            );
        }

        return getNamedErrorMessageOrEmpty("getExecutionStatusInvalidMessage: ", message);
    }

    static String getExecutionEndParametersInvalidMessage(ExecutionStateData state, String key, int index, int length) {
        return getNamedErrorMessageOrEmpty(
            "getExecutionEndParametersInvalidMessage: ",
            (
                getExecutionStatusInvalidMessage(state) +
                isBlankMessageWithName(key, "Last key from execution") +
                isNegativeMessageWithName(index, "Index") +
                isNegativeMessageWithName(length, "Length")
            )
        );
    }

    static String getNamedErrorMessageOrEmptyNoIssues(String name, String message) {
        final var nameof = isNotBlank(name) ? name : "getNamedErrorMessageOrEmpty: (Name was empty.) ";
        return isNotBlank(message) ? nameof + message : CoreFormatterConstants.EMPTY;
    }

    static String getExecutionEndMessage(ExecutionStateData state, String key, int index, int length) {
        final var errorMessage = getExecutionEndParametersInvalidMessage(state, key, index, length);
        if (isNotBlank(errorMessage)) {
            return errorMessage;
        }

        final var map = state.executionMap;
        final var valueSet = map.values();
        final var passedValueAmount = valueSet.stream().filter(DataValidators::isValidNonFalse).count();
        final var valuesLength = valueSet.size();
        final var failedValueAmount = valuesLength - passedValueAmount;
        final var builder = new StringBuilder();
        final var values = valueSet.toArray(new Data<?>[0]);
        Data<?> step;
        if (BasicPredicateFunctions.isPositiveNonZero((int)failedValueAmount)) {
            for (var stepIndex = 0; stepIndex < valuesLength; ++stepIndex) {
                step = values[stepIndex];
                builder.append(getExecutionStepMessage(stepIndex, (isValidNonFalse(step) ? "Passed" : "Failed") + CoreFormatterConstants.COLON_SPACE + step.message.toString()));
            }
        } else {
            step = map.get(key);
            builder.append(getExecutionStepMessage(valuesLength - 1, (isValidNonFalse(step) ? "Passed" : "Failed") + CoreFormatterConstants.COLON_SPACE + step.message.toString()));
        }

        final var message = (
            ((index == length) ? "All" : "Some") + " steps were executed" + CoreFormatterConstants.COLON_SPACE +
            (BasicPredicateFunctions.isPositiveNonZero((int)failedValueAmount) ? (
                passedValueAmount + " passed, " + failedValueAmount + " failed"
            ) : ("All(" + passedValueAmount + ") passed")) + CoreFormatterConstants.END_LINE +
            "    " + builder.toString().replaceAll("\n", "\n    ")
        );

        return getNamedErrorMessageOrEmptyNoIssues("Execution end: ", message);
    }

    static String getExecutionEndMessageAggregate(ExecutionStateData state, String key, int index, int length) {
        final var errorMessage = getExecutionEndParametersInvalidMessage(state, key, index, length);
        if (isNotBlank(errorMessage)) {
            return errorMessage;
        }

        final var valueSet = state.executionMap.values();
        final var passedValueAmount = valueSet.stream().filter(DataValidators::isValidNonFalse).count();
        final var failedValueAmount = length - passedValueAmount;
        final var builder = new StringBuilder();
        final var valuesLength = valueSet.size();
        final var values = valueSet.toArray(new Data<?>[0]);
        var stepIndex = 0;
        Data<?> step;
        for(; stepIndex < valuesLength; ++stepIndex) {
            step = values[stepIndex];
            builder.append(getExecutionStepMessage(stepIndex, (isValidNonFalse(step) ? "Passed" : "Failed") + CoreFormatterConstants.COLON_SPACE + step.message.toString()));
        }

        final var message = (
            ((index == length) ? "All" : "Some") + " steps were executed" + CoreFormatterConstants.COLON_SPACE +
            (BasicPredicateFunctions.isPositiveNonZero((int)failedValueAmount) ? (
                passedValueAmount + " passed, " + failedValueAmount + " failed"
            ) : ("All(" + passedValueAmount + ") passed")) + CoreFormatterConstants.END_LINE +
            "    " + builder.toString().replaceAll("\n", "\n    ")
        );

        return getNamedErrorMessageOrEmpty("getExecutionEndMessage", message);
    }

    static String getExecuteFragment(boolean status) {
        return status ? CoreFormatterConstants.SUCCESSFULLY_EXECUTE : CoreFormatterConstants.COULDNT_EXECUTE;
    }

    static String getScreenshotFileName(String path) {
        return (
            path +
            String.join(CoreFormatterConstants.SS_NAME_SEPARATOR, CoreFormatterConstants.NAME_START, LocalTime.now().toString(), UUID.randomUUID().toString()) +
            CoreFormatterConstants.EXTENSION
        );
    }

    static String getMethodFromListMessage(String methodName, boolean status) {
        return "Method(" + methodName + ") " + getOptionMessage(status) + " found" + CoreFormatterConstants.END_LINE;
    }

    static String getInputErrorMessage(String input) {
        return isBlankMessageWithName(input, "Input");
    }

    static <T> String isEmptyMessage(T data, String parameterName) {
        var message = isNullMessageWithName(data, parameterName);
        if (isBlank(message)) {
            //TODO Java13-14 instanceof + switch expression.
            var type = "";
            if (data instanceof List && EmptiableFunctions.isEmpty((List)data)) {
                type = "(List)";
            }

            if (data instanceof Map && EmptiableFunctions.isEmpty((Map)data)) {
                type = "(Map)";
            }

            if (isNotBlank(type)) {
                message += parameterName + type + " was empty" + CoreFormatterConstants.END_LINE;
            }
        }

        return getNamedErrorMessageOrEmpty("isEmptyMessage: ", message);
    }

    static <T> String isEmptyMessage(T data) {
        return isEmptyMessage(data, "Emptiable data");
    }

    static Data<String> isNumberConditionCore(boolean status, int number, int expected, String parameterName, String conditionDescriptor, String nameof) {
        var message = (
            isNullMessageWithName(nameof, "Caller function's name") +
            isBlankMessageWithName(parameterName, "Name of the parameter") +
            isBlankMessageWithName(conditionDescriptor, "Condition Descriptor")
        );

        final var lNameof = isBlank(nameof) ? "isNumberConditionCore" : nameof;
        if (isNotBlank(message)) {
            return DataFactoryFunctions.getWithNameAndMessage(CoreFormatterConstants.EMPTY, false, lNameof, CoreFormatterConstants.PARAMETER_ISSUES + message);
        }

        final var lParameterName = isBlank(parameterName) ? "Number" : parameterName;
        final var option = getOptionMessage(status);
        final var object = lParameterName + "(\"" + number + "\") was" + option + " " + conditionDescriptor + " expected(\"" + expected +"\")" + CoreFormatterConstants.END_LINE;
        final var returnMessage = "Parameters were" + option + " okay." ;
        return status ? (
            DataFactoryFunctions.getWithNameAndMessage(object, status, lNameof, returnMessage + "\n")
        ) : DataFactoryFunctions.getWithNameAndMessage(CoreFormatterConstants.EMPTY, status, lNameof, returnMessage + " " + object);
    }

    static Data<String> isEqualToExpected(int number, int expected, String parameterName) {
        return isNumberConditionCore(number == expected, number, expected, parameterName, "equal to", "isEqualToExpected");
    }

    static Data<String> isLessThanExpected(int number, int expected, String parameterName) {
        return isNumberConditionCore(number < expected, number, expected, parameterName, "less than", "isLessThanExpected");
    }

    static Data<String> isMoreThanExpected(int number, int expected, String parameterName) {
        return isNumberConditionCore(number > expected, number, expected, parameterName, "more than", "isMoreThanExpected");
    }

    static String getCommandRangeParameterMessage(CommandRangeData range) {
        var message = isNullMessageWithName(range, "Range object");
        if (isBlank(message)) {
            final var minData = isMoreThanExpected(range.min, 0, "Range minimum");
            final var maxData = isLessThanExpected(range.max, 1000, "Range maximum");
            message += (
                (isFalse(minData) ? minData.message.toString() : CoreFormatterConstants.EMPTY) +
                (isFalse(maxData) ? maxData.message.toString() : CoreFormatterConstants.EMPTY) +
                isNullMessageWithName(range.rangeInvalidator, "Command Range validator function")
            );
        }

        return getNamedErrorMessageOrEmpty("getCommandRangeParameterMessage: ", message);
    }

    static String getCommandAmountRangeErrorMessage(int length, CommandRangeData range) {
        var message = getCommandRangeParameterMessage(range);
        if (isNotBlank(message)) {
            return message + "Cannot invalidate length(\"" + length + "\") on an invalid range" + CoreFormatterConstants.END_LINE;
        }

        final var status = range.rangeInvalidator.apply(range.min, length, range.max);
        if (!status) {
            return CoreFormatterConstants.EMPTY;
        }

        final var parameterName = "Actual Command amount";
        final var minData = isLessThanExpected(length, range.min, parameterName);
        if (!minData.status) {
            message += minData.object;
        }

        final var maxData = isMoreThanExpected(length, range.max, parameterName);
        if (!maxData.status) {
            message += maxData.object;
        }

        return message;
    }

    static String getCommandAmountRangeErrorMessage(int length, int min, int max) {
        var message = "";
        if (length < min) {
            message += "The commands' amount was below the minimum(" + min + ") limit";
        }
        if (length > max) {
            message += "The commands' amount was above the maximum(" + max +") limit";
        }

        return message;
    }

    static String getCommandAmountRangeErrorMessage(int length) {
        return getCommandAmountRangeErrorMessage(length, CommandRangeDataConstants.MINIMUM_COMMAND_LIMIT, CommandRangeDataConstants.MAXIMUM_COMMAND_LIMIT);
    }

    static String formatPrefixSuffixMessage(String prefix, Boolean status, String suffix) {
        return prefix + status + suffix;
    }

    static <T> String getInvokeMethodCoreMessage(Exception exception, String message, String returnType, String parameterTypes) {
        final var endLine = CoreFormatterConstants.END_LINE;
        return CoreUtilities.isException(exception) ? (
            String.join(
                endLine,
                message,
                "An Exception(" + exception.getClass() + ") has occurred",
                "Exception Message:\n" + exception.getMessage(),
                "Cause: " + exception.getCause(),
                "Method parameter types: " + parameterTypes,
                "Result is of type " + returnType
            ) + endLine
        ) : CoreFormatterConstants.EMPTY;
    }

    static String getInvokeMethodCommonMessage(InvokeCommonMessageParametersData data, Exception exception) {
        return (areNotNull(data, exception) && areNotBlank(data.message, data.parameterTypes, data.returnType)) ? (
            getInvokeMethodCoreMessage(exception, data.message, data.parameterTypes, data.returnType)
        ) : "Data parameter" + CoreFormatterConstants.WAS_NULL;
    }

    static String getInvokeMethodParameterizedMessage(InvokeParameterizedMessageData data, Exception exception) {
        if (areAnyNull(data, exception) || areAnyBlank(data.message, data.parameterTypes, data.returnType)) {
            return "Data parameter" + CoreFormatterConstants.WAS_NULL;
        }

        final var parameter = data.parameter;
        final var parameterMessage = (isNotBlank(parameter) ? "Parameter was specified: " + parameter : "Parameter wasn't specified") + CoreFormatterConstants.END_LINE;
        final var invokeMessage = getInvokeMethodCoreMessage(exception, data.message, data.parameterTypes, data.returnType);
        return isNotBlank(invokeMessage) ? invokeMessage + parameterMessage : CoreFormatterConstants.EMPTY;
    }

    static Function<Exception, String> getInvokeMethodCommonMessageFunction(InvokeCommonMessageParametersData data) {
        return exception -> areAnyNull(data, exception) ? getInvokeMethodCommonMessage(data, exception) : CoreFormatterConstants.PARAMETER_ISSUES;
    }

    static Function<Exception, String> getInvokeMethodParameterizedMessageFunction(InvokeParameterizedMessageData data) {
        return exception -> areAnyNull(data, exception) ? getInvokeMethodParameterizedMessage(data, exception) : "Data or exception" + CoreFormatterConstants.WAS_NULL;
    }

    static String isNullOrEmptyListMessage(List<?> list, String parameterName) {
        var message = isNullMessageWithName(list, parameterName);
        final var name = (isBlank(parameterName) ? "List" : parameterName);
        if (isBlank(message)) {
            message += list.isEmpty() ? name + " was empty" + CoreFormatterConstants.END_LINE : CoreFormatterConstants.EMPTY;
        }

        return getNamedErrorMessageOrEmpty("isNullOrEmptyListMessage: ", message);
    }


    static <T> String getListEmptyMessage(DecoratedList<T> list, String parameterName) {
        final var name = isBlank(parameterName) ? "List" : parameterName;
        var message = "";
        if (list.isNull()) {
            message += name + CoreFormatterConstants.WAS_NULL;
        }

        if (isBlank(message) && list.isEmpty()) {
            message += name + "was empty" + CoreFormatterConstants.END_LINE;
        }

        return isNotBlank(message) ? "getListEmptyMessage: " + message : CoreFormatterConstants.EMPTY;
    }

    static <T> String getListNotEnoughMessage(DecoratedList<T> list, String parameterName, int expected) {
        final var name = isBlank(parameterName) ? "List" : parameterName;
        var message = "";
        if (list.isNull()) {
            message += name + CoreFormatterConstants.WAS_NULL;
        }

        if (isBlank(message) && !list.hasAtleast(expected)) {
            message += name + "length was less than " + expected + CoreFormatterConstants.END_LINE;
        }

        return isNotBlank(message) ? "getListEmptyMessage: " + message : CoreFormatterConstants.EMPTY;
    }

    static Function<Boolean, String> isFormatterNullAndMessageBlank() {
        return status -> CoreFormatterConstants.EXECUTION_STATUS_COLON_SPACE + status + CoreFormatterConstants.END_LINE;
    }

    static Function<Boolean, String> isFormatterNull(String message) {
        return status -> CoreFormatterConstants.EXECUTION_STATUS_COLON_SPACE + status + CoreFormatterConstants.END_LINE + "Message: " + message;
    }

    static Function<Boolean, String> isMessageBlank(BiFunction<String, Boolean, String> formatter) {
        return status -> CoreFormatterConstants.EXECUTION_STATUS_COLON_SPACE + status + "Message was empty, please fix - result: " + formatter.apply(CoreFormatterConstants.EMPTY, status) + CoreFormatterConstants.END_LINE;
    }

    static Function<Boolean, String> isFormatterAndMessageValid(BiFunction<String, Boolean, String> formatter, String message) {
        return status -> formatter.apply(message, status);
    }

    static String isNegativeMessageWithName(int value, String parameterName) {
        final var name = isNotBlank(parameterName) ? parameterName : "Value parameter";
        final var status = BasicPredicateFunctions.isNegative(value);
        var message = "";
        if (status) {
            message += name + "(\"" + value +"\") is negative" + CoreFormatterConstants.END_LINE;
        }

        return isNotBlank(message) ? "isNegativeMessage: " + CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }

    static String isNegativeMessage(int value) {
        return isNegativeMessageWithName(value, "Value parameter");
    }

    static String isNullOrEmptyMessageWithName(IEmptiable emptiable, String parameterName) {
        final var baseName = isNotBlank(parameterName) ? parameterName : "Emptiable";
        var message = isNullMessageWithName(emptiable, baseName);
        if (isBlank(message)) {
            if (emptiable.isEmpty()) {
                message += baseName + " was null or empty" + CoreFormatterConstants.END_LINE;
            }
        }

        return getNamedErrorMessageOrEmpty("isNullOrEmpty: ", message);
    }

    static String isNullOrEmptyMessage(IEmptiable emptiable) {
        return isNullOrEmptyMessageWithName(emptiable, "Emptiable");
    }

    static String getExecutionResultKey(String name, int index) {
        return name + "-" + index;
    }

    static String isEqualMessage(Object left, String leftDescriptor, Object right, String rightDescriptor) {
        var message = isNullMessageWithName(left, "Left Object") + isNullMessageWithName(right, "Right Object");
        if (isBlank(message) && Objects.equals(left, right)) {
            message += (
                (
                    areAnyBlank(leftDescriptor, rightDescriptor) ? "The two objects" : (leftDescriptor + " and " + rightDescriptor)
                ) + " are equal" + CoreFormatterConstants.END_LINE
            );
        }

        return isNotBlank(message) ? "isEqualMessage: " + CoreFormatterConstants.PARAMETER_ISSUES_LINE + message : CoreFormatterConstants.EMPTY;
    }
}
