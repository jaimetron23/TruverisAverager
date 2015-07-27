package com.jameswolcott.omgandroid;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    TextView mainTextView;

    Button mainButton;
    EditText mainEditText;

    URL url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Open permissions on StrictMode.ThreadPolicy
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Accessing TextView defined in layout XML and then setting text
        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainTextView.setText("Calculating average");

        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);

        mainEditText = (EditText) findViewById(R.id.main_edittext);
        mainEditText.setText("http://tamentis.com/tmp/truveris/dc_acs_2009_5yr_g00__data1.txt");
//        mainEditText.setText("http://tamentis.com/tmp/truveris/dc_acs_2009_5yr_g00__data1.txt");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu.
        // Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
//        String urlToTest = "http://tamentis.com/tmp/truveris/dc_acs_2009_5yr_g00__data1.txt";
        Editable newText = mainEditText.getText();
        String urlToTest = newText.toString();
        String average = "";
       average = runAverage(urlToTest);
        String post = "Resulting Average is : ";
        mainTextView.setText(post + average);
    }

    public String urlToTest (String url) {
        String finalUrl = url;
        return finalUrl;
    }


    public String runAverage(String urlToTest) {
        String result = "";
        try {
            // Create a URL for the desired page
            URL url = new URL (urlToTest);

            HttpURLConnection destination = testRedirect(url);
            System.err.println("finished redirect");
            BRRead file = new BRRead(destination);

            double end = file.OpenFile();
            double newEnd = Math.round(end*100.0)/100.0;

            result = Double.toString(newEnd);

            System.err.println("AVERAGE IS " + result);

        } catch (MalformedURLException e) {
            result = "Malformed Exception";
        } catch (IOException e) {
            result = "IOException" + e;
        }
        return result;
    }

    private HttpURLConnection testRedirect(URL url) throws IOException {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);

        conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
        conn.addRequestProperty("User-Agent", "Mozilla");
        conn.addRequestProperty("Referer", "google.com");

        System.out.println("Request URL ... " + url);

        boolean redirect = false;

        // normally, 3xx is redirect
        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            if (status == HttpURLConnection.HTTP_MOVED_TEMP
                    || status == HttpURLConnection.HTTP_MOVED_PERM
                    || status == HttpURLConnection.HTTP_SEE_OTHER)
                redirect = true;
        }

        if (redirect) {

            // get redirect url from "location" header field
            String newUrl = conn.getHeaderField("Location");

            // get the cookie if need, for login
            String cookies = conn.getHeaderField("Set-Cookie");

            // open the new connection again
            conn = (HttpURLConnection) new URL(newUrl).openConnection();
            conn.setRequestProperty("Cookie", cookies);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla");
            conn.addRequestProperty("Referer", "google.com");

            System.out.println("Redirect to URL : " + newUrl);

        }
        return conn;
    }



}
