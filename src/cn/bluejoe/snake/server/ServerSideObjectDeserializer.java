package cn.bluejoe.snake.server;

import cn.bluejoe.snake.pool.ServiceObjectPool;
import cn.bluejoe.snake.proxy.ServiceObjectHandle;

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
	private ServiceObjectPool _pool;

	public ServerSideObjectDeserializer(ServiceObjectPool pool)
	{
		super(ServiceObjectHandle.class);
		_pool = pool;
	}

	@Override
	protected Object resolve(AbstractHessianInput in, Object obj) throws Exception
	{
		ServiceObjectHandle remoteObject = (ServiceObjectHandle) obj;

		String objectId = remoteObject.getObjectId();
		if (!_pool.containsServiceObject(objectId))
		{
			throw new RuntimeException(String.format("'%s' is no longer a valid object", objectId));
		}

		return _pool.getServiceObject(objectId);
	}
}
