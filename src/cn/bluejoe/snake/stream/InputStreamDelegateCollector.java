package cn.bluejoe.snake.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class InputStreamDelegateCollector
{
	private static final int BYTES_LENGTH = 1024 * 8;

	List<InputStreamDelegate> _delegates = new ArrayList<InputStreamDelegate>();

	StreamReceiverFactory _streamSourceFactory;

	public InputStreamDelegateCollector(StreamReceiverFactory streamSourceFactory)
	{
		super();
		_streamSourceFactory = streamSourceFactory;
	}

	public void collect(InputStreamDelegate inputStreamDelegate)
	{
		_delegates.add(inputStreamDelegate);
	}

	private void readAs(InputStream is, InputStreamDelegate delegate) throws IOException
	{
		StreamReceiver ss = _streamSourceFactory.create();
		int length = delegate.getHandle().getLength();
		OutputStream os = ss.openOutputStream(length);
		while (true)
		{
			byte[] bytes = length > BYTES_LENGTH ? new byte[BYTES_LENGTH] : new byte[length];
			int consumed = is.read(bytes);
			//已经到了流结尾
			if (consumed == -1)
				break;
			os.write(bytes, 0, consumed);
			//是否读完？
			length -= consumed;
			if (length <= 0)
				break;
		}

		os.close();
		InputStream nis = ss.openInputStream(length);
		Logger.getLogger(this.getClass()).debug(String.format("delegating %s as %s", is, nis));
		delegate.setSource(nis);
	}

	public void readFrom(InputStream is) throws IOException
	{
		for (InputStreamDelegate delegate : _delegates)
		{
			readAs(is, delegate);
		}
	}

	public void reset()
	{
		_delegates.clear();
	}
}
