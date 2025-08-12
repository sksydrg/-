package console.plugin;

import console.plugin.res.AplProvider;
import console.plugin.res.Drawable;
import console.plugin.res.CanvasPrint;
import console.plugin.res.Canvas_Attrs;
import console.plugin.res.Shader;
import console.plugin.res.BasicRender;
import console.plugin.res.BasicViewUI;
import java.util.function.Function;
import android.app.Activity;
import android.os.Looper;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

public class Canvas
{
	private static Canvas applyCanvas;
	
	private static CanvasPrint queue;
	
	public static AplProvider applyApl() {
		return provider;
	}
	
	private static AplProvider provider = new AplProvider() {
		public Activity getActivityContext() {
			if (Looper.getMainLooper().isCurrentThread()) {
				return applyCanvas.activity;
			} else {
				throw new RuntimeException("The other thread cannot read this aplContext.");
				//return null;
			}
		}
	};
	
	Activity activity;
	
	private void register_Drawable(Canvas_Attrs canvas_attrs,Drawable drawable) {
		drawable.register(canvas_attrs);
		queue.registerDrawable(canvas_attrs,drawable);
	}
	
	public void register_Drawable(Drawable drawable) {
		var canvas_attrs = new Canvas_Attrs();
		register_Drawable(canvas_attrs,drawable);
	}
	
	public void register_Drawable(Function<Canvas_Attrs,Drawable> drawableConfiguration) {
		var canvas_attrs = new Canvas_Attrs();
		var drawable = drawableConfiguration.apply(canvas_attrs);
		register_Drawable(canvas_attrs,drawable);
	}
	
	public Shader.Shader_Render register_Shader_ARGB(Function<Canvas_Attrs,Shader.graphic_shader_argb> shaderConfiguration) {
		var canvas_attrs = new Canvas_Attrs();
		var shader = shaderConfiguration.apply(canvas_attrs);
		var render = Shader.Shader_Render.graphic_shader(shader);
		register_Drawable(canvas_attrs,render);
		return render;
	}
	
	public Shader.Shader_Render register_Shader(Function<Canvas_Attrs,Shader.graphic_shader_color> shaderConfiguration) {
		var canvas_attrs = new Canvas_Attrs();
		var shader = shaderConfiguration.apply(canvas_attrs);
		var render = Shader.Shader_Render.graphic_shader(shader);
		register_Drawable(canvas_attrs,render);
		return render;
	}
	
	public Shader.Shader_Render register_Shader_ARGB(Shader.graphic_shader_argb shader) {
		return register_Shader(shader);
	}
	
	public Shader.Shader_Render register_Shader(Shader.graphic_shader_color shader) {
		var canvas_attrs = new Canvas_Attrs();
		var render = Shader.Shader_Render.graphic_shader(shader);
		register_Drawable(canvas_attrs,render);
		return render;
	}
	
	public void update_Drawable_Configuration(Drawable drawable) {
		queue.updateDrawableConfiguration(drawable);
	}
	
	public void applyRegisterBasicRender(BasicRender render) {
		render.registerCanvasPrint(queue);
	}
	
	public Canvas () {
		queue = new CanvasPrint();
	}
	
	public Canvas (Activity current) {
		this();
		activity = current;
	}
	
	public static void startCanvasPrint() {
		var currentActivity = Configuration.getCurrentActivity();
		
		applyCanvas = new Canvas(currentActivity);
		
		BasicRender basic = BasicRender.basicRender(currentActivity);
		
		applyCanvas.applyRegisterBasicRender(basic);
		
		var renderView = basic.getRenderView();
		
		var generator = BasicViewUI.basicViewGenerator(currentActivity);
		
		var view = generator.basic();
		
		generator.registerRenderViewParams(renderView);
		
		view.addView(renderView);
		
		System.setOut(queue.get_print_outupt());
		
		System.setErr(queue.get_print_debug());
		
		var decorView = castView(currentActivity.getWindow().getDecorView());
		
		currentActivity.runOnUiThread(() -> decorView.removeAllViewsInLayout());
		
		currentActivity.runOnUiThread(() -> decorView.addView(view));
		
	}
	
	public static void registerDrawable(Drawable drawable) {
		applyCanvas.register_Drawable(drawable);
	}

	public static void registerDrawable(Function<Canvas_Attrs,Drawable> drawableConfiguration) {
		applyCanvas.register_Drawable(drawableConfiguration);
	}

	public static Shader.Shader_Render registerShaderARGB(Function<Canvas_Attrs,Shader.graphic_shader_argb> shaderConfiguration) {
		return applyCanvas.register_Shader_ARGB(shaderConfiguration);
	}

	public static Shader.Shader_Render registerShader(Function<Canvas_Attrs,Shader.graphic_shader_color> shaderConfiguration) {
		return applyCanvas.register_Shader(shaderConfiguration);
	}
	
	public static Shader.Shader_Render registerShaderARGB(Shader.graphic_shader_argb shader) {
		return applyCanvas.register_Shader_ARGB(shader);
	}
	
	public static Shader.Shader_Render registerShader(Shader.graphic_shader_color shader) {
		return applyCanvas.register_Shader(shader);
	}
	
	public static void updateDrawableConfiguration(Drawable drawable) {
		applyCanvas.update_Drawable_Configuration(drawable);
	}
	
	public static ViewGroup castView(View v) {
		if (v instanceof ViewGroup vt) {
			return vt;
		} else {
			return null;
		}
	}
	
	
	class Configuration {
		
		
		
		public static Activity getCurrentActivity() {
			try {
				var type = Class.forName("android.app.ActivityThread");
				var activityThread = type.getDeclaredMethod("currentActivityThread").invoke(type);
				var field = type.getDeclaredField("mActivities");
				field.setAccessible(true);
				if (field.get(activityThread) instanceof ArrayMap activityMap) {
					var provider = activityMap.values().stream().findFirst().get();
					var activityProvider = provider.getClass().getDeclaredField("activity");
					activityProvider.setAccessible(true);
					Activity activity = (Activity) activityProvider.get(provider);
					activityProvider.setAccessible(false);
					field.setAccessible(false);
					return activity;
				}
			} catch (Exception E) {
				E.printStackTrace();
			}
			return null;
		}
	}
	
}
