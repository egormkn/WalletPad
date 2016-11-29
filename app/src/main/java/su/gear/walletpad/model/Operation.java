package su.gear.walletpad.model;

import java.util.List;

/**
 * Объект операции (доход/расход/перевод...)
 */

public class Operation implements RecyclerViewItem /* Parcelable */ {

    public enum Type {
        INCOME, EXPENSE, TRANSFER
    };

    /**
     * ID операции
     */
    private final long id;

    /**
     * Тип операции (доход/расход/перевод...)
     */
    private final Type type;

    /**
     * Валюта операции
     */
    private final String currency;

    /**
     * Сумма операции в указанной валюте
     */
    private final double sum;

    /**
     * Описание операции
     */
    private final String description;

    /**
     * Категория расхода или дохода
     */
    private final String category;

    /**
     * Теги операции
     */
    private final List<String> tags;

    /**
     * UNIX timestamp
     */
    private final long timestamp;

    public Operation(long id, Type type, String currency, double sum, String description, String category, List<String> tags, long timestamp) {
        this.id = id;
        this.type = type;
        this.currency = currency;
        this.sum = sum;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public double getSum() {
        return sum;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getCurrency() {
        return currency;
    }

    public List<String> getTags() {
        return tags;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Type getType() {
        return type;
    }
}