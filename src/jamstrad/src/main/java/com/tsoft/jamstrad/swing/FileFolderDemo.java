package com.tsoft.jamstrad.swing;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileFolderDemo {
    public FileFolderDemo() {
    }

    public static void main(String[] args) {
        FileFolderInputField field = createFileFolderInputField();
        JFrame frame = new JFrame("Folder input");
        frame.setDefaultCloseOperation(3);
        frame.add(new JLabel("Folder of choice:"), "North");
        frame.add(field, "Center");
        frame.pack();
        frame.setLocationRelativeTo((Component)null);
        frame.setVisible(true);
    }

    private static FileFolderInputField createFileFolderInputField() {
        FileFolderInputField field = new FileFolderInputField(new File("."));
        field.setFolderChooserDialogTitle("Choose a folder");
        field.setEnabled(true);
        field.addListener(new FileFolderInputFieldListener() {
            public void folderChanged(FileFolderInputField inputField) {
                System.out.println("Folder changed: " + inputField.getFolder().getAbsolutePath());
            }
        });
        return field;
    }
}
