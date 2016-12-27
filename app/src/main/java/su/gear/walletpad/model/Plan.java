package su.gear.walletpad.model;

import com.google.firebase.database.DataSnapshot;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import su.gear.walletpad.R;

public class Plan implements PlansListItem {

    public enum Type {
        GIFT,     //
        LUXURY,   //  HERE SHOULD BE MORE OPTIONS
        REST,     //
        TAX,      //  TODO: add more options :/
        OTHER     //
        ;

        public static Type fetchType (String type) {
            if (type != null && type.length () > 0) {
                type = type.toLowerCase ().trim ();

                if (type.equals ("gift"))   { return GIFT; }
                if (type.equals ("luxury")) { return LUXURY; }
                if (type.equals ("rest"))   { return REST; }
                if (type.equals ("tax"))    { return TAX; }
            }

            return OTHER;
        }

        public static Type fetchType (int index) {
            if (index >= 0 && index < values ().length) {
                return values () [index];
            }

            return null;
        }
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
    private final Currency currency;

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

    public Plan (DataSnapshot snapshot) {
        this.id = 0L;
        this.timestamp   = 0L;
        this.type        = Type.fetchType ((String) snapshot.child ("type").getValue());
        this.amount      = Double.parseDouble ((String) snapshot.child ("amount").getValue ());
        //System.out.println ((String) snapshot.child ("amount").getValue ());
        this.currency    = Currency.getInstance ((String) snapshot.child ("currency").getValue ());
        this.title       = (String) snapshot.child ("title").getValue ();
        this.description = (String) snapshot.child ("description").getValue ();
    }

    public Plan (long id,
                 long time,
                 Type type,
                 double amount,
                 Currency currency,
                 String title,
                 String description) {

        this.id          = id;
        this.timestamp   = time;
        this.amount      = amount;
        this.type        = type == null ? Type.OTHER : type;
        this.currency    = (currency != null)
                                ? currency
                                : Currency.getInstance ("RUB");
        this.title       = (title != null && title.length () > 0)
                                ? title
                                : "";
        this.description = (description != null && description.length () > 0)
                                ? description
                                : "";
    }

    /* GETS */

    public Map <String, Object> toMap () {
        Map <String, Object> result = new HashMap <> ();
        result.put ("time",        timestamp);
        result.put ("amount",      amount + "");
        result.put ("type",        type);
        result.put ("currency",    currency.toString ());
        result.put ("title",       title);
        result.put ("description", description);

        return result;
    }

    public double amount      () { return amount; }
    public Currency currency  () { return currency; }
    public String description () { return description; }

    public long   id          () { return id; }
    public long   timestamp   () { return timestamp; }

    public String title       () { return title; }
    public Type   type        () { return type; }
}
