package com.ns.kgraphicsengin;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.View;

public class ScreenGraphics
{
	public final static int			STATE_NORMAL	= 0;
	public final static int			STATE_PRESSED	= 1;
	public final static int			STATE_DISSABLED	= 2;

	public final static int			LOGIN_BTN		= 0;
	public final static int			LOGIN_PAN		= 1;
	public final static int			LOGIN_EDT_TXT	= 2;
	public final static int			MENU_ITEM		= 3;

	public final static int			IMAGE_RES		= 1;
	public final static int			XML_RES			= 2;
	public final static int			WEL_EDT_TXT		= 4;
	Context							ctx;
	Resources						res;
	private static ScreenGraphics	graphics		= null;
	private Orientation				orientation;

	private int[] getArray(String name)
	{
		String temp = KEngin.getPref().getString(name, null);
		if (temp != null)
		{
			if (temp.startsWith("@")) return getArray(temp.replaceFirst("@", ""));
			int ar[] = null;
			if (!temp.startsWith("@"))
			{

				String array[] = temp.split("\\|");
				ar = new int[array.length];
				for (int i = 0; i < array.length; i++)
				{
					if (array[i] != null && !array[i].equals(""))
					{
						ar[i] = Integer.valueOf(array[i]);
					}
				}

			}
			if (ar != null) return ar;
		}
		return new int[]
		{
				0, 0, 0
		};

	}

	/*
	 * private int[] getArray(String name) { String temp =
	 * KEngin.getPref().getString(name, null); int colorGenerator; if (temp !=
	 * null) { if (temp.startsWith("@")) return getArray(temp.replaceFirst("@",
	 * "")); int ar[] = null; if (!temp.startsWith("@")) {
	 * 
	 * String array[] = temp.split("\\|"); ar = new int[array.length]; for (int
	 * i = 0; i < array.length; i++) { if (array[i] != null &&
	 * !array[i].equals("")) { ar[i] = Integer.valueOf(array[i]);
	 * 
	 * colorGenerator = new ColorGenerator(-300333195).getColor(2); } }
	 * 
	 * } if (ar != null) return ar; } return new int[] { 0xff48BFEA, 0xff158CB7,
	 * 0xff48BFEA };
	 * 
	 * }
	 */
	/**
	 * Level 0=Top,1=Bottom
	 * */
	private int[] getColorArray(int color, int state, int level[])
	{
		ColorGenerator colorGenerator = new ColorGenerator(color);
		if (state == 1)
		{
			int car[] = new int[3];

			car[1] = color;
			if (level.length > 1)
			{
				car[0] = colorGenerator.getColor(level[0]);
				car[2] = colorGenerator.getColor(level[1]);
			}
			else
			{
				car[0] = colorGenerator.getColor(level[0]);
				car[2] = colorGenerator.getColor(level[0]);
			}
			return car;
		}
		return null;

	}

	private int getInteger(String name)
	{
		return KEngin.getPref().getInt(name, 0);

	}

	/**
	 * This static method provides instance of graphics class.
	 * 
	 * @param ctc
	 *            ->Context of your application.
	 * @param fronserver
	 *            -> {@literal}true if you want to get resources from your
	 *            server and false if you want to get resources from local
	 *            assets
	 * @return Instance of ScreenGraphics Class
	 * @author khalid khan
	 */
	static ScreenGraphics getInstance(Context ctx, boolean fronserver)
	{
		if (graphics == null) graphics = new ScreenGraphics(ctx);
		return graphics;
	}

	private ScreenGraphics(Context ctx)
	{
		this.orientation = Orientation.TOP_BOTTOM;
		this.ctx = ctx;
		this.res = ctx.getResources();

	}

	public Orientation getOrientation()
	{
		return orientation;
	}

	public void setOrientation(Orientation orientation)
	{
		this.orientation = orientation;
	}

	private int[] getShapeColor(String name, int state)
	{
		int color[] = null;
		switch (state)
		{

			case STATE_NORMAL:
				color = getArray("s0_" + name);
				return color;
			case STATE_PRESSED:
				color = getArray("s1_" + name);
				return color;
			case STATE_DISSABLED:
				color = getArray("s2_" + name);
				return color;
		}
		return null;

	}

