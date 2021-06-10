package com.github.karsaii.core.records.wait.tasks;

import com.github.karsaii.core.records.wait.WaitData;
import com.github.karsaii.core.records.wait.WaitTimeData;
import com.github.karsaii.core.records.wait.tasks.common.WaitTaskCommonData;

public class VoidWaitData<U, ReturnType> extends WaitData<Void, U, ReturnType> {
    public VoidWaitData(WaitTaskCommonData<Void, U, ReturnType> taskData, String conditionMessage, WaitTimeData timeData) {
        super(taskData, conditionMessage, timeData);
    }
}
