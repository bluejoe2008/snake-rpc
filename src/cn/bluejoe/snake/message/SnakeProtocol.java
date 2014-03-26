package cn.bluejoe.snake.message;

import java.io.Serializable;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class SnakeProtocol implements Serializable
{
	long _majorVersion = 1;

	long _minorVersion = 0;

	String _protocolName = "snake";

	public SnakeProtocol()
	{
		super();
	}

	public long getMajorVersion()
	{
		return _majorVersion;
	}

	public long getMinorVersion()
	{
		return _minorVersion;
	}

	public String getProtocolName()
	{
		return _protocolName;
	}

	public void setMajorVersion(long majorVersion)
	{
		_majorVersion = majorVersion;
	}

	public void setMinorVersion(long minorVersion)
	{
		_minorVersion = minorVersion;
	}

	public void setProtocolName(String protocolName)
	{
		_protocolName = protocolName;
	}

}