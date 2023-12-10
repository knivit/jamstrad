package com.tsoft.jamstrad.swing;

import java.util.Objects;

public abstract class ActionableDialogOption {
    private String id;
    private String label;

    protected ActionableDialogOption(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.getId()});
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            ActionableDialogOption other = (ActionableDialogOption)obj;
            return Objects.equals(this.getId(), other.getId());
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ActionableDialogOption [id=");
        builder.append(this.getId());
        builder.append(", label=");
        builder.append(this.getLabel());
        builder.append("]");
        return builder.toString();
    }

    public abstract boolean isConfirmation();

    public abstract boolean isCancellation();

    public boolean isClosingDialog() {
        return true;
    }

    public String getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }
}
