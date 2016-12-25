package su.gear.walletpad;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;

public class WalletPadApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        // FireBase Crash Reporting
        /*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                FirebaseCrash.report(ex);
            }
        });*/
    }
}
