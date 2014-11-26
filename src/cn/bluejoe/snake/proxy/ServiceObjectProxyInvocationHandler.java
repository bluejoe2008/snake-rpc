package cn.bluejoe.snake.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cn.bluejoe.snake.client.SnakeClient;
import cn.bluejoe.snake.message.MethodCall;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ServiceObjectProxyInvocationHandler implements InvocationHandler
{
	private Class[] _apiClasses;

	private SnakeClient _client;

	private String _serviceObjectId;

	public ServiceObjectProxyInvocationHandler(SnakeClient client, String serviceObjectId, Class[] apiClasses)
	{
		super();
		_client = client;
		_serviceObjectId = serviceObjectId;
		_apiClasses = apiClasses;
	}

	public ServiceObjectHandle createServiceObjectHandle()
	{
		ServiceObjectHandle handle = new ServiceObjectHandle();
		handle.setApiClasses(_apiClasses);
		handle.setObjectId(_serviceObjectId);
		return handle;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		String methodName = method.getName();

		if (methodName.equals("hashCode") && (args == null || args.length == 0))
			return new Integer(_serviceObjectId.hashCode());

		if (methodName.equals("toString") && (args == null || args.length == 0))
			return "ServiceObjectProxy[" + _serviceObjectId + "]";

		if (methodName.equals("equals") && args.length == 1)
		{
			Object other = args[0];
			if (!(Proxy.isProxyClass(other.getClass())))
			{
				return false;
			}

			InvocationHandler ih = Proxy.getInvocationHandler(other);
			if (!(ih instanceof ServiceObjectProxyInvocationHandler))
			{
				return false;
			}

			ServiceObjectProxyInvocationHandler oih = (ServiceObjectProxyInvocationHandler) ih;
			return this._client == oih._client && this._serviceObjectId.equals(oih._serviceObjectId);
		}

		MethodCall methodCall = new MethodCall();
		methodCall.setServiceObjectName(_serviceObjectId);
		methodCall.setMethodName(method.getName());
		methodCall.setParameterTypes(method.getParameterTypes());
		methodCall.setArgs(args);

		return _client.invoke(methodCall);
	}
}
