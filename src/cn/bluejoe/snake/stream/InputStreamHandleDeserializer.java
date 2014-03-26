package cn.bluejoe.snake.stream;

import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.JavaDeserializer;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class InputStreamHandleDeserializer extends JavaDeserializer implements Deserializer
{
	InputStreamDelegateCollector _inputStreamDelegateCollector;

	public InputStreamHandleDeserializer(InputStreamDelegateCollector inputStreamDelegateCollector)
	{
		super(InputStreamHandle.class);
		_inputStreamDelegateCollector = inputStreamDelegateCollector;
	}

	@Override
	protected Object resolve(AbstractHessianInput in, Object obj) throws Exception
	{
		InputStreamHandle isHandle = (InputStreamHandle) obj;
		InputStreamDelegate inputStreamDelegate = new InputStreamDelegate(isHandle);
		_inputStreamDelegateCollector.collect(inputStreamDelegate);
		return inputStreamDelegate;
	}
}
