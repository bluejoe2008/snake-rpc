package cn.bluejoe.snake.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

import cn.bluejoe.snake.mem.ObjectPoolService;

public class SnakeClientTest
{
	SnakeClient _client;

	@Before
	public void setUp() throws Exception
	{
		new Thread()
		{
			public void run()
			{
				Server server = new Server(8080);
				Context root = new Context(server, "/", Context.SESSIONS);
				root.addServlet(new ServletHolder(new MySnakeServlet()), "/rpc");
				try
				{
					server.start();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();

		Thread.sleep(1000);
		HttpHost hc = new HttpHost("http://localhost:8080");
		_client = new SnakeClient(hc, "http://localhost:8080/rpc", "", "");
		_client.setCheckServiceObjectsInterval(100);
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test() throws FileNotFoundException, IOException, InterruptedException
	{
		test1();
		ObjectPoolService pool = _client.getServerSideServiceObjectPool();
		//等待服务器端清除过期对象
		Thread.sleep(600);
		//临时的对象应该被删除
		//主动注册的对象应该一直存在
		Assert.assertEquals(0, pool.getCachedObjectNames().length);
		Assert.assertEquals(2, pool.getResidentObjectNames().length);
	}

	protected void test1() throws IOException, FileNotFoundException
	{
		File dir = new File("./testdir");
		File f1 = new File("./testdir/1.gif");

		FileObject fo = (FileObject) _client.createServiceObjectProxy("file", FileObject.class);
		Assert.assertEquals(true, fo.isDirectory());
		//get objects derived from returned proxy is ok
		FileObject[] cfs = fo.listFiles();
		Assert.assertEquals(1, cfs.length);
		FileObject cfs0 = cfs[0];
		Assert.assertEquals(false, cfs0.isDirectory());
		Assert.assertEquals(f1.length(), cfs0.length());
		//inputstream as output is ok
		Assert.assertTrue(IOUtils.contentEquals(new FileInputStream(f1), cfs0.open()));
		//inputstream as input is ok
		Assert.assertTrue(cfs0.isContentEquals(new FileInputStream(f1)));
		Assert.assertFalse(cfs0.isContentEquals(this.getClass().getResourceAsStream("/log4j.properties")));
		//multiple inputstreams as input is ok, method overriding is ok
		Assert.assertTrue(cfs0.isContentEquals(new FileInputStream(f1), new FileInputStream(f1)));
		Assert.assertFalse(cfs0.isContentEquals(new FileInputStream(f1),
			this.getClass().getResourceAsStream("/log4j.properties")));

		//inputstreams as input and output is ok
		Assert.assertTrue(IOUtils.contentEquals(new ByteArrayInputStream(DigestUtils.md5(new FileInputStream(f1))),
			cfs0.md5(new FileInputStream(f1))));

		ObjectPoolService pool = _client.getServerSideServiceObjectPool();
		Assert.assertEquals(2, pool.getResidentObjectNames().length);
		Assert.assertEquals(1, pool.getCachedObjectNames().length);
	}
}
