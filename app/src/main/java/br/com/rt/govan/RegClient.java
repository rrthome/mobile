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
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import govan.BuscarCepTask;
import govan.UserError;

public class RegClient extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    // Dados Texto
    private TextView tvEstado, tvCidade;
    private EditText edtClientCEP;
    private EditText edtClientNome, edtClientEmail;
    private EditText edtClientPass, edtClientConfPass;
    private String cep;

    // Botões
    private Button btnRegClientConf, btnRegClientCanc;

    // Funções
    UserError er;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_client);

        // Inicialização
        this.inicializaButtons();
        this.inicializaTexts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reg_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.regFB) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        boolean ret = false;
        String retorno = "";

        if (v == btnRegClientCanc) {
            finish();
        }

        if (v == btnRegClientConf) {

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
                er.setMsg(getResources().getString(R.string.confereSenha));
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
        if (view == edtClientCEP) {
            try {
                this.preencheLocalizacao();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean verificaDados() {
        String nome, email, senha, confSenha, cep;

        nome = edtClientNome.getText().toString();
        email = edtClientEmail.getText().toString();
        senha = edtClientPass.getText().toString();
        confSenha = edtClientConfPass.getText().toString();
        cep = edtClientCEP.getText().toString();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confSenha.isEmpty() || cep.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean verificaSenha() {
        if (edtClientPass.getText().toString().equals(edtClientConfPass.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public void inicializaTexts() {
        edtClientCEP = (EditText) findViewById(R.id.regclientcep);
        edtClientCEP.setOnFocusChangeListener(this);

        edtClientNome = (EditText) findViewById(R.id.regClientNome);
        edtClientEmail = (EditText) findViewById(R.id.regClientEmail);

        edtClientPass = (EditText) findViewById(R.id.clientSenha);
        edtClientConfPass = (EditText) findViewById(R.id.clientConfSenha);

        tvEstado = (TextView) findViewById(R.id.regclientspestado);
        tvCidade = (TextView) findViewById(R.id.regclientspcidade);
    }

    public void inicializaButtons() {
        // Botão registra
        btnRegClientConf = (Button) findViewById(R.id.clientRegdados);
        btnRegClientConf.setOnClickListener(this);

        // Botão cancela
        btnRegClientCanc = (Button) findViewById(R.id.clientCancdados);
        btnRegClientCanc.setOnClickListener(this);
    }

    public void preencheLocalizacao() throws JSONException {
        cep = edtClientCEP.getText().toString();

        if (!cep.isEmpty()) {
            BuscaCepTask buscaEndereco = new BuscaCepTask();
            buscaEndereco.execute(cep);
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

            request.addProperty("nome", this.getNome());
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
            return edtClientCEP.getText().toString();
        }

        private String getUF() {
            return tvEstado.getText().toString();
        }

        private String getCidade() {
            return tvCidade.getText().toString();
        }

        private String getNome() {
            return edtClientNome.getText().toString();
        }

        private String getEmail() {
            return edtClientEmail.getText().toString();
        }

        private String getSenha() {
            return edtClientPass.getText().toString();
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
}
