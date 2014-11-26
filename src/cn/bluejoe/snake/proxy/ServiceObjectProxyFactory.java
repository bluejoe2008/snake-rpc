package cn.bluejoe.snake.proxy;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import cn.bluejoe.snake.client.SnakeClient;
import cn.bluejoe.snake.util.ListUtils;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ServiceObjectProxyFactory
{
	public Object create(SnakeClient client, String serviceObjectName, Class... apiClasses)
	{
		List<Class> list = new ArrayList<Class>();
		ListUtils.addAll(list, apiClasses);
		list.add(ServiceObjectProxy.class);
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), list.toArray(new Class[0]),
			new ServiceObjectProxyInvocationHandler(client, serviceObjectName, apiClasses));
	}
}
