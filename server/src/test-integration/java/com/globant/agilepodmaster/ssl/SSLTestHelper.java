package com.globant.agilepodmaster.ssl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.AccessLevel;
import lombok.Getter;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("PMD.TooManyMethods")
public class SSLTestHelper {
  public static class Memento {
    @Getter(AccessLevel.PRIVATE)
    private HostnameVerifier defaultHostnameVerifier;
    public Memento(HostnameVerifier defaultHostnameVerifier) {
      this.defaultHostnameVerifier = defaultHostnameVerifier;
    }
  }

  private static class NullHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }

  private static class MySimpleClientHttpRequestFactory extends SimpleClientHttpRequestFactory {
    private final HostnameVerifier verifier;

    public MySimpleClientHttpRequestFactory(HostnameVerifier verifier) {
      this.verifier = verifier;
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod)
        throws IOException {
      if (connection instanceof HttpsURLConnection) {
        ((HttpsURLConnection) connection).setHostnameVerifier(verifier);
      }
      super.prepareConnection(connection, httpMethod);
    }
  }

  public static RestTemplate createNotVerifyingTestRestTemplate() {
    RestTemplate rest = new TestRestTemplate();
    rest.setRequestFactory(createNotVerifyingHttpRequestFactory());
    return rest;
  }

  public static SimpleClientHttpRequestFactory createNotVerifyingHttpRequestFactory() {
    HostnameVerifier verifier = new NullHostnameVerifier();
    SimpleClientHttpRequestFactory factory = new MySimpleClientHttpRequestFactory(verifier);
    return factory;
  }

  public static Memento trustAllHosts() {
    HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    });
    
    return new Memento(defaultHostnameVerifier);
  }

  public static void restoreMemento(Memento memento) {
    restoreHostnameVerifier(memento.getDefaultHostnameVerifier());
  }
  
  private static void restoreHostnameVerifier(HostnameVerifier verifier) {
    HttpsURLConnection.setDefaultHostnameVerifier(verifier);
  }

  public static Memento trustAllCertificates() {
    trustSelfSignedSSL();
    return trustAllHosts();
  }
  
  public static void trustSelfSignedSSL() {
    X509TrustManager tm = new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] xcs, String string)
          throws CertificateException {
      }

      public void checkServerTrusted(X509Certificate[] xcs, String string)
          throws CertificateException {
      }

      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }
    };

    try {
      SSLContext ctx = SSLContext.getInstance("TLS");
      ctx.init(null, new TrustManager[] { tm }, null);
      SSLContext.setDefault(ctx);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void restoreSSLContext() {
    try {
      SSLContext ctx = SSLContext.getInstance("TLS");
      ctx.init(null, null, null);
      SSLContext.setDefault(ctx);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
