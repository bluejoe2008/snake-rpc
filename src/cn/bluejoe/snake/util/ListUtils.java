package cn.bluejoe.snake.util;

import java.util.List;

/**
 * @author bluejoe2008@gmail.com
 */

public abstract class ListUtils
{
	public static <T> void addAll(List<T> list, T[] array)
	{
		for (T t : array)
		{
			list.add(t);
		}
	}

	public static <T> void addAll(List<T> list, T[] array, Matcher<T> matcher)
	{
		for (T t : array)
		{
			if (matcher.matches(t))
			{
				list.add(t);
			}
		}
	}

	public static <T> T find(List<T> list, Matcher<T> matcher)
	{
		for (T t : list)
		{
			if (matcher.matches(t))
			{
				return t;
			}
		}

		return null;
	}

	public static <T> void find(List<T> list, Matcher<T> matcher, List<T> output)
	{
		for (T t : list)
		{
			if (matcher.matches(t))
			{
				output.add(t);
			}
		}
	}
}
