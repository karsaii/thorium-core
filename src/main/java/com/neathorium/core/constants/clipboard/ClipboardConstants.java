package com.neathorium.core.constants.clipboard;

import com.neathorium.core.constants.validators.CoreFormatterConstants;
import com.neathorium.core.extensions.namespaces.CoreUtilities;
import com.neathorium.core.records.caster.BasicCastData;
import com.neathorium.core.records.clipboard.ClipboardData;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.HashMap;
import java.util.Map;

public abstract class ClipboardConstants {
    public static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    public static final Map<String, String> COMMANDS = new HashMap<>();
    public static final ClipboardData<String> CLIPBOARD_DATA = new ClipboardData<>(
        new BasicCastData<>(
            CoreFormatterConstants.EMPTY,
            CoreUtilities::castToString
        ),
        CLIPBOARD,
        null,
        DataFlavor.stringFlavor
    );
}
