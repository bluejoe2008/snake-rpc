package cn.bluejoe.snake.mem;

public interface ObjectPoolService
{
	public abstract void removeCachedServiceObjects(String[] handles);

	public abstract boolean containsServiceObject(String serviceObjectName);

	public abstract String[] getCachedObjectNames();

	public abstract long getIdCounter();

	public abstract String[] getResidentObjectNames();
}