package su.gear.walletpad.model;

/**
 * Объект кошелька
 */

public class Wallet implements WalletsListItem {

    public enum Type {
        CARD,
        CASH,
        DEPOSIT,
        STOCK,
        OTHER
    };

    /**
     * Unique identification of plan
     * It used to fetch this line from database
     * */
    private final long id;

    /**
     * The main idea of plan
     * (It will be displayed in bold)
     * */
    private final String title;

    /**
     * The amount that is kept in wallet
     * This value is in Conventional Units
     * */
    private final double amount;

    /**
     * Type of wallet
     * On this depends icon in a circle
     * (And might be other features)
     */
    private final Type type;

    public Wallet (double amount, long id, String title, Type type) {
        this.amount = amount;
        this.id     = id;
        this.title  = (title != null && title.length () > 0)
                        ? title
                        : "";
        this.type   = (type != null) ? type : Type.OTHER;
    }

    public double amount() { return amount; }

    public String title () { return title; }

    public Type   type  () { return type; }
}
