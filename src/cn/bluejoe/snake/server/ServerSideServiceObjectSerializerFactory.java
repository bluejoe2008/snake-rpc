package cn.bluejoe.snake.server;

import java.util.ArrayList;
import java.util.List;

import cn.bluejoe.snake.mem.DefaultServiceObjectPool;
import cn.bluejoe.snake.so.ServerSideObjectSerializer;
import cn.bluejoe.snake.so.ServiceObjectHandle;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ServerSideServiceObjectSerializerFactory extends AbstractSerializerFactory
{
	private DefaultServiceObjectPool _pool;

	List<Class> _serverSideObjectClasses = new ArrayList<Class>();

	public ServerSideServiceObjectSerializerFactory(DefaultServiceObjectPool pool)
	{
		super();
		_pool = pool;
	}

	public void declareServerSideObjectClass(Class... classes)
	{
		for (Class clazz : classes)
		{
			declareServerSideObjectClass(clazz);
		}
	}

	public void declareServerSideObjectClass(Class clazz)
	{
		if (!clazz.isInterface())
		{
			throw new RuntimeException(String.format("not an interface: %s", clazz.getName()));
		}
		_serverSideObjectClasses.add(clazz);
	}

	@Override
	public Deserializer getDeserializer(Class clazz) throws HessianProtocolException
	{
		if (clazz == ServiceObjectHandle.class)
		{
			return new ServerSideObjectDeserializer(this);
		}

		return null;
	}

	public DefaultServiceObjectPool getPool()
	{
		return _pool;
	}

	private Class getRegisteredInterfaceOf(Class clazz)
	{
		for (Class ri : _serverSideObjectClasses)
		{
			if (ri.isAssignableFrom(clazz))
			{
				return ri;
			}
		}

		return null;
	}

	@Override
	public Serializer getSerializer(Class clazz) throws HessianProtocolException
	{
		Class ic = getRegisteredInterfaceOf(clazz);
		if (ic == null)
			return null;

		return new ServerSideObjectSerializer(_pool, ic);
	}

	public List<Class> getServerSideObjectClasses()
	{
		return _serverSideObjectClasses;
	}

	public void setPool(DefaultServiceObjectPool pool)
	{
		_pool = pool;
	}

	public void setServerSideObjectClasses(List<Class> serverSideObjectClasses)
	{
		_serverSideObjectClasses = serverSideObjectClasses;
	}
}
