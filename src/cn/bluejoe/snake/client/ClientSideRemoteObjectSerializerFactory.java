package cn.bluejoe.snake.client;

import cn.bluejoe.snake.proxy.ServiceObjectHandle;
import cn.bluejoe.snake.proxy.ServiceObjectProxy;
import cn.bluejoe.snake.proxy.ServiceObjectProxySerializer;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ClientSideRemoteObjectSerializerFactory extends AbstractSerializerFactory
{
	SnakeClient _client;

	public ClientSideRemoteObjectSerializerFactory(SnakeClient client)
	{
		super();
		_client = client;
	}

	public SnakeClient getClient()
	{
		return _client;
	}

	@Override
	public Deserializer getDeserializer(Class clazz) throws HessianProtocolException
	{
		if (clazz == ServiceObjectHandle.class)
		{
			return new ClientSideServiceObjectHandleDeserializer(this);
		}

		return null;
	}

	@Override
	public Serializer getSerializer(Class clazz) throws HessianProtocolException
	{
		if (ServiceObjectProxy.class.isAssignableFrom(clazz))
			return new ServiceObjectProxySerializer();

		return null;
	}

	public void setClient(SnakeClient client)
	{
		_client = client;
	}
}
