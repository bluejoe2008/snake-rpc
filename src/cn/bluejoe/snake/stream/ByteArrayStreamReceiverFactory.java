package cn.bluejoe.snake.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class ByteArrayStreamReceiverFactory implements StreamReceiverFactory
{
	public StreamReceiver create()
	{
		return new StreamReceiver()
		{
			ByteArrayOutputStream _baos = new ByteArrayOutputStream();

			public void destroy()
			{
				_baos.reset();
			}

			public InputStream openInputStream(int size)
			{
				return new ByteArrayInputStream(_baos.toByteArray());
			}

			public OutputStream openOutputStream(int size)
			{
				return _baos;
			}
		};
	}
}
