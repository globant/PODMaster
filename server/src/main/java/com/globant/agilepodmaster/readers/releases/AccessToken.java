package com.globant.agilepodmaster.readers.releases;

import com.globant.agilepodmaster.sync.AbortSyncException;

import org.springframework.util.StringUtils;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Token used to encrypt user/pass for different purposes.
 * @author jose.dominguez@globant.com
 *
 */
@Data
@RequiredArgsConstructor
public class AccessToken {

  //TODO put this in the app configuration file.
  private static final String KEY = "posmasterizado 1234!!! - this is the key";

  @NonNull
  public String username;

  @NonNull
  public String password;

  @NonNull
  public String purpose;


  /**
   * Encrypt members data.
   * @return an encrypted string.
   */
  public String encrypt() {

    return EncryptionUtils.encrypt(KEY, String.format("%s:|:%s:|:%s:|:%s",
        "ProjectDashboard", username, password, purpose));
  }

  /**
   * Decrypt a token, validates purpose and creates a AccessToken.
   * @param encryptedToken encrypted token to be decrypted.
   * @param expectedPurpose purposed to validate the encryptedToken.
   * @return AccessToken created with data decrypted.
   */
  public static AccessToken decrypt(String encryptedToken,
      String expectedPurpose) {
    String token = EncryptionUtils.decrypt(KEY, encryptedToken);
    String[] parts = StringUtils.tokenizeToStringArray(token, ":|:");
    String context = parts[0];
    AccessToken accessToken = new AccessToken(parts[1], parts[2], parts[3]);

    if (!context.equals("ProjectDashboard")
        || !expectedPurpose.equals(accessToken.getPurpose())) {
      throw new AbortSyncException("Invalid access token");
    }

    return accessToken;
  }
}
