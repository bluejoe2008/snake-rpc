package cn.bluejoe.snake.proxy;

import java.io.IOException;
import java.lang.reflect.Proxy;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Serializer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ServiceObjectProxySerializer implements Serializer
{
	public void writeObject(Object obj, AbstractHessianOutput out) throws IOException
	{
		ServiceObjectProxyInvocationHandler ih = (ServiceObjectProxyInvocationHandler) Proxy.getInvocationHandler(obj);

		ServiceObjectHandle handle = ih.createServiceObjectHandle();
		out.writeObject(handle);
	}

}
