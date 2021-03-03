package com.github.karsaii.core.namespaces.executor.step;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.wait.Wait;
import com.github.karsaii.core.records.Data;

import java.util.function.Function;

public interface CommonSteps {
    private static Function<Void, Data<Boolean>> executeParallelTimedCore(int duration, DataSupplier<?>... steps) {
        return (v) -> StepExecutor.execute(duration, steps);
    }

    private static Data<Boolean> sleepCore(int duration) {
        Wait.sleep(duration);
        return DataFactoryFunctions.getBoolean(true, "sleep", "Sleep(\"" + duration + "\" milliseconds) " + CoreFormatterConstants.WAS_SUCCESSFUL);
    }

    private static Function<Void, Data<Boolean>> sleepCoreF(int duration) {
        return (v) -> sleepCore(duration);
    }

    static DataSupplier<Boolean> executeParallelTimed(int duration, DataSupplier<?>... steps) {
        return StepFactory.voidStep(executeParallelTimedCore(duration, steps));
    }

    static DataSupplier<Boolean> sleep(int duration) {
        return StepFactory.voidStep(sleepCoreF(duration));
    }
}
