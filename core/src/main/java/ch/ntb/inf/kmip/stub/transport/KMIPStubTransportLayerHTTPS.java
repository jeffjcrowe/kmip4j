package ch.ntb.inf.kmip.stub.transport;
/**
 * KMIPStubTransportLayerHTTPS.java
 * ------------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * ------------------------------------------------------------------
 * Description:
 * The KMIPStubTransportLayerHTTPS provides the communication between 
 * a server and a client via HTTPS, using a HttpsUrlConnection. 
 *
 * @author     Stefanie Meile <stefaniemeile@gmail.com>
 * @author     Michael Guster <michael.guster@gmail.com>
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @copyright  Copyright © 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 * 
 */

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import ch.ntb.inf.kmip.utils.KMIPUtils;

/**
 * The KMIPStubTransportLayerHTTPS provides the communication between a server and a client via HTTPS,
 * using a HttpsUrlConnection. 
 */
public class KMIPStubTransportLayerHTTPS implements KMIPStubTransportLayerInterface{

	private static final Logger logger = Logger.getLogger(KMIPStubTransportLayerHTTP.class);
	
	private SSLSocketFactory factory;
	private String url;
	private String keyStoreFileName;
	private String keyStorePassword;
	private String alias = "ntb";

	
	
	public KMIPStubTransportLayerHTTPS() {
		logger.info("KMIPStubTransportLayerHTTPS initialized...");
		
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
			    new javax.net.ssl.HostnameVerifier(){
			 
			        public boolean verify(String hostname,
			                javax.net.ssl.SSLSession sslSession) {
			        		return true;
			        }
			    });
	}
    
    public ArrayList<Byte> send(ArrayList<Byte> al){
    	try {
            // create key and trust managers
            KeyManager[] keyManagers = createKeyManagers(keyStoreFileName, keyStorePassword, alias);
            
            TrustManager[] trustManagers = createTrustManagers();
            		
            // init context with managers data   
            factory = initItAll(keyManagers, trustManagers);
           
            // execute Post
            return executePost(url, al, factory);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 

	private ArrayList<Byte> executePost(String targetURL, ArrayList<Byte> al, SSLSocketFactory sslSocketFactory) throws IOException {
        URLConnection connection = new URL(targetURL).openConnection();
        HttpsURLConnection httpsConnection = null;
        if (connection instanceof HttpsURLConnection) {
        	httpsConnection = (HttpsURLConnection) connection;
        }
        else{
        	logger.warn("Connection is no HttpsURLConnection!");
        }
        
        try{
        	sendRequest(httpsConnection, al);
        	byte[] response = getResponse(httpsConnection);
			return KMIPUtils.convertByteArrayToArrayList(response);
		} 
        catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
        finally {
			if (httpsConnection != null) {
				httpsConnection.disconnect();
			}
		}
    }
 
	
	private void sendRequest(HttpsURLConnection httpsConnection, ArrayList<Byte> al) throws IOException {
    	httpsConnection.setSSLSocketFactory(factory);
    	httpsConnection.setRequestMethod("POST");
    	httpsConnection.setRequestProperty("Content-Type","*/*");
    	httpsConnection.setDoInput(true);
    	httpsConnection.setDoOutput(true); 
    
		// Send request
		DataOutputStream wr = new DataOutputStream(httpsConnection.getOutputStream());
		wr.write(KMIPUtils.toByteArray(al));
	
		wr.flush();
		wr.close(); 	
	}
	

	private byte[] getResponse(HttpsURLConnection httpsConnection) throws IOException {
		InputStream is = httpsConnection.getInputStream();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			 buffer.write(data, 0, nRead);
		}

		buffer.flush();
		return buffer.toByteArray();
	}

	private SSLSocketFactory initItAll(KeyManager[] keyManagers, TrustManager[] trustManagers)
        throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLSv1");
        context.init(keyManagers, trustManagers, null);
        return context.getSocketFactory();
    }
 
    private KeyManager[] createKeyManagers(String keyStoreFileName, String keyStorePassword, String alias) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        java.io.InputStream inputStream = new java.io.FileInputStream(keyStoreFileName);
        //create keystore object, load it with keystorefile data
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, keyStorePassword == null ? null : keyStorePassword.toCharArray());
 
        KeyManager[] managers;
        if (alias != null) {
        	managers = new KeyManager[] {new KMIPStubTransportLayerHTTPS().new AliasKeyManager(keyStore, alias, keyStorePassword)};
        } else {
            //create keymanager factory and load the keystore object in it 
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword == null ? null : keyStorePassword.toCharArray());
            managers = keyManagerFactory.getKeyManagers();
        }
        return managers;
    }
    
	private TrustManager[] createTrustManagers() {
		return new TrustManager[] { 
        	    new X509TrustManager() {     
        	        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
        	            return null;
        	        } 
        	        public void checkClientTrusted( 
        	            java.security.cert.X509Certificate[] certs, String authType) {
        	            } 
        	        public void checkServerTrusted( 
        	            java.security.cert.X509Certificate[] certs, String authType) {
        	        }
        	    } 
        	}; 

	}

 
	public void setTargetHostname(String value) {
		this.url = value;
		logger.info("Connection to: "+value);
	}
	
	public void setKeyStoreLocation(String property) {
		keyStoreFileName = property;
	}
	
	public void setKeyStorePW(String property) {
		keyStorePassword = property;
	}
    
    
    private class AliasKeyManager implements X509KeyManager {
 
        private KeyStore _ks;
        private String _alias;
        private String _password;
 
        public AliasKeyManager(KeyStore ks, String alias, String password) {
            _ks = ks;
            _alias = alias;
            _password = password;
        }
 
        public String chooseClientAlias(String[] str, Principal[] principal, Socket socket) {
            return _alias;
        }
 
        public String chooseServerAlias(String str, Principal[] principal, Socket socket) {
            return _alias;
        }
 
        public X509Certificate[] getCertificateChain(String alias) {
            try {
                java.security.cert.Certificate[] certificates = this._ks.getCertificateChain(alias);
                if(certificates == null){
                	throw new FileNotFoundException("no certificate found for alias:" + alias);
                }
                X509Certificate[] x509Certificates = new X509Certificate[certificates.length];
                System.arraycopy(certificates, 0, x509Certificates, 0, certificates.length);
                return x509Certificates;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
 
        public String[] getClientAliases(String str, Principal[] principal) {
            return new String[] { _alias };
        }
 
        public PrivateKey getPrivateKey(String alias) {
            try {
                return (PrivateKey) _ks.getKey(alias, _password == null ? null : _password.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
 
        public String[] getServerAliases(String str, Principal[] principal) {
            return new String[] { _alias };
        }
 
    }

}
