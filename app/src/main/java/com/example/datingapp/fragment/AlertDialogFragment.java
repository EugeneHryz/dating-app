package com.example.datingapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.datingapp.R;

public class AlertDialogFragment extends DialogFragment {

    public static final String TAG = AlertDialogFragment.class.getName();

    public static final String DIALOG_MESSAGE_KEY = "dialog_message_key";
    public static final String DIALOG_TITLE_KEY = "dialog_title_key";
    public static final String DIALOG_HAS_NEGATIVE_BUTTON_KEY = "dialog_has_negative_button_key";

    private final DialogResultListener listener;

    public AlertDialogFragment(@NonNull DialogResultListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String messageString = getString(R.string.dialog_generic_error_msg);
        String titleString = getString(R.string.dialog_title_alert);
        boolean hasNegativeButton = false;
        Bundle args = getArguments();
        if (args != null) {
            messageString = args.getString(DIALOG_MESSAGE_KEY);
            titleString = args.getString(DIALOG_TITLE_KEY);
            hasNegativeButton = args.getBoolean(DIALOG_HAS_NEGATIVE_BUTTON_KEY);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext())
                .setTitle(titleString)
                .setMessage(messageString)
                .setPositiveButton(R.string.dialog_ok_button, (dialog, which) -> listener.onOk());
        if (hasNegativeButton) {
            dialogBuilder.setNegativeButton(R.string.dialog_no_button, (dialog, which) -> listener.onCancel());
        }

        return dialogBuilder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        listener.onDismissed();
        super.onDismiss(dialog);
    }
}
