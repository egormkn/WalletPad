package su.gear.walletpad.model;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.google.firebase.database.DataSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import su.gear.walletpad.R;

/**
 * Объект операции (доход/расход/перевод...)
 */

public class Operation implements OperationsListItem {

    private final String id;          // ID операции в базе данных
    private final Type type;          // Тип операции (доход/расход/перевод...)
    private final Date date;          // Дата операции
    private final double amount;      // Сумма операции в указанной валюте
    private final Currency currency;  // Валюта операции
    private final String wallet;      // Кошелёк операции
    private final String description; // Описание операции
    private final String category;    // Категория операции
    private final List<String> tags;  // Теги операции

    public Operation(DataSnapshot snapshot) {
        this.amount = Double.parseDouble((String) snapshot.child(TAG_AMOUNT).getValue());
        this.id = snapshot.getKey();
        this.type = Type.valueOf((String) snapshot.child(TAG_TYPE).getValue());
        Date date;
        try {
            date = dateFormat.parse((String) snapshot.child(TAG_DATE).getValue());
        } catch (ParseException e) {
            date = new Date();
        }
        this.date = date;
        this.currency = Currency.getInstance((String) snapshot.child(TAG_CURRENCY).getValue());
        this.wallet = (String) snapshot.child(TAG_WALLET).getValue();
        this.description = (String) snapshot.child(TAG_DESCRIPTION).getValue();
        this.category = (String) snapshot.child(TAG_CATEGORY).getValue();
        this.tags = new ArrayList<>();
    }

    public Operation(double amount, String id, Type type, Date date, Currency currency, String wallet, String description, String category, List<String> tags) {
        this.amount = amount;
        this.id = id;
        this.type = type;
        this.date = date;
        this.currency = currency;
        this.wallet = wallet;
        this.description = description;
        this.category = category;
        this.tags = tags;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    public Type getType() {
        return type;
    }

    public String getWallet() {
        return wallet;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(TAG_TYPE, type.toString());
        map.put(TAG_DATE, dateFormat.format(date));
        map.put(TAG_AMOUNT, String.valueOf(amount));
        map.put(TAG_CURRENCY, currency.getCurrencyCode());
        map.put(TAG_WALLET, wallet);
        map.put(TAG_DESCRIPTION, description);
        map.put(TAG_CATEGORY, category);
        map.put(TAG_TAGS, tags);
        return map;
    }

    private static final String TAG_TYPE = "type";
    private static final String TAG_DATE = "date";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_CURRENCY = "currency";
    private static final String TAG_WALLET = "wallet";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_TAGS = "tags";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd.HHmmss", Locale.US);

    public enum Type {
        INCOME,
        EXPENSE,
        TRANSFER
    }

    public static Type toType(int id) {
        return Type.values()[id];
    }

    public enum Category {

        FOOD(R.string.category_expense_food, R.drawable.ic_default, R.color.flatColor1),
        CLOTHES(R.string.category_expense_clothes, R.drawable.category_expense_clothes, R.color.flatColor2),
        HOUSE(R.string.category_expense_house, R.drawable.ic_default, R.color.flatColor3),
        MEDICINE(R.string.category_expense_medicine, R.drawable.ic_default, R.color.flatColor4),
        TRANSPORT(R.string.category_expense_transport, R.drawable.ic_default, R.color.flatColor5),
        EDUCATION(R.string.category_expense_education, R.drawable.ic_default, R.color.flatColor6),
        ENTERTAINMENT(R.string.category_expense_entertainment, R.drawable.ic_default, R.color.flatColor6),
        WORK(R.string.category_expense_work, R.drawable.ic_default, R.color.flatColor6),
        INTERNET(R.string.category_expense_internet, R.drawable.ic_default, R.color.flatColor6),
        GIFTS(R.string.category_expense_gifts, R.drawable.ic_default, R.color.flatColor6),
        HOBBY(R.string.category_expense_hobby, R.drawable.ic_default, R.color.flatColor6),
        OTHER(R.string.category_expense_other, R.drawable.ic_default, R.color.flatColor6);

        @StringRes
        private final int title;

        @DrawableRes
        private final int icon;

        @ColorRes
        private final int color;

        Category(int title, int icon, int color) {
            this.title = title;
            this.icon = icon;
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public int getIcon() {
            return icon;
        }

        public String getTitle(Context context) {
            return context.getResources().getString(title);
        }
    }
}