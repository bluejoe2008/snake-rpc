package cn.bluejoe.snake.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.bluejoe.snake.message.MethodCallReply;

import com.caucho.hessian.io.SerializerFactory;

/**
 * 
 * @author bluejoe2008@gmail.com
 * 
 */
public class SnakeServlet extends HttpServlet
{
	protected SnakeServer _skeleton;

	@Override
	public void destroy()
	{
		_skeleton.destroy();
	}

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		_skeleton = new SnakeServer(new SerializerFactory());
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException
	{
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		if (!request.getMethod().equals("POST"))
		{
			response.setStatus(500, "Hessian Requires POST");
			PrintWriter out = response.getWriter();

			response.setContentType("text/html");
			out.println("<h1>Hessian Requires POST</h1>");

			return;
		}

		response.setContentType("application/x-hessian");
		InputStream is = request.getInputStream();
		OutputStream os = response.getOutputStream();
		try
		{
			_skeleton.service(is, os);
		}
		catch (Exception e)
		{
			response.reset();
			_skeleton.response(os, new MethodCallReply(e));
		}
	}
}
