package su.gear.walletpad.converter;

/**
 * Created by Андрей on 30.11.2016.
 */

public class ConverterResult {

    public enum Status {
        NOT_STARTED,
        NO_INTERNET,
        PARSE_FAILED,
        READY
    }

    private Status status;
    private double result;
    private String date;

    public ConverterResult () {
        this.status = Status.NOT_STARTED;
        this.result = 0D;
    }

    public void setStatus (Status status) {
        this.status = status;
    }

    public void setResult (double value) {
        this.result = value;
    }

    public void setDate (String date) { this.date = date; }

    public double getResult () {
        if (this.status != Status.READY) {
            return -1D;
        }

        return result;
    }

    public String getDate () {
        return date;
    }

}
