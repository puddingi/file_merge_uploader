
public class mux_merge_thread implements Runnable
{
	long _sleepTickTime = 2000;
	
	int _GetWorkMessgeSize()
	{
		return work_message.QueueMuxMerge.size();
	}
	
	work_message _PeekWorkMessage()
	{
		return work_message.QueueMuxMerge.peek();
	}
	
	void _TakeWorkMessage()
	{
		try
		{
			work_message.QueueMuxMerge.take();
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
						
						case work_message.TYPE_MUX_CONVERTED_FILE:
						{
							work_message_mux_converted_file workMessageMuxConvertedFile = 
									(work_message_mux_converted_file)workMessage;
							workMessageMuxConvertedFile.Process();
						
							_TakeWorkMessage();
						}
						break;
						
						case work_message.TYPE_UPLOAD_MUXED_FILE_TO_S3:
						{
							work_message_upload_muxed_file_to_s3 workMessageUploadMuxedFileToS3 = 
									(work_message_upload_muxed_file_to_s3)workMessage;
							workMessageUploadMuxedFileToS3.Process();
						
							_TakeWorkMessage();
						}
						break;
						
						case work_message.TYPE_JANUS_ARCHIVE_CALLBACK_MUXED_FILE:
						{
							work_message_janus_archive_callback_muxed_file workMessageJanusArchiveCallback = 
									(work_message_janus_archive_callback_muxed_file)workMessage;
							workMessageJanusArchiveCallback.Process();
						
							_TakeWorkMessage();
						}
						break;
					
						case work_message.TYPE_MERGE_CONVERTED_FILE:
						{
							work_message_merge_converted_file workMessageMergeConvertedFile = 
									(work_message_merge_converted_file)workMessage;
							workMessageMergeConvertedFile.Process();
						
							_TakeWorkMessage();
						}
						break;
					
						case work_message.TYPE_UPLOAD_MERGED_FILE_TO_S3:
						{
							work_message_upload_merged_file_to_s3 workMessageUploadMergedFileToS3 = 
									(work_message_upload_merged_file_to_s3)workMessage;
							workMessageUploadMergedFileToS3.Process();
						
							_TakeWorkMessage();
						}
						break;
					
						case work_message.TYPE_JANUS_ARCHIVE_CALLBACK_MERGED_FILE:
						{
							work_message_janus_archive_callback_merged_file workMessageJanusArchiveCallback = 
									(work_message_janus_archive_callback_merged_file)workMessage;
							workMessageJanusArchiveCallback.Process();
						
							_TakeWorkMessage();
						}
						break;
						
						case work_message.TYPE_QUIT_THREAD:
						{
							loop = false;
							
							log_handler.WriteConsole("[merge_upload_call_thread::run] - message : TYPE_QUIT_THREAD");
							_TakeWorkMessage();
							
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