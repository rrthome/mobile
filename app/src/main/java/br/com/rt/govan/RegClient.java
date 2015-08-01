package br.com.rt.govan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import govan.BuscarCepTask;

public class RegClient extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {


    // Dados Texto
    private TextView tvEstado, tvCidade;
    private EditText edtClientCEP;
    private String cep;

    // Botões
    private Button btnRegClientConf, btnRegClientCanc;

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
        if (v == btnRegClientCanc) {
            finish();
        }

        if (v == btnRegClientConf) {

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

    public void inicializaTexts() {
        edtClientCEP = (EditText) findViewById(R.id.regclientcep);
        edtClientCEP.setOnFocusChangeListener(this);

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
        String uf, cidade;
        cep = edtClientCEP.getText().toString();

        if (!cep.isEmpty()) {
            BuscarCepTask buscaEndereco = new BuscarCepTask();
            buscaEndereco.execute(cep);

            uf = buscaEndereco.retornaUF();
            tvEstado.setText(uf);

            cidade = buscaEndereco.retornaCidade();
            tvCidade.setText(cidade);
        }
    }
}
