package cn.bluejoe.snake.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class InputStreamDelegate extends InputStream
{
	InputStreamHandle _handle;

	InputStream _source;

	public InputStreamDelegate(InputStreamHandle handle)
	{
		super();
		_handle = handle;
	}

	public int available() throws IOException
	{
		return _source.available();
	}

	public void close() throws IOException
	{
		_source.close();
	}

	public InputStreamHandle getHandle()
	{
		return _handle;
	}

	public InputStream getSource()
	{
		return _source;
	}

	public void mark(int readlimit)
	{
		_source.mark(readlimit);
	}

	public boolean markSupported()
	{
		return _source.markSupported();
	}

	public int read() throws IOException
	{
		return _source.read();
	}

	public int read(byte[] b) throws IOException
	{
		return _source.read(b);
	}

	public int read(byte[] b, int off, int len) throws IOException
	{
		return _source.read(b, off, len);
	}

	public void reset() throws IOException
	{
		_source.reset();
	}

	public void setHandle(InputStreamHandle handle)
	{
		_handle = handle;
	}

	public void setSource(InputStream source)
	{
		_source = source;
	}

	public long skip(long n) throws IOException
	{
		return _source.skip(n);
	}

}
