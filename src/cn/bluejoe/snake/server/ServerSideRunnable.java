package cn.bluejoe.snake.server;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public interface ServerSideRunnable
{
	Object execute(SnakeServer server) throws Exception;
}
