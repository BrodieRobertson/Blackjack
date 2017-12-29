package player;

public class HumanException extends Exception
{
	public HumanException() 
	{
		super("Exception in Human");
	}
	
	public HumanException(String str)
	{
		super(str);
	}
}
