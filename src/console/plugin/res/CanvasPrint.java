package console.plugin.res;

import java.util.LinkedHashMap;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CanvasPrint
{
	LinkedHashMap<Canvas_Attrs,Drawable> prepare = new LinkedHashMap<Canvas_Attrs,Drawable>();
	
	int columnNum;
	
	ByteArrayOutputStream cached_output = new ByteArrayOutputStream() {
		public synchronized void write(byte[] b, int off, int len) {
			int start = off;
			int end = off + len;
			while (start < end) {
				if (b[start] == 10) {
					columnNum++;
				}
				start++;
			}
			super.write(b,off,len);
		}
	};

	PrintStream print_output = new PrintStream(cached_output);
	PrintStream print_debug = new PrintStream(cached_output);
	
	//var cached_input = new ByteArrayInputStream("".getBytes());
	
	public void registerDrawable(Canvas_Attrs attrs,Drawable drawable) {
		columnNum++;
		attrs.columnNum = columnNum;
		prepare.put(attrs,drawable);
	}
	
	Column columnArray = new Column();
	
	public boolean updateDeque() {
		if (cached_output.size() > 0) {
			String[] add = cached_output.toString().split("\n");
			
			cached_output.reset();
			
			int end = columnArray.size() - 1;
			
			if (end >= 0) {
				var array = columnArray.array();
				var str = array[end];
			
				if (str.endsWith("\n")) {
					columnArray.addAll(add);
				} else {
					array[end] = str.concat(add[0]);
				
					columnArray.addAllSkipStart(add);
				}
			} else {
				columnArray.addAll(add);
			}
			
			return true;
		}
		
		return false;
	}
	
	public void updateDrawableConfiguration(Drawable drawable) {
		for (var entry : prepare.entrySet()) {
			if (entry.getValue() == drawable) {
				entry.getKey().register();
				break;
			}
		}
	}
	
	public PrintStream get_print_outupt() {
		return print_output;
	}
	
	public PrintStream get_print_debug() {
		return print_debug;
	}
	
}
