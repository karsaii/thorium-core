package com.github.karsaii.core.namespaces.command;

import com.github.karsaii.core.constants.CommandRangeDataConstants;
import com.github.karsaii.core.extensions.interfaces.functional.TriFunction;
import com.github.karsaii.core.namespaces.validators.Range;
import com.github.karsaii.core.records.command.CommandRangeData;

public interface CommandRangeDataFactory {
    static CommandRangeData getWithDefaultRangeValues(TriFunction<Integer, Integer, Integer, Boolean> invalidator) {
        return new CommandRangeData(invalidator, CommandRangeDataConstants.MINIMUM_COMMAND_LIMIT, CommandRangeDataConstants.MAXIMUM_COMMAND_LIMIT);
    }

    static CommandRangeData getWithDefaultRangeInvalidator(int min, int max) {
        return new CommandRangeData(Range::isOutOfRange, min, max);
    }

    static CommandRangeData getWithDefaults() {
        return getWithDefaultRangeInvalidator(CommandRangeDataConstants.MINIMUM_COMMAND_LIMIT, CommandRangeDataConstants.MAXIMUM_COMMAND_LIMIT);
    }
}
