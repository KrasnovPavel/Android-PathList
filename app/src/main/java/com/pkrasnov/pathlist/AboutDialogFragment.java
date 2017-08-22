package com.pkrasnov.pathlist;

import android.app.*;
import android.os.*;
import android.content.*;
import android.text.*;
import android.text.util.*;
import android.text.method.*;
import android.widget.*;

public class AboutDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.about_title);
        final SpannableString s = new SpannableString(getText(R.string.about_text));
        Linkify.addLinks(s, Linkify.ALL);
        builder.setMessage(s);
        builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ((TextView)getDialog().findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }
}
