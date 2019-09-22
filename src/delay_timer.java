
public class delay_timer
{
    private long _startTickTime;
    private boolean _stopFlag = false;
    private int _delayTickCount = -1;
    private int _callCount = -1;

    public delay_timer()
    {
        Reset();
    }
    
    public void Reset()
    {
        _startTickTime = System.currentTimeMillis();
    }


    public long ElapsedSeconds()
    {
        long elapsedTimeMillis = System.currentTimeMillis() - _startTickTime;
        return elapsedTimeMillis / 1000;
    }

    public boolean ElapsedTickCount (int delayTickTime)
    {
    	if(0 == delayTickTime)
    	{
    		Reset();
    		return true;
    	}
    	
        long elapsedTimeMillis = System.currentTimeMillis() - _startTickTime;
        if (elapsedTimeMillis >= delayTickTime)
        {
            Reset();
            return true;
        }

        return false;
    }
}
