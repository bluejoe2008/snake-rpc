package cn.bluejoe.snake.pool;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class DefaultServiceObjectPool implements  ServiceObjectPool
{
	long _idCounter;

	static String OBJECT_ID_FORMAT = "remote-object-%d";

	public long getIdCounter()
	{
		return _idCounter;
	}

	/**
	 * 永久对象
	 */
	Map<String, Object> _residentServiceObjects = new HashMap<String, Object>();

	/**
	 * 临时生成的对象，cache以备后用
	 */
	Map<String, Object> _cachedServiceObjects = new HashMap<String, Object>();

	public DefaultServiceObjectPool()
	{
	}

	public String[] getCachedObjectNames()
	{
		return _cachedServiceObjects.keySet().toArray(new String[0]);
	}

	public boolean containsServiceObject(String serviceObjectName)
	{
		return _residentServiceObjects.containsKey(serviceObjectName)
				|| _cachedServiceObjects.containsKey(serviceObjectName);
	}

	public void destroy()
	{
		Logger.getLogger(this.getClass()).info(
			String.format("service object pool destroyed: %s", _residentServiceObjects.keySet()));

		_residentServiceObjects.clear();
		_cachedServiceObjects.clear();
	}

	private String findServiceObject(Object serviceObject)
	{
		for (Entry<String, Object> me : _residentServiceObjects.entrySet())
		{
			if (serviceObject == me.getValue())
			{
				return me.getKey();
			}
		}

		for (Entry<String, Object> me : _cachedServiceObjects.entrySet())
		{
			if (serviceObject == me.getValue())
			{
				return me.getKey();
			}
		}

		return null;
	}

	public Object getServiceObject(String serviceObjectName)
	{
		if (_residentServiceObjects.containsKey(serviceObjectName))
		{
			return _residentServiceObjects.get(serviceObjectName);
		}

		return _cachedServiceObjects.get(serviceObjectName);
	}

	/**
	 * 注册远程服务对象，自动创建ID
	 */
	public String cacheServiceObject(Object serviceObject)
	{
		//可能会已经存在
		String found = findServiceObject(serviceObject);

		if (found != null)
			return found;

		String id = String.format(OBJECT_ID_FORMAT, _idCounter++);
		Logger.getLogger(this.getClass()).debug(String.format("caching object: %s = %s", id, serviceObject));
		_cachedServiceObjects.put(id, serviceObject);

		return id;
	}

	public void registerServiceObject(String objectId, Object serviceObject)
	{
		Logger.getLogger(this.getClass()).info(String.format("registering object: %s = %s", objectId, serviceObject));
		_residentServiceObjects.put(objectId, serviceObject);
	}

	public void removeCachedServiceObjects(String[] handles)
	{
		for (String name : handles)
		{
			_cachedServiceObjects.remove(name);
			Logger.getLogger(this.getClass()).debug(String.format("removing object: %s", name));
		}
	}

	public String[] getResidentObjectNames()
	{
		return _residentServiceObjects.keySet().toArray(new String[0]);
	}
}
