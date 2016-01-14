package br.com.rt.govan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import govan.UserError;

public class Orcamento extends AppCompatActivity implements View.OnClickListener {

    // Componentes visuais
    private Spinner spTipo;
    private Button btnOrcamento;
    private Button btnReseta;
    private Button horaIni;
    private Button horaFim;
    private Button dataIni;
    private Button dataFim;

    private EditText edtOrigem;
    private EditText edtDestino;

    private TimePickerDialog fromTime, toTime;
    private DatePickerDialog fromDate, toDate;
    private SimpleDateFormat dateFormatter;

    // Dados
    private String Usuario;
    private ArrayAdapter<CharSequence> adpOpcoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        this.inicializa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orcamento, menu);
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

        if (v == horaIni) {
            fromTime.show();
        }

        if (v == horaFim) {
            toTime.show();
        }

        if (v == dataIni) {
            fromDate.show();
        }

        if (v == dataFim) {
            toDate.show();
        }

        if (v == btnOrcamento) {
            this.enviaOrcamento(v);
        }

        if (v == btnReseta) {
            this.limpaCampos();
        }
    }

    public void limpaCampos() {
        String vazia = "";
        spTipo.setSelection(0);
        edtOrigem.setText(vazia);
        edtDestino.setText(vazia);
        horaIni.setText(vazia);
        horaFim.setText(vazia);
        dataIni.setText(vazia);
        dataFim.setText(vazia);
    }

    public void enviaOrcamento(View view) {

        boolean dados = false;

        if (view == btnOrcamento) {

            dados = this.verifyData();
            if (!dados) {
                UserError ue = new UserError();
                ue.setMsg(getResources().getString(R.string.orcMsgDadosInvalidos));
                ue.show(getFragmentManager(), "Erro");
            } else {
                WebService ws = new WebService();
                ws.execute();
            }
        }
    }

    private boolean verifyData() {
        int tpSvc = spTipo.getSelectedItemPosition();
        String origem = edtOrigem.getText().toString();
        String destino = edtDestino.getText().toString();
        String horaInicial = horaIni.getText().toString();
        String horaFinal = horaFim.getText().toString();
        String dataInicial = dataIni.getText().toString();
        String dataFinal = dataFim.getText().toString();

        if (tpSvc < 1 || origem.isEmpty() || destino.isEmpty() || horaInicial.isEmpty() ||
                horaFinal.isEmpty() || dataInicial.isEmpty() || dataFinal.isEmpty()) {
            return false;
        } else {

            return true;
        }
    }

    private void setDate() {
        Calendar newCalendar = Calendar.getInstance();

        fromDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dataIni.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dataFim.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setTime() {
        final Calendar newCalendar = Calendar.getInstance();

        fromTime = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int hour, min;
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newTime.set(Calendar.MINUTE, minute);
                hour = newTime.get(Calendar.HOUR_OF_DAY);
                min = newTime.get(Calendar.MINUTE);
                horaIni.setText(String.format("%02d", hour) + ":" + String.format("%02d", min));
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

        toTime = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int hour, min;
                Calendar newTime = Calendar.getInstance();
                newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newTime.set(Calendar.MINUTE, minute);
                hour = newTime.get(Calendar.HOUR_OF_DAY);
                min = newTime.get(Calendar.MINUTE);
                horaFim.setText(String.format("%02d", hour) + ":" + String.format("%02d", min));
            }
        }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
    }

    private void recuperaUsuario(){
        String sUsuario = String.valueOf(R.string.usuario);
        SharedPreferences shared =  getSharedPreferences(sUsuario, Context.MODE_PRIVATE);
        Usuario = shared.getString(sUsuario,"");
    }

    private void inicializa() {
        spTipo = (Spinner) findViewById(R.id.tiposvc);
        btnOrcamento = (Button) findViewById(R.id.btnok);
        btnReseta = (Button) findViewById(R.id.btnreset);
        horaIni = (Button) findViewById(R.id.horainipesquisa);
        horaFim = (Button) findViewById(R.id.horafimpesquisa);
        dataIni = (Button) findViewById(R.id.datainipesquisa);
        dataFim = (Button) findViewById(R.id.datafimpesquisa);
        edtOrigem = (EditText) findViewById(R.id.orcSaida);
        edtDestino = (EditText) findViewById(R.id.orcRetorno);

        btnReseta.setOnClickListener(this);
        btnOrcamento.setOnClickListener(this);
        horaIni.setOnClickListener(this);
        horaFim.setOnClickListener(this);
        dataIni.setOnClickListener(this);
        dataFim.setOnClickListener(this);

        adpOpcoes = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adpOpcoes = ArrayAdapter.createFromResource(this, R.array.tiposervicos, android.R.layout.simple_spinner_dropdown_item);
        adpOpcoes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spTipo.setAdapter(adpOpcoes);

        this.setDate();
        this.setTime();
        this.recuperaUsuario();
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

            request.addProperty("dataIni", this.getDataIni());
            request.addProperty("dataFim", this.getDataFim());
            request.addProperty("localOri", this.getOrigem());
            request.addProperty("localDest", this.getDestino());
            request.addProperty("tipoSVC", this.getTipoSVC());
            request.addProperty("horaIni", this.getHoraIni());
            request.addProperty("horaFim", this.getHoraFim());

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
                UserError ue = new UserError();
                ue.setMsg(getResources().getString(R.string.orcMsgConfOrcamento));
                ue.show(getFragmentManager(), "Enviado");
            } else {
                ret = false;
            }

            return ret;
        }

        private String getOrigem() {
            return edtOrigem.getText().toString();
        }

        private String getDestino() {
            return edtDestino.getText().toString();
        }

        private String getTipoSVC() {
            return spTipo.getSelectedItem().toString();
        }

        private String getDataIni() {
            return dataIni.getText().toString();
        }

        private String getDataFim() {
            return dataFim.getText().toString();
        }

        private String getHoraIni() {
            return horaIni.getText().toString();
        }

        private String getHoraFim(){
            return horaFim.getText().toString();
        }
    }

}
