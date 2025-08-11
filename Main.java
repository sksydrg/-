import android.app.*;
import android.content.*;
import java.lang.reflect.*;
import java.util.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.graphics.*;
import java.io.*;

import static java.util.Arrays.*;
import java.util.stream.*;
import java.util.function.*;
import android.util.*;
import java.util.concurrent.*;
import androidx.annotation.*;

public class Main {
	
	private static AplFunction applyApl;
	
	abstract static class AplFunction {
		
		ExecutorService executer;
		
		public abstract Activity getActivityContext();
		
		public void sendToClip(String text) {
			var clip = getActivityContext().getSystemService(ClipboardManager.class);
			clip.setText(text);
		}
		
		public void postBackgroundThread(Consumer<Activity> current) {
			if (executer == null) {
				executer = Executors.newSingleThreadExecutor();
			}
			var apl = getActivityContext();
			executer.submit(() -> current.accept(apl));
		}
	}
	
	public static void main(String[] args) {
		
		class ColumnNum {
			int num;
		}
		
		var columnNum = new ColumnNum();
		
		var cached_output = new ByteArrayOutputStream() {
			public synchronized void write(@RecentlyNonNull byte[] b, int off, int len) {
				int start = off;
				int end = off + len;
				while (start < end) {
					if (b[start] == 10) {
						columnNum.num++;
					}
					start++;
				}
				super.write(b,off,len);
			}
		};
		
		//var cached_input = new ByteArrayInputStream("".getBytes());
		
		var print_output = new PrintStream(cached_output);
		var print_debug = new PrintStream(cached_output);
		
		System.setOut(print_output);
		System.setErr(print_debug);
		
		var aapl = getAppContext();
		System.out.println(aapl);

		var acv = (Activity) getCurrentActivityReflected();
		System.out.println(acv);

		if (aapl instanceof Application apln) {
			var apl = (Application) apln;
			var wm = apl.getSystemService(WindowManager.class);
			System.out.println(wm);

			var wp = apl.getDisplay();
			var overlay = new RelativeLayout(acv);
			var ps = new WindowManager.LayoutParams(wp.getWidth() / 2, wp.getHeight() / 4,
					WindowManager.LayoutParams.TYPE_BASE_APPLICATION,
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
					PixelFormat.RGBA_8888);
			ps.y = 120;
			overlay.setBackgroundColor(Color.parseColor("#5F5BE4FF"));
			ps.gravity = Gravity.START | Gravity.TOP;
			overlay.setLayoutParams(ps);

			acv.runOnUiThread(() -> {
				var wq = acv.getWindowManager(); //getSystemService(WindowManager.class);
				wq.addView(overlay, ps);
				//var wg = (ViewGroup) acv.getWindow().getDecorView();
				//wg.addView(overlay);
				overlay.bringToFront();
				//Toast toast = Toast.makeText(acv,"Null Exception",Toast.LENGTH_SHORT);
				//toast.show();
			});
		}
		
		var basic = castView(acv.getWindow().getDecorView());
		
		//var plane = castView(basic.findViewById(Integer.parseInt("16908869")));
		
		try {
			acv.runOnUiThread(() -> basic.removeAllViewsInLayout());
		} catch (Exception E) {
			Toast toast = Toast.makeText(acv,"This " +E != null ? E.getLocalizedMessage() : "Null Exception",Toast.LENGTH_SHORT);
			toast.show();
		}
		
		//var planed = castView(plane.findViewById(Integer.parseInt("16908290")));
		//planed.removeAllViews();
		
		
		
		applyApl = new AplFunction() {
			public Activity getActivityContext() {
				if (Looper.getMainLooper().isCurrentThread()) {
					return acv;
				} else {
					throw new RuntimeException("The other thread cannot read aplContext");
					//return null;
				}
			}
		};
		
		var planes = new LinearLayout(acv) {
			{
				var displayMetrics = new DisplayMetrics();
				acv.getDisplay().getMetrics(displayMetrics);
				var dp = displayMetrics.density;
				
				int icon_size = (int) (dp * 64);
				
				int width = displayMetrics.widthPixels;
				int height = displayMetrics.heightPixels;
				
				setLayoutParams(new ViewGroup.LayoutParams(width,height));
				setOrientation(LinearLayout.VERTICAL);
				
				addView(new RelativeLayout(acv) {
					{
						var pts = new ViewGroup.LayoutParams(width,icon_size);
						setLayoutParams(pts);
						setBackgroundColor(Color.WHITE);
						setZ(12.0f);
						
						var psw = new LinearLayout.LayoutParams(icon_size,icon_size);
						
						addView(new ImageView(acv) {
							{
								setLayoutParams(psw);
								var icon = Bitmap.createBitmap(icon_size,icon_size,Bitmap.Config.RGB_565);
								var canvas = new Canvas(icon);
								var paint = new Paint();
								paint.setColor(Color.WHITE);
								paint.setStyle(Paint.Style.FILL);
								canvas.drawRect(0f,0f,canvas.getWidth(),canvas.getHeight(),paint);
								paint.reset();
								paint.setColor(Color.BLACK);
								paint.setStyle(Paint.Style.FILL);
								paint.setAntiAlias(true);
								paint.setFilterBitmap(true);
								int position = icon_size / 4;
								paint.setTextSize(icon_size / 2);
								canvas.save();
								canvas.scale(-1,1,icon_size/2,icon_size);
								canvas.drawText("➪",position,icon_size - position - (int) (6 * dp) ,paint);
								canvas.restore();
								paint.reset();
								setImageBitmap(icon);
								//icon.recycle();
								setOnClickListener(v -> acv.finish());
							}
						});
						addView(new LinearLayout(acv) {
							{
								setLayoutParams(pts);
								setOrientation(LinearLayout.HORIZONTAL);
								setGravity(Gravity.CENTER);
								
								addView(new ImageView(acv) {
									{
										setLayoutParams(psw);
										var icon = Bitmap.createBitmap(icon_size,icon_size,Bitmap.Config.RGB_565);
										var canvas = new Canvas(icon);
										var paint = new Paint();
										paint.setColor(Color.WHITE);
										paint.setStyle(Paint.Style.FILL);
										canvas.drawRect(0f,0f,canvas.getWidth(),canvas.getHeight(),paint);
										paint.reset();
										paint.setColor(Color.BLACK);
										paint.setStyle(Paint.Style.FILL);
										paint.setAntiAlias(true);
										paint.setFilterBitmap(true);
										int position = icon_size / 4;
										paint.setTextSize(icon_size / 2);
										canvas.drawText(">_",position,icon_size - position - (int) (5.2 * dp) ,paint);
										paint.reset();
										setImageBitmap(icon);
										//icon.recycle();
									}
								});
								addView(new TextView(acv) {
									{
										setText("Porgram Output");
										setTextSize(8 * dp);
										setTextColor(Color.BLACK);
									}
								});
							}
						});
					}
				});
				
				addView(new RelativeLayout(acv) {
					int x = 20,y = 20;
					Paint paint = new Paint();
					
					{
						paint.setAntiAlias(true);
						paint.setFilterBitmap(true);
						paint.setStyle(Paint.Style.FILL);
						paint.setTextSize(8*dp);
						paint.setColor(Color.BLACK);
						
						setLayoutParams(new LinearLayout.LayoutParams(width,height - icon_size));
						//setBackgroundColor(Color.TRANSPARENT);
						setWillNotDraw(false);
						setClickable(true);
						
						post(() -> {
							var executer = Executors.newSingleThreadScheduledExecutor();
							executer.scheduleWithFixedDelay(new Runnable() {
								int cached_size_output,cached_size_debug;
								public void run() {
									if (acv.isFinishing() ||acv.isDestroyed()) {
										executer.shutdown();
										//if (cached_fabric != null && rendered_fabric) {
										//	cached_fabric.recycle();
										//	cached_fabric = null;
										//}
									} else if (isShown()) {
										cached_output.size();
										int size_output = cached_output.size();
										//int size_debug = cached_debug.size();
										if (size_output > cached_size_output) {
											postInvalidate();
											cached_size_output = size_output;
										}
										/*if (size_output > cached_size_output || size_debug > cached_size_debug) {
											postInvalidate();
											cached_size_output = size_output;
											cached_size_debug = size_debug;
										}*/
									}
								}
							},250,120,TimeUnit.MILLISECONDS);
						});
					}
					
					float camera_x,camera_y;
					float zoom = 1f;
					int draw_y = (int) y;
					int num;
					
					int graphic_fabric_started = 250;
					
					static class Canvas_Attrs {
						static final int NoneClearColor = -1;
						int viewport_width;
						int viewport_height;
						graphics_render render;
						int clearColor = NoneClearColor;
						boolean isTouchable;
						boolean disposeable;
						boolean useAbsPosition;
						int[] absPosition;
						boolean useBackground;
						graphics_render background;
						boolean useStringClip;
						Supplier<String> clipProvider;
						
						public Canvas_Attrs register() {
							render.register(this);
							return this;
						}
					}
					
					static abstract class graphics_render {
						
						int graphic_fabric_width = 1200;
						int graphic_fabric_height = 800;
						
						public void register(Canvas_Attrs canvas_attrs) {
							canvas_attrs.viewport_width = graphic_fabric_width;
							canvas_attrs.viewport_height = graphic_fabric_height;
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
											applyApl.sendToClip(canvad_attrs.clipProvider.get());
										}
									}
								}
							}
						}
						
						public abstract int draw(Canvas canvas,int x,int y,Paint paint,int width,int height,Consumer<BiConsumer<Activity,Runnable>> backgroundSinglePoster);
						
					}
					
					static class graphic_shader_density_render extends graphics_render {

						graphic_shader_density_argb graphic_shader;
						
						int pixel_x,pixel_y,pixel_width_render,pixel_height_render,pixel_width_full,pixel_height_full;
						double x,y;
						long startedMills;
						long mills;
						long currentMills;
						float fTime;
						double time;
						double scale;
						double micro;
						double delay;
						double scale_viewport;
						double scale_draw;
						
						float[] canvas_matrix;
						
						private double lastCount;

						public int draw(Canvas canvas,int canvas_x,int canvas_y,Paint paint,int width,int height,Consumer<BiConsumer<Activity,Runnable>> backgroundSinglePoster) {
							if (canvas_matrix == null) {
								canvas_matrix = new float[9];
							}
							canvas.getMatrix().getValues(canvas_matrix);
							scale = canvas_matrix[Matrix.MSCALE_X];
							scale = scale / 16.0;
							double scaled = 1.0 / scale;
							double scale_viewport_x = (double) width / (double) this.graphic_fabric_width;
							double scale_viewport_y = (double) height / (double) this.graphic_fabric_height;
							scale_viewport = scale_viewport_x < scale_viewport_y ? scale_viewport_x : scale_viewport_y;
							scale_draw = scale_viewport / scale;
							mills = System.currentTimeMillis();
							currentMills = mills - startedMills;
							if (currentMills <= 0 || startedMills > currentMills) {
								startedMills = System.currentTimeMillis();
								currentMills = 0;
							}
							double count = System.nanoTime();
							if (lastCount > count) {
								lastCount = count;
							}
							count -= lastCount;
							count = count / 1e9;
							delay = count;
							time += count;
							int st =  count < 1.0 ? 0 : (int) Math.floor(count);
							micro = count - ((double)st);
							lastCount = count;
							pixel_width_render = (int) ((double) width * scale);
							pixel_height_render = (int) ((double) height * scale);
							pixel_width_full = this.graphic_fabric_width > pixel_width_render ? this.graphic_fabric_width : pixel_width_render;
							pixel_height_full = this.graphic_fabric_height > pixel_height_render? this.graphic_fabric_height : pixel_height_render;
							float canvas_draw_x = canvas_x;
							float canvas_draw_y = canvas_y;
							x = 0.0d;
							y = 0.0d;
							pixel_x = 0;
							pixel_y = 0;
							while (pixel_x < pixel_width_render - 1) {
								//paint.setStyle(Paint.Style.FILL);
								if (pixel_y < pixel_height_render - 1) {
									paint.setColor(graphic_shader.argb(this));
									canvas.drawRect(canvas_draw_x,canvas_draw_y,(float) (canvas_draw_x + scaled),(float) (canvas_draw_y + scaled),paint);
									paint.reset();
									canvas_draw_y += 1.0 / scale;
									y += 1.0 / pixel_height_render;
									pixel_y++;
								} else {
									
									paint.setColor(graphic_shader.argb(this));
									canvas.drawRect(canvas_draw_x,canvas_draw_y,(float) (canvas_draw_x + scaled),(float) (canvas_y + height),paint);
									paint.reset();
									
									canvas_draw_x += 1.0 / scale;
									x += 1.0 / pixel_width_render;
									pixel_x++;
									canvas_draw_y = canvas_y;
									y = 0.0d;
									pixel_y = 0;
								}
							}
							for(canvas_draw_y = canvas_y;pixel_y < pixel_height_render -1; pixel_y++) {
								
								paint.setColor(graphic_shader.argb(this));
								canvas.drawRect(canvas_draw_x,canvas_draw_y,(float) (canvas_x + width),(float) (canvas_draw_y + scaled),paint);
								paint.reset();
								
								canvas_draw_y += 1.0 / scale;
								y += 1.0 / pixel_height_render;
							}
							
							paint.setColor(graphic_shader.argb(this));
							canvas.drawRect(canvas_draw_x,canvas_draw_y,(float) (canvas_x + width),(float) (canvas_y + height),paint);
							paint.reset();
							
							return height;
						}
						
						public graphic_shader_density_render(graphic_shader_density_argb shader_argb) {
							this.graphic_shader = shader_argb;
						};
						
						public static graphic_shader_density_render graphic_shader(graphic_shader_density_argb shader_argb) {
							return new graphic_shader_density_render(shader_argb);
						}
						
						public graphic_shader_density_render() {};
						
						public static graphic_shader_density_render graphic_shader() {
							return new graphic_shader_density_render();
						}
						
					}
					
					interface graphic_shader_density_argb {
						public abstract int argb(graphic_shader_density_render res);
					}
					
					static class graphic_shader_render extends graphics_render {

						graphic_shader_argb graphic_shader;
						
						Bitmap cached_fabric;
						boolean rendered_fabric;

						public void register(Canvas_Attrs canvas_attrs) {
							super.register(canvas_attrs);
							canvas_attrs.disposeable = true;
						}

						public void disopse() {
							cached_fabric.recycle();
							cached_fabric = null;
						}

						public int draw(Canvas canvas,int x,int y,Paint paint,int width,int height,Consumer<BiConsumer<Activity,Runnable>> backgroundSinglePoster) {
							if (cached_fabric == null) {
								rendered_fabric = false;
								cached_fabric = Bitmap.createBitmap(this.graphic_fabric_width,this.graphic_fabric_height,Bitmap.Config.ARGB_8888);
								backgroundSinglePoster.accept((aapl,postInvalidate) -> {
									for(int fabric_x = 0,fabric_y = 0;fabric_x < graphic_fabric_width;fabric_y++) {
										if (fabric_y < graphic_fabric_height) {
											cached_fabric.setPixel(fabric_x,fabric_y,graphic_shader.argb(fabric_x,fabric_y));
										} else {
											fabric_y = 0;
											fabric_x++;
										}
									}
									if (aapl.isFinishing() || aapl.isDestroyed()) {
										cached_fabric.recycle();
										cached_fabric = null;
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
						
						public graphic_shader_render(graphic_shader_argb shader_argb) {
							this.graphic_shader = shader_argb;
						};
						
						public static graphic_shader_render graphic_shader(graphic_shader_argb shader_argb) {
							return new graphic_shader_render(shader_argb);
						}
						
						public graphic_shader_render() {};
						
						public static graphic_shader_render graphic_shader() {
							return new graphic_shader_render();
						}
						
					}
					
					interface graphic_shader_argb {
						public abstract int argb(int fabric_x,int fabric_y);
					}
					
					Color white = Color.valueOf(Color.WHITE);
					Color cyan = Color.valueOf(Color.CYAN);
					Color green = Color.valueOf(Color.GREEN);
					
					int graphic_fabric_width = 1200;
					int graphic_fabric_height = 800;
					
					graphic_shader_argb argb_shader = (fabric_x,fabric_y) -> {
						float x = (float) fabric_x / (float) graphic_fabric_width;
						float y = (float) fabric_y / (float) graphic_fabric_height;
						float alpha = white.alpha(),cr = white.red(),cg = white.green(),cb = white.blue();
						cr = x * cyan.red();
						cg = x * cyan.green() + y * green.green();
						cb = x * cyan.blue();
						return Color.argb(alpha,cr,cg,cb);
					};
					
					LinkedHashMap<Canvas_Attrs,graphics_render> prepare = new LinkedHashMap<Canvas_Attrs,graphics_render>();
					
					ArrayDeque<PrepaerdPreview_Attrs> queue = new ArrayDeque<PrepaerdPreview_Attrs>();
					
					public Canvas_Attrs registerDrawable(graphics_render drawable) {
						var canvas_attrs = new Canvas_Attrs();
						drawable.register(canvas_attrs);
						prepare.put(canvas_attrs,drawable);
						queue.addLast(applyQueuePreview());
						return canvas_attrs;
					}
					
					public Canvas_Attrs registerShadern(graphic_shader_argb shader_argb) {
						return registerDrawable(graphic_shader_render.graphic_shader(shader_argb));
					}
					
					public Canvas_Attrs registerShadernDensity(graphic_shader_density_argb shader_argb) {
						return registerDrawable(graphic_shader_density_render.graphic_shader(shader_argb));
					}
					
					{
						registerShadern(argb_shader);
						int fabric_width = 1250;
						var rs = registerShadern((fabric_x,fabric_y) -> {
							float x = (float) fabric_x / (float) fabric_width;
							float y = (float) fabric_x / (float) graphic_fabric_height;
							
							return Color.BLACK - (int) (fabric_x * fabric_y * Color.CYAN);
						});
						 rs.render.graphic_fabric_width = fabric_width;
						 rs.register();
						 
						 registerShadernDensity(res -> {
							 float cr = cyan.red() * (float) res.x;
							 float cg = cyan.green() * (float) res.x;
							 float cb = cyan.blue() * (float) res.x;
							 return Color.rgb(cr,cg,cb);
						 });
						 
						registerShadernDensity(res -> {
							float x = (float) res.x;
							float y = (float) res.y;
							float alpha = white.alpha(),cr = white.red(),cg = white.green(),cb = white.blue();
							cr = x * cyan.red();
							cg = x * cyan.green() + y * green.green();
							cb = x * cyan.blue();
							return Color.argb(alpha,cr,cg,cb);
						});
						 
						prepare.values().forEach(v -> {
							println(v.getClass().getSimpleName() + " " + v);
						});
						
						prepare.keySet().forEach(v -> {
							println(v.getClass().getSimpleName() + " " + v);
						});
						 
						queue.forEach(v -> {
							println(v.getClass().getSimpleName() + " int value " + v.prepareRenderHeight + " " + (v.usePrepareRenderHeight ? "usePrepareRenderHeight" : "nnone"));
						});
					}
					
					class PrepaerdPreview_Attrs {
						int columnNum;
						boolean usePrepareRenderHeight;
						int prepareRenderHeight;
					}
					
					private PrepaerdPreview_Attrs applyQueuePreview() {
						columnNum.num++;
						var preview_attrs = new PrepaerdPreview_Attrs();
						preview_attrs.columnNum = columnNum.num;
						//preview_attrs.usePrepareRenderHeight = true;
						//preview_attrs.prepareRenderHeight = graphic_fabric_started * (queue.size() + 1);
						return preview_attrs;
					}
					
					Paint fabric_paint = new Paint();
					
					public void onDraw(Canvas canvas) {
						super.onDraw(canvas);
						int size = (int) paint.getTextSize();
						num = 1;
						draw_y = (int) y + size;
						var prepared_preview = prepare.keySet().iterator();
						var preview = prepared_preview.hasNext() ? prepared_preview.next() : null;
						var prepared_queue_preview = queue.iterator();
						var column_preview = prepared_queue_preview.hasNext() ? prepared_queue_preview.next() : null;
						canvas.save();
						canvas.scale(zoom,zoom);
						canvas.translate(camera_x,camera_y);
						canvas.drawText("cream_x : " + camera_x + " creame_y:" + camera_y + " zoom : " + zoom,x + 3 * size,y,paint);
						String[] deque = new String(cached_output.toByteArray()).split("\n");
						int dequeColumnAt = 0;
						while (dequeColumnAt < deque.length) {
							var s = deque[dequeColumnAt];
							canvas.drawText(String.valueOf(num),x,draw_y + 2,paint);
							if (preview != null) {
								if (!preview.useAbsPosition || preview.absPosition == null || preview.absPosition.length < 1) {
									if (column_preview != null) {
										if (column_preview.usePrepareRenderHeight && draw_y + size > column_preview.prepareRenderHeight && draw_y < column_preview.prepareRenderHeight + preview.viewport_height - size) {
											draw_y += prepare.get(preview).draw(canvas,x + 3 * size,draw_y,fabric_paint,preview.viewport_width,preview.viewport_height,posted -> applyApl.postBackgroundThread(activity -> posted.accept(activity,() -> this.postInvalidate())));
											draw_y += size;
											num++;
											preview = prepared_preview.hasNext() ? prepared_preview.next() : null;
											column_preview = prepared_queue_preview.hasNext() ? prepared_queue_preview.next() : null;
											if (preview != null && column_preview != null && (!preview.useAbsPosition || preview.absPosition == null || preview.absPosition.length < 1) && column_preview.usePrepareRenderHeight) {
												if (draw_y > column_preview.prepareRenderHeight + preview.viewport_height - size) {
													column_preview.prepareRenderHeight = draw_y;
												} 
											}
											continue;
										} else {
											if (column_preview.columnNum == num) {
												draw_y += prepare.get(preview).draw(canvas,x + 3 * size,draw_y,paint,preview.viewport_width,preview.viewport_height,posted -> applyApl.postBackgroundThread(activity -> posted.accept(activity,() -> this.postInvalidate())));
												draw_y += size;
												num++;
												preview = prepared_preview.hasNext() ? prepared_preview.next() : null;
												column_preview = prepared_queue_preview.hasNext() ? prepared_queue_preview.next() : null;
												continue;
											}
										};
									}
								} else {};
							}
							canvas.drawText(s,x + 3 * size,draw_y,paint);
							draw_y += size;
							num++;
							dequeColumnAt++;
						};
						
						while (preview != null) {
							canvas.drawText(String.valueOf(num),x,draw_y + 2,paint);
							if (preview != null) {
								if (!preview.useAbsPosition || preview.absPosition == null || preview.absPosition.length < 1) {
									if (column_preview != null) {
										if (column_preview.usePrepareRenderHeight && draw_y < column_preview.prepareRenderHeight + preview.viewport_height - size) {
											draw_y += prepare.get(preview).draw(canvas,x + 3 * size,draw_y,paint,preview.viewport_width,preview.viewport_height,posted -> applyApl.postBackgroundThread(activity -> posted.accept(activity,() -> this.postInvalidate())));
											draw_y += size;
											num++;
											preview = prepared_preview.hasNext() ? prepared_preview.next() : null;
											column_preview = prepared_queue_preview.hasNext() ? prepared_queue_preview.next() : null;
										} else {
											if (column_preview.columnNum == num) {
												draw_y += prepare.get(preview).draw(canvas,x + 3 * size,draw_y,paint,preview.viewport_width,preview.viewport_height,posted -> applyApl.postBackgroundThread(activity -> posted.accept(activity,() -> this.postInvalidate())));
												draw_y += size;
												num++;
												preview = prepared_preview.hasNext() ? prepared_preview.next() : null;
												column_preview = prepared_queue_preview.hasNext() ? prepared_queue_preview.next() : null;
											}
										};
									}
								} else {};
							}
							preview = prepared_preview.hasNext() ? prepared_preview.next() : null;
							column_preview = prepared_queue_preview.hasNext() ? prepared_queue_preview.next() : null;
							if (preview != null && column_preview != null && (!preview.useAbsPosition || preview.absPosition == null || preview.absPosition.length < 1) && column_preview.usePrepareRenderHeight) {
								if (draw_y > column_preview.prepareRenderHeight + preview.viewport_height - size) {
									column_preview.prepareRenderHeight = draw_y;
								} 
							}
						}
						
						canvas.restore();
					}
					
					boolean hasLastEvent;
					float num_x = -1,num_y = -1,num_sx,num_sy,ps;
					
					public boolean onTouchEvent(MotionEvent event) {
						int event_action = event.getAction();
						int count = event.getPointerCount();
						if (event_action == MotionEvent.ACTION_UP || event_action == MotionEvent.ACTION_DOWN || (hasLastEvent && (ps != count))) {
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
							
							for(int countAt = 0; countAt < count; countAt++) {
								sum_x += event.getX(countAt);
								sum_y += event.getY(countAt);
							}
							sum_x = sum_x / count;
							sum_y = sum_y / count;
							float scaled_sx = sum_x;
							float scaled_abs_x = 0f;
							float scaled_sy = sum_y;
							float scaled_abs_y = 0f;
							for(int countf = 0; countf < count; countf++) {
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
								invalidate();
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
					
				});
			}
		};
		
		acv.runOnUiThread(() -> {
			try {
				planes.setBackgroundColor(Color.parseColor("#FFF2F2F2"));
				//planes.setBackgroundColor(Color.WHITE);
				basic.addView(planes);
				planes.bringToFront();
				
			} catch (Exception E) {
				Toast toast = Toast.makeText(acv,"This " +E != null ? E.getLocalizedMessage() : "Null Exception",Toast.LENGTH_SHORT);
				toast.show();
			}
		});
		
		/*acv.runOnUiThread(() -> {
			try {
				
				planes.setBackgroundColor(Color.parseColor("#FFF2F2F2"));
				//planes.setBackgroundColor(Color.WHITE);
				var wp = acv.getWindowManager();
				var relativeLayout = new RelativeLayout(acv);
				
				relativeLayout.addView(planes);
				//relativeLayout.setBackgroundColor(Color.BLACK);
				var pws = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
														 WindowManager.LayoutParams.TYPE_BASE_APPLICATION,
														 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
														 PixelFormat.RGBA_8888);

				pws.gravity = Gravity.START | Gravity.TOP;
				wp.addView(relativeLayout, pws);
				relativeLayout.bringToFront();
				planes.bringToFront();
				
			} catch(Exception E) {
				Toast toast = Toast.makeText(acv,"This " +E != null ? E.getLocalizedMessage() : "Null Exception",Toast.LENGTH_SHORT);
				toast.show();
			}
			
		});*/
		
		/*
		acv.runOnUiThread(() -> {
			var wq = acv.getWindowManager();
			wq.addView(planes, new WindowManager.LayoutParams(field , field,
															  WindowManager.LayoutParams.TYPE_BASE_APPLICATION,
															  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
															  PixelFormat.RGBA_8888) {
																  {
																	  gravity = Gravity.START | Gravity.TOP;
																  }
															  });
			planes.bringToFront();
		});
		*/
		

		var call = new Intent();
		call.setClass(aapl, actv.class);
		//acv.startActivity(call);

		var sts = new Object[]{acv.getActionBar(), acv.getApplication(), acv.getApplicationInfo(),
				acv.getWindow().getDecorView()};

		System.out.println(Arrays.toString(sts).replace(",", ",\n"));

		if (acv.getWindow().getDecorView() instanceof ViewGroup vt) {
			var base = (ViewGroup) vt;
			println(base.getFocusedChild());
			println(base.getClass().getSimpleName());
			println("────────");
			println(viewscantree(base));
			println("────────");
			println(viewscantree(base, "│  ", "   ", "└", v -> {
				var res = v.getResources();
				var id = v.getId();
				String ress = null;
				try {
					ress = id == View.NO_ID ? "none" : "[" + res.getResourceEntryName(id) +" " + id + "]";
				} catch (Throwable T) {}
				return v.getClass().getSimpleName() + " " + (ress == null ? "hide" : ress);
			}));
			stream(viewscan(base)).peek(view -> println(Arrays.toString(viewscan(view)).replace("},", "},\n"))).toArray();
		}
		
		System.out.println();
		
		int find = Integer.parseInt("2131231023");
		var actv = acv.findViewById(find);
		//actv = ((ViewGroup)actv).getChildAt(0);
		
		try {
			var cla = actv.getClass();
			System.out.println(cla.getCanonicalName());
			System.out.println();
			var ths = asList(cla.getMethods());
			var call_Printer = ths.stream().filter(v -> v.toString().contains("getOutput")).findFirst().get();
			println(call_Printer);
			//asList(cla.getMethods()).forEach(Main::println);
			//asList(cla.getMethods()).stream().map(String::valueOf).filter(s -> s.contains("Text")).filter(s -> {var rs = s.split(" "); return rs.length > 2 && rs[1].matches(".*([Ss]tring|Char|Text).*");}).forEach(System.out::println);
			System.out.println();
			
			var call_v_print = call_Printer.invoke(actv);
			println(call_v_print.getClass());
			//asList(call_v_print.getClass().getDeclaredFields()).forEach(Main::println);
			//printEach(call_v_print.getClass().getDeclaredMethods());
			var callWriter = stream(call_v_print.getClass().getMethods()).filter(v -> v.toString().contains("write(byte[])")).findFirst().get();
			println(callWriter);
			callWriter.invoke(call_v_print,cached_output.toByteArray());
			//callWriter.invoke(call_v_print,cached_debug.toByteArray());
			callWriter.invoke(call_v_print,"".getBytes());
			
		} catch (Exception E) {
			System.out.println(E.getLocalizedMessage());
		}
		

	}

	//main method;

	public static Context getAppContext() {
		try {
			Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
			Method currentApplication = activityThreadClass.getDeclaredMethod("currentApplication");
			Application app = (Application) currentApplication.invoke(null);
			return app != null ? app.getApplicationContext() : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Activity getCurrentActivityReflected() {
		try {
			Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
			Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
			Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
			activitiesField.setAccessible(true);
			Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
			if (activities == null || activities.isEmpty())
				return null;

			for (Object activityRecord : activities.values()) {
				Class<?> activityRecordClass = activityRecord.getClass();
				Field pausedField = activityRecordClass.getDeclaredField("paused");
				pausedField.setAccessible(true);
				if (!pausedField.getBoolean(activityRecord)) {
					Field activityField = activityRecordClass.getDeclaredField("activity");
					activityField.setAccessible(true);
					return (Activity) activityField.get(activityRecord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static class actv extends Activity {

		@Override
		public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
			// TODO: Implement this method
			super.onCreate(savedInstanceState, persistentState);
			var wd = getDisplay();
			var laview = new LinearLayout(this);
			laview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
			laview.setLayoutDirection(LinearLayout.VERTICAL);
			laview.setGravity(Gravity.CENTER);
			var textView = new TextView(this);
			textView.setText("Hi");
			laview.addView(textView);
			setContentView(laview);
		}

	}

	public static void println(Object obj) {
		System.out.println(obj);
	}

	//auto ln;
	public static String viewscantree(View view) {

		var column = viewscan(view);

		if (column != null) {
			var cun = asList(column);

			if (cun.stream().anyMatch(v -> v instanceof ViewGroup)) {
				var table = new HashMap<View, Collection<View>>();
				var col = new ArrayDeque<View>();
				col.addAll(cun);

				while (!col.isEmpty()) {
					View v = col.poll();
					View[] array;
					if ((array = viewscan(v)) != null) {
						var collection = asList(array);
						table.put(v, collection);
						col.addAll(collection);
					}
				}

				class MultInt {
					public int mult = 1;
					Consumer<View> call = null;
				}

				var tabd = new MultInt();
				var cache = new ByteArrayOutputStream(view.getAccessibilityClassName().length() * 120);
				var print = new PrintStream(cache);

				print.println(viewSimpleName(view));

				Consumer<View> each = v -> {
					print.println("   ".repeat(tabd.mult) + viewSimpleName(v));
					if (table.containsKey(v)) {
						tabd.mult++;
						table.get(v).forEach(tabd.call);
						tabd.mult--;
					}
				};

				tabd.call = each;

				cun.forEach(each);

				print.close();
				return new String(cache.toByteArray());
			}

			return view.getClass().getSimpleName() + cun.stream().collect(StringBuilder::new,(s, v) -> s.append("\n   " + viewSimpleName(v)), (s1, s2) -> s1.append(s2.toString())).toString();
		} else {
			return viewSimpleName(view);
		}
	}

	public static View[] viewscan(View v) {
		if (v instanceof ViewGroup vg) {
			var array = new View[vg.getChildCount()];
			for (int countAt = 0; countAt < array.length; countAt++) {
				array[countAt] = vg.getChildAt(countAt);
			}
			return array;
		} else {
			return null;
		}
	}

	public static String viewSimpleName(View v) {
		return v.getClass().getSimpleName();
	}

	public static String viewSimpleTitle(View v) {
		var res = v.getResources();
		var id = v.getId();
		String ress = null;
		try {
			ress = id == View.NO_ID ? "none" : "[" + res.getResourceEntryName(id) + "]";
		} catch (Throwable T) {
		}
		return viewSimpleName(v) + " " + (ress == null ? "hide" : ress);
	}

	//auto ln;
	public static String viewscantree(View view, String customText, String customStartText,String customIconText,Function<View, String> customTitle) {

		var column = viewscan(view);

		if (column != null) {
			var cun = asList(column);

			if (cun.stream().anyMatch(v -> v instanceof ViewGroup)) {
				var table = new HashMap<View, Collection<View>>();
				var col = new ArrayDeque<View>();
				col.addAll(cun);

				while (!col.isEmpty()) {
					View v = col.poll();
					View[] array;
					if ((array = viewscan(v)) != null) {
						var collection = asList(array);
						table.put(v, collection);
						col.addAll(collection);
					}
				}

				class MultInt {
					public int mult = 1;
					Consumer<View> call = null;
				}

				var tabd = new MultInt();
				var cache = new ByteArrayOutputStream(view.getAccessibilityClassName().length() * 120);
				var print = new PrintStream(cache);

				print.println(customTitle.apply(view));

				Consumer<View> each = v -> {
					print.println(customStartText + customText.repeat(tabd.mult - 1) + customIconText + customTitle.apply(v));
					if (table.containsKey(v)) {
						tabd.mult++;
						table.get(v).forEach(tabd.call);
						tabd.mult--;
					}
				};

				tabd.call = each;

				cun.forEach(each);

				print.close();
				return new String(cache.toByteArray());
			}

			return customTitle.apply(view)+ cun.stream().collect(StringBuilder::new, (s, v) -> s.append("\n   " + customTitle.apply(v)),(s1, s2) -> s1.append(s2.toString())).toString();
		} else {
			return customTitle.apply(view);
		}
	}
	
	public static void printEach(Object... arrayeds) {
		stream(arrayeds).forEach(Main::println);
	}
	
	public static ViewGroup castView(View v) {
		if (v instanceof ViewGroup vt) {
			return vt;
		} else {
			return null;
		}
	}

}

