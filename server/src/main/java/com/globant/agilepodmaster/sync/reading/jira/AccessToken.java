package com.globant.agilepodmaster.sync.reading.jira;

import com.globant.agilepodmaster.sync.AbortSyncException;

import org.acegisecurity.util.EncryptionUtils;
import org.springframework.util.StringUtils;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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


  public String Encrypt() {

    return EncryptionUtils.encrypt(KEY, String.format("%s:|:%s:|:%s:|:%s",
        "ProjectDashboard", username, password, purpose));
  }

  public static AccessToken Decrypt(String encryptedToken,
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
