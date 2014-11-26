package cn.bluejoe.snake.so;

import java.io.IOException;

import org.apache.commons.lang.ClassUtils;

import cn.bluejoe.snake.mem.DefaultServiceObjectPool;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Serializer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ServerSideObjectSerializer implements Serializer
{
	private Class _interfaceClass;

	private DefaultServiceObjectPool _pool;

	public ServerSideObjectSerializer(DefaultServiceObjectPool pool, Class ic)
	{
		_interfaceClass = ic;
		_pool = pool;
	}

	public void writeObject(Object obj, AbstractHessianOutput out) throws IOException
	{
		ServiceObjectHandle handle = new ServiceObjectHandle();
		handle.setApiClasses((Class[]) ClassUtils.getAllInterfaces(obj.getClass()).toArray(new Class[0]));
		handle.setObjectId(_pool.registerServiceObject(obj));
		out.writeObject(handle);
	}

}
