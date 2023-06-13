package com.leanplum.rondo;

import android.os.AsyncTask;
import android.util.Base64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.crypto.Cipher;
import org.erdtman.jcs.JsonCanonicalizer;
import org.json.JSONArray;

public class SecuredVarsHelper {

  private static PublicKey getPublicKey(String key) {
    try {
      byte[] byteKey = Base64.decode(key, Base64.DEFAULT);
      X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return kf.generatePublic(X509publicKey);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static byte[] payloadHash(String payload) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      return md.digest(payload.getBytes("UTF-8"));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean verifySignature(
      byte[] encryptedPayloadHash,
      String originalMessage,
      PublicKey publicKey) {
    try {
      Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
      cipher.init(Cipher.DECRYPT_MODE, publicKey);
      byte[] decryptedMessageHash = cipher.doFinal(encryptedPayloadHash);
      byte[] origHash = payloadHash(originalMessage);
      return Arrays.equals(decryptedMessageHash, origHash);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String downloadPublicKey() {
    try {
      URL url = new URL("https://keys.prod.leanplum.com/public");
      URLConnection conn = url.openConnection();

      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
      StringBuilder builder = new StringBuilder();
      for (String line; (line = reader.readLine()) != null; ) {
        builder.append(line).append("\n");
      }
      reader.close();

      JSONArray json = new JSONArray(builder.toString());
      return json.getString(0);

    } catch (Throwable t) {
      return null;
    }
  }


}
