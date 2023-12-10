package com.tsoft.jamstrad.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class FileFolderInputField extends JPanel {
    private File folder;
    private JLabel folderLabel;
    private JLabel folderControl;
    private String folderChooserDialogTitle;
    private static final String DEFAULT_DIALOG_TITLE = "Select a folder";
    private List<FileFolderInputFieldListener> folderListeners;

    public FileFolderInputField() {
        this((File)null);
    }

    public FileFolderInputField(File folder) {
        super(new BorderLayout(4, 0));
        this.folder = folder;
        this.folderListeners = new Vector();
        this.buildUI();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.getFolderLabel().setEnabled(enabled);
        this.getFolderControl().setEnabled(enabled);
    }

    public void addListener(FileFolderInputFieldListener listener) {
        this.getFolderListeners().add(listener);
    }

    public void removeListener(FileFolderInputFieldListener listener) {
        this.getFolderListeners().remove(listener);
    }

    private void buildUI() {
        this.setFolderLabel(this.createFolderLabel());
        this.setFolderControl(this.createFolderControl());
        this.updateFolderLabel();
        this.add(this.getFolderLabel(), "Center");
        this.add(this.getFolderControl(), "East");
    }

    private JLabel createFolderLabel() {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(2);
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        label.setFont(label.getFont().deriveFont(0));
        return label;
    }

    private JLabel createFolderControl() {
        Icon icon = SwingUtils.getIcon("org/maia/swing/icons/io/folder24.png");
        JLabel control = new JLabel(icon);
        control.addMouseListener(new FileFolderInputField.FolderControlClickHandler());
        return control;
    }

    private void updateFolderLabel() {
        String text = this.getFolder() != null ? this.getFolder().getAbsolutePath() : "";
        this.getFolderLabel().setText(text);
        this.getFolderLabel().setToolTipText(text);
    }

    public File getFolder() {
        return this.folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
        this.updateFolderLabel();
        Iterator var3 = this.getFolderListeners().iterator();

        while(var3.hasNext()) {
            FileFolderInputFieldListener listener = (FileFolderInputFieldListener)var3.next();
            listener.folderChanged(this);
        }

    }

    private JLabel getFolderLabel() {
        return this.folderLabel;
    }

    private void setFolderLabel(JLabel folderLabel) {
        this.folderLabel = folderLabel;
    }

    private JLabel getFolderControl() {
        return this.folderControl;
    }

    private void setFolderControl(JLabel folderControl) {
        this.folderControl = folderControl;
    }

    public String getFolderChooserDialogTitle() {
        return this.folderChooserDialogTitle;
    }

    public void setFolderChooserDialogTitle(String title) {
        this.folderChooserDialogTitle = title;
    }

    private List<FileFolderInputFieldListener> getFolderListeners() {
        return this.folderListeners;
    }

    private class FolderControlClickHandler extends MouseAdapter {
        public FolderControlClickHandler() {
        }

        public void mouseClicked(MouseEvent event) {
            if (this.isEnabled()) {
                JFileChooser fileChooser = this.buildFileChooser();
                int returnValue = fileChooser.showDialog(FileFolderInputField.this, "Select");
                if (returnValue == 0) {
                    FileFolderInputField.this.setFolder(fileChooser.getSelectedFile());
                }
            }

        }

        public void mouseEntered(MouseEvent event) {
            if (this.isEnabled()) {
                FileFolderInputField.this.setCursor(Cursor.getPredefinedCursor(12));
            }

        }

        public void mouseExited(MouseEvent event) {
            if (this.isEnabled()) {
                FileFolderInputField.this.setCursor(Cursor.getDefaultCursor());
            }

        }

        private JFileChooser buildFileChooser() {
            JFileChooser fileChooser = new JFileChooser(FileFolderInputField.this.getFolder());
            String title = FileFolderInputField.this.getFolderChooserDialogTitle();
            if (title == null) {
                title = "Select a folder";
            }

            fileChooser.setDialogTitle(title);
            fileChooser.setFileSelectionMode(1);
            return fileChooser;
        }

        private boolean isEnabled() {
            return FileFolderInputField.this.isEnabled();
        }
    }
}
