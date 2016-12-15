package su.gear.walletpad.model;

/**
 * Объект кошелька
 */

public class Wallet implements WalletsListItem {

    public enum Type {
        CARD, CASH, DEPOSIT, STOCK
    };

    private final long id;

    private final String title;

    private final double amount;

    private final Type type;

    public Wallet (double amount, long id, String title, Type type) {
        this.amount = amount;
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public Type getType() {
        return type;
    }
}
