package govan;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class BuscarCepTask extends AsyncTask<String, Void, String> {
    URL url = null;
    HttpURLConnection httpURLConnection = null;
    public JSONObject object;
    StringBuilder result = null;

    @Override
    protected String doInBackground(String... params) {
        int respCode = -1;

        try {
            url = new URL("http://cep.correiocontrol.com.br/" + params[0] + ".json");
            httpURLConnection = (HttpURLConnection) url.openConnection();

            do {
                if (httpURLConnection != null) {
                    respCode = httpURLConnection.getResponseCode();
                }
            } while (respCode == -1);

            if (respCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                result = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                this.object = new JSONObject(result.toString());
                br.close();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
                httpURLConnection = null;
            }
        }

        return (result != null) ? result.toString() : null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            this.object = new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String retornaUF() throws JSONException {
        String conteudo = "";
        conteudo = object.getString("uf");
        return conteudo;
    }

    public String retornaCidade() throws JSONException {
        String conteudo = "";
        conteudo = object.getString("localidade");
        return conteudo;
    }

}