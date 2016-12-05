package su.gear.walletpad.converter;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.facebook.stetho.urlconnection.StethoURLConnectionManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import su.gear.walletpad.utils.IOUtils;

/**
 * Created by Андрей on 29.11.2016.
 */

public class CurrencyConverter extends AsyncTaskLoader<ConverterResult> {

    private final String TAG = "CurrencyConverter";

    private double amount;
    private String fromCode, toCode;
    private ConverterResult result;

    public CurrencyConverter (Context context, String fromNumCode, String toNumCode, double amount) {
        super (context);

        this.fromCode = fromNumCode;
        this.toCode   = toNumCode;
        this.amount = amount;

        Log.d (TAG, "Constructor created");
    }

    protected void onStartLoading () {
        forceLoad ();
        Log.d (TAG, "onStartLoad");
    }

    public ConverterResult loadInBackground () {
        Log.d (TAG, "Starting loading in back");
        StethoURLConnectionManager connectionManager
                                        = new StethoURLConnectionManager ("API");
        result = new ConverterResult ();

        HttpURLConnection httpConnection = _createHttpConnection ();
        InputStream          inputStream = null;
        Map <String, Double> currencies  = null;

        try {
            connectionManager.preConnect  (httpConnection, null);
            httpConnection.connect        ();
            connectionManager.postConnect ();

            Log.d (TAG, "Before checking response code");
            if (httpConnection.getResponseCode () == 200) {
                Log.d (TAG, "Response code is OK");
                inputStream = httpConnection.getInputStream ();
                inputStream = connectionManager.interpretResponseStream (inputStream);

                currencies = _parseAnswer (inputStream);
                Log.d (TAG, "After parser situation");

                //Stop if everything is bad
                if (currencies == null) {
                    Log.e (TAG, "Failed to parse answer from requst :(");
                    result.setStatus (ConverterResult.Status.PARSE_FAILED);

                    return result;
                }

                //Counting the result
                if (currencies.containsKey (fromCode)) {
                    if (currencies.containsKey (toCode)) {
                        Double from = currencies.get (fromCode);
                        Double to   = currencies.get (toCode);

                        result.setResult (amount * to / from);
                        result.setStatus (ConverterResult.Status.READY);
                    } else {
                        Log.e (TAG, "Currency `" + toCode + "` ratio was not found");
                        result.setStatus (ConverterResult.Status.PARSE_FAILED);
                    }
                } else {
                    Log.e (TAG, "Currency `" + toCode + "` ratio was not found");
                    result.setStatus (ConverterResult.Status.PARSE_FAILED);
                }
            } else {
                Log.e (TAG, "Server answer not 200 code: HTTP Request Code "
                                + httpConnection.getResponseCode ());
                result.setStatus (ConverterResult.Status.PARSE_FAILED);
            }
        } catch (Exception e) {
            Log.e (TAG, "Loading failed due to `" + e.getMessage () + "`");
            //connectionManager.httpExchangeFailed (e);

            if (IOUtils.isConnectionAvailable (getContext (), false)) {
                //Connection is between dead and alive
                result.setStatus (ConverterResult.Status.PARSE_FAILED);
            } else {
                //What can we do without Almighty?
                result.setStatus (ConverterResult.Status.NO_INTERNET);
            }
        }

        Log.d (TAG, "Converter finished");
        return result;
    }

    private Map <String, Double> _parseAnswer (InputStream input) throws Exception {
        String answer = IOUtils.readToString (input, "UTF-8");

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance ();
        XmlPullParser        parser  = factory.newPullParser ();
        parser.setInput (new StringReader (answer));

        Map <String, Double> currencies = new HashMap <> ();

        Log.d (TAG, "Start parsing");
        int eventType = parser.getEventType ();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName ();
                Log.d (TAG, "Now node: " + name);

                if (name.equals ("Cube")) {
                    if (parser.getAttributeCount () == 2) {
                        String currency = parser.getAttributeValue (0);
                        String ration  = parser.getAttributeValue (1);
                        Double value   = Double.parseDouble (ration);

                        Log.d (TAG, "It's a currency node: "
                                        + currency + " - " + ration);
                        currencies.put (currency, value);
                    }
                }
            }

            eventType = parser.next ();
        }

        return currencies;
    }

    private HttpURLConnection _createHttpConnection () {
        String basic = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        return _createHttpConnection (basic);
    }

    private HttpURLConnection _createHttpConnection (String url) {
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) new URL (url).openConnection ();
        } catch (Exception e) {
            Log.e (TAG, "Failed get connection due to `" + e.getMessage () + "`");
        }

        return connection;
    }

}
