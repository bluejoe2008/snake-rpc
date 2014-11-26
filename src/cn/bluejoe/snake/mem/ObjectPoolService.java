package cn.bluejoe.snake.mem;

public interface ObjectPoolService
{
	public abstract void destoryServiceObjects(String[] handles);

	public abstract boolean containsServiceObject(String serviceObjectName);

	public abstract String[] getPooledObjectNames();

	public abstract long getIdCounter();
}