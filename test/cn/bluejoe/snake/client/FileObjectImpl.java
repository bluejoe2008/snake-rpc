package cn.bluejoe.snake.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

public class FileObjectImpl implements FileObject
{
	private File _file;

	public FileObjectImpl(File file)
	{
		_file = file;
	}

	public long length()
	{
		return _file.length();
	}

	public InputStream open() throws IOException
	{
		return new FileInputStream(_file);
	}

	public boolean isDirectory()
	{
		return _file.isDirectory();
	}

	public FileObject[] listFiles()
	{
		File[] files = _file.listFiles();
		FileObject[] fos = new FileObject[files.length];
		int i = 0;
		for (File file : files)
		{
			fos[i++] = new FileObjectImpl(file);
		}

		return fos;
	}

	public boolean isContentEquals(InputStream is) throws IOException
	{
		InputStream mis = open();
		boolean equals = IOUtils.contentEquals(mis, is);
		mis.close();
		return equals;
	}

	public boolean isContentEquals(InputStream is1, InputStream is2) throws IOException
	{
		return IOUtils.contentEquals(is1, is2);
	}

	public InputStream md5(InputStream is) throws IOException
	{
		return new ByteArrayInputStream(DigestUtils.md5(is));
	}

}
