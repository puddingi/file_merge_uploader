
public class convert_thread implements Runnable
{
	long _sleepTickTime = 2000L;
	
	int _GetWorkMessgeSize()
	{
		return work_message.QueueConvert.size();
	}
	
	work_message _PeekWorkMessage()
	{
		return work_message.QueueConvert.peek();
	}
	
	void _TakeWorkMessage()
	{
		try
		{
			work_message.QueueConvert.take();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void run()
	{
		try
		{
			boolean loop = true;
			while(true == loop)
			{
				if(_GetWorkMessgeSize() > 0)
				{
					work_message workMessage = _PeekWorkMessage();
					switch(workMessage.Type)
					{
						case work_message.TYPE_CONVERT_MJR_FILE:
						{
							work_message_convert_mjr_file workMessageConvertMjrFile = 
										(work_message_convert_mjr_file)workMessage;
							workMessageConvertMjrFile.Process();
							
							_TakeWorkMessage();
						}
						break;
							
						case work_message.TYPE_UPLOAD_CONVERTED_FILE_TO_S3:
						{
							work_message_upload_converted_file_to_s3 workMessageUploadConvertedFileToS3 = 
										(work_message_upload_converted_file_to_s3)workMessage;
							workMessageUploadConvertedFileToS3.Process();
							
							_TakeWorkMessage();
						}
						break;
						
						case work_message.TYPE_JANUS_ARCHIVE_CALLBACK_CONVERTED_FILE:
						{
							work_message_janus_archive_callback_converted_file workMessageJanusArchiveCallbackConvertedFile = 
										(work_message_janus_archive_callback_converted_file)workMessage;
							workMessageJanusArchiveCallbackConvertedFile.Process();
							
							_TakeWorkMessage();
						}
						break;
							
						case work_message.TYPE_QUIT_THREAD:
						{
							loop = false;
								
							log_handler.WriteConsole("[convert_thread::run] - message : TYPE_QUIT_THREAD");
								
							_TakeWorkMessage();
								
							//
							work_message_quit_thread workMessageQuitThread = 
			        					new work_message_quit_thread();
			        			
			        		try 
			        		{
			        			work_message.QueueMuxMerge.put(workMessageQuitThread);
			        		} 
			        		catch (InterruptedException e) 
			        		{
			        			// TODO Auto-generated catch block
			        			e.printStackTrace();
			        		}
						}
						break;
							
					}
					
				}
				
				Thread.sleep(_sleepTickTime);
			}
			
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}
}