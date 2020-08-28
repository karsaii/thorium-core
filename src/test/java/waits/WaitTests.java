package waits;

import com.github.karsaii.core.exceptions.WaitTimeoutException;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.executor.step.StepExecutor;
import com.github.karsaii.core.namespaces.executor.step.StepFactory;
import com.github.karsaii.core.namespaces.predicates.DataPredicates;
import com.github.karsaii.core.namespaces.wait.Wait;
import com.github.karsaii.core.namespaces.factories.wait.WaitTimeDataFactory;
import com.github.karsaii.core.records.wait.WaitData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

public class WaitTests {
    private static int count1 = 0;
    private static int increaseAndGetCount1() {
        return ++count1;
    }

    private static int count2 = 0;
    private static int increaseAndGetCount2() {
        return ++count2;
    }

    private static int count3 = 0;
    private static int increaseAndGetCount3() {
        return ++count3;
    }

    private static int count4 = 0;
    private static int increaseAndGetCount4() {
        return ++count4;
    }

    @DisplayName("Wait Repeat - one always fails second")
    @Test
    void oneFailsSecond() {
        final var countStep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getBoolean(increaseAndGetCount1() == 3, "test1", "Step was okay"), null);
        final var trueStringStep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWithNameAndMessage("Applesauce", false, "test2", "StringStep was oookay"), null);
        final var steps = StepExecutor.executeState("waitRepeat Test result message", countStep, trueStringStep);
        final var waitData = new WaitData<>(steps, DataPredicates::isExecutionValidNonFalse, "Steps passed", WaitTimeDataFactory.getWithDefaultClock(100, 1000));

        Assertions.assertThrows(WaitTimeoutException.class, () -> Wait.repeatWithDefaultState(waitData));
    }

    @DisplayName("Wait Repeat - one always fails first")
    @Test
    void oneFailsFirst() {
        final var countStep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getBoolean(increaseAndGetCount2() == 3, "test1", "Step was okay"), null);
        final var trueStringStep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWithNameAndMessage("Applesauce", false, "test2", "StringStep was oookay"), null);
        final var steps = StepExecutor.executeState("waitRepeat Test result message", trueStringStep, countStep);
        final var waitData = new WaitData<>(steps, DataPredicates::isExecutionValidNonFalse, "Steps passed", WaitTimeDataFactory.getWithDefaultClock(100, 1000));

        Assertions.assertThrows(WaitTimeoutException.class, () -> Wait.repeatWithDefaultState(waitData));
    }

    @DisplayName("Wait Repeat - none fails over time")
    @Test
    @Tags(@Tag("slow"))
    void noneFails() {
        final var countStep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getBoolean(increaseAndGetCount3() == 3, "test1", "Step was okay"), null);
        final var trueStringStep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWithNameAndMessage("Applesauce", true, "test2", "StringStep was oookay"), null);
        final var steps = StepExecutor.executeState("waitRepeat Test result message", countStep, trueStringStep);
        final var waitData = new WaitData<>(steps, DataPredicates::isExecutionValidNonFalse, "Steps passed", WaitTimeDataFactory.getWithDefaultClock(100, 10000));
        final var result = Wait.repeatWithDefaultState(waitData);

        Assertions.assertTrue(result.status, result.message.toString());
    }

    @DisplayName("Wait Repeat - both always fail")
    @Test
    void waitRepeatTest() {
        final var countStep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getBoolean(increaseAndGetCount4() < -1, "test1", "Step was okay"), null);
        final var trueStringStep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWithNameAndMessage("Applesauce", false, "test2", "StringStep was oookay"), null);
        final var steps = StepExecutor.executeState("waitRepeat Test result message", countStep, trueStringStep);
        final var waitData = new WaitData<>(steps, DataPredicates::isExecutionValidNonFalse, "Steps passed", WaitTimeDataFactory.getWithDefaultClock(100, 1000));

        Assertions.assertThrows(WaitTimeoutException.class, () -> Wait.repeatWithDefaultState(waitData));
    }

    @DisplayName("Wait Repeat - single one always fails")
    @Test
    void singleOneAlwaysFailsTest() {
        final var countStep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getBoolean(increaseAndGetCount4() < -1, "test1", "Step was okay"), null);
        final var steps = StepExecutor.executeState("waitRepeat Test result message", countStep);
        final var waitData = new WaitData<>(steps, DataPredicates::isExecutionValidNonFalse, "Steps passed", WaitTimeDataFactory.getWithDefaultClock(100, 1000));

        Assertions.assertThrows(WaitTimeoutException.class, () -> Wait.repeatWithDefaultState(waitData));
    }
}
