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
public class UserError extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.validacaosenha)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });

        Dialog dialog = builder.create();

        // Create the AlertDialog object and return it
        return dialog;
    }
}
