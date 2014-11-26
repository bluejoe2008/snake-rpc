package cn.bluejoe.snake.pool;

public interface ServiceObjectPool
{
	public String cacheServiceObject(Object serviceObject);

	public Object getServiceObject(String objectId);
	public  void removeCachedServiceObjects(String[] handles);

	public  boolean containsServiceObject(String serviceObjectName);

	public  String[] getCachedObjectNames();

	public  String[] getResidentObjectNames();
}
