package com.ns.kgraphicsengin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author khalid khan
 */
public class KEngin extends Application
{
	private static Context					mContext;
	private static boolean					isUpgrade;
	private static ScreenGraphics			graphics;
	private static SharedPreferences		pref;
	private static SharedPreferences.Editor	editor;
	private static int						currentVersion;
	private static int						previousVersion;
	private static ConnectivityManager		cm;
	private static String					resourceUrl	= "";

	/**
	 * @return ResourceUrl from which it is accepted to get required resourecs
	 *         for the applecation.
	 * @author khalid khan
	 */
	public static String getResourceUrl()
	{
		return resourceUrl;
	}
	/**
	 * @param ResourceUrl
	 *            from which it is accepted to get required resourecs for the
	 *            applecation.
	 * @return void
	 * @author khalid khan
	 */
	public static void setResourceUrl(String resourceUrl)
	{
		KEngin.resourceUrl = resourceUrl;
	}
	/**
	 * @param email
	 *            Email address to Verify if it is correct or not.
	 * @return true if email is correct else false.
	 * @author khalid Khan
	 * */
	public boolean isEmailValid(String email)
	{
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches())
		{
			isValid = true;
		}
		return isValid;
	}

	@Override
	public void onCreate()
	{
		mContext = this;
		super.onCreate();
		init();
		cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * @return Current version of Application.
	 * @author khalid khan
	 */
	public static int getCurrentVersion()
	{
		return currentVersion;
	}

	/**
	 * @return Olde version of Application.
	 * @author khalid khan
	 */
	public static int getPreviousVersion()
	{
		return previousVersion;
	}

	/**
	 * @return True if Network is available.
	 * @author khalid khan
	 */
	public static boolean isOnline()
	{
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) { return true; }
		return false;
	}

	private void init()
	{
		graphics = ScreenGraphics.getInstance(this, false);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = pref.edit();
		previousVersion = pref.getInt("versionNumber", 0);
		checkUpgrade();

	}

	/**
	 * @return Application Context.
	 * @author khalid Khan
	 * */
	public static Context getContext()
	{
		return mContext;
	}

	/**
	 * @return Resource Object.
	 * @author khalid Khan
	 * */
	public static Resources getResource()
	{
		return mContext.getResources();
	}

	/**
	 * @param int colorId
	 * @return Color
	 * @author khalid khan
	 * */
	public static int getColor(int id)
	{
		return mContext.getResources().getColor(id);
	}

	/**
	 * 
	 * @return True if the Application is Upgraded else False.
	 * @author khalid khan
	 */
	public static boolean isUpgrade()
	{
		return isUpgrade;
	}

	/**
	 * @return Static Instance of ScreenGraphics.
	 * @author khalid khan.
	 */
	public static ScreenGraphics getGraphics()
	{
		return graphics;
	}

	/**
	 * @return SharedPreferences object.
	 * @author khalid khan
	 */
	public static SharedPreferences getPref()
	{
		return pref;
	}

	/***
	 * To verify if the entered server domain is in correct format ie.
	 * domain:5060
	 * 
	 * @return True or False
	 * @author khalid khan
	 * */
	public static boolean varifyServerFormat(EditText v)
	{
		boolean b = v.getText().toString().split(":").length == 2 ? true : false;
		if (!b) Toast.makeText(getContext(), "Please enter domain/IP with port in given format", Toast.LENGTH_LONG).show();
		return b;

	}

	/**
	 * @return Editor dont forget to commit.
	 * @author khalid khan
	 */
	public static SharedPreferences.Editor getEditor()
	{
		return editor;
	}

	/**
	 * This method can be used only one time in whole the Application.
	 * @return True if it is forst Run of the Application.
	 * @author khalid khan
	 * */
	public static boolean isFirst()
	{
		if(pref.getBoolean("isfirst", true))
		{
			editor.putBoolean("isfirst", false).commit();
			return true;
		}
		return true;
	}

	private void checkUpgrade()
	{
		PackageInfo pinfo;
		currentVersion = 0;
		try
		{
			pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			currentVersion = pinfo.versionCode;
		}
		catch (NameNotFoundException e)
		{}
		isUpgrade = previousVersion < currentVersion;
		if(isUpgrade)
			editor.putBoolean("isfirst", true).commit();
		editor.putInt("versionNumber", currentVersion).commit();
		}

}
