package cn.bluejoe.snake.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class InputStreamHandleFactory
{
	int _offset;

	Map<InputStream, InputStreamHandle> _streams = new HashMap<InputStream, InputStreamHandle>();

	public InputStreamHandle create(InputStream is) throws IOException
	{
		InputStreamHandle handle = new InputStreamHandle();
		handle.setOffset(_offset);
		int length = is.available();
		handle.setLength(length);

		_offset += length;
		_streams.put(is, handle);
		return handle;
	}

	public Map<InputStream, InputStreamHandle> getStreams()
	{
		return _streams;
	}

	public void writeTo(OutputStream os) throws IOException
	{
		for (Entry<InputStream, InputStreamHandle> me : _streams.entrySet())
		{
			int length = IOUtils.copy(me.getKey(), os);
			InputStreamHandle handle = me.getValue();
			if (length != handle.getLength())
			{
				throw new IOException(String.format("dismatched stream length, expected %d, but is %d",
					handle.getLength(), length));
			}
		}
	}
}
