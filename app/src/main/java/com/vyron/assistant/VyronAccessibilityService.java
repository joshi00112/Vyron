package com.vyron.assistant;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

/**
 * Starter accessibility service. It only reports that it is enabled.
 * Screen inspection/action orchestration must be added and tested carefully
 * before using it for automated interactions.
 */
public class VyronAccessibilityService extends AccessibilityService {
    private static VyronAccessibilityService instance;

    public static boolean isConnected() { return instance != null; }

    @Override public void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        // Intentionally no continuous screen capture or content upload.
        // Future screen reading should be initiated by an explicit user command.
    }

    @Override public void onInterrupt() { }

    @Override public void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    public String getAccessibleWindowSummary() {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return "No accessible window is available.";
        StringBuilder out = new StringBuilder();
        appendText(root, out, 0);
        root.recycle();
        return out.toString();
    }

    private void appendText(AccessibilityNodeInfo node, StringBuilder out, int depth) {
        if (node == null || depth > 20 || out.length() > 8000) return;
        CharSequence text = node.getText();
        CharSequence desc = node.getContentDescription();
        if (text != null && text.length() > 0) out.append(text).append('\n');
        else if (desc != null && desc.length() > 0) out.append(desc).append('\n');
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                appendText(child, out, depth + 1);
                child.recycle();
            }
        }
    }
}
