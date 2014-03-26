package cn.bluejoe.snake.stream;

import java.io.InputStream;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class InputStreamSerializerFactory extends AbstractSerializerFactory
{
	private InputStreamDelegateCollector _inputStreamDelegateCollector;

	private InputStreamHandleFactory _inputStreamHandleFactory;

	public InputStreamSerializerFactory(InputStreamHandleFactory inputStreamHandleFactory,
			InputStreamDelegateCollector inputStreamDelegateCollector)
	{
		super();
		_inputStreamHandleFactory = inputStreamHandleFactory;
		_inputStreamDelegateCollector = inputStreamDelegateCollector;
	}

	@Override
	public Deserializer getDeserializer(Class clazz) throws HessianProtocolException
	{
		if (clazz == InputStreamHandle.class)
		{
			return new InputStreamHandleDeserializer(_inputStreamDelegateCollector);
		}

		return null;
	}

	@Override
	public Serializer getSerializer(Class clazz) throws HessianProtocolException
	{
		if (InputStream.class.isAssignableFrom(clazz))
		{
			return new InputStreamHandleSerializer(_inputStreamHandleFactory);
		}

		return null;
	}
}
