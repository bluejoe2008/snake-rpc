package cn.bluejoe.snake.stream;

import java.io.Serializable;

import com.caucho.hessian.io.HessianHandle;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class InputStreamHandle implements Serializable, HessianHandle
{
	int _length = -1;

	int _offset = 0;

	public int getLength()
	{
		return _length;
	}

	public int getOffset()
	{
		return _offset;
	}

	public void setLength(int length)
	{
		_length = length;
	}

	public void setOffset(int offset)
	{
		_offset = offset;
	}
}
