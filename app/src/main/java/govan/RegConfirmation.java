package govan;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import br.com.rt.govan.R;
import br.com.rt.govan.regVan;

/**
 * Created by Rafael on 20/07/2015.
 */
public class RegConfirmation extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.registrar)
                .setPositiveButton(R.string.regvan, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent it = new Intent(getActivity(),regVan.class);
                        startActivity(it);
                    }
                })
                .setNegativeButton(R.string.regclient, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(),R.string.regclient,Toast.LENGTH_SHORT);
                    }
                });

        Dialog dialog = builder.create();

        // Create the AlertDialog object and return it
        return dialog;
    }
}
