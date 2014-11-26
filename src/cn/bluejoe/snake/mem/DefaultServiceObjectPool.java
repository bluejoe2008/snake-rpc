package cn.bluejoe.snake.mem;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class DefaultServiceObjectPool implements ObjectPoolService
{
	long _idCounter;

	static String OBJECT_ID_FORMAT = "remote-object-%d";

	public long getIdCounter()
	{
		return _idCounter;
	}

	Map<String, Object> _pooledServiceObjects = new HashMap<String, Object>();

	public DefaultServiceObjectPool()
	{
	}

	public String[] getPooledObjectNames()
	{
		return _pooledServiceObjects.keySet().toArray(new String[0]);
	}

	public boolean containsServiceObject(String serviceObjectName)
	{
		return _pooledServiceObjects.containsKey(serviceObjectName);
	}

	public void destroy()
	{
		Logger.getLogger(this.getClass()).info(
			String.format("service object pool destroyed: %s", _pooledServiceObjects.keySet()));
		_pooledServiceObjects.clear();
	}

	private String findServiceObject(Object serviceObject)
	{
		for (Entry<String, Object> me : _pooledServiceObjects.entrySet())
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
		return _pooledServiceObjects.get(serviceObjectName);
	}

	/**
	 * 注册远程服务对象，自动创建ID
	 */
	public String registerServiceObject(Object serviceObject)
	{
		//可能会已经存在
		String found = findServiceObject(serviceObject);

		if (found != null)
			return found;

		String id = String.format(OBJECT_ID_FORMAT, _idCounter++);
		registerServiceObject(id, serviceObject);

		return id;
	}

	public void registerServiceObject(String objectId, Object serviceObject)
	{
		Logger.getLogger(this.getClass()).debug(String.format("registering object: %s = %s", objectId, serviceObject));
		_pooledServiceObjects.put(objectId, serviceObject);
	}

	public void destoryServiceObjects(String[] handles)
	{
		for (String name : handles)
		{
			_pooledServiceObjects.remove(name);
			Logger.getLogger(this.getClass()).debug(String.format("removing object: %s", name));
		}
	}
}
