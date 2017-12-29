package player;

public class CpuException extends Exception
{
	public CpuException() 
	{
		super("Exception in CPU");
	}
	
	public CpuException(String str)
	{
		super(str);
	}

}
