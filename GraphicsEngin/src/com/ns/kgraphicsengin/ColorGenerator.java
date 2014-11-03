package com.ns.kgraphicsengin;

public class ColorGenerator
{
	float a,r, g, b, fR,fG,fB;
	ColorGenerator(int color)
	{
		
		a=(color>>24)&0xFF;
		r=(color>>16)&0xFF;
		g=(color>>8)&0xFF;
		b=(color>>0)&0xFF;
		
		fR=(255f-r)/10f;
		fG=(255f-g)/10f;
		fB=(255f-b)/10f;
		color=0;
	}

	public int getColor(float cp)
	{
		int	color=0;
		color=color<<0|(int)a;
		color=color<<8|(int)(r+fR*cp);
		color=color<<8|(int)(g+fG*cp);
		color=color<<8|(int)(b+fB*cp);
		return color;
	}
}
