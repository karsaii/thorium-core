package com.github.karsaii.core.namespaces.executor;

import com.github.karsaii.core.constants.ExecutorConstants;
import com.github.karsaii.core.extensions.interfaces.functional.QuadFunction;
import com.github.karsaii.core.extensions.interfaces.functional.QuadPredicate;
import com.github.karsaii.core.extensions.interfaces.functional.TriPredicate;
import com.github.karsaii.core.extensions.interfaces.functional.boilers.IGetMessage;
import com.github.karsaii.core.extensions.namespaces.CoreUtilities;
import com.github.karsaii.core.extensions.namespaces.ExecutorPredicates;
import com.github.karsaii.core.extensions.namespaces.NullableFunctions;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.SimpleMessageData;
import com.github.karsaii.core.records.executor.ExecuteParametersData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.records.executor.ExecutorFunctionData;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;

import java.util.function.Predicate;

import static com.github.karsaii.core.extensions.namespaces.NullableFunctions.isNotNull;

public interface ExecutorFunctionDataFactory {
    static ExecutorFunctionData getWith(
        IGetMessage messageData,
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler,
        Predicate<Data<?>> filterCondition
    ) {
        return new ExecutorFunctionData(
            isNotNull(messageData) ? messageData : new SimpleMessageData(),
            isNotNull(breakCondition) ? breakCondition : ExecutorPredicates::isExecuting,
            isNotNull(endCondition) ? endCondition : CoreUtilities::isAllDone,
            isNotNull(endMessageHandler) ? endMessageHandler : CoreFormatter::getExecutionEndMessage,
            isNotNull(filterCondition) ? filterCondition : NullableFunctions::isNotNull
        );
    }

    static ExecutorFunctionData getWithDefaultBreakCondition(
        IGetMessage messageData,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler,
        Predicate<Data<?>> filterCondition
    ) {
        return getWith(messageData, ExecutorPredicates::isExecuting, endCondition, endMessageHandler, filterCondition);
    }

    static ExecutorFunctionData getWithDefaultEndCondition(
        IGetMessage messageData,
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler,
        Predicate<Data<?>> filterCondition
    ) {
        return getWith(messageData, breakCondition, CoreUtilities::isAllDone, endMessageHandler, filterCondition);
    }

    static ExecutorFunctionData getWithDefaultEndMessageHandler(
        IGetMessage messageData,
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        Predicate<Data<?>> filterCondition
    ) {
        return getWith(messageData, breakCondition, endCondition, CoreFormatter::getExecutionEndMessage, filterCondition);
    }

