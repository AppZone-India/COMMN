package com.ns.kgraphicsengin;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * This Provides Common Asynch Tast.
 * 
 * @author khalid Khan
 */

public class RemoteData
{
	public static int	RESULT_PLANE_TEXT	= 3;
	public static int	RESULT_JSON			= 1;
	public static int	RESULT_XML			= 2;
	boolean				isProgressDialogOn;
	RemoteProperty		remoteProperty;
	ProgressDialog		pdial;
	OnRemoteCompleated	remoteCompleated;
	private int			resultType			= 0;

	/**
	 * @return ProgressDialog
	 * @author khalid Khan
	 */
	public ProgressDialog getProgressDialog()
	{
		return pdial;
	}

	/**
	 * Method To set Progress dialog With Defauld Method. Connecting to
	 * remote... if you want to set your own message please use
	 * getProgressDialog().setMessage(message)
	 * 
	 * @param Context
	 *            Your Activity Context
	 * @author khalid khan
	 */
	public void setProgressDialog(Context context)
	{
		isProgressDialogOn = true;
		pdial = new ProgressDialog(context);
		pdial.setCancelable(false);
		pdial.setCanceledOnTouchOutside(false);
		pdial.setMessage(context.getText(R.string.connecting));
	}

	/**
	 * @param {@code}int Id for Callback Method remoteCompleated()
	 * @param Object
	 *            holder Object of that class in which you have implemented
	 *            OnRemoteCompleated
	 * @author khalid khan
	 */
	public RemoteData(int id, Object holder)
	{

		remoteCompleated = (OnRemoteCompleated) holder;
		remoteProperty = new RemoteProperty();
		remoteProperty.setId(id);

	}

	/**
	 * This method is only for JSON. for Other than JSON please use execute(int
	 * resultType,String... params)
	 * 
	 * @see execute(int resultType,String... params)
	 * @param String
	 *            ... list of parameters to execute Asynch Task.
	 * @return JSON object
	 * @author khalid Khan
	 * **/
	public void execute(String... params)
	{

		RemoteTask remoteTask = new RemoteTask();
		remoteTask.execute(params);
	}

	/**
	 * @param resultType
	 *            accepted Type of Result Plane text,XML ,or JSON
	 * @param String
	 *            ... list of parameters to execute Asynch Task.
	 * @return planeData text
	 * @author khalid Khan
	 * **/
	public void execute(int resultType, String... params)
	{
		this.resultType = resultType;
		execute(params);
	}

	public class RemoteProperty
	{
		private int			id;
		private JSONObject	jsonObject;
		private String		planeData;
		private Document document;

		public Document getDocument()
		{
			return document;
		}

		public void setDocument(String xml) throws ParserConfigurationException, IOException, SAXException
		{
			this.document = getDomElement(xml);
		}

		public String getPlaneData()
		{
			return planeData;
		}

		public void setPlaneData(String planeData)
		{
			this.planeData = planeData;
		}

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}

		public JSONObject getJsonObject()
		{
			return jsonObject;
		}

		public void setJsonObject(JSONObject jsonObject)
		{
			this.jsonObject = jsonObject;
		}

		/**
		 * Getting XML DOM element
		 * 
		 * @param XML
		 *            string
		 * @throws ParserConfigurationException
		 * @throws IOException
		 * @throws SAXException
		 * */
		private Document getDomElement(String xml) throws ParserConfigurationException, IOException, SAXException
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			return db.parse(is);
		}

		/**
		 * Getting node value
		 * 
		 * @param elem
		 *            element
		 */
		public final String getElementValue(Node elem)
		{
			Node child;
			if (elem != null)
			{
				if (elem.hasChildNodes())
				{
					for (child = elem.getFirstChild(); child != null; child = child.getNextSibling())
					{
						if (child.getNodeType() == Node.TEXT_NODE) { return child.getNodeValue(); }
					}
				}
			}
			return "";
		}

		/**
		 * Getting node value
		 * 
		 * @param Element
		 *            node
		 * @param key
		 *            string
		 * */
		public String getValue(Element item, String str)
		{
			NodeList n = item.getElementsByTagName(str);
			return this.getElementValue(n.item(0));
		}
	}

	public interface OnRemoteCompleated
	{

		public void remoteCompleated(RemoteProperty remoteProperty);

	}

	private class RemoteTask extends AsyncTask<String, ArrayList<HashMap<String, String>>, RemoteProperty>
	{

		@Override
		protected void onPreExecute()
		{
			if (isProgressDialogOn) pdial.show();
			super.onPreExecute();
		}

		@Override
		protected RemoteProperty doInBackground(String... params)
		{

			try
			{
				URL url = new URL(params[0]);

				String xml = new NetworkData(url.getHost()).getRemoteData(params[0]);
				if (resultType == RESULT_PLANE_TEXT) remoteProperty.setPlaneData(xml);
				else if(resultType == RESULT_XML) 
					remoteProperty.setDocument(xml);
				else
					remoteProperty.setJsonObject(new JSONObject(xml));
				return remoteProperty;
			}
			catch (ParseException e)
			{
				return null;
			}
			catch (UnsupportedEncodingException e)
			{
				return null;
			}
			catch (ClientProtocolException e)
			{
				return null;
			}
			catch (IOException e)
			{
				return null;
			}
			catch (Exception e)
			{
				return null;
			}
		}

		@Override
		protected void onPostExecute(RemoteProperty result)
		{
			try{
			if (isProgressDialogOn) pdial.dismiss();
			}catch(Exception exception)
			{}
			remoteCompleated.remoteCompleated(result);
			super.onPostExecute(result);

		}
	}

}
