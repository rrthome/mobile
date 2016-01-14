package br.com.rt.govan;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import govan.UserError;

public class regVan extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    // Dados
    private TextView tvEstado, tvCidade;
    private EditText edtVanCEP, edtVanNome, edtVanContato;
    private EditText edtVanEmail, edtVanSenha, edtConfSenha;
    private String cep;

    // Botões
    private Button btnRegVanConf, btnRegVanCanc;

    // Funções
    UserError er;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_van);

        this.inicializarTextos();
        this.inicializaButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reg_van, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        boolean ret;

        if (v == btnRegVanCanc) {
            finish();
        }

        if (v == btnRegVanConf) {
            ret = this.verificaDados();
            if (!ret) {
                er = new UserError();
                er.setMsg(getResources().getString(R.string.confDados));
                er.show(getFragmentManager(), "Erro");
                return;
            }

            ret = this.verificaSenha();
            if (!ret) {
                er = new UserError();
                er.setMsg(getResources().getString(R.string.confDados));
                er.show(getFragmentManager(), "Erro");
                return;
            }

            if (ret) {
                WebService ws = new WebService();
                ws.execute();
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == edtVanCEP) {
            try {
                this.preencheLocalizacao();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean verificaSenha() {
        if (edtVanSenha.getText().toString().equals(edtConfSenha.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean verificaDados() {
        String nome, nomeVan, email, senha, confSenha, cep;

        nome = edtVanContato.getText().toString();
        nomeVan = edtVanNome.getText().toString();
        email = edtVanEmail.getText().toString();
        senha = edtVanSenha.getText().toString();
        confSenha = edtConfSenha.getText().toString();
        cep = edtVanCEP.getText().toString();

        if (nome.isEmpty() || nomeVan.isEmpty() || email.isEmpty() || senha.isEmpty() || confSenha.isEmpty() || cep.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void inicializarTextos() {
        // Textos
        tvEstado = (TextView) findViewById(R.id.regvanspestado);
        tvCidade = (TextView) findViewById(R.id.regvanspcidade);
        edtVanNome = (EditText) findViewById(R.id.vanNomeRzsc);
        edtVanContato = (EditText) findViewById(R.id.vanNome);
        edtVanEmail = (EditText) findViewById(R.id.vanEmail);
        edtVanSenha = (EditText) findViewById(R.id.vanSenha);
        edtConfSenha = (EditText) findViewById(R.id.vanConfSenha);

        // CEP
        edtVanCEP = (EditText) findViewById(R.id.regvancep);
        edtVanCEP.setOnFocusChangeListener(this);
    }

    public void inicializaButtons() {
        // Botão registra
        btnRegVanConf = (Button) findViewById(R.id.vanRegdados);
        btnRegVanConf.setOnClickListener(this);

        // Botão cancela
        btnRegVanCanc = (Button) findViewById(R.id.vanCancdados);
        btnRegVanCanc.setOnClickListener(this);
    }

    public void preencheLocalizacao() throws JSONException {
        cep = edtVanCEP.getText().toString();

        if (!cep.isEmpty()) {
            BuscaCepTask buscaEndereco = new BuscaCepTask();
            buscaEndereco.execute(cep);
        }
    }

    private class BuscaCepTask extends AsyncTask<String, Void, String> {
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        private JSONObject object;
        public String uf, cidade;
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
            try {
                uf = object.getString("uf");
                tvEstado.setText(uf);
                cidade = object.getString("localidade");
                tvCidade.setText(cidade);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class WebService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String msg = "";
            boolean retorno = false;

            try {
                retorno = this.chamaWS();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            if (!retorno) {
                msg = getResources().getString(R.string.cadError);
                UserError er = new UserError();
                er.setMsg(msg);
                er.show(getFragmentManager(), "Erro");
            } else {
                msg = getResources().getString(R.string.cadOk);
                UserError er = new UserError();
                er.setMsg(msg);
                er.show(getFragmentManager(), "Erro");
                finish();
            }
            return null;
        }

        private boolean chamaWS() throws IOException, XmlPullParserException {

            Boolean ret;
            String res;
            SoapObject resultado;
            final String SOAP_ACTION = "http://tempuri.org/gravarDados";
            final String NAMESPACE = "http://tempuri.org/";
            final String NOMEMETODO = "gravarDados";
            final String URL = "http://192.168.10.139/wscliente/GravarDados.asmx";

            SoapObject request = new SoapObject(NAMESPACE, NOMEMETODO);

            request.addProperty("nomeVan", this.getNomeVan());
            request.addProperty("contatoVan", this.getContatoVan());
            request.addProperty("email", this.getEmail());
            request.addProperty("cep", this.getCEP());
            request.addProperty("cidade", this.getCidade());
            request.addProperty("uf", this.getUF());
            request.addProperty("senha", this.getSenha());

            try {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTrans = new HttpTransportSE(URL);
                androidHttpTrans.call(SOAP_ACTION, envelope);
                resultado = (SoapObject) envelope.bodyIn;
                res = resultado.getProperty(0).toString();
            } catch (Exception e) {
                throw e;
            }

            if (res.equals("true")) {
                ret = true;
            } else {
                ret = false;
            }

            return ret;
        }

        private String getCEP() {
            return edtVanCEP.getText().toString();
        }

        private String getUF() {
            return tvEstado.getText().toString();
        }

        private String getCidade() {
            return tvCidade.getText().toString();
        }

        private String getNomeVan() {
            return edtVanNome.getText().toString();
        }

        private String getContatoVan() {
            return edtVanContato.getText().toString();
        }

        private String getEmail() {
            return edtVanEmail.getText().toString();
        }

        private String getSenha() {
            return edtVanSenha.getText().toString();
        }
    }
}
