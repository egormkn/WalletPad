package su.gear.walletpad.model;

import su.gear.walletpad.R;

/**
 * Created by Андрей on 15.12.2016.
 */

public class Plan implements PlansListItem {

    public enum Type {
        GIFT,     //
        LUXURY,   //  HERE SHOULD BE MORE OPTIONS
        REST,     //
        TAX,      //  TODO: add more options :/
        OTHER     //
    }

    /**
     * Unique identification of plan
     * It used to fetch this line from database
     * */
    private final long id;

    /**
     * Time of adding plan on desc
     * */
    private final long timestamp;

    /**
     * Type of plan
     * On this depends icon in a circle
     * (And might be other features)
     */
    private final Type type;

    /**
     * The amount that is planned to spent
     * This value is in Conventional Units
     * */
    private final double amount;

    /**
     * In what currency converted amount
     * for this plan that given above
     * */
    private final String currency;

    /**
     * The main idea of plan
     * (It will be displayed in bold)
     * */
    private final String title;

    /**
     * The full description of idea to spent money
     * (In preview it will be erased to one line)
     * */
    private final String description;

    public Plan (long id,
                 long time,
                 Type type,
                 double amount,
                 String currency,
                 String title,
                 String description) {

        this.id          = id;
        this.timestamp   = time;
        this.amount      = amount;
        this.type        = type == null ? Type.OTHER : type;
        this.currency    = (currency != null && currency.length () > 0)
                                ? currency
                                : /*R.string.default_currency*/ "RUB";
        this.title       = (title != null && title.length () > 0)
                                ? title
                                : "";
        this.description = (description != null && description.length () > 0)
                                ? description
                                : "";
    }

    /* GETS */

    public double amount      () { return amount; }
    public String currency    () { return currency; }
    public String description () { return description; }

    public long   id          () { return id; }
    public long   timestamp   () { return timestamp; }

    public String title       () { return title; }
    public Type   type        () { return type; }

}
