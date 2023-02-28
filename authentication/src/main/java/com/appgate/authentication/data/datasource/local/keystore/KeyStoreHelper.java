package com.appgate.authentication.data.datasource.local.keystore;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Pair;
import com.appgate.authentication.domain.repository.OnRequestCompletedListener;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import static android.content.Context.MODE_PRIVATE;

public class KeyStoreHelper {

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String CHIPPER_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final String ALIAS = "appGateAlias";
    private static final String PASSWORD_KEY = "password";
    private static final String USERNAME_KEY = "username";
    private static final String SHARED_PREFERENCES_NAME = "appGAteSharedPreferences";
    private static final boolean ENCRYPTED_SUCCESS = true;
    private final SharedPreferences sharedPreferences;
    private KeyStore keyStore;

    public KeyStoreHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        initKeyStore();
    }

    private void initKeyStore() {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
        } catch (CertificateException | KeyStoreException | IOException |
                 NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void encrypt(String username, String password, OnRequestCompletedListener<Boolean> listener) {
        try {
            if (!keyStore.containsAlias(ALIAS)) {
                KeyPairGenerator keyPairGenerator;
                keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE);
                KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);
                keyPairGenerator.initialize(builder.build());
                keyPairGenerator.generateKeyPair();
            }

            KeyStore.Entry entry = keyStore.getEntry(ALIAS, null);
            if (entry instanceof KeyStore.PrivateKeyEntry) {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;

                byte[] usernameBytes = username.getBytes();
                byte[] passwordBytes = password.getBytes();

                Cipher cipher = Cipher.getInstance(CHIPPER_TRANSFORMATION);
                cipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

                byte[] encryptedUsernameBytes = cipher.doFinal(usernameBytes);
                byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USERNAME_KEY, Base64.encodeToString(encryptedUsernameBytes, Base64.DEFAULT));
                editor.putString(PASSWORD_KEY, Base64.encodeToString(encryptedPasswordBytes, Base64.DEFAULT));
                editor.apply();
                listener.onSuccess(ENCRYPTED_SUCCESS);
            }
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException |
                 NoSuchPaddingException | InvalidKeyException | KeyStoreException |
                 UnrecoverableEntryException |
                 InvalidAlgorithmParameterException | NoSuchProviderException e) {
            e.printStackTrace();
            listener.onError(e);
        }
    }

    public void decrypt(OnRequestCompletedListener<Pair<String, String>> listener) {
        try {
            String encryptedUsernameString = sharedPreferences.getString(USERNAME_KEY, null);
            String encryptedPasswordString = sharedPreferences.getString(PASSWORD_KEY, null);

            if (encryptedUsernameString != null && encryptedPasswordString != null) {
                byte[] encryptedUsernameBytes = Base64.decode(encryptedUsernameString, Base64.DEFAULT);
                byte[] encryptedPasswordBytes = Base64.decode(encryptedPasswordString, Base64.DEFAULT);

                KeyStore.Entry entry = keyStore.getEntry(ALIAS, null);
                if (entry instanceof KeyStore.PrivateKeyEntry) {
                    KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;

                    Cipher cipher = Cipher.getInstance(CHIPPER_TRANSFORMATION);
                    cipher.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

                    byte[] decryptedUsernameBytes = cipher.doFinal(encryptedUsernameBytes);
                    byte[] decryptedPasswordBytes = cipher.doFinal(encryptedPasswordBytes);

                    String username = new String(decryptedUsernameBytes);
                    String password = new String(decryptedPasswordBytes);
                    listener.onSuccess(Pair.create(username, password));
                }
            }
        } catch (UnrecoverableEntryException | NoSuchPaddingException | IllegalBlockSizeException |
                 KeyStoreException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            e.printStackTrace();
            listener.onError(e);
        }
    }
}
