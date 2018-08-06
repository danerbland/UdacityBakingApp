package utils;



import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.android.baking_app.MainActivity;
import com.example.android.baking_app.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static URL buildGenericJSONURL (Context context){
        String baseURLString = context.getString(R.string.json_url);
        URL responseURL = null;

        try {
            responseURL = new URL(baseURLString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        return responseURL;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        //Create a HttpURLConnection object to handle the streams.
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

}
