package com.ns.kgraphicsengin.customewidgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.ns.kgraphicsengin.R;

public class FontButton extends View
{
	private Paint	topTextPaint;
	private int		topTextFont;
	private String	topText;
	private int		topTextColor;
	private Paint	textPaint;
	private String	text;
	private int		textFont;
	private int		textColor;
//	private float	textSize, topTextSize;
	//float			toptextHeight;
	private int		topTextWidth;
	float			textHeight;
	private int		textWidth;

	public Paint getTopTextPaint()
	{
		return topTextPaint;
	}

	public void setTopTextPaint(Paint topTextPaint)
	{
		this.topTextPaint = topTextPaint;
	}

	public int getTopTextFont()
	{
		return topTextFont;
	}

	public void setTopTextFont(int topTextFont)
	{
		this.topTextFont = topTextFont;
	}

	public String getTopText()
	{
		return topText;
	}

	public void setTopText(String topText)
	{
		this.topText = topText;
	}

	public int getTopTextColor()
	{
		return topTextColor;
	}

	public void setTopTextColor(int topTextColor)
	{
		this.topTextColor = topTextColor;
	}

	public Paint getTextPaint()
	{
		return textPaint;
	}

	public void setTextPaint(Paint textPaint)
	{
		this.textPaint = textPaint;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public int getTextFont()
	{
		return textFont;
	}

	public void setTextFont(int textFont)
	{
		this.textFont = textFont;
	}

	public int getTextColor()
	{
		return textColor;
	}

	public void setTextColor(int textColor)
	{
		this.textColor = textColor;
	}

	/*public float getTextSize()
	{
		return textSize;
	}

	public void setTextSize(float textSize)
	{
		this.textSize = textSize;
	}

	public float getTopTextSize()
	{
		return topTextSize;
	}

	public void setTopTextSize(float topTextSize)
	{
		this.topTextSize = topTextSize;
	}
*/
	/*public float getToptextHeight()
	{
		return toptextHeight;
	}

	public void setToptextHeight(int toptextHeight)
	{
		this.toptextHeight = toptextHeight;
	}
*/
	public int getTopTextWidth()
	{
		return topTextWidth;
	}

	public float gettextHeight()
	{
		return textHeight;
	}

	public int getTextWidth()
	{
		return textWidth;
	}

	public void setTopTextWidth(int topTextWidth)
	{
		this.topTextWidth = topTextWidth;
	}

	public void setTextHeight(int textHeight)
	{
		this.textHeight = textHeight;
	}

	public void setTextWidth(int textWidth)
	{
		this.textWidth = textWidth;
	}

	private int getTextWidth(String text, Paint paint)
	{
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int width = bounds.left + bounds.width();
		return width;
	}

	private float getTextHeight(String text, Paint paint)
	{
		/*
		 * Rect bounds = new Rect(); paint.getTextBounds(text, 0, text.length(),
		 * bounds); int height = bounds.bottom + bounds.height();
		 */
		return -paint.ascent();
	}

	boolean	isbottomText;

	public FontButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.font_button, 0, 0);
		text = a.getString(R.styleable.font_button_text);
		textFont = a.getInt(R.styleable.font_button_textFont, 0);
		topText = a.getString(R.styleable.font_button_topText);
		topTextFont = a.getInt(R.styleable.font_button_topTextFont, 0);
		topTextColor = a.getColor(R.styleable.font_button_topTextColor, Color.BLACK);
		textColor = a.getColor(R.styleable.font_button_textColor, Color.BLACK);
	/*	textSize = a.getFloat(R.styleable.font_button_textSize, 7);
		topTextSize = a.getFloat(R.styleable.font_button_topTextSize, 7);
	*/	isbottomText = a.getBoolean(R.styleable.font_button_isbottomText, true);
		if (isbottomText)
		{
			textPaint = new Paint();
			textPaint.setAntiAlias(true);
			textPaint.setColor(textColor);
			//textPaint.setTextSize((float) (textSize * 0.75));
			textHeight = getTextHeight(text, textPaint);
			textWidth = getTextWidth(text, textPaint);
		}
		a.recycle();
		topTextPaint = new Paint();
		topTextPaint.setAntiAlias(true);
		topTextPaint.setColor(topTextColor);
		//topTextPaint.setTextSize((float) (topTextSize * 0.75));
		Typeface tf = Typeface.create(Typeface.createFromAsset(getResources().getAssets(), "pulsarjs.ttf"), Typeface.BOLD);
		topTextPaint.setTypeface(tf);
		//toptextHeight = getTextHeight(topText, topTextPaint);
		topTextWidth = getTextWidth(topText, topTextPaint);

	}

	private void setFont(int font, Paint paint)
	{

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{

		int desiredWidth = Math.max(textWidth, topTextWidth) + getPaddingLeft() + getPaddingRight();
		float desiredHeight = (textHeight +/* toptextHeight */40+ getPaddingBottom() + getPaddingTop()) + 10;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		float height;

		// Measure Width
		if (widthMode == MeasureSpec.EXACTLY)
		{
			// Must be this size
			width = widthSize;
		}
		else if (widthMode == MeasureSpec.AT_MOST)
		{
			// Can't be bigger than...
			width = Math.min(desiredWidth, widthSize);
		}
		else
		{
			// Be whatever you want
			width = desiredWidth;
		}

		// Measure Height
		if (heightMode == MeasureSpec.EXACTLY)
		{
			// Must be this size
			height = heightSize;
		}
		else if (heightMode == MeasureSpec.AT_MOST)
		{
			// Can't be bigger than...
			height = Math.min(desiredHeight, heightSize);
		}
		else
		{
			// Be whatever you want
			height = desiredHeight;
		}

		// MUST CALL THIS
		setMeasuredDimension(width, (int) height);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();
		int width = getWidth();
		int height=getHeight();		
		canvas.save();
		topTextPaint.setTextAlign(Align.CENTER);
		if (isbottomText)
		{
			float Y=height*.7f;
			float X=width/2;
			float destextsize=height-Y-paddingBottom;
			float textwidth=textPaint.measureText(text);
			float deswidth=width-paddingLeft-paddingBottom;	
			
			textPaint.setTextAlign(Align.CENTER);
			topTextPaint.setTextSize(Y-paddingTop);			
			textPaint.setTextSize(destextsize);
			while(textwidth>deswidth)
			{
				textPaint.setTextSize(--destextsize);
				textwidth=textPaint.measureText(text);
			}
			canvas.drawText(topText,X, Y , topTextPaint);
			canvas.drawText(text, X, height-paddingBottom, textPaint);
		}
		else canvas.drawText(topText, (width) / 2, getHeight() / 2, topTextPaint);
		
		super.onDraw(canvas);
	}
}
