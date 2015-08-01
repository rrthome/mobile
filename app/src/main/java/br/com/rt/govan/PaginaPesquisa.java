package br.com.rt.govan;

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

public class PaginaPesquisa extends AppCompatActivity implements View.OnClickListener {

    // Componentes visuais
    private Spinner spTipo;
    private Button btnPesquisa;
    private Button btnReseta;

    // Dados
    private ArrayAdapter<CharSequence> adpOpcoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_pesquisa);

        String erro;

        this.inicializa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pagina_pesquisa, menu);
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

    }

    private void inicializa() {
        spTipo = (Spinner) findViewById(R.id.tiposvc);
        btnPesquisa = (Button) findViewById(R.id.btnok);
        btnReseta = (Button) findViewById(R.id.btnreset);

        btnReseta.setOnClickListener(this);
        btnPesquisa.setOnClickListener(this);

        adpOpcoes = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adpOpcoes = ArrayAdapter.createFromResource(this, R.array.tiposervicos, android.R.layout.simple_spinner_dropdown_item);
        adpOpcoes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTipo.setAdapter(adpOpcoes);
    }
}
