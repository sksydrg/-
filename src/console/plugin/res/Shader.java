package console.plugin.res;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.graphics.Rect;
import java.util.function.Consumer;
import java.util.function.BiConsumer;

public abstract class Shader
{
	public static interface graphic_shader_argb extends graphic_shader_color {
		
		public default int argb(Shader_Render_Attrs res) {
			return argb(res.pixel_x,res.pixel_y);
		}
		
		public abstract int argb(int pixel_x,int pixel_y);
	}
	
	public static interface graphic_shader_color {
		public abstract int argb(Shader_Render_Attrs res);
	}
	
	public static class Shader_Render_Attrs {
		float x;
		float y;
		float time;
		float time_total;
		float frameTime;
		int frame_total;
		int graphic_frames;
		int pixel_x;
		int pixel_y;
		int graphic_pixel_width;
		int graphic_pixel_height;
		int dynamic_pixel_width;
		int dynamic_pixel_height;
	}
	
	public static class Shader_Render extends Drawable
	{
		graphic_shader_color graphic_shader;

		Bitmap cached_fabric;
		boolean rendered_fabric;
		
		boolean dynamic;
		boolean direct_render_pixels;
		
		Shader_Render_Attrs res = new Shader_Render_Attrs();

		public void register(Canvas_Attrs canvas_attrs) {
			super.register(canvas_attrs);
			canvas_attrs.disposeable = true;
			res.graphic_pixel_width = graphic_fabric_width;
			res.graphic_pixel_height = graphic_fabric_height;
		}

		public void disopse() {
			if (cached_fabric != null) {
				if (!cached_fabric.isRecycled()) {
					cached_fabric.recycle();
				}
				cached_fabric = null;
			}
		}
		
		long lastTime;
		int lastFrame;
		long totalTime;
		
		int render_x, render_y;
		int render_width, render_height;
		
		public void act() {
			long current = System.currentTimeMillis();
			if (lastTime < current && current > 0) {
				long delay = current - lastTime;
				if (delay > 0) {
					totalTime += delay;
					double total = (double)totalTime / 1e3;
					res.time_total = (float) total;
					res.time = (float) (total - Math.floor(total));
				}
			}
			lastTime = current;
			
			if (res.graphic_frames != 0) {
				res.frame_total++;
				res.frameTime = (float) res.frame_total / (float) res.graphic_frames;
			}
			
			res.dynamic_pixel_width = res.graphic_pixel_width;
			res.dynamic_pixel_height = res.graphic_pixel_height;
			
		}
		
		public void update() {
			res.pixel_x = render_x;
			res.pixel_y = render_y;
			res.x = (float) render_x / (float) res.graphic_pixel_width;
			res.y = (float) render_y / (float) res.graphic_pixel_height;
		}
		
		public void draw(Bitmap pixels) {
			
			render_x = 0;
			render_y = 0;
			render_width = graphic_fabric_width;
			render_height = graphic_fabric_height;
			
			act();
			
			update();
			
			while (render_x < render_width) {
				render_y = 0;
				while (render_y < render_height) {
					
					pixels.setPixel(render_x,render_y,graphic_shader.argb(res));
					
					render_y++;
					update();
				}
				render_x++;
			}
			
		}

		public int draw(Canvas canvas,int x,int y,Paint paint,int width,int height,Consumer<BiConsumer<Activity,Runnable>> backgroundSinglePoster) {
			if (cached_fabric == null) {
				rendered_fabric = false;
				cached_fabric = Bitmap.createBitmap(this.graphic_fabric_width,this.graphic_fabric_height,Bitmap.Config.ARGB_8888);
				backgroundSinglePoster.accept((aapl,postInvalidate) -> {
					
					draw(cached_fabric);
					
					if (aapl.isFinishing() || aapl.isDestroyed()) {
						disopse();
					} else {
						rendered_fabric = true;
						postInvalidate.run();
					}
					//single.shutdown(); 这个卡位图
				});
			} else if (rendered_fabric) {
				float scale = (float) width / (float) graphic_fabric_width;
				float scaled = (float) height / (float) graphic_fabric_height;
				scale = scale < scaled ? scale : scaled;
				canvas.save();
				canvas.translate(x,y);
				canvas.scale(scale,scale);
				canvas.drawBitmap(cached_fabric,0,0,paint);
				canvas.restore();
			}
			return height;
		}
		
		public void directDrawPixels(Bitmap pixels,Rect rect,Paint paint,Consumer<BiConsumer<AplProvider,Runnable>> backgroundSinglePoster) {}

		public Shader_Render(graphic_shader_color shader_color) {
			this.graphic_shader = shader_color;
		};
		
		public Shader_Render() {};

		public static Shader_Render graphic_shader(graphic_shader_color shader_color) {
			return new Shader_Render(shader_color);
		}

		public static Shader_Render graphic_shader() {
			return new Shader_Render();
		}
	}
	
}
