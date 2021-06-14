package datasupplier;

import com.github.karsaii.core.constants.validators.CoreFormatterConstants;
import com.github.karsaii.core.extensions.interfaces.functional.boilers.DataSupplier;
import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.DataFunctions;
import com.github.karsaii.core.namespaces.executor.step.CommonSteps;
import com.github.karsaii.core.namespaces.executor.step.StepExecutor;
import com.github.karsaii.core.namespaces.executor.step.StepFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DataSupplierTests {
    private static final String OKAY_MESSAGE = "Step was okay" + CoreFormatterConstants.END_LINE;

    @DisplayName("Can execute some basic stuff")
    @Test
    void canExecuteBasicStuffTest() {
        final var stepOne = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test1", OKAY_MESSAGE));
        final var stepOne2 = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test2", OKAY_MESSAGE));
        final var stepOne3 = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce3", true, "test3", OKAY_MESSAGE));
        final var stepTwo = StepFactory.step((String str) -> DataFactoryFunctions.getWith(str, true, str + "NAME", "The message for " + str), "Johnny Applesauce");
        final var result = StepExecutor.execute("This is smokin.", stepOne, stepOne2, stepOne3, stepTwo).get();

        Assertions.assertTrue(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Conditional Sequence")
    @Test
    void conditionalSequenceTest() {
        final var result = StepExecutor.conditionalSequence(
            StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test1", OKAY_MESSAGE), null),
            StepFactory.step((String str) -> DataFactoryFunctions.getWith(str, true, str + "NAME", "The message for " + str), "Johnny Applesauce"),
            Boolean.class
        ).get();
        Assertions.assertTrue(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Nullsuppliers test - A name and two nulls")
    @Test
    void nullSuppliersTest() {
        final var result = StepExecutor.execute("Empty Executor", (DataSupplier<?>)null, null).get();
        final var messageData = result.message;
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Single step executor")
    @Test
    void singleStepTest() {
        final var step =  StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test1", OKAY_MESSAGE), null);
        final var result = StepExecutor.execute("Name", step).get();
        Assertions.assertTrue(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Parallel step execution")
    @Test
    void parallelStepExecutionTest() {
        final var step = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test1", OKAY_MESSAGE), null);
        final var stepSleep = CommonSteps.sleep(10000);
        final var stepSleepLess = CommonSteps.sleep(3000);

        final var result = CommonSteps.executeParallelTimed(12000, step, stepSleep, stepSleepLess).apply();
        Assertions.assertTrue(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Parallel step timeout")
    @Test
    void parallelStepExecutionTimeoutTest() {
        final var step = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test1", OKAY_MESSAGE), null);
        final var stepSleep = CommonSteps.sleep(10000);
        final var stepSleepLess = CommonSteps.sleep(3000);

        final var result = CommonSteps.executeParallelTimed(9300, step, stepSleep, stepSleepLess).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Parallel step failure")
    @Test
    void parallelStepExecutionFailureTest() {
        final var step = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test1", OKAY_MESSAGE), null);
        final var stepSleep = CommonSteps.sleep(9000);
        final var failureStep = StepFactory.voidStep(() -> {
            CommonSteps.sleep(2000).apply();
            throw new RuntimeException("Throwing stuff");
        });

        final var result = CommonSteps.executeParallelTimed(10000, step, stepSleep, failureStep).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Parallel step failure, nested")
    @Test
    void parallelStepExecutionFailureNestedTest() {
        final var step = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test1", OKAY_MESSAGE), null);
        final var stepSleep = CommonSteps.sleep(9000);
        final var failureStep = StepFactory.voidStep(() -> {
            CommonSteps.sleep(2000).apply();
            throw new RuntimeException("Throwing stuff");
        });

        final var result = CommonSteps.executeParallelTimed(11000, step, CommonSteps.executeParallelTimed(10000, stepSleep, failureStep)).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Parallel step failure - any")
    @Test
    void parallelStepExecutionFailureAnyTest() {
        final var step =  StepFactory.voidStep(() -> {
            CommonSteps.sleep(1000).apply();
            throw new RuntimeException("Throwing stuff1");
        });
        final var stepSleep =  StepFactory.voidStep(() -> {
            CommonSteps.sleep(2000).apply();
            throw new RuntimeException("Throwing stuff2");
        });
        final var failureStep = StepFactory.voidStep(() -> {
            CommonSteps.sleep(3000).apply();
            throw new RuntimeException("Throwing stuff3");
        });

        final var result = CommonSteps.executeParallelEndOnAnyTimed(11000, step, stepSleep, failureStep).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Parallel step pass - any")
    @Test
    void parallelStepExecutionPassAnyTest() {
        final var step =  StepFactory.voidStep(() -> CommonSteps.sleep(1000).apply());
        final var stepSleep =  StepFactory.voidStep(() -> {
            CommonSteps.sleep(2000).apply();
            throw new RuntimeException("Throwing stuff2");
        });
        final var failureStep = StepFactory.voidStep(() -> {
            CommonSteps.sleep(12000).apply();
            throw new RuntimeException("Throwing stuff3");
        });

        final var result = CommonSteps.executeParallelEndOnAnyTimed(11000, step, stepSleep, failureStep).apply();
        Assertions.assertTrue(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("One step failed")
    @Test
    void oneStepFailedTest() {
        final var step = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test1", "Step wasn't okay" + CoreFormatterConstants.END_LINE), null);
        final var stepSleep = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test1", OKAY_MESSAGE), null);
        final var stepSleepLess = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test1", OKAY_MESSAGE), null);

        final var result = CommonSteps.executeParallelTimed(1000, step, stepSleep, stepSleepLess).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("All three steps failed")
    @Test
    void allThreeStepsTest() {
        final var step = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test1", "Step wasn't okay" + CoreFormatterConstants.END_LINE), null);
        final var step2 = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test2", "Step 2 wasn't okay" + CoreFormatterConstants.END_LINE), null);
        final var step3 = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test3", "Step 3 wasn't okay" + CoreFormatterConstants.END_LINE), null);

        final var result = CommonSteps.executeParallelTimed(1000, step, step2, step3).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Two steps failed, first and second")
    @Test
    void twoStepsFailedFirstandSecondTest() {
        final var step = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test1", "Step wasn't okay" + CoreFormatterConstants.END_LINE));
        final var step2 = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test2", "Step 2 wasn't okay" + CoreFormatterConstants.END_LINE));
        final var step3 = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test3", OKAY_MESSAGE));

        final var result = CommonSteps.executeParallelTimed(1000, step, step2, step3).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Two steps failed, first and second, embedded")
    @Test
    void twoStepsFailedFirstandSecondEmbeddedTest() {
        final var step = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test1", "Step wasn't okay" + CoreFormatterConstants.END_LINE));
        final var step2 = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test2", "Step 2 wasn't okay" + CoreFormatterConstants.END_LINE));
        final var parallelStep = CommonSteps.executeParallelTimed(1000, step, step2);
        final var step3 = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test3", OKAY_MESSAGE));

        final var result = CommonSteps.executeParallelTimed(13000, CommonSteps.sleep(12000), parallelStep, step3).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }

    @DisplayName("Two steps failed, first and second, embedded 2")
    @Test
    void twoStepsFailedFirstandSecondEmbeddedTwiceTest() {
        final var step = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test1", "Step wasn't okay" + CoreFormatterConstants.END_LINE));
        final var step2 = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test2", "Step 2 wasn't okay" + CoreFormatterConstants.END_LINE));
        final var parallelStep = CommonSteps.executeParallelTimed(1000, step, step2);
        final var parallelStep2 = CommonSteps.executeParallelTimed(2000, parallelStep, step, step2);
        final var step3 = StepFactory.voidStep((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test3", OKAY_MESSAGE));

        final var result = CommonSteps.executeParallelTimed(11000, CommonSteps.sleep(12000), parallelStep2, step3).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }


    @DisplayName("Two steps failed, first and third")
    @Test
    void twoStepsFailedFirstandThirdTest() {
        final var step = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test1", "Step wasn't okay" + CoreFormatterConstants.END_LINE), null);
        final var step2 = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", true, "test2", OKAY_MESSAGE), null);
        final var step3 = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWith("Applesauce", false, "test3", "Step 3 wasn't okay" + CoreFormatterConstants.END_LINE), null);

        final var result = CommonSteps.executeParallelTimed(1000, step, step2, step3).apply();
        Assertions.assertFalse(result.status, DataFunctions.getFormattedMessage(result));
    }
}
