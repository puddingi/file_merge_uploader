import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

public class log_handler 
{
	private static String _GetCurrentDateTime()
	{
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		
        long year = calendar.get(Calendar.YEAR);
        long month = calendar.get(Calendar.MONTH)+1;
        long day = calendar.get(Calendar.DAY_OF_MONTH);
        long hour = calendar.get(Calendar.HOUR_OF_DAY);
        long minute = calendar.get(Calendar.MINUTE);
        long second = calendar.get(Calendar.SECOND);
		
        long yearMonthDay = year * 10000 + month * 100 + day;
        long hourMinuteSecond = hour * 10000 + minute * 100 + second;
        
        long nowDateTimeExceptTick = date.getTime() / 1000;
        
        if(hour < 12)
        	return "["+yearMonthDay+"_0"+hourMinuteSecond+"] - ";
        return "["+yearMonthDay+"_"+hourMinuteSecond+"] - ";
	}
	
	public static void WriteConsole(String logInfo)
	{
		System.out.println(_GetCurrentDateTime()+logInfo);
	}

	public static void WriteFile(String fileName,String logInfo)
	{
		String path = utility.CurrentExecutablePath()+"/log";
		try(PrintWriter output = new PrintWriter(new FileWriter(path+"/"+fileName,true))) 
		{
		    output.printf(logInfo+utility.NewLine());
		    output.close();
		    output.flush();
		} 
		catch (Exception e) {}
	}
	
	public static void Write(boolean writeConsole,boolean writeFile,String logInfo,String fileName)
	{
		if(true == writeConsole)
			WriteConsole(logInfo);
		if(true == writeFile)
			WriteFile(fileName,logInfo);
	}
}
