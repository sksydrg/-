package console.plugin.res;

public class Column
{
	String[] column;
	int size;
	
	int total;
	long last;
	
	public void addAll(String[] deque) {
		int add = deque.length;
		int len = size + add;
		ensureCapacity(len);
		System.arraycopy(deque,0,column,size,add);
		size = len;
	}
	
	public void addAllSkipStart(String[] deque) {
		int add = deque.length - 1;
		int len = size + add;
		ensureCapacity(len);
		System.arraycopy(deque,1,column,size,add);
		size = len;
	}
	
	public void add(String str) {
		ensureCapacity(size + 1);
		column[size] = str;
		size++;
	}
	
	public String[] array() {
		return column;
	}
	
	public void ensureCapacity(int len) {
		long current = System.currentTimeMillis();
		long delay = current - last;
		if (delay > 0 && delay < 2) {
			total++;
		}
		last = current;
		if (column.length < len) {
			String[] array;
			if (total > 2) {
				array = new String[len + len >> 1];
				total = 0;
			} else {
				array = new String[len + 20];
			}
			System.arraycopy(column,0,array,0,size);
			column = array;
		}
	}
	
	public int size() {
		return size;
	}
	
	public Column(int size) {
		column = new String[20];
	}
	
	public Column() {
		this(20);
	}
	
}
