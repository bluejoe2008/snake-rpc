package cn.bluejoe.snake.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.protocol.BasicHttpContext;

import cn.bluejoe.snake.message.SnakeMessageReader;
import cn.bluejoe.snake.message.SnakeMessageWriter;
import cn.bluejoe.snake.server.ServerSideRunnable;
import cn.bluejoe.snake.so.ServiceObjectProxyFactory;
import cn.bluejoe.snake.stream.ByteArrayStreamReceiverFactory;
import cn.bluejoe.snake.stream.StreamReceiverFactory;

import com.caucho.hessian.io.SerializerFactory;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class SnakeClient
{
	HttpClient _httpClient;

	SerializerFactory _serializerFactory;

	String _serviceUrl;

	StreamReceiverFactory _streamSourceFactory = new ByteArrayStreamReceiverFactory();

	public SnakeClient(HttpClient httpClient, String serviceUrl)
	{
		_httpClient = httpClient;
		_serviceUrl = serviceUrl;
		_serializerFactory = new SerializerFactory();
		_serializerFactory.addFactory(new ClientSideRemoteObjectSerializerFactory(this));
	}

	public SnakeClient(HttpHost targetHost, String serviceUrl, String user, String password)
	{
		this(null, serviceUrl);
		_httpClient = createHttpClient(targetHost, user, password);
	}

	public SnakeClient(String serviceUrl)
	{
		this(null, serviceUrl);
		_httpClient = createHttpClient();
	}

	protected DefaultHttpClient createHttpClient()
	{
		return new DefaultHttpClient(new SingleClientConnManager());
	}

	protected HttpClient createHttpClient(HttpHost targetHost, String user, String password)
	{
		final DefaultHttpClient httpClient = createHttpClient();

		httpClient.getCredentialsProvider().setCredentials(
			new AuthScope(targetHost.getHostName(), targetHost.getPort()),
			new UsernamePasswordCredentials(user, password));

		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);
		// Add AuthCache to the execution context
		final BasicHttpContext clientContext = new BasicHttpContext();
		clientContext.setAttribute(ClientContext.AUTH_CACHE, authCache);

		return (HttpClient) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { HttpClient.class },
			new InvocationHandler()
			{
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
				{
					Class<?>[] parameterTypes = method.getParameterTypes();
					if ("execute".equals(method.getName()) && parameterTypes.length == 1
							&& parameterTypes[0] == HttpUriRequest.class)
					{
						return httpClient.execute((HttpUriRequest) args[0], clientContext);
					}

					return method.invoke(httpClient, args);
				}
			});
	}

	public Object createServiceObjectProxy(String serviceObjectName, Class... apiClasses)
	{
		return new ServiceObjectProxyFactory().create(this, serviceObjectName, apiClasses);
	}

	public StreamReceiverFactory getStreamSourceFactory()
	{
		return _streamSourceFactory;
	}

	public Object invoke(final ServerSideRunnable requestCommand) throws Exception
	{
		HttpPost httpPost = new HttpPost(_serviceUrl);
		try
		{
			EntityTemplate entity = new EntityTemplate(new ContentProducer()
			{
				public void writeTo(OutputStream os) throws IOException
				{
					new SnakeMessageWriter().write(os, requestCommand, _serializerFactory);
				}
			});

			entity.setChunked(true);
			httpPost.setEntity(entity);
			HttpResponse response = _httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null)
			{
				if (response.getStatusLine().getStatusCode() != 200)
				{
					throw new IOException(response.getStatusLine().getReasonPhrase());
				}

				InputStream is = responseEntity.getContent();
				ClientSideRunnable responseCommand = new SnakeMessageReader().read(is, _serializerFactory,
					_streamSourceFactory);
				return responseCommand.execute(this);
			}

			throw new IOException();
		}
		finally
		{
			if (httpPost != null)
			{
				httpPost.abort();
			}
		}
	}

	public void setStreamSourceFactory(StreamReceiverFactory streamSourceFactory)
	{
		_streamSourceFactory = streamSourceFactory;
	}
}
