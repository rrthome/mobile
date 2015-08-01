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
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import govan.BuscarCepTask;

public class regVan extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    // Dados
    private TextView tvEstado, tvCidade;
    private EditText edtVanCEP;
    private String cep;

    // Botões
    private Button btnRegVanConf, btnRegVanCanc;

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
        if (v == btnRegVanCanc){
            finish();
        }

        if (v == btnRegVanConf){

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

    public void inicializarTextos(){
        // Textos
        tvEstado = (TextView)findViewById(R.id.regvanspestado);
        tvCidade = (TextView)findViewById(R.id.regvanspcidade);

        // CEP
        edtVanCEP = (EditText) findViewById(R.id.regvancep);
        edtVanCEP.setOnFocusChangeListener(this);
    }

    public void inicializaButtons(){
        // Botão registra
        btnRegVanConf = (Button)findViewById(R.id.vanRegdados);
        btnRegVanConf.setOnClickListener(this);

        // Botão cancela
        btnRegVanCanc = (Button)findViewById(R.id.vanCancdados);
        btnRegVanCanc.setOnClickListener(this);
    }

    public void preencheLocalizacao() throws JSONException {
        String uf, cidade;
        cep = edtVanCEP.getText().toString();

        if (!cep.isEmpty()) {
            BuscarCepTask buscaEndereco = new BuscarCepTask();
            try {
                String str = buscaEndereco.execute(cep).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // uf = buscaEndereco.retornaUF();
            // tvEstado.setText(uf);

            // cidade = buscaEndereco.retornaCidade();
            // tvCidade.setText(cidade);
        }
    }
}
