package com.ns.kgraphicsengin.customewidgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class SingleFontButton extends TextView
{

	public SingleFontButton(Context context) {
		super(context);
		init();
	}

	public SingleFontButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SingleFontButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public void init() {
      
	//	Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "pulsarjs.ttf");
		Typeface tf = Typeface.create(Typeface.createFromAsset(getResources().getAssets(), "pulsarjs.ttf"), Typeface.BOLD);
		
       setTypeface(tf);

    }
}
