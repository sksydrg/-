package console.plugin.res;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.app.Activity;
import android.util.DisplayMetrics;

import static console.plugin.Canvas.applyApl;

public class BasicRender
{
	Activity acv;
	float dp;
	CanvasPrint deque;
	
	public static BasicRender basicRender(Activity activity) {
		return new BasicRender(activity);
	}
	
	public BasicRender(Activity activity) {
		
		acv = activity;
		
		var displayMetrics = new DisplayMetrics();
		activity.getDisplay().getMetrics(displayMetrics);

		dp = displayMetrics.density;
		
		renderView = new RelativeLayout(acv) {
			int x = 20, y = 2;
			Paint paint = new Paint();

			{
				paint.setAntiAlias(true);
				paint.setFilterBitmap(true);
				paint.setStyle(Paint.Style.FILL);
				paint.setTextSize(8 * dp);
				paint.setColor(Color.BLACK);
				
				//setBackgroundColor(Color.TRANSPARENT);
				setWillNotDraw(false);
				setClickable(true);

				post(() -> {
					
					var executer = Executors.newSingleThreadScheduledExecutor();
					executer.scheduleWithFixedDelay(new Runnable() {
							int cached_size_output;//, cached_size_debug;
							public void run() {
								if (acv.isFinishing() || acv.isDestroyed()) {
									executer.shutdown();
									//if (cached_fabric != null && rendered_fabric) {
									//	cached_fabric.recycle();
									//	cached_fabric = null;
									//}
								} else if (isShown() && deque != null) {

									if (deque.updateDeque()) {
										applyInvalidate();
									}
									//int size_output = deque.cached_output.size();
									//int size_debug = cached_debug.size();
									/*if (size_output > cached_size_output) {
									 applyInvalidate();
									 cached_size_output = size_output;
									 }*/
									/*if (size_output > cached_size_output || size_debug > cached_size_debug) {
									 postInvalidate();
									 cached_size_output = size_output;
									 cached_size_debug = size_debug;
									 }*/
								}
							}
						}, 250, 120, TimeUnit.MILLISECONDS);
				});
			}

			float camera_x, camera_y;
			float zoom = 1f;

			Paint fabric_paint = new Paint();

			public int isRenderDrawable(Canvas canvas,int draw_x,int draw_y,int num,Canvas_Attrs preview,boolean skipColumns) {
				if (preview != null) {
					if (preview.useAbsPosition) {}
					else if (preview.usePrepareRenderHeight) {}
					else if (preview.columnNum == num || (skipColumns && preview.columnNum > num) ) {
						return deque.prepare.get(preview).draw(canvas,draw_x,draw_y,fabric_paint,preview.viewport_width,preview.viewport_height,posted -> applyApl().postBackgroundThread(activity -> posted.accept(activity,() -> applyInvalidate())));
					}
				}
				return 0;
			}

			public void onDraw(Canvas canvas) {
				
				int size = (int) paint.getTextSize();
				int draw_y = y;
				int draw_x = x + 3 * size;
				int num = 1;
				int columnAt = 0;
				
				applyedInvalidate = false;

				deque.updateDeque();
				var columns = deque.columnArray.array();
				int columnSize = deque.columnArray.size();

				var prepared = deque.prepare.keySet().iterator();
				var preview = prepared.hasNext() ? prepared.next() : null;

				canvas.save();

				canvas.translate(camera_x,camera_y);

				canvas.scale(zoom,zoom);

				while (columnAt < columnSize) {
				    draw_y += size;
					canvas.drawText(String.valueOf(num),x,draw_y + 2,paint);

					int hasRenderDrawableHeight = isRenderDrawable(canvas,draw_x,draw_y,num,preview,false);
					if (hasRenderDrawableHeight != 0) {
						draw_y += hasRenderDrawableHeight;
						preview = prepared.hasNext() ? prepared.next() : null;
					} else {
						canvas.drawText(columns[columnAt],draw_x,draw_y,paint);
						columnAt++;
					}
					num++;
				}

				while (preview != null) {
					draw_y += isRenderDrawable(canvas,draw_x,draw_y,num,preview,true);
					preview = prepared.hasNext() ? prepared.next() : null;
					num++;
				}

				canvas.restore();

			}

			boolean hasLastEvent;
			float num_x = -1, num_y = -1, num_sx, num_sy, ps;

			public boolean onTouchEvent(MotionEvent event) {
				int event_action = event.getAction();
				int count = event.getPointerCount();
				if (event_action == MotionEvent.ACTION_UP || event_action == MotionEvent.ACTION_DOWN
					|| (hasLastEvent && (ps != count))) {
					if (hasLastEvent) {
						num_x = -1;
						num_y = -1;
						num_sx = -1;
						num_sy = -1;
						ps = -1;
						hasLastEvent = false;
					}
				} else {
					float sum_x = 0f, sum_y = 0f;

					for (int countAt = 0; countAt < count; countAt++) {
						sum_x += event.getX(countAt);
						sum_y += event.getY(countAt);
					}
					sum_x = sum_x / count;
					sum_y = sum_y / count;
					float scaled_sx = sum_x;
					float scaled_abs_x = 0f;
					float scaled_sy = sum_y;
					float scaled_abs_y = 0f;
					for (int countf = 0; countf < count; countf++) {
						float scaled_x = event.getX(countf) - scaled_sx;
						scaled_abs_x += scaled_x >= 0f ? scaled_x : -scaled_x;
						float scaled_y = event.getY(countf) - scaled_sy;
						scaled_abs_y += scaled_y >= 0f ? scaled_y : -scaled_y;
					}
					scaled_abs_x = scaled_abs_x / count + scaled_sx;
					scaled_abs_x = scaled_abs_x / scaled_sx;
					scaled_abs_y = scaled_abs_y / count + scaled_sy;
					scaled_abs_y = scaled_abs_y / scaled_sy;
					if (hasLastEvent) {
						camera_x += sum_x - num_x;
						camera_y += sum_y - num_y;
						zoom *= (scaled_abs_x / num_sx) * (scaled_abs_y / num_sy);
						applyInvalidate();
					}
					num_x = sum_x;
					num_y = sum_y;
					num_sx = scaled_abs_x;
					num_sy = scaled_abs_y;
					ps = count;
					hasLastEvent = true;
				}
				return super.onTouchEvent(event);
			}

		};
	}
	
	boolean applyedInvalidate;
	
	public void applyInvalidate() {
		if (applyedInvalidate) {
			return;
		} else {
			applyedInvalidate = true;
			renderView.postInvalidate();
		}
	}
	
	RelativeLayout renderView;
	
	public View getRenderView() {
		return renderView;
	}
	
	public void registerCanvasPrint(CanvasPrint canvasPrint) {
		deque = canvasPrint;
	}

}
