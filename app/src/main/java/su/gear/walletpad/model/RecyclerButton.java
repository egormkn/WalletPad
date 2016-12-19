package su.gear.walletpad.model;

public class RecyclerButton implements OperationsListItem {

    /**
     * Текст кнопки
     */
    private final String text;

    public RecyclerButton(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
