package su.gear.walletpad.model;

import android.text.TextUtils;

import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Объект операции (доход/расход/перевод...)
 */

public class Operation implements OperationsListItem /* Parcelable */ {

    public enum Type {
        INCOME,
        EXPENSE,
        TRANSFER
    }

    public static Type toType(int id) {
        return Type.values()[id];
    }

    /**
     * ID операции
     */
    private final String id;

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

    public Operation(String id, Type type, String currencyCode, double amount, String description, String category, List<String> tags, long timestamp) {
        this.id = id;
        this.type = type;
        this.currency = Currency.getInstance(currencyCode);
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.tags = tags;
        this.date = new Date(timestamp);
    }

    public String getId() {
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("amount", String.valueOf(amount));
        map.put("type", "income");
        map.put("currency", currency.getCurrencyCode());
        map.put("description", description);
        map.put("category", category);
        map.put("tags", TextUtils.join(",", tags));
        map.put("date", String.valueOf(date.getTime()));
        return map;
    }
}