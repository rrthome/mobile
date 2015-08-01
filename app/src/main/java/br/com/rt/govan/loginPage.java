package br.com.rt.govan;

import android.app.AlertDialog;
import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login_page);

        usuario = (EditText) findViewById(id.user);
        senha = (EditText) findViewById(id.password);

        btLogin = (Button) findViewById(id.btnLogin);
        btLogin.setOnClickListener(this);

        btRegistrar = (Button) findViewById(id.registrar);
        btRegistrar.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_page, menu);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == btLogin) {
            String usr = usuario.getText().toString();
            String pwd = senha.getText().toString();

            if (usr.isEmpty() && pwd.isEmpty()) {
                Intent it = new Intent(loginPage.this, painelVans.class);
                startActivity(it);
            } else {
                this.showDialog(v);
            }
        }

        if (v == btRegistrar) {
            this.showDialog(v);
        }
    }

    public void showDialog(View view) {

        if (view == btLogin) {
            UserError ue = new UserError();
            ue.show(getFragmentManager(),"Erro");
        }

        if (view == btRegistrar) {
            RegConfirmation msg = new RegConfirmation();
            msg.show(getFragmentManager(), "Mensagem");
        }
    }
}
