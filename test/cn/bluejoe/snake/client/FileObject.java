package cn.bluejoe.snake.client;

import java.io.IOException;
import java.io.InputStream;

public interface FileObject
{
	public long length();

	public InputStream open() throws IOException;

	public boolean isDirectory();

	public FileObject[] listFiles();

	public boolean isContentEquals(InputStream is) throws IOException;

	public boolean isContentEquals(InputStream is1, InputStream is2) throws IOException;
	
	public InputStream md5(InputStream is) throws IOException;
}