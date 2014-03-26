package cn.bluejoe.snake.message;

import java.io.IOException;
import java.io.OutputStream;

import cn.bluejoe.snake.stream.InputStreamHandleFactory;
import cn.bluejoe.snake.stream.InputStreamSerializerFactory;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class SnakeMessageWriter
{
	public void write(OutputStream os, Object value, SerializerFactory serializerFactory) throws IOException
	{
		Hessian2Output ho2 = new Hessian2Output(os);
		SerializerFactory sf = new SerializerFactory();
		InputStreamHandleFactory handleFactory = new InputStreamHandleFactory();
		sf.addFactory(new InputStreamSerializerFactory(handleFactory, null));
		sf.addFactory(serializerFactory);
		ho2.setSerializerFactory(sf);
		//写入协议信息
		ho2.writeObject(new SnakeProtocol());
		//写入主体对象
		ho2.writeObject(value);
		ho2.flush();
		//写入附加流
		handleFactory.writeTo(os);
	}
}
