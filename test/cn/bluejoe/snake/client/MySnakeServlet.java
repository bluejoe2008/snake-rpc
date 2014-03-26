package cn.bluejoe.snake.client;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import cn.bluejoe.snake.server.SnakeServlet;

public class MySnakeServlet extends SnakeServlet
{

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		//declares that FileObjects are only avaliable on server side
		_skeleton.declareServerSideObjectClass(FileObject.class);
		//register an object named file
		_skeleton.registerServiceObject("file", new FileObjectImpl(new File("./testdir")));
	}

}
