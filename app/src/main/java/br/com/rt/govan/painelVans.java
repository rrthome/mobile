package br.com.rt.govan;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import govan.UserError;
import govan.nome_van_item;

import static br.com.rt.govan.R.id.panelContato;

public class painelVans extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_vans);

        /*
        WebService ws = new WebService();
        ws.execute();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_painel_vans, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.painelBusca:
                Intent it = new Intent(painelVans.this, Orcamento.class);
                startActivity(it);
                return true;

            case R.id.finalizaAPP:
                this.limpaLogin();
                finish();
                System.exit(0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void limpaLogin(){
        String persisteLogin = String.valueOf(R.string.consiteLogin), S = "";
        SharedPreferences shared = getSharedPreferences(persisteLogin, Context.MODE_PRIVATE);

        S = shared.getString(persisteLogin,"N");
        if (S.equals("S")){
            String sSenha = String.valueOf(R.string.senha);
            SharedPreferences.Editor editor = shared.edit();
            editor.putString(sSenha,"");
            editor.putString(persisteLogin,"N");
            editor.commit();
        }
    }

    private class WebService extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String retorno = "";

            try {
                retorno = this.chamaWS();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            if (!retorno.isEmpty()) {
                UserError er = new UserError();
                er.setMsg(getResources().getString(R.string.validacaosenha));
                er.show(getFragmentManager(), "Erro");
            }
            return null;
        }

        private String chamaWS() throws IOException, XmlPullParserException {

            String res;
            final String SOAP_ACTION = "http://tempuri.org/verificaSenha";
            final String NAMESPACE = "http://tempuri.org/";
            final String NOMEMETODO = "verificaSenha";
            final String URL = "http://192.168.10.104/wscliente/verificarsenha.asmx";

            SoapObject request = new SoapObject(NAMESPACE, NOMEMETODO);

            request.addProperty("usr", "1");
            request.addProperty("pwd", "");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTrans = new HttpTransportSE(URL);
            try {
                androidHttpTrans.call(SOAP_ACTION, envelope);
                SoapObject resultado = (SoapObject) envelope.bodyIn;
                res = resultado.getProperty(0).toString();
            } catch (Exception e) {
                throw e;
            }
            return res;
        }
    }


}
