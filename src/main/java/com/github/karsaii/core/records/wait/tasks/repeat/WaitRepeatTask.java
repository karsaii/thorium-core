package com.github.karsaii.core.records.wait.tasks.repeat;

import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.records.Data;
import com.github.karsaii.core.records.executor.ExecutionResultData;
import com.github.karsaii.core.records.executor.ExecutionStateData;
import com.github.karsaii.core.records.wait.tasks.regular.WaitTask;
import com.github.karsaii.core.records.wait.tasks.common.WaitTaskCommonData;
import com.github.karsaii.core.records.wait.tasks.common.WaitTaskStateData;

import java.util.concurrent.ScheduledExecutorService;

public class WaitRepeatTask<V> extends WaitTask<ExecutionStateData, DataSupplier<ExecutionResultData<V>>, Data<ExecutionResultData<V>>> {
    public WaitRepeatTask(
        ScheduledExecutorService scheduler,
        WaitTaskCommonData<ExecutionStateData, DataSupplier<ExecutionResultData<V>>, Data<ExecutionResultData<V>>> commonData,
        WaitTaskStateData<ExecutionStateData, Data<ExecutionResultData<V>>> stateData
    ) {
        super(scheduler, commonData, stateData);
    }
}
