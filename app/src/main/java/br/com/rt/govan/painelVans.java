package br.com.rt.govan;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;

import govan.UserError;

public class painelVans extends AppCompatActivity {

    private EditText edtCEP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_vans);

        WebService ws = new WebService();

        ws.execute();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializaTextos(){

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

            if (!retorno.isEmpty()){
                UserError er = new UserError();
                er.show(getFragmentManager(),"Erro");
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
