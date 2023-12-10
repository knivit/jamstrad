package com.tsoft.jamstrad.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ActionableDialog extends JDialog {
    public static final ActionableDialogOption OK_OPTION = new ActionableDialog.ConfirmationOption();
    public static final ActionableDialogOption CANCEL_OPTION = new ActionableDialog.CancellationOption();
    private JComponent mainComponent;
    private List<ActionableDialogOption> dialogOptions;
    private List<ActionableDialogButton> dialogButtons;
    private List<ActionableDialogListener> dialogListeners;

    public ActionableDialog(Window owner, String title, boolean modal, JComponent mainComponent, List<ActionableDialogOption> dialogOptions) {
        super(owner, title, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        this.mainComponent = mainComponent;
        this.dialogOptions = dialogOptions;
        this.dialogButtons = new Vector(dialogOptions.size());
        this.dialogListeners = new Vector();
        this.buildUI();
        this.pack();
        this.setLocationRelativeTo(owner);
        this.setDefaultCloseOperation(2);
        this.addWindowListener(new ActionableDialog.DialogClosingHandler());
    }

    public static ActionableDialog createOkModalDialog(String title, JComponent mainComponent) {
        return createOkModalDialog((Window)null, title, mainComponent);
    }

    public static ActionableDialog createOkModalDialog(Window owner, String title, JComponent mainComponent) {
        List<ActionableDialogOption> dialogOptions = new Vector(2);
        dialogOptions.add(OK_OPTION);
        return new ActionableDialog(owner, title, true, mainComponent, dialogOptions);
    }

    public static ActionableDialog createOkCancelModalDialog(String title, JComponent mainComponent) {
        return createOkCancelModalDialog((Window)null, title, mainComponent);
    }

    public static ActionableDialog createOkCancelModalDialog(Window owner, String title, JComponent mainComponent) {
        List<ActionableDialogOption> dialogOptions = new Vector(2);
        dialogOptions.add(OK_OPTION);
        dialogOptions.add(CANCEL_OPTION);
        return new ActionableDialog(owner, title, true, mainComponent, dialogOptions);
    }

    private void buildUI() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.add(this.getMainComponent(), "Center");
        panel.add(this.createDialogButtonsPanel(), "South");
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        this.add(panel);
    }

    private JComponent createDialogButtonsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 0, 24, 0));
        Iterator var3 = this.getDialogOptions().iterator();

        while(var3.hasNext()) {
            ActionableDialogOption option = (ActionableDialogOption)var3.next();
            ActionableDialogButton button = this.createDialogButton(option);
            this.getDialogButtons().add(button);
            panel.add(button);
        }

        Box box = new Box(0);
        box.add(Box.createHorizontalGlue());
        box.add(panel);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    private ActionableDialogButton createDialogButton(final ActionableDialogOption option) {
        ActionableDialogButton button = new ActionableDialogButton(option);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ActionableDialog dialog = ActionableDialog.this;
                Iterator var4 = dialog.getDialogListeners().iterator();

                while(var4.hasNext()) {
                    ActionableDialogListener listener = (ActionableDialogListener)var4.next();
                    listener.dialogButtonClicked(dialog, option);
                    if (option.isConfirmation()) {
                        listener.dialogConfirmed(dialog);
                    }

                    if (option.isCancellation()) {
                        listener.dialogCancelled(dialog);
                    }
                }

                if (option.isClosingDialog()) {
                    dialog.dispose();
                }

            }
        });
        return button;
    }

    public void setVisible(final boolean visible) {
        if (ModalityType.MODELESS.equals(this.getModalityType())) {
            super.setVisible(visible);
        } else {
            (new Thread(new Runnable() {
                public void run() {
                    ActionableDialog.this.show(visible);
                }
            })).start();
        }

    }

    public void addListener(ActionableDialogListener listener) {
        this.getDialogListeners().add(listener);
    }

    public void removeListener(ActionableDialogListener listener) {
        this.getDialogListeners().remove(listener);
    }

    public JComponent getMainComponent() {
        return this.mainComponent;
    }

    public List<ActionableDialogOption> getDialogOptions() {
        return this.dialogOptions;
    }

    public List<ActionableDialogButton> getDialogButtons() {
        return this.dialogButtons;
    }

    private List<ActionableDialogListener> getDialogListeners() {
        return this.dialogListeners;
    }

    private static class CancellationOption extends ActionableDialogOption {
        public CancellationOption() {
            super("CANCEL", "Cancel");
        }

        public boolean isConfirmation() {
            return false;
        }

        public boolean isCancellation() {
            return true;
        }
    }

    private static class ConfirmationOption extends ActionableDialogOption {
        public ConfirmationOption() {
            super("OK", "OK");
        }

        public boolean isConfirmation() {
            return true;
        }

        public boolean isCancellation() {
            return false;
        }
    }

    private class DialogClosingHandler extends WindowAdapter {
        public DialogClosingHandler() {
        }

        public void windowClosed(WindowEvent event) {
            Iterator var3 = ActionableDialog.this.getDialogListeners().iterator();

            while(var3.hasNext()) {
                ActionableDialogListener listener = (ActionableDialogListener)var3.next();
                listener.dialogClosed(ActionableDialog.this);
            }

        }
    }
}
