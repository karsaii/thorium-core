package com.neathorium.core.clipboard;

import com.neathorium.core.namespaces.DataFunctions;
import com.neathorium.core.namespaces.clipboard.ClipboardFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClipboardTests {
    @DisplayName("Copy to clipboard")
    @Test
    void oneFailsSecond() {
        final var result = ClipboardFunctions.copyToClipboard("Test String");
        Assertions.assertTrue(result.status, DataFunctions.getFormattedMessage(result));
    }
}