	private Drawable getDrawable(String name, int state)
	{

		switch (state)
		{
			case STATE_NORMAL:
				return res.getDrawable(getResId("s0_+" + name, Drawable.class));
			case STATE_PRESSED:
				return res.getDrawable(getResId("s1_+" + name, Drawable.class));
			case STATE_DISSABLED:
				res.getDrawable(getResId("s2_+" + name, Drawable.class));
		}
		return null;

	}

	private Drawable getDrawable(GradientDrawable.Orientation oriantation, int shap, int[] color, float[] radii)
	{
		GradientDrawable drawable = new GradientDrawable(oriantation, color);
		drawable.setShape(shap);
		drawable.setCornerRadii(radii);
		return drawable;
	}

	/*
	 * private int[] getShapeColors(int id) { TypedArray ta =
	 * res.obtainTypedArray(id); int[] colors = new int[ta.length()]; for (int i
	 * = 0; i < ta.length(); i++) { colors[i] = ta.getColor(i, 0); }
	 * ta.recycle(); return colors; }
	 */
	private float[] getShapeRadii(String name, int state)
	{
		float srd[] = null;
		switch (state)
		{
			case STATE_NORMAL:
				srd = getCornerRadius(getArray("rd0_" + name));
				return srd;
			case STATE_PRESSED:
				srd = getCornerRadius(getArray("rd1_" + name));
				return srd;
			case STATE_DISSABLED:
				srd = getCornerRadius(getArray("rd2_" + name));
				return srd;
		}
		return null;
	}

	private float[] getCornerRadius(int[] radii)
	{

		float[] rd = new float[]
		{
				0, 0, 0, 0, 0, 0, 0, 0
		};
		if (radii == null) { return rd; }

		int j = 0;
		if (radii.length == 1)
		{
			for (int i = 0; i < rd.length; i++)
				rd[i] = radii[0];
			return rd;
		}
		if (radii.length == 2)
		{
			for (int i = 0; i < rd.length; i++)
				rd[i] = i % 2 == 0 ? radii[0] : radii[1];
			return rd;
		}
		for (int i = 0; i < radii.length; i++)
		{
			rd[j] = radii[i];
			if (radii.length <= 4) rd[++j] = radii[i];
			j++;
		}
		return rd;
	}

	private int[] getShapeStrok(String name, int state)
	{
		switch (state)
		{
			case STATE_NORMAL:
				return new int[]
				{
						getInteger("stw0_" + name), getInteger("stc0_" + name)
				};
			case STATE_PRESSED:
				return new int[]
				{
						getInteger("stw1_" + name), getInteger("stc1_" + name)
				};
			case STATE_DISSABLED:
				return new int[]
				{
						getInteger("stw2_" + name), getInteger("stc2_" + name)
				};
		}
		return null;
	}

