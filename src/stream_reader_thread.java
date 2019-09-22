import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class stream_reader_thread extends Thread
{
	String _result;
	InputStream _inputStream;
	
    public stream_reader_thread(InputStream inputStream)
    {
    	_inputStream = inputStream;
    }
    
    public String Result()
    {
    	return _result;
    }
    
    public void run()
    {
        try
        {
            InputStreamReader inputStreamReader = new InputStreamReader(_inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readLine;
            while ( (readLine = bufferedReader.readLine()) != null)
            {
            	if(null == _result)
            		_result = readLine + "#@#@#";
            	else
            		_result += readLine + "#@#@#";
            	
            	System.out.println(readLine);
            }
        } 
        catch (IOException e) 
        {
          System.out.println(e);
        }
    }
}
