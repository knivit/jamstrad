package com.tsoft.jamstrad.swing;

import javax.swing.*;

public class SwingUtils {
    private SwingUtils() {
    }

    public static Icon getIcon(String iconFilepath) {
        return new ImageIcon(ClassLoader.getSystemResource(iconFilepath));
    }
}
