package br.com.rt.govan;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.*;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import govan.RegConfirmation;
import govan.UserError;

import static br.com.rt.govan.R.*;

public class loginPage extends AppCompatActivity implements View.OnClickListener {

    // Botões
    private Button btLogin;
    private Button btRegistrar;

    // Valores
    EditText usuario;
    private EditText senha;
    private CheckBox chkConsistir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login_page);

        this.Inicializa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_login_page, menu);

        return false;
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        String login = String.valueOf(string.consiteLogin);
        String sUsuario = String.valueOf(string.usuario);
        String sSenha = String.valueOf(string.senha);

        if (v == btLogin) {
            boolean validaUsr;
            String usr = usuario.getText().toString();
            String pwd = senha.getText().toString();

            validaUsr = this.validaLogin(usr, pwd);
            if (validaUsr) {
                if (chkConsistir.isChecked()) {
                    SharedPreferences shared = getSharedPreferences(login, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString(login, "S");
                    editor.putString(sUsuario, usr);
                    editor.putString(sSenha, pwd);
                    editor.commit();
                } else {
                    SharedPreferences shared = getSharedPreferences(login, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString(login, "N");
                    editor.putString(sUsuario, usr);
                    editor.putString(sSenha, "");
                    editor.commit();
                }
                this.chamaActivity();
            } else {
                this.showDialog(v);
            }
        }

        if (v == btRegistrar) {
            this.showDialog(v);
        }
    }

    public void Inicializa() {

        // Esconde menu
        getSupportActionBar().hide();

        // Usuario
        usuario = (EditText) findViewById(id.user);
        senha = (EditText) findViewById(id.password);

        // Botões
        btLogin = (Button) findViewById(id.btnLogin);
        btLogin.setOnClickListener(this);

        btRegistrar = (Button) findViewById(id.registrar);
        btRegistrar.setOnClickListener(this);

        // Checkbox
        chkConsistir = (CheckBox) findViewById(id.loginConsist);

        // Verifica dados gravados
        this.verificaConsistencia();
    }

    public void verificaConsistencia() {
        String consisteLogin = String.valueOf(string.consiteLogin);
        String sUsuario = String.valueOf(string.usuario);
        String sSenha = String.valueOf(string.senha);
        String s = "", usr = "", pwd = "";
        boolean loginOk;

        SharedPreferences shared = getSharedPreferences(consisteLogin, Context.MODE_PRIVATE);
        s = shared.getString(consisteLogin, "N");

        if (s.equals("S")) {
            usr = shared.getString(sUsuario, "");
            pwd = shared.getString(sSenha, "");
            loginOk = this.validaLogin(usr, pwd);
            if (loginOk) {
                this.chamaActivity();
            }
        }
    }

    private void chamaActivity(){
        Intent it = new Intent(loginPage.this, painelVans.class);
        startActivity(it);
        finish();
    }

    public boolean validaLogin(String usr, String pwd) {
        if (!usr.isEmpty() && !pwd.isEmpty()) {
            /*WebService ws = new WebService();
            ws.setUsr(usr);
            ws.setPwd(pwd);

            ws.execute();*/

            return true;
        } else {
            return false;
        }
    }

    public void showDialog(View view) {

        if (view == btLogin) {
            UserError ue = new UserError();
            ue.setMsg(getResources().getString(string.validacaosenha));
            ue.show(getFragmentManager(), "Erro");
        }

        if (view == btRegistrar) {
            RegConfirmation msg = new RegConfirmation();
            msg.show(getFragmentManager(), "Mensagem");
        }
    }

    private class WebService extends AsyncTask<String, Void, String> {
        private String User, Password;

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

            request.addProperty("usr", User);
            request.addProperty("pwd", Password);

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

        public void setUsr(String User) {
            this.User = User;
        }

        public void setPwd(String Password) {
            this.Password = Password;
        }
    }
}