    static ExecutorFunctionData getWithDefaultFilterCondition(
        IGetMessage messageData,
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler
    ) {
        return getWith(messageData, breakCondition, endCondition, endMessageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithDefaultEndMessageHandlerFilterCondition(
        IGetMessage messageData,
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition
    ) {
        return getWith(messageData, breakCondition, endCondition, CoreFormatter::getExecutionEndMessage, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithDefaultEndConditionFilterCondition(
        IGetMessage messageData,
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler
    ) {
        return getWith(messageData, breakCondition, CoreUtilities::isAllDone, endMessageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithDefaultEndConditionEndMessageHandler(
        IGetMessage messageData,
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        Predicate<Data<?>> filterCondition
    ) {
        return getWith(messageData, breakCondition, CoreUtilities::isAllDone, CoreFormatter::getExecutionEndMessage, filterCondition);
    }

    static ExecutorFunctionData getWithDefaultBreakConditionFilterCondition(
        IGetMessage messageData,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler
    ) {
        return getWith(messageData, ExecutorPredicates::isExecuting, endCondition, endMessageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithDefaultBreakConditionEndMessageHandler(
        IGetMessage messageData,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        Predicate<Data<?>> filterCondition
    ) {
        return getWith(messageData, ExecutorPredicates::isExecuting, endCondition, CoreFormatter::getExecutionEndMessage, filterCondition);
    }

    static ExecutorFunctionData getWithDefaultBreakConditionEndCondition(
        IGetMessage messageData,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler,
        Predicate<Data<?>> filterCondition
    ) {
        return getWith(messageData, ExecutorPredicates::isExecuting, CoreUtilities::isAllDone, endMessageHandler, filterCondition);
    }

    static ExecutorFunctionData getWithSpecificMessageDataAndBreakCondition(IGetMessage messageData, TriPredicate<Data<?>, Integer, Integer> breakCondition, ExecuteParametersData data) {
        return getWith(messageData, breakCondition, data.endCondition, data.messageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificMessageDataAndBreakCondition(IGetMessage messageData, TriPredicate<Data<?>, Integer, Integer> breakCondition) {
        final var data = ExecutorConstants.DEFAULT_EXECUTION_DATA;
        return getWithSpecificMessageDataAndBreakCondition(messageData, breakCondition, data);
    }

    static ExecutorFunctionData getWithSpecificMessageAndBreakCondition(String message, TriPredicate<Data<?>, Integer, Integer> breakCondition) {
        final var data = ExecutorConstants.DEFAULT_EXECUTION_DATA;
        return getWithSpecificMessageDataAndBreakCondition(new SimpleMessageData(message), breakCondition, data);
    }

    static ExecutorFunctionData getWithSpecificMessageDataAndEndCondition(IGetMessage messageData, QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition) {
        return getWith(messageData, ExecutorPredicates::isExecuting, endCondition, CoreFormatter::getExecutionEndMessage, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificMessageDataAndEndMessageHandler(IGetMessage messageData, QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler) {
        return getWith(messageData, ExecutorPredicates::isExecuting, CoreUtilities::isAllDone, endMessageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificMessageDataAndFilterCondition(IGetMessage messageData, Predicate<Data<?>> filterCondition) {
        return getWith(messageData, ExecutorPredicates::isExecuting, CoreUtilities::isAllDone, CoreFormatter::getExecutionEndMessage, filterCondition);
    }

    static ExecutorFunctionData getWithExecuteParametersDataAndDefaultExitCondition(IGetMessage messageData, ExecuteParametersData data) {
        return getWith(messageData, ExecutorPredicates::isExecuting, data.endCondition, data.messageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificMessageData(IGetMessage messageData) {
        final var data = ExecutorConstants.DEFAULT_EXECUTION_DATA;
        return getWithExecuteParametersDataAndDefaultExitCondition(messageData, data);
    }

    static ExecutorFunctionData getWithSpecificMessage(String message) {
        final var data = ExecutorConstants.DEFAULT_EXECUTION_DATA;
        return getWithExecuteParametersDataAndDefaultExitCondition(new SimpleMessageData(message), data);
    }

    static ExecutorFunctionData getWithDefaultMessageData(
            TriPredicate<Data<?>, Integer, Integer> breakCondition,
            QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
            QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler,
            Predicate<Data<?>> filterCondition
    ) {
        return getWith(new SimpleMessageData(), breakCondition, endCondition, endMessageHandler, filterCondition);
    }

    static ExecutorFunctionData getWithSpecificBreakCondition(TriPredicate<Data<?>, Integer, Integer> breakCondition) {
        return getWithDefaultMessageData(breakCondition, CoreUtilities::isAllDone, CoreFormatter::getExecutionEndMessage, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificEndCondition(QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition) {
        return getWithDefaultMessageData(ExecutorPredicates::isExecuting, endCondition, CoreFormatter::getExecutionEndMessage, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificEndMessageHandler(QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler) {
        return getWithDefaultMessageData(ExecutorPredicates::isExecuting, CoreUtilities::isAllDone, endMessageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificFilterCondition(Predicate<Data<?>> filterCondition) {
        return getWithDefaultMessageData(ExecutorPredicates::isExecuting, CoreUtilities::isAllDone, CoreFormatter::getExecutionEndMessage, filterCondition);
    }

    static ExecutorFunctionData getWithDefaultMessageDataFilterCondition(
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler
    ) {
        return getWithDefaultMessageData(breakCondition, endCondition, endMessageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithDefaultMessageDataEndMessageHandler(
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        Predicate<Data<?>> filterCondition
    ) {
        return getWithDefaultMessageData(breakCondition, endCondition, CoreFormatter::getExecutionEndMessage, filterCondition);
    }

    static ExecutorFunctionData getWithDefaultMessageDataEndCondition(
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler,
        Predicate<Data<?>> filterCondition
    ) {
        return getWithDefaultMessageData(breakCondition, CoreUtilities::isAllDone, endMessageHandler, filterCondition);
    }

    static ExecutorFunctionData getWithSpecificBreakConditionAndEndCondition(
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition
    ) {
        return getWithDefaultMessageData(breakCondition, endCondition, CoreFormatter::getExecutionEndMessage, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificBreakConditionAndEndMessageHandler(
        TriPredicate<Data<?>, Integer, Integer> breakCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler
    ) {
        return getWithDefaultMessageData(breakCondition, CoreUtilities::isAllDone, endMessageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificBreakConditionAndFilterCondition(TriPredicate<Data<?>, Integer, Integer> breakCondition, Predicate<Data<?>> filterCondition) {
        return getWithDefaultMessageData(breakCondition, CoreUtilities::isAllDone, CoreFormatter::getExecutionEndMessage, filterCondition);
    }

    static ExecutorFunctionData getWithDefaultMessageDataBreakCondition(
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler,
        Predicate<Data<?>> filterCondition
    ) {
        return getWithDefaultMessageData(ExecutorPredicates::isExecuting, endCondition, endMessageHandler, filterCondition);
    }

    static ExecutorFunctionData getWithSpecificEndConditionAndEndMessageHandler(
        QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition,
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler
    ) {
        return getWithDefaultMessageData(ExecutorPredicates::isExecuting, endCondition, endMessageHandler, NullableFunctions::isNotNull);
    }

    static ExecutorFunctionData getWithSpecificEndConditionAndFilterCondition(QuadPredicate<ExecutionStateData, Integer, Integer, Integer> endCondition, Predicate<Data<?>> filterCondition) {
        return getWithDefaultMessageData(ExecutorPredicates::isExecuting, endCondition, CoreFormatter::getExecutionEndMessage, filterCondition);
    }

    static ExecutorFunctionData getWithSpecificEndMessageHandlerAndFilterCondition(QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler, Predicate<Data<?>> filterCondition) {
        return getWithDefaultMessageData(ExecutorPredicates::isExecuting, CoreUtilities::isAllDone, endMessageHandler, filterCondition);
    }

    static ExecutorFunctionData getWithDefaultExitCondition(IGetMessage messageData, QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler) {
        return getWithSpecificMessageDataAndEndMessageHandler(messageData, endMessageHandler);
    }

    static ExecutorFunctionData getWithDefaultExitConditionAndMessageData(
        QuadFunction<ExecutionStateData, String, Integer, Integer, String> endMessageHandler
    ) {
        return getWithDefaultExitCondition(new SimpleMessageData(), endMessageHandler);
    }
}
