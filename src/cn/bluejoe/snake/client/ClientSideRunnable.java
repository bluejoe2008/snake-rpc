package cn.bluejoe.snake.client;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public interface ClientSideRunnable
{
	Object execute(SnakeClient snakeClient) throws Exception;
}
