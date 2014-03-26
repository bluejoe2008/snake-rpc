package cn.bluejoe.snake.message;

import java.io.Serializable;

import cn.bluejoe.snake.client.ClientSideRunnable;
import cn.bluejoe.snake.client.SnakeClient;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class MethodCallReply implements ClientSideRunnable, Serializable
{
	Exception _exception;

	Object _value;

	public MethodCallReply()
	{
	}

	public MethodCallReply(Exception exception)
	{
		super();
		_exception = exception;
	}

	public MethodCallReply(Object value)
	{
		super();
		_value = value;
	}

	public Object execute(SnakeClient snakeClient) throws Exception
	{
		if (_exception != null)
			throw _exception;

		return _value;
	}

	public Exception getException()
	{
		return _exception;
	}

	public Object getValue()
	{
		return _value;
	}

	public void setException(Exception exception)
	{
		_exception = exception;
	}

	public void setValue(Object value)
	{
		_value = value;
	}
}
