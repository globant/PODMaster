package com.globant.agilepodmaster.sync.jira;

import com.globant.agilepodmaster.sync.EncryptionException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * An utility class to encrypt and decrypt a text.
 * 
 * @author jose.dominguez@globant.com
 *
 */
public final class EncryptionUtils {
  
  private static final String UTF8_CHARSET = "UTF8";

  private EncryptionUtils() {
  }

  /**
   * Encrypts the input String using the key.
   * 
   * @param key at least 24 character long key.
   * @param input the string to encrypt.
   * @return the encrypted version of the input.
   */
  public static String encrypt(String key, String input) {
    isValidKey(key);
    final byte[] cipherText = cipher(key, stringToByteArray(input, UTF8_CHARSET),
        Cipher.ENCRYPT_MODE);
    return byteArrayToString(Base64.encodeBase64(cipherText), UTF8_CHARSET);
  }

  /**
   * Decrypts the input String using the key.
   * 
   * @param key at least 24 character long key.
   * @param input the string to decrypt.
   * @return the decrypted version of the input.
   * @throws EncryptionException
   */
  public static String decrypt(String key, String input) {
    Assert.hasText(key, "A key is required to attempt decryption");
    final byte[] cipherText = cipher(key,
        Base64.decodeBase64(stringToByteArray(input, UTF8_CHARSET)),
        Cipher.DECRYPT_MODE);
    return byteArrayToString(cipherText, UTF8_CHARSET);
  }

  private static void isValidKey(String key) {
    Assert.hasText(key, "A key to perform the encryption is required");
    Assert
        .isTrue(key.length() >= 24, "Key must be at least 24 characters long");
  }


  private static byte[] stringToByteArray(String input, String charset) {
    Assert.hasLength(input, "Input required");
    try {
      return input.getBytes(charset);
    } catch (UnsupportedEncodingException e) {
      throw new EncryptionException(e.getMessage(), e);
    }
  }


  private static String byteArrayToString(byte[] byteArray, String charset) {
    Assert.notNull(byteArray, "ByteArray required");
    Assert.isTrue(byteArray.length > 0, "ByteArray cannot be empty");
    try {
      return new String(byteArray, charset);
    } catch (final UnsupportedEncodingException e) {
      throw new EncryptionException(e.getMessage(), e);
    }
  }

  private static byte[] cipher(String key, byte[] passedBytes, int cipherMode) {
    try {
      final KeySpec keySpec = new DESedeKeySpec(stringToByteArray(key,
          UTF8_CHARSET));
      final SecretKeyFactory keyFactory = SecretKeyFactory
          .getInstance("DESede");
      final Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
      final SecretKey secretKey = keyFactory.generateSecret(keySpec);
      cipher.init(cipherMode, secretKey);
      return cipher.doFinal(passedBytes);
    } catch (final Exception e) {
      throw new EncryptionException(e.getMessage(), e);
    }
  }


}
