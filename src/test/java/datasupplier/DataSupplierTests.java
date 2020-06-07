package datasupplier;

import com.github.karsaii.core.namespaces.DataFactoryFunctions;
import com.github.karsaii.core.namespaces.executor.step.StepExecutor;
import com.github.karsaii.core.namespaces.executor.step.StepFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DataSupplierTests {
    @DisplayName("Can execute some basic stuff")
    @Test
    void canExecuteBasicStuffTest() {
        final var stepOne = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWithNameAndMessage("Applesauce", true, "test1", "Step was okay"), null);
        final var stepOne2 = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWithNameAndMessage("Applesauce", true, "test2", "Step was okay"), null);
        final var stepOne3 = StepFactory.step((Void nothing) -> DataFactoryFunctions.getWithNameAndMessage("Applesauce3", true, "test3", "Step was okay"), null);
        final var stepTwo = StepFactory.step((String str) -> DataFactoryFunctions.getWithNameAndMessage(str, true, str + "NAME", "The message for " + str), "Johnny Applesauce");
        final var result = StepExecutor.execute("This is smokin.", stepOne, stepOne2, stepOne3, stepTwo).get();

        Assertions.assertTrue(result.status, result.message.toString());
    }

    @DisplayName("Conditional Sequence")
    @Test
    void conditionalSequenceTest() {
        final var result = StepExecutor.conditionalSequence(
            StepFactory.step((Void nothing) -> DataFactoryFunctions.getWithNameAndMessage("Applesauce", true, "test1", "Step was okay"), null),
            StepFactory.step((String str) -> DataFactoryFunctions.getWithNameAndMessage(str, true, str + "NAME", "The message for " + str), "Johnny Applesauce"),
            Boolean.class
        ).get();
        Assertions.assertTrue(result.status, result.message.toString());
    }

    @DisplayName("I don't know")
    @Test
    void emptyExecutorTest() {
        final var result = StepExecutor.execute("Empty Executor", null, null).get();
        Assertions.assertTrue(result.status, result.message.toString());
    }

    @DisplayName("Single step executor")
    @Test
    void singleStepTest() {
        final var step =  StepFactory.step((Void nothing) -> DataFactoryFunctions.getWithNameAndMessage("Applesauce", true, "test1", "Step was okay"), null);
        final var result = StepExecutor.execute("Name", step).get();
        Assertions.assertTrue(result.status, result.message.toString());
    }
}
