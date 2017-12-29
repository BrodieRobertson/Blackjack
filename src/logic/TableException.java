package logic;

public class TableException extends Exception 
{

	public TableException() 
	{
		super("Exception in Table");
	}

	public TableException(String str) 
	{
		super(str);
	}
}
