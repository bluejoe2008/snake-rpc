package cn.bluejoe.snake.stream;

import java.io.IOException;
import java.io.InputStream;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Serializer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class InputStreamHandleSerializer implements Serializer
{
	InputStreamHandleFactory _inputStreamHandleFactory;

	public InputStreamHandleSerializer(InputStreamHandleFactory inputStreamHandleFactory)
	{
		super();
		_inputStreamHandleFactory = inputStreamHandleFactory;
	}

	public void writeObject(Object obj, AbstractHessianOutput out) throws IOException
	{
		InputStreamHandle isHandle = _inputStreamHandleFactory.create((InputStream) obj);
		out.writeObject(isHandle);
	}

}
