package cn.bluejoe.snake.client;

import cn.bluejoe.snake.proxy.ServiceObjectHandle;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.JavaDeserializer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ClientSideServiceObjectHandleDeserializer extends JavaDeserializer implements Deserializer
{
	private ClientSideRemoteObjectSerializerFactory _serializerFactory;

	public ClientSideServiceObjectHandleDeserializer(ClientSideRemoteObjectSerializerFactory serializerFactory)
	{
		super(ServiceObjectHandle.class);
		_serializerFactory = serializerFactory;
	}

	@Override
	protected Object resolve(AbstractHessianInput in, Object obj) throws Exception
	{
		ServiceObjectHandle remoteObject = (ServiceObjectHandle) obj;
		return _serializerFactory.getClient().createServiceObjectProxy(remoteObject.getObjectId(),
			remoteObject.getApiClasses());
	}
}
