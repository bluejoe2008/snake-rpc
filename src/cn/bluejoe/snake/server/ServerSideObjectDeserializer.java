package cn.bluejoe.snake.server;

import cn.bluejoe.snake.mem.DefaultServiceObjectPool;
import cn.bluejoe.snake.so.ServiceObjectHandle;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.JavaDeserializer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ServerSideObjectDeserializer extends JavaDeserializer implements Deserializer
{
	private ServerSideServiceObjectSerializerFactory _serializerFactory;

	public ServerSideObjectDeserializer(ServerSideServiceObjectSerializerFactory serializerFactory)
	{
		super(ServiceObjectHandle.class);
		_serializerFactory = serializerFactory;
	}

	@Override
	protected Object resolve(AbstractHessianInput in, Object obj) throws Exception
	{
		ServiceObjectHandle remoteObject = (ServiceObjectHandle) obj;
		DefaultServiceObjectPool pool = _serializerFactory.getPool();
		String objectId = remoteObject.getObjectId();
		if (!pool.containsServiceObject(objectId))
		{
			throw new RuntimeException(String.format("'%s' is no longer a valid object", objectId));
		}

		return pool.getServiceObject(objectId);
	}
}
