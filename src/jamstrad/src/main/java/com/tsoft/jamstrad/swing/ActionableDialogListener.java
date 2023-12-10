package com.tsoft.jamstrad.swing;

public interface ActionableDialogListener {
    void dialogClosed(ActionableDialog var1);

    void dialogButtonClicked(ActionableDialog var1, ActionableDialogOption var2);

    void dialogConfirmed(ActionableDialog var1);

    void dialogCancelled(ActionableDialog var1);
}
