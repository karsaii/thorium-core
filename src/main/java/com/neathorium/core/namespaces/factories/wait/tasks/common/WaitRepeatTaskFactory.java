package com.neathorium.core.namespaces.factories.wait.tasks.common;

import com.neathorium.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.neathorium.core.records.Data;
import com.neathorium.core.records.executor.ExecutionResultData;
import com.neathorium.core.records.executor.ExecutionStateData;
import com.neathorium.core.records.wait.tasks.common.WaitTaskCommonData;
import com.neathorium.core.records.wait.tasks.common.WaitTaskStateData;
import com.neathorium.core.records.wait.tasks.repeat.WaitRepeatTask;

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
