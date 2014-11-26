package cn.bluejoe.snake.message;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import cn.bluejoe.snake.server.ServerSideRunnable;
import cn.bluejoe.snake.server.SnakeServer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class MethodCall implements ServerSideRunnable, Serializable
{
	Object[] _args;

	String _methodName;

	Class[] _parameterTypes;

	String _serviceObjectName;

	public Object execute(SnakeServer multiSkeleton) throws Exception
	{
		Object serviceObject = multiSkeleton.getServiceObject(_serviceObjectName);

		if (serviceObject == null)
		{
			String errMsg = "Unknown Service Object Id: " + _serviceObjectName;
			throw new Exception(errMsg);
		}
		try
		{
			Method method = serviceObject.getClass().getMethod(_methodName, _parameterTypes);
			method.setAccessible(true);
			Logger.getLogger(this.getClass()).debug(String.format("invoking method: %s", method));
			Object returnValue = method.invoke(serviceObject, _args);
			return returnValue;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	public Object[] getArgs()
	{
		return _args;
	}

	public String getMethodName()
	{
		return _methodName;
	}

	public Class[] getParameterTypes()
	{
		return _parameterTypes;
	}

	public String getServiceObjectName()
	{
		return _serviceObjectName;
	}

	public void setArgs(Object[] args)
	{
		_args = args;
	}

	public void setMethodName(String methodName)
	{
		_methodName = methodName;
	}

	public void setParameterTypes(Class[] parameterTypes)
	{
		_parameterTypes = parameterTypes;
	}

	public void setServiceObjectName(String serviceObjectName)
	{
		_serviceObjectName = serviceObjectName;
	}
}
