package main.java;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {

        printGoldPrice();

        System.out.println();
        System.out.println();

        printChfRate();

        System.out.println();
        System.out.println();
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

    public static void printChfRate() throws IOException {
        URL url = new URL("http://api.nbp.pl/api/exchangerates/rates/a/chf/");

        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
        JSONArray jsonRates = (JSONArray) jsonObject.get("rates");
        JSONObject chf = (JSONObject) jsonRates.get(0);
        String chfRate = chf.get("mid").toString();
        System.out.println("Swiss franc exchange rate " + chfRate);
    }

    private static void printGoldPrice() throws IOException {
        URL url = new URL("http://api.nbp.pl/api/cenyzlota/");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("GET");
        http.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        JSONArray jsonRates = new JSONArray(stringBuilder.toString());
        JSONObject jsonObject = (JSONObject) jsonRates.get(0);

        double goldPrice = jsonObject.getDouble("cena");

        System.out.println("Gold price: " + goldPrice);

    }
}
