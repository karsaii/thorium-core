package com.neathorium.core.namespaces.command;

import com.neathorium.core.constants.CommandRangeDataConstants;
import com.neathorium.core.extensions.interfaces.functional.TriFunction;
import com.neathorium.core.namespaces.validators.Range;
import com.neathorium.core.records.command.CommandRangeData;

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
