package com.tsoft.jamstrad.swing;

import javax.swing.*;

public class ActionableDialogButton extends JButton {

    private ActionableDialogOption option;

    public ActionableDialogButton(ActionableDialogOption option) {
        super(option.getLabel());
        this.option = option;
    }

    public ActionableDialogOption getOption() {
        return this.option;
    }
}
