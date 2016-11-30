package su.gear.walletpad.converter;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
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
import java.util.regex.Pattern;

import su.gear.walletpad.utils.IOUtils;

/**
 * Created by Андрей on 29.11.2016.
 */

public class CurrencyConverter extends AsyncTaskLoader <Double> {

    private final String TAG = "CurrencyConverter";

    private double start;
    private String fromCode, toCode;
    private ConverterResult result;

    public CurrencyConverter (Context context, String fromNumCode, String toNumCode, double start) {
        super (context);

        this.result   = ConverterResult.NOT_STARTED;
        this.fromCode = fromNumCode;
        this.toCode   = toNumCode;
        this.start    = start;
    }

    public Double loadInBackground () {
        StethoURLConnectionManager connectionManager
                                        = new StethoURLConnectionManager ("API");

        result = ConverterResult.NOT_STARTED;
        double res  = 0D;

        HttpURLConnection httpConnection = _createHttpConnection ();
        InputStream          inputStream = null;
        Map <String, Double> currencies  = null;

        try {
            connectionManager.preConnect  (httpConnection, null);
            httpConnection.connect        ();
            connectionManager.postConnect ();

            if (httpConnection.getResponseCode () == 200) {
                inputStream = httpConnection.getInputStream ();
                inputStream = connectionManager.interpretResponseStream (inputStream);

                currencies = _parseAnswer (inputStream);
                if (currencies.containsKey (fromCode)) {
                    if (currencies.containsKey (toCode)) {
                        Double from = currencies.get (fromCode);
                        Double to   = currencies.get (toCode);

                        res = start * to / from;
                        result = ConverterResult.LOADED;
                    } else {
                        Log.e (TAG, "Currency `" + toCode + "` ratio was not found");
                        result = ConverterResult.CONVERTING_FALIED;
                    }
                } else {
                    Log.e (TAG, "Currency `" + toCode + "` ratio was not found");
                    result = ConverterResult.CONVERTING_FALIED;
                }
            } else {
                Log.e (TAG, "Server answer not 200 code: HTTP Request Code "
                                + httpConnection.getResponseCode ());
                result = ConverterResult.LOADING_FALIED;
            }
        } catch (Exception e) {
            Log.e (TAG, "Loading failed due to `" + e.getMessage () + "`");
            //connectionManager.httpExchangeFailed (e);

            if (IOUtils.isConnectionAvailable (getContext (), false)) {
                //Connection is between dead and alive
                result = ConverterResult.CONNECTION_FAILED;
            } else {
                //What can we do without Almighty?
                result = ConverterResult.NO_INTERNET;
            }
        }

        if (result == ConverterResult.LOADED) { return res; }
        else                                  { return -1D; }
    }

    public ConverterResult getResult () {
        return result;
    }

    private HttpURLConnection _createHttpConnection () {
        String basic = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
        return _createHttpConnection (basic);
    }

    private Map <String, Double> _parseAnswer (InputStream input) throws Exception {
        String answer = IOUtils.readToString (input, "UTF-8");

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance ();
        XmlPullParser        parser  = factory.newPullParser ();
        parser.setInput (new StringReader (answer));

        Map <String, Double> currencies = new HashMap <> ();

        int eventType = parser.getEventType ();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName ();
                Log.d (TAG, "Now node: " + name);

                if (name.equals ("Cube")) {
                    if (parser.getAttributeCount () == 2) {
                        String currecy = parser.getAttributeValue (0);
                        String ration  = parser.getAttributeValue (1);
                        Double value   = Double.parseDouble (ration);

                        Log.d (TAG, "It's a currency node: "
                                        + currecy + " - " + ration);
                        currencies.put (currecy, value);
                    }
                }
            }
        }

        return null;
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
