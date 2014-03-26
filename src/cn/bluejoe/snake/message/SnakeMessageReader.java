package cn.bluejoe.snake.message;

import java.io.IOException;
import java.io.InputStream;

import cn.bluejoe.snake.stream.InputStreamDelegateCollector;
import cn.bluejoe.snake.stream.InputStreamSerializerFactory;
import cn.bluejoe.snake.stream.StreamReceiverFactory;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.SerializerFactory;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class SnakeMessageReader
{
	public <T> T read(InputStream is, SerializerFactory serializerFactory, StreamReceiverFactory streamSourceFactory)
			throws IOException
	{
		Hessian2Input h2i = new Hessian2Input(is);
		SerializerFactory sf = new SerializerFactory();
		InputStreamDelegateCollector delegateCollector = new InputStreamDelegateCollector(streamSourceFactory);
		sf.addFactory(new InputStreamSerializerFactory(null, delegateCollector));
		sf.addFactory(serializerFactory);
		h2i.setSerializerFactory(sf);

		//读取协议
		SnakeProtocol protocol = (SnakeProtocol) h2i.readObject();
		//读取返回值
		Object value = h2i.readObject();
		//处理输入流
		delegateCollector.readFrom(is);

		return (T) value;
	}
}
