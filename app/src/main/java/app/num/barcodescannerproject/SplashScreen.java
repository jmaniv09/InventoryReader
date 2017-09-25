package app.num.barcodescannerproject;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by LINTZZZ on 25/09/2017.
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3000);

    }

}
