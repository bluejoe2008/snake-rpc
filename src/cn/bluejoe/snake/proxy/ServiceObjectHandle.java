package cn.bluejoe.snake.proxy;

import java.io.Serializable;

import com.caucho.hessian.io.HessianHandle;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ServiceObjectHandle implements Serializable, HessianHandle
{
	private Class[] _apiClasses;

	String _objectId;

	public Class[] getApiClasses()
	{
		return _apiClasses;
	}

	public String getObjectId()
	{
		return _objectId;
	}

	public void setApiClasses(Class[] apiClasses)
	{
		_apiClasses = apiClasses;
	}

	public void setObjectId(String objectId)
	{
		_objectId = objectId;
	}
}
