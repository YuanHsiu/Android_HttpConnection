package com.brickred.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpParams;

public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
				// TODO Auto-generated method stub
				
			}
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }
/*
    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
*/
    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return enableTLSOnSocket(sslContext.getSocketFactory().createSocket(s, host, port, autoClose));
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return enableTLSOnSocket(sslContext.getSocketFactory().createSocket(host, port));
    }

    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return enableTLSOnSocket(sslContext.getSocketFactory().createSocket(host, port, localHost, localPort));
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        return enableTLSOnSocket(sslContext.getSocketFactory().createSocket(host, port));
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return enableTLSOnSocket(sslContext.getSocketFactory().createSocket(address, port, localAddress, localPort));
    }

    private Socket enableTLSOnSocket(Socket socket) {
        if(socket != null && (socket instanceof SSLSocket)) {
            ((SSLSocket)socket).setEnabledProtocols(new String[] {"TLSv1.1", "TLSv1.2"});
        }
        return socket;
    }
}