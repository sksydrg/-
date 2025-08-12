package console.plugin.res;

import java.util.function.Supplier;

public class Canvas_Attrs
{
	static final int NoneClearColor = -1;
	int viewport_width;
	int viewport_height;
	Drawable render;
	int clearColor = NoneClearColor;
	boolean isTouchable;
	boolean disposeable;
	boolean useBackground;
	Drawable background;
	boolean useStringClip;
	Supplier<String> clipProvider;
	
	boolean useAbsPosition;
	int[] absPosition;
	boolean usePrepareRenderHeight;
	int prepareRenderHeight;
	int columnNum;

	public Canvas_Attrs register() {
		render.register(this);
		return this;
	}
}
