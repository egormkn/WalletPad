package su.gear.walletpad.utils;

import android.content.Context;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

import su.gear.walletpad.R;

public final class DateUtils {

    public static SimpleDateFormat getDateFormat(Context context) {
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.getDefault());
        dateFormatSymbols.setMonths(context.getResources().getStringArray(R.array.months));
        return new SimpleDateFormat("dd MMMM yyyy", dateFormatSymbols);
    }

    private DateUtils() {
    }
}
