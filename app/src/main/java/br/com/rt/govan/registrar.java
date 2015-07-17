package br.com.rt.govan;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class registrar extends AppCompatActivity implements View.OnClickListener {

    // Componentes visuais
    private Button btnRegistrar;
    private Button btnCancelar;
    private EditText edtEmail;
    private EditText edtNome;
    private EditText edtSenha, edtConfSenha;
    private EditText edtDDD, edtDDI, edtFone;

    // dados
    private String ddd, ddi, telefone, email, nome, senha, confSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        this.inicializar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registrar, menu);
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

    protected void inicializar() {
        // Botões
        btnRegistrar = (Button) findViewById(R.id.regdados);
        btnCancelar = (Button) findViewById(R.id.cancdados);
        btnRegistrar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        // Textos
        edtDDD = (EditText) findViewById(R.id.regddd);
        edtDDI = (EditText) findViewById(R.id.regddi);
        edtFone = (EditText) findViewById(R.id.regfone);
        edtEmail = (EditText) findViewById(R.id.email);
        edtNome = (EditText) findViewById(R.id.nome);
        edtSenha = (EditText) findViewById(R.id.senha);
        edtConfSenha = (EditText) findViewById(R.id.confsenha);
    }

    protected boolean verificaDados() {
        boolean dadosOk = false;

        email = edtEmail.getText().toString();
        nome = edtNome.getText().toString();
        ddi = edtDDI.getText().toString();
        ddd = edtDDD.getText().toString();
        telefone = edtFone.getText().toString();
        senha = edtSenha.getText().toString();
        confSenha = edtConfSenha.getText().toString();

        if (!email.isEmpty() && !nome.isEmpty() && !ddi.isEmpty() && !ddd.isEmpty() && !telefone.isEmpty()
                && !senha.isEmpty() && !confSenha.isEmpty()) {
            if (!senha.equals(confSenha)) {
                dadosOk = false;
            } else if (senha.equals(confSenha)){
                dadosOk = true;
            }
        }

        return dadosOk;
    }

    @Override
    public void onClick(View v) {

        boolean dadosOk;

        if (v == btnRegistrar) {
            dadosOk = this.verificaDados();
            if (!dadosOk){
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Go Van");
                alertDialog.setMessage("Dados inválidos. Favor verificar");
                alertDialog.show();
            }
        }

        if (v == btnCancelar) {
            finish();
        }

    }
}
