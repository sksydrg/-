package console.plugin.res;

import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import android.app.Activity;

import static console.plugin.Canvas.applyApl;

public abstract class Drawable {

	public int graphic_fabric_width = 1200;
	public int graphic_fabric_height = 800;

	public void register(Canvas_Attrs canvas_attrs) {
		if (canvas_attrs.render == null) {
			canvas_attrs.viewport_width = canvas_attrs.viewport_width == 0 ? graphic_fabric_width : canvas_attrs.viewport_width;
			canvas_attrs.viewport_height = canvas_attrs.viewport_height == 0 ? graphic_fabric_height : canvas_attrs.viewport_height;
		} else {
			canvas_attrs.viewport_width = graphic_fabric_width;
			canvas_attrs.viewport_height = graphic_fabric_height;
		}
		canvas_attrs.render = this;
	}

	public void disopse() {}

	public void isTouched(MotionEvent event,Canvas_Attrs canvad_attrs,int x,int y) {
		if (canvad_attrs.useStringClip && canvad_attrs.clipProvider != null) {
			if (event.getPressure() > 0.5f) {
				int event_x = (int) event.getX();
				if (event_x > x && event_x < x + canvad_attrs.viewport_width) {
					int event_y = (int) event.getY();
					if (event_y > y && event_y < y + canvad_attrs.viewport_height) {
						applyApl().sendToClip(canvad_attrs.clipProvider.get());
					}
				}
			}
		}
	}

	public abstract int draw(Canvas canvas,int x,int y,Paint paint,int width,int height,Consumer<BiConsumer<Activity,Runnable>> backgroundSinglePoster);

}
