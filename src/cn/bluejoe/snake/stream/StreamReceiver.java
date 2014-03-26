package cn.bluejoe.snake.stream;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public interface StreamReceiver
{
	void destroy();

	InputStream openInputStream(int size);

	OutputStream openOutputStream(int size);
}
