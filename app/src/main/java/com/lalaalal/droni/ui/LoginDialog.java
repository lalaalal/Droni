package com.lalaalal.droni.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lalaalal.droni.LoginException;
import com.lalaalal.droni.LoginSessionHandler;
import com.lalaalal.droni.R;
import com.lalaalal.droni.client.UserLogin;

public class LoginDialog extends DialogFragment
        implements DialogInterface.OnClickListener {

    private static final String LOGIN_SESSION = "LOGIN_SESSION";

    private View root;

    private String getUserId() throws LoginException {
        EditText userIdEditText = root.findViewById(R.id.user_id_et);

        if (userIdEditText.getText().toString().equals(""))
            throw new LoginException(LoginException.EMPTY_FIELD);

        return userIdEditText.getText().toString();
    }

    private String getUserPw() throws LoginException {
        EditText userPwEditText = root.findViewById(R.id.user_pw_et);

        if (userPwEditText.getText().toString().equals(""))
            throw new LoginException(LoginException.EMPTY_FIELD);

        return userPwEditText.getText().toString();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        root = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(root);

        builder.setPositiveButton(R.string.login, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        try {
            LoginSessionHandler loginSession = LoginSessionHandler.getLoginSession(getContext());
            UserLogin userLogin = new UserLogin(getUserId(), getUserPw());
            if (userLogin.requestLogin()) {
                loginSession.logIn(getUserId());
                Toast.makeText(getContext(), "성공!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "실패 ㅠ", Toast.LENGTH_SHORT).show();
            }
        } catch (LoginException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
