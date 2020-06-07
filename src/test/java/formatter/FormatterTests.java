package formatter;

import com.github.karsaii.core.constants.CommandRangeDataConstants;
import com.github.karsaii.core.namespaces.validators.CoreFormatter;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Objects;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class FormatterTests {
    @DisplayName("Default command range")
    @Test
    void defaultCommandRangeTest() {
        final var result = CoreFormatter.getCommandRangeParameterMessage(CommandRangeDataConstants.DEFAULT_RANGE);
        Assertions.assertTrue(isBlank(result), result);
    }

    @DisplayName("isNumberConditionCore with Default Executor Range")
    @Test
    void isNumberConditionCoreWithDefaultCommandRangeTest() {
        final var min = CommandRangeDataConstants.DEFAULT_RANGE.min;
        final var result = CoreFormatter.isNumberConditionCore(min > 0, min, 0, "Range minimum", "more than", "isMoreThanExpected");
        Assertions.assertTrue(result.status, result.object + " Message:  " + result.message);
    }

    public static Stream<Arguments> isLessThanExpectedProvider() {
        return Stream.of(
            Arguments.of("Zero less than one, parameter x", 0, 1, "x", true, "isLessThanExpected: Parameters were okay.\n"),
            Arguments.of("One isn't less than Zero, parameter x", 1, 0, "x", false, "isLessThanExpected: Parameters werenot okay. x(\"1\") wasnot less than expected(\"0\").\n"),
            Arguments.of("0 equals 0, result is false, parameter x", 0, 0, "x", false, "isLessThanExpected: Parameters werenot okay. x(\"0\") wasnot less than expected(\"0\").\n"),
            Arguments.of("Any, empty string parameter name", 0, 1, "", false, "isLessThanExpected:  There were parameter issue(s):\nName of the parameter parameter was blank, empty or null.\n"),
            Arguments.of("Any, null parameter name", 0, 1, null, false, "isLessThanExpected:  There were parameter issue(s):\nName of the parameter parameter was blank, empty or null.\n")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("isLessThanExpectedProvider")
    public void isLessThanExpected(String name, int number, int expected, String parameterName, boolean expectedStatus, String expectedMessage) {
        final var result = CoreFormatter.isLessThanExpected(number, expected, parameterName);
        final var message = result.message.toString();
        Assertions.assertTrue(
            (
                Objects.equals(result.status, expectedStatus) &&
                Objects.equals(message, expectedMessage)
            ),
            message
        );
    }
}
