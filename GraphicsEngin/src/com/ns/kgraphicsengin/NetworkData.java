package com.ns.kgraphicsengin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.ns.kgraphicsengin.security.CustomSSLSocketFactory;

public class NetworkData
{
	private ClientConnectionManager	clientConnectionManager;
	DefaultHttpClient				client;
	private HttpContext				context;
	private HttpParams				params;
	String							username, password;

	private void setup(String host)
	{
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		SchemeRegistry schemeRegistry = new SchemeRegistry();

		try
		{
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			CustomSSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
			sf.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);

			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", sf, 443));

		}
		catch (KeyManagementException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (KeyStoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnrecoverableKeyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (CertificateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 1);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(1));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);

		HttpConnectionParams.setConnectionTimeout(params, 50000);
		HttpConnectionParams.setSoTimeout(params, 50000);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf8");

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		// set the user credentials for our site "example.com"
		// credentialsProvider.setCredentials(new AuthScope("example.com",
		// AuthScope.ANY_PORT),
		// new UsernamePasswordCredentials("UserNameHere", "UserPasswordHere"));
		credentialsProvider.setCredentials(new AuthScope(host, AuthScope.ANY_PORT), new UsernamePasswordCredentials("", ""));
		clientConnectionManager = new ThreadSafeClientConnManager(params, schemeRegistry);

		context = new BasicHttpContext();
		context.setAttribute("http.auth.credentials-provider", credentialsProvider);

		client = new DefaultHttpClient(clientConnectionManager, params);

		// Set verifier
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

	}

	/**
	 * @param String
	 *            Host for https or http scheams.
	 * @author khalid khan
	 */
	public NetworkData(String host)
	{
		setup(host);
	}

	/**
	 * @param String
	 *            Host for http scheams.
	 * @author khalid khan
	 */
	public NetworkData()
	{
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		client = new DefaultHttpClient(httpParams);
		context = null;

	}

	/**
	 * This Method returns XML, JSON or any String Resource from URI
	 * 
	 * @param String
	 *            Resource URI
	 * @author khalid khan
	 */
	public String getRemoteData(String url) throws ParseException, IOException, UnsupportedEncodingException, ClientProtocolException
	{
		HttpGet httpPost = new HttpGet(url);
		HttpResponse httpResponse = client.execute(httpPost, context);

		int status_cd = httpResponse.getStatusLine().getStatusCode();

		if (status_cd == 200)
		{
			HttpEntity httpEntity = httpResponse.getEntity();
			return EntityUtils.toString(httpEntity);
		}
		else

		return null;

	}
}
