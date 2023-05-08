package com.example.datingapp.client.token;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.datingapp.R;

import javax.inject.Inject;

public class TokenServiceImpl implements TokenService {

    private final Context context;
    private final String fileName;
    private final String authTokenKey;

    @Inject
    public TokenServiceImpl(Context context) {
        this.context = context;
        fileName = context.getString(R.string.auth_token_shared_prefs_filename);
        authTokenKey = context.getString(R.string.auth_token_key);
    }

    @Override
    public String getToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sharedPrefs.getString(authTokenKey, null);
    }

    @Override
    public void updateToken(String token) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(authTokenKey, token);
        editor.apply();
    }

    @Override
    public void deleteToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(authTokenKey);
        editor.apply();
    }
}
