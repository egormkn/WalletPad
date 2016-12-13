package su.gear.walletpad.model;

/**
 * Объект даты для отображения в качестве
 * разделителя для списка операций
 */

public class Date implements OperationsListItem {

    /**
     * Дата операции
     */
    private final long timestamp;

    public Date(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