	private int getResId(String variableName, Class<?> c)
	{

		try
		{
			Field idField = c.getDeclaredField(variableName);
			return idField.getInt(idField);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * The method provides single drawable of type.
	 * 
	 * @param type
	 *            ->XML_RES if you want dynamic resources and IMAGES_RES if you
	 *            want to get real drawables .
	 * @param name
	 *            -> name of drawable excluding prefix
	 * @param state
	 *            -> state of Drawable
	 *            STATE_NORMAL,STATE_PRESSED,STATE_DISSABLED
	 * @return Drawable
	 * 
	 * @deprecated getDrawable(int type, String name, int state, boolean flag)
	 * @author khalid khan
	 */
	public Drawable getDrawable(int type, String name, int state)
	{
		return getDrawable(type, name, state, true);
	}

	/**
	 * The method provides single drawable of type.
	 * 
	 * @param type
	 *            ->XML_RES if you want dynamic resources and IMAGES_RES if you
	 *            want to get real drawables .
	 * @param name
	 *            -> name of drawable excluding prefix
	 * @param state
	 *            -> state of Drawable
	 *            STATE_NORMAL,STATE_PRESSED,STATE_DISSABLED
	 * 
	 * @param flag
	 *            if it is true, drawable will have round corner
	 * @return Drawable
	 * 
	 * @author khalid khan
	 */
	public Drawable getDrawable(int type, String name, int state, boolean flag)
	{
		return getDrawable(type, name, state, orientation, flag);
	}

	/**
	 * The method provides single drawable of type.
	 * 
	 * @param type
	 *            ->XML_RES if you want dynamic resources and IMAGES_RES if you
	 *            want to get real drawables .
	 * @param name
	 *            -> name of drawable excluding prefix
	 * @param state
	 *            -> state of Drawable
	 *            STATE_NORMAL,STATE_PRESSED,STATE_DISSABLED
	 * @param orientation
	 *            To Specify {@link Orientation}
	 * @param flag
	 *            if it is true, drawable will have round corner
	 * @return Drawable
	 * 
	 * @author khalid khan
	 */
	public Drawable getDrawable(int type, String name, int state, GradientDrawable.Orientation orientation, boolean flag)
	{
		if (type == XML_RES)
		{
			int stroak[] = getShapeStrok(name, state);
			GradientDrawable drawable = new GradientDrawable(orientation, getShapeColor(name, state));
			if (stroak != null) drawable.setStroke(stroak[0], stroak[1]);
			float ar[] = getShapeRadii(name, state);
			if (ar != null) if (flag) drawable.setCornerRadii(ar);
			return drawable;
		}
		else
		{
			return getDrawable(name, state);
		}

	}

	/**
	 * The method provides selector of given type.
	 * 
	 * @param type
	 *            ->XML_RES if you want dynamic resources and IMAGES_RES if you
	 *            want to get real drawables .
	 * @param name
	 *            -> name of drawable excluding prefix
	 * 
	 * @return Selector
	 * 
	 * @deprecated StateListDrawable getSLTRDrawable(int type, String name,
	 *             boolean flag)
	 * @author khalid khan
	 */
	public StateListDrawable getSLTRDrawable(int type, String name)
	{
		return getSLTRDrawable(type, name, true);
	}

	/**
	 * The method provides selector of given type.
	 * 
	 * @param type
	 *            ->XML_RES if you want dynamic resources and IMAGES_RES if you
	 *            want to get real drawables .
	 * @param name
	 *            -> name of drawable excluding prefix
	 * @param flag
	 *            To set corner Defined in Asset XML
	 * @return Selector
	 * 
	 * @author khalid khan
	 */
	public StateListDrawable getSLTRDrawable(int type, String name, boolean flag)
	{
		StateListDrawable drawable = new StateListDrawable();

		drawable.addState(new int[]
		{
				-android.R.attr.state_pressed, -android.R.attr.state_focused, android.R.attr.state_enabled
		}, getDrawable(type, name, STATE_NORMAL, flag));
		drawable.addState(new int[]
		{
				android.R.attr.state_pressed, android.R.attr.state_enabled
		}, getDrawable(type, name, STATE_PRESSED, flag));
		drawable.addState(new int[]
		{
				android.R.attr.state_focused, android.R.attr.state_enabled
		}, getDrawable(type, name, STATE_PRESSED, flag));
		drawable.addState(new int[]
		{
			-android.R.attr.state_enabled
		}, getDrawable(type, name, STATE_DISSABLED, flag));

		return drawable;

	}

	/**
	 * The method provides selector with color Stops.
	 * 
	 * @param name
	 *            -> name of drawable excluding prefix Defined in Assets.Xml
	 * @param state
	 *            State Defined in Assets.Xml
	 * @param stop
	 *            array of Color Stops on Line of Gradient in range the of 0-1
	 * @param view
	 *            The view in Which You want to set Drawable
	 * @return PaintDrawable
	 * @author khalid khan
	 */
	public Drawable getDrawableWithColorStop(final String name, final int state, final float[] stop, final View view)
	{
		ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory()
		{
			@Override
			public Shader resize(int width, int height)
			{
				LinearGradient lg = new LinearGradient(0, 0, view.getWidth(), 0, getShapeColor(name, state), stop, Shader.TileMode.REPEAT);
				return lg;
			}
		};
		PaintDrawable p = new PaintDrawable();
		p.setShape(new RectShape());
		p.setShaderFactory(sf);
		return (Drawable) p;
	}

}
