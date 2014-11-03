package com.ns.kgraphicsengin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

public class XMLThemeParser
{
	String	TAG	= "XML Parsing";

	public XMLThemeParser()
	{

	}

	/**
	 * Getting XML from Assets
	 * 
	 * @param xml
	 *            string name of xml
	 * @throws IOException
	 * 
	 * 
	 * @author khalid khan
	 * */
	public String getLocalXML(String xml) throws IOException
	{
		InputStream in = KEngin.getContext().getAssets().open(xml);
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(isr);

		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null)
		{
			sb.append(line);
		}

		return sb.toString();
	}

	/**
	 * Getting XML from URL making HTTP request
	 * 
	 * @param url
	 *            string
	 * @throws IOException
	 * @throws ParseException
	 * @throws UnsupportedEncodingException
	 * @throws ClientProtocolException
	 * 
	 * @author khalid khan
	 * */
	public String getXmlFromUrl(String url, String xml) throws ParseException, IOException, UnsupportedEncodingException, ClientProtocolException
	{
		String data = (new NetworkData()).getRemoteData(url);
		if (data != null) return data;
		else return getLocalXML(xml);

	}

	private Map<String, String> getAttributes(XmlPullParser parser) throws Exception
	{
		Map<String, String> attrs = null;
		int acount = parser.getAttributeCount();
		if (acount != -1)
		{

			attrs = new HashMap<String, String>(acount);
			for (int x = 0; x < acount; x++)
			{

				attrs.put(parser.getAttributeName(x), parser.getAttributeValue(x));
			}
		}
		else
		{
			throw new Exception("Required entity attributes missing");
		}
		return attrs;
	}

	/**
	 * Getting Resource data
	 * 
	 * @param ctx
	 *            Context
	 * @param xml
	 *            real xml either from getLocalXML or getXMLFromUrl
	 * @throws Exception
	 * 
	 * 
	 * @author khalid khan
	 * */
	public boolean getData(Context ctx, String xml) throws Exception
	{
		boolean res = false;
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader(xml));
		int eventType = xpp.getEventType();
		/*String test = "";*/
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		SharedPreferences.Editor editor = prefs.edit();
		Map<String, String> attributes = null;
		Map<String, String> itemattrib = null;
		String array = "";
		boolean isArray = false;
		boolean isItem = false;
		boolean isColor = false;
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			if (eventType == XmlPullParser.START_TAG)
			{
				if (xpp.getName().equals("array") || xpp.getName().equals("integer-array"))
				{
					isArray = true;
					attributes = getAttributes(xpp);
				}
				if (xpp.getName().equals("item"))// depth2
				{
					isItem = true;
					itemattrib = getAttributes(xpp);
				}
				if (xpp.getName().equals("color"))
				{
					isColor = true;
					itemattrib = getAttributes(xpp);
				}

			}

			else if (eventType == XmlPullParser.END_TAG)
			{

				if (xpp.getName().equals("array") || xpp.getName().equals("integer-array"))
				{
					/*test += array;*/
					editor.putString(attributes.get("name"), array);
					editor.commit();
					isArray = false;
					array = "";
					attributes = null;

				}
				if (xpp.getName().equals("item"))
				{
					isItem = false;
					itemattrib = null;
				}
				if (xpp.getName().equals("color") && isColor)
				{
					isColor = false;
					itemattrib = null;
				}

			}
			else if (eventType == XmlPullParser.TEXT)
			{
				try
				{
					if (isArray && isItem && array != null && itemattrib.get("format") != null && itemattrib.get("format").equals("color"))
					{
						array += "" + Color.parseColor(xpp.getText()) + "|";

					}

					else if (isArray && isItem && array != null && attributes.get("name").startsWith("rd"))
					{
						array += "" + Integer.parseInt(xpp.getText()) + "|";

					}
					else if (isColor && itemattrib != null)
					{
						/*test += Color.parseColor(xpp.getText());*/
						editor.putInt(itemattrib.get("name"), Color.parseColor(xpp.getText()));
						editor.commit();
						itemattrib = null;
					}
					else if (isItem && itemattrib.get("format").equals("integer") && xpp.getDepth() == 2)
					{
						/*test += Integer.parseInt(xpp.getText());*/
						editor.putInt(itemattrib.get("name"), Integer.parseInt(xpp.getText()));
						editor.commit();

					}
					else if (isArray && array != null && xpp.getText().startsWith("@"))
					{
						array = xpp.getText().trim();

					}
				}
				catch (Exception exception)
				{}
			}
			eventType = xpp.next();
		}
		if (eventType == XmlPullParser.END_DOCUMENT) res = true;
		return res;

	}
}
