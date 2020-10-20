package com.github.karsaii.core.constants.clipboard;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.util.HashMap;
import java.util.Map;

public abstract class ClipboardConstants {
    public static final Clipboard CLIPBOARD = Toolkit.getDefaultToolkit().getSystemClipboard();
    public static final Map<String, String> COMMANDS = new HashMap<>();
}
