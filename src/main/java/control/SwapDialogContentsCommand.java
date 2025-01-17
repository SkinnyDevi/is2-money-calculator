package control;

public class SwapDialogContentsCommand implements Command {
    private final MoneyControl control;

    public SwapDialogContentsCommand(MoneyControl control) {
        this.control = control;
    }

    @Override
    public void execute() {
        control.swapDialogContents();
    }
}