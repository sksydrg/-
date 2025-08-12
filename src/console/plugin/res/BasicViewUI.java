package console.plugin.res;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Gravity;

public class BasicViewUI {
	
	Activity acv;
	
	float dp;
	int width;
	int height;
	int icon_size;
	
	public static BasicViewUI basicViewGenerator(Activity activity) {
		return new BasicViewUI(activity);
	}
	
	public BasicViewUI(Activity activity) {
		acv = activity;
		
		var displayMetrics = new DisplayMetrics();
		activity.getDisplay().getMetrics(displayMetrics);
		
		dp = displayMetrics.density;
		
		width = displayMetrics.widthPixels;
		height = displayMetrics.heightPixels;
		
		icon_size = (int) (dp * 64);
		
	}
	
	public ViewGroup basic() {
		var basic = basicNoActionView();
		basic.addView(customActionView());
		return basic;
	}
	
	public ViewGroup basicNoActionView() {
		return new LinearLayout(acv) {
			{
				setLayoutParams(new ViewGroup.LayoutParams(width, height));
				setOrientation(LinearLayout.VERTICAL);
				setBackgroundColor(Color.parseColor("#FFF2F2F2"));
			}
		};
	}
	
	public View customActionView() {
		return new RelativeLayout(acv) {
			{
				var pts = new ViewGroup.LayoutParams(width, icon_size);
				setLayoutParams(pts);
				setBackgroundColor(Color.WHITE);
				setZ(12.0f);

				var psw = new LinearLayout.LayoutParams(icon_size, icon_size);

				addView(new ImageView(acv) {
						{
							setLayoutParams(psw);
							var icon = Bitmap.createBitmap(icon_size, icon_size, Bitmap.Config.RGB_565);
							var canvas = new Canvas(icon);
							var paint = new Paint();
							paint.setColor(Color.WHITE);
							paint.setStyle(Paint.Style.FILL);
							canvas.drawRect(0f, 0f, canvas.getWidth(), canvas.getHeight(), paint);
							paint.reset();
							paint.setColor(Color.BLACK);
							paint.setStyle(Paint.Style.FILL);
							paint.setAntiAlias(true);
							paint.setFilterBitmap(true);
							int position = icon_size / 4;
							paint.setTextSize(icon_size / 2);
							canvas.save();
							canvas.scale(-1, 1, icon_size / 2, icon_size);
							canvas.drawText("➪", position, icon_size - position - (int) (6 * dp), paint);
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
										var icon = Bitmap.createBitmap(icon_size, icon_size, Bitmap.Config.RGB_565);
										var canvas = new Canvas(icon);
										var paint = new Paint();
										paint.setColor(Color.WHITE);
										paint.setStyle(Paint.Style.FILL);
										canvas.drawRect(0f, 0f, canvas.getWidth(), canvas.getHeight(), paint);
										paint.reset();
										paint.setColor(Color.BLACK);
										paint.setStyle(Paint.Style.FILL);
										paint.setAntiAlias(true);
										paint.setFilterBitmap(true);
										int position = icon_size / 4;
										paint.setTextSize(icon_size / 2);
										canvas.drawText(">_", position, icon_size - position - (int) (5.2 * dp), paint);
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
		};
	}
	
	public void registerRenderViewParams(View render) {
		render.setLayoutParams(new LinearLayout.LayoutParams(width, height - icon_size));
	}

}

