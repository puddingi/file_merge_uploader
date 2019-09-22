
public class work_message_mux_converted_file  extends work_message
{
	work_unit _workUnit;
	
	public work_message_mux_converted_file()
	{
		Type = work_message.TYPE_MUX_CONVERTED_FILE;
	}
	
	public void Build(work_unit workUnit)
	{
		_workUnit = workUnit;	
	}
	
	public void Process()
	{
		ffmpeg_handler.MuxConvertedFile(_workUnit);
		
		
		work_message_set_muxed_file_attribute message = 
				new work_message_set_muxed_file_attribute();
		message.Build(_workUnit);
		
		try 
		{
			work_message.QueueCheck.put(message);
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}