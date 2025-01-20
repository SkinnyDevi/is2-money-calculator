package software.ulpgc.moneycalculator.control;

public class SwapDialogContentsCommand implements Command {
    private final MoneyPresenter presenter;

    public SwapDialogContentsCommand(MoneyPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        presenter.swapDialogContents();
    }
}