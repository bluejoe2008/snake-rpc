package cn.bluejoe.snake.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ServiceObjectPool
{
	class PoolEntry
	{
		long _expired;

		Object _object;

		private void updateExpired()
		{
			if (_expired != -1)
			{
				_expired = System.currentTimeMillis() + _maxAliveTime;
			}
		}
	}

	long _idCounter;

	private boolean _invalidateMonitorFlag = false;

	long _maxAliveTime = 3600000;

	long _monitorInterval = 2000;

	Map<String, PoolEntry> _pooledServiceObjects = new HashMap<String, PoolEntry>();

	Thread _thread;

	public ServiceObjectPool(long monitorInterval, long maxAliveTime)
	{
		_monitorInterval = monitorInterval;
		_maxAliveTime = maxAliveTime;

		if (_maxAliveTime > 0)
		{
			_thread = new Thread()
			{
				@Override
				public void run()
				{
					while (!_invalidateMonitorFlag)
					{
						try
						{
							Thread.sleep(_monitorInterval);
						}
						catch (InterruptedException e)
						{
							//e.printStackTrace();
						}

						//查找过期的对象
						List<String> expiredList = new ArrayList<String>();
						long now = System.currentTimeMillis();

						Map<String, PoolEntry> newPool = new HashMap<String, PoolEntry>();
						for (Entry<String, PoolEntry> me : _pooledServiceObjects.entrySet())
						{
							PoolEntry pe = me.getValue();

							if (pe._expired <= 0 || pe._expired > now)
							{
								newPool.put(me.getKey(), pe);
							}
							else
							{
								expiredList.add(me.getKey());
							}
						}

						if (!expiredList.isEmpty())
						{
							synchronized (this)
							{
								_pooledServiceObjects = newPool;
							}

							Logger.getLogger(ServiceObjectPool.class).debug(
								String.format("expired object removed: %s", expiredList));
						}
					}
				}
			};

			_thread.start();
		}
	}

	public boolean containsServiceObject(String serviceObjectName)
	{
		return _pooledServiceObjects.containsKey(serviceObjectName);
	}

	public void destroy()
	{
		_invalidateMonitorFlag = true;
		Logger.getLogger(this.getClass()).info(
			String.format("service object pool destroyed: %s", _pooledServiceObjects.keySet()));
		_pooledServiceObjects.clear();
	}

	private String findServiceObject(Object serviceObject)
	{
		for (Entry<String, PoolEntry> me : _pooledServiceObjects.entrySet())
		{
			if (serviceObject == me.getValue()._object)
			{
				return me.getKey();
			}
		}

		return null;
	}

	public long getMaxAliveTime()
	{
		return _maxAliveTime;
	}

	public long getMonitorInterval()
	{
		return _monitorInterval;
	}

	public Object getServiceObject(String serviceObjectName)
	{
		PoolEntry pe = _pooledServiceObjects.get(serviceObjectName);
		if (pe == null)
			return null;

		pe.updateExpired();
		return pe._object;
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

		String id = String.format("remote-object-%d", _idCounter++);
		registerServiceObject(id, serviceObject, System.currentTimeMillis() + _maxAliveTime);

		return id;
	}

	public void registerServiceObject(String objectId, Object serviceObject, long expired)
	{
		Logger.getLogger(this.getClass()).debug(String.format("registering object: %s = %s", objectId, serviceObject));
		PoolEntry pe = new PoolEntry();
		pe._object = serviceObject;
		pe._expired = expired;
		_pooledServiceObjects.put(objectId, pe);
	}

	public void setMaxAliveTime(long maxAliveTime)
	{
		_maxAliveTime = maxAliveTime;
	}

	public void setMonitorInterval(long monitorInterval)
	{
		_monitorInterval = monitorInterval;
	}
}
