package com.ns.kgraphicsengin.customewidgets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ns.kgraphicsengin.R;

public class SocialMedia extends LinearLayout implements OnClickListener
{
	ImageView facebook,twiter,share,rate;
	private Context mContext;
	String facebookPageURI="https://www.facebook.com/pages/MobiSnow/241334915977377";
	String twiterURI="https://mobile.twitter.com/mobisnow";//
	String shareBody ,shareSubject="Applications from Mobisnow",markaetURI;
	
	private onSocialClick click;
	
	public void setClickListner(onSocialClick click)
	{
		this.click = click;
	}

	public String getFacebookPageURI()
	{
		return facebookPageURI;
	}

	public void setFacebookPageURI(String facebookPageURI)
	{
		this.facebookPageURI = facebookPageURI;
	}

	public String getTwiterURI()
	{
		return twiterURI;
	}

	public void setTwiterURI(String twiterURI)
	{
		this.twiterURI = twiterURI;
	}

	public String getShareBody()
	{
		return shareBody;
	}

	public void setShareBody(String shareBody)
	{
		this.shareBody = shareBody;
	}

	public String getShareSubject()
	{
		return shareSubject;
	}

	public void setShareSubject(String shareSubject)
	{
		this.shareSubject = shareSubject;
	}

	public String getMarkaetURI()
	{
		return markaetURI;
	}

	public void setMarkaetURI(String markaetURI)
	{
		this.markaetURI = markaetURI;
	}

	public SocialMedia(Context context)
	{
		super(context);
		
	}

	public interface onSocialClick
	{
		public void onClick(String name);
	}
	public SocialMedia(final Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext=context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View v=inflater.inflate(R.layout.social, this, true);
		facebook=(ImageView)v.findViewById(R.id.facebook_like);
		twiter=(ImageView)v.findViewById(R.id.follow_on_twiter);
		share=(ImageView)v.findViewById(R.id.share);
		rate=(ImageView)v.findViewById(R.id.rateus);
		share.setOnClickListener(this);
		rate.setOnClickListener(this);
		twiter.setOnClickListener(this);
		facebook.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v)
	{
		int id=v.getId();
		String sid="";
		if(id==R.id.facebook_like)
		{
			sid="facebook";
			Intent viewIntent =  new Intent("android.intent.action.VIEW", Uri.parse(facebookPageURI));
			mContext.startActivity(viewIntent);
		}
		else if(id==R.id.follow_on_twiter)
		{
			sid="twiter";
			Intent viewIntent =  new Intent("android.intent.action.VIEW", Uri.parse(twiterURI));
			mContext.startActivity(viewIntent);
		}
		else if(id==R.id.share)
		{
			sid="share";
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			//String shareBody = "Hi, I am using A2Billing Callback client to make cheap calls and manage my account. Download from http://goo.gl/WE5G61";
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
			sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
		}
		else if(id==R.id.rateus)
		{
			sid="rateus";
			 Intent intent = new Intent(Intent.ACTION_VIEW);
			  intent.setData(Uri.parse("market://details?id="+markaetURI));//"com.project.a2billingcallback"
			  mContext.startActivity(intent);
		}
		if(click!=null)
			click.onClick(sid);
	}

}
