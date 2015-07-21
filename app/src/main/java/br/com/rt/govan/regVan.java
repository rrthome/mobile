package br.com.rt.govan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class regVan extends AppCompatActivity implements View.OnClickListener {

    // Dados Dropdown
    private ArrayAdapter<CharSequence> adpPais, adpEstado, adpCidade;
    private Spinner spPais, spEstado, spCidade;

    // Botões
    private Button btnRegVanConf, btnRegVanCanc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_van);

        this.inicializarSpinners();
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

    public void inicializarSpinners(){
        // Spinner Pais
        spPais = (Spinner)findViewById(R.id.regvanspPais);
        adpPais = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item);
        adpPais = ArrayAdapter.createFromResource(this,R.array.arrayPais,android.R.layout.simple_spinner_dropdown_item);
        adpPais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPais.setAdapter(adpPais);

        // Spinner Estado
        spEstado = (Spinner)findViewById(R.id.regvanspestado);
        adpEstado = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item);
        adpEstado = ArrayAdapter.createFromResource(this,R.array.arrayEstado,android.R.layout.simple_spinner_dropdown_item);
        adpEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(adpEstado);

        // Spinner Cidade
        spCidade = (Spinner)findViewById(R.id.regvanspcidade);
        adpCidade = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item);
        adpCidade = ArrayAdapter.createFromResource(this,R.array.arrayCidade,android.R.layout.simple_spinner_dropdown_item);
        adpCidade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCidade.setAdapter(adpCidade);
    }

    public void inicializaButtons(){
        // Botão registra
        btnRegVanConf = (Button)findViewById(R.id.vanRegdados);
        btnRegVanConf.setOnClickListener(this);

        // Botão cancela
        btnRegVanCanc = (Button)findViewById(R.id.vanCancdados);
        btnRegVanCanc.setOnClickListener(this);
    }
}
