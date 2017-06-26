package main.java;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by lhyla on 26.06.2017.
 */
public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String[] tables = {"A", "B", "C"};

        for (String table : tables) {
            System.out.println("TABLE " + table);
            URL url = new URL("http://api.nbp.pl/api/exchangerates/tables/" + table + "/");
            HttpURLConnection http;
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();

            int responseCode = http.getResponseCode();
            if (responseCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));

                String line = null;
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                JSONArray jsonArray = new JSONArray(stringBuilder.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    JSONArray ratesArray = (JSONArray) obj.get("rates");

                    for (int j = 0; j < ratesArray.length(); j++) {
                        JSONObject o = (JSONObject) ratesArray.get(j);
                        System.out.print(o.get("code"));
                        System.out.print(" : ");
                        System.out.println(o.get("currency"));
                    }
                }
            }
            System.out.println();
        }
    }
}
