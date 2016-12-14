package su.gear.walletpad.model;

/**
 * Разделитель для списков
 */

public class Separator implements OperationsListItem {

    /**
     * Текст разделителя
     */
    private final String text;

    public Separator(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
