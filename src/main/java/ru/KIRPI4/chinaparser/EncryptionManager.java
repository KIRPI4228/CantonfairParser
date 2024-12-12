package ru.KIRPI4.chinaparser;

import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStreamReader;

public final class EncryptionManager {
    private static final String CRYPT_FILE_NAME = "crypt.js";


    public static String encryptAuthenticationParameters(String publicKey, String email, String password, String sliderToken) throws ScriptException, IOException, NoSuchMethodException {
        return JsExecutor.execute(new InputStreamReader(EncryptionManager.class.getClassLoader().getResourceAsStream(CRYPT_FILE_NAME)), "generateCode", publicKey, email, password, sliderToken);
    }
}
