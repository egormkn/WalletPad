package su.gear.walletpad;

import android.app.Application;

import com.google.firebase.crash.FirebaseCrash;

public class WalletPadApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // FireBase Crash Reporting
        /*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                FirebaseCrash.report(ex);
            }
        });*/
    }
}
