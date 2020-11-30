package com.bunker.cloudstorage;


import android.util.Log;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Aes256Class {

    private SecretKey secretKey;

    public Aes256Class(int d){
        Log.e("secretKey dec", String.valueOf(this.secretKey));
    }
    public Aes256Class() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);

            secretKey = keyGenerator.generateKey();
            Log.e("secretKey ency", String.valueOf(this.secretKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public byte[] makeAes(byte[] rawMessage, int cipherMode) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, this.secretKey);
            return cipher.doFinal(rawMessage);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}