package clashsoft.playerinventoryapi.lib;

import java.util.ArrayList;

import clashsoft.cslib.reflect.CSReflection;

public class FakeArrayList extends ArrayList
{
	private static final long	serialVersionUID	= 5436247638996771146L;
	
	public int					fakeLength;
	
	public FakeArrayList(int fakeLength)
	{
		this.fakeLength = fakeLength;
	}
	
	@Override
	public int size()
	{
		String clazz = CSReflection.getCallerClassName();
		if (clazz.equals(PIConstants.MINECRAFT_CLASS))
		{
			return this.fakeLength;
		}
		return super.size();
	}
}