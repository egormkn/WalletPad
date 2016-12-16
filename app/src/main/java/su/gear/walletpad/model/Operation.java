package su.gear.walletpad.model;

import java.util.Currency;
import java.util.Date;
import java.util.List;

/**
 * Объект операции (доход/расход/перевод...)
 */

public class Operation implements OperationsListItem /* Parcelable */ {

    public enum Type {
        INCOME, EXPENSE, TRANSFER;
    }

    public static Type getType(int id) {
        return Type.values()[id];
    }

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
    private final Currency currency;

    /**
     * Сумма операции в указанной валюте
     */
    private final double amount;

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
     * UNIX date
     */
    private final Date date;

    public Operation(long id, Type type, String currencyCode, double amount, String description, String category, List<String> tags, long timestamp) {
        this.id = id;
        this.type = type;
        this.currency = Currency.getInstance(currencyCode);
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.date = new Date(timestamp);
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Currency getCurrency() {
        return currency;
    }

    public List<String> getTags() {
        return tags;
    }

    public Date getDate() {
        return date;
    }

    public Type getType() {
        return type;
    }
}