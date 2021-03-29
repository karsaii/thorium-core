package com.github.karsaii.core.namespaces.factories.wait.tasks.common;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.records.wait.tasks.common.WaitTaskCommonData;
import com.github.karsaii.core.records.wait.tasks.common.WaitTaskStateData;
import com.github.karsaii.core.records.wait.tasks.repeat.WaitRepeatTask;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public interface WaitRepeatTaskFactory {
    static <ReturnType> WaitRepeatTask<ReturnType> getWith(
        ScheduledExecutorService scheduler,
        WaitTaskCommonData<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>, Data<ExecutionResultData<ReturnType>>> commonData,
        WaitTaskStateData<ExecutionStateData, Data<ExecutionResultData<ReturnType>>> stateData
    ) {
        return new WaitRepeatTask<>(scheduler, commonData, stateData);
    }

    static <ReturnType> WaitRepeatTask<ReturnType> getWithDefaultScheduler(
        WaitTaskCommonData<ExecutionStateData, DataSupplier<ExecutionResultData<ReturnType>>, Data<ExecutionResultData<ReturnType>>> commonData,
        WaitTaskStateData<ExecutionStateData, Data<ExecutionResultData<ReturnType>>> stateData
    ) {
        return getWith(Executors.newSingleThreadScheduledExecutor(), commonData, stateData);
    }
}
