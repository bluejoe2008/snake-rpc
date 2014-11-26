package cn.bluejoe.snake.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.bluejoe.snake.message.MethodCallReply;
import cn.bluejoe.snake.message.SnakeMessageReader;
import cn.bluejoe.snake.message.SnakeMessageWriter;
import cn.bluejoe.snake.pool.DefaultServiceObjectPool;
import cn.bluejoe.snake.pool.ServiceObjectPool;
import cn.bluejoe.snake.stream.ByteArrayStreamReceiverFactory;
import cn.bluejoe.snake.stream.StreamReceiverFactory;

import com.caucho.hessian.io.SerializerFactory;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class SnakeServer
{
	private SerializerFactory _serializerFactory;

	ServerSideServiceObjectSerializerFactory _serverSideSerializerFactory;

	DefaultServiceObjectPool _serviceObjectPool;

	public static String SERVICE_OBJECT_POOL = ServiceObjectPool.class.getName();

	StreamReceiverFactory _streamSourceFactory;

	public SnakeServer(SerializerFactory serializerFactory)
	{
		_serviceObjectPool = new DefaultServiceObjectPool();
		_serverSideSerializerFactory = new ServerSideServiceObjectSerializerFactory(_serviceObjectPool);
		serializerFactory.addFactory(_serverSideSerializerFactory);
		_serializerFactory = serializerFactory;
		_streamSourceFactory = new ByteArrayStreamReceiverFactory();

		this.registerServiceObject(SERVICE_OBJECT_POOL, _serviceObjectPool);
	}

	public void declareServerSideObjectClass(Class... classes)
	{
		_serverSideSerializerFactory.declareServerSideObjectClass(classes);
	}

	public void destroy()
	{
		_serviceObjectPool.destroy();
	}

	public Object getServiceObject(String serviceObjectName)
	{
		return _serviceObjectPool.getServiceObject(serviceObjectName);
	}

	public StreamReceiverFactory getStreamSourceFactory()
	{
		return _streamSourceFactory;
	}

	public String registerServiceObject(Object serviceObject)
	{
		return _serviceObjectPool.cacheServiceObject(serviceObject);
	}

	public void registerServiceObject(String objectId, Object serviceObject)
	{
		_serviceObjectPool.registerServiceObject(objectId, serviceObject);
	}

	public void response(OutputStream os, MethodCallReply responseCommand) throws IOException
	{
		new SnakeMessageWriter().write(os, responseCommand, _serializerFactory);
	}

	public void service(InputStream is, OutputStream os) throws Exception
	{
		ServerSideRunnable requestCommand = new SnakeMessageReader().read(is, _serializerFactory, _streamSourceFactory);
		Exception executionException = null;
		Object value = null;
		try
		{
			value = requestCommand.execute(this);
		}
		catch (Exception e)
		{
			executionException = e;
		}

		MethodCallReply responseCommand = new MethodCallReply();
		responseCommand.setValue(value);
		responseCommand.setException(executionException);
		//写的时候还有可能有异常
		response(os, responseCommand);
	}

	public void setStreamSourceFactory(StreamReceiverFactory streamSourceFactory)
	{
		_streamSourceFactory = streamSourceFactory;
	}
}
