package control;

public enum CommandIdentifiers {
    SWAP_DIALOG_CONTENTS("swap_dialog_contents");

    private final String identifier;

    CommandIdentifiers(String identifier) {
        this.identifier = identifier;
    }

    public String get() {
        return identifier;
    }
}