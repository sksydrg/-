package console.plugin.res;

import android.app.Activity;
import android.content.ClipboardManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public abstract class AplProvider
{
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
