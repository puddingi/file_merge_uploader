import java.io.IOException;
import java.util.ArrayList;


// ffmpeg static builds
// https://johnvansickle.com/ffmpeg/

public class ffmpeg_handler 
{
	public static int ThreadCount = 2;
	
	public static int MergeResolutionWidth;
	public static int MergeResolutionHeight;
	
	//1179 x 911
    public static int ResizeEmptyImage(String originBackgroundImageFileName,
    		int resizedResolutionWidth,int resizedResolutionHeight,String resizedBackgroundImageFileName)
    {
        //ffmpeg -i background_origin.png -vf scale=320:240 background.png
        int resultCode = 0;
        
    	Runtime runTime = Runtime.getRuntime();
        Process process = null;
       
        String commandLine = utility.CurrentExecutablePath() + "/ffmpeg";
        commandLine += " ";
        commandLine += "-i";
        commandLine += " ";
        commandLine += originBackgroundImageFileName;
        commandLine += " -vf scale=" + resizedResolutionWidth + ":" + resizedResolutionHeight;
        commandLine += " ";
        commandLine += resizedBackgroundImageFileName;
        commandLine += " -y";
        
        //process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	if(null != process)
	    	{
	    		stream_reader_thread streamReaderThread1 = new stream_reader_thread(process.getInputStream());
	    		stream_reader_thread streamReaderThread2 = new stream_reader_thread(process.getErrorStream());
	    		streamReaderThread1.start();
	    		streamReaderThread2.start();
	    	   
	    		while (true)
	    		{
	    			if (!streamReaderThread1.isAlive() && !streamReaderThread2.isAlive()) 
	    			{  
	    				//System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    				//System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
	    				process.waitFor();
	    				resultCode = 1;
	    				break;
	    			}
	    		}
	    	}
	    } 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }
	  
		return resultCode;
    }
    
    public static int ConvertVideoFile(work_unit workUnit,String convertedVideoFileFullName,String convertVideoFileExtend)
    {
    	int resultCode = 0;
    	if(convertedVideoFileFullName.contains(convertVideoFileExtend))
				return resultCode;
    	
    	String commandLineArgument = "";
    	
 		if(false == utility.IsNullOrEmpty(convertedVideoFileFullName))
     	{
 			commandLineArgument += "-i";
 			commandLineArgument += " ";
 			commandLineArgument += preset.GetRecordFolderPath()+convertedVideoFileFullName;
 			commandLineArgument += " ";
     	
 			switch(convertVideoFileExtend)
 			{
 				case "mp4":
 				{
 					commandLineArgument += "-c:v libx264";
 				}
 				break;
 			}
 			
 			commandLineArgument += " ";
     	}
 		
 		commandLineArgument += preset.GetRecordFolderPath() + workUnit.GetFileTitleName()+"."+convertVideoFileExtend;
 		commandLineArgument += " ";
 		commandLineArgument += "-y";
 		commandLineArgument += " ";
 		commandLineArgument += "-threads "+ThreadCount;
 		
		//
		Runtime runTime = Runtime.getRuntime();
		Process process = null;
		String commandLine = utility.CurrentExecutablePath() + "/ffmpeg";
		commandLine += " ";
		commandLine += commandLineArgument;
		
		//process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	if(null != process)
	    	{
	    		stream_reader_thread streamReaderThread1 = new stream_reader_thread(process.getInputStream());
	    		stream_reader_thread streamReaderThread2 = new stream_reader_thread(process.getErrorStream());
	    		streamReaderThread1.start();
	    		streamReaderThread2.start();
	    	   
	    		while (true)
	    		{
	    			if (!streamReaderThread1.isAlive() && !streamReaderThread2.isAlive()) 
	    			{  
	    				System.out.println("[ffmpeg_handler::ConvertVideoFile] - Finished : "+streamReaderThread1.getState()+" , "+streamReaderThread2.getState());
	    				//System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    				//System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
	    				process.waitFor();
	    				break;
	    			}
	    		}
	    	}
	    } 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }
	  
		return resultCode;
    }
    
    
    public static int ConvertAudioFile(work_unit workUnit,String convertedAudioFileFullName,String convertAudioFileExtend)
    {
    	int resultCode = 0;
    	String commandLineArgument = "";
    	
 		if(false == utility.IsNullOrEmpty(convertedAudioFileFullName))
     	{
 			commandLineArgument += "-i";
 			commandLineArgument += " ";
 			commandLineArgument += preset.GetRecordFolderPath()+convertedAudioFileFullName;
 			commandLineArgument += " ";
     	
 			switch(convertAudioFileExtend)
 			{
 				case "m4a":
 				{
 					commandLineArgument += "-c:a aac";
 				}
 				break;
 			}
 			
 			commandLineArgument += " ";
     	}
 		
 		commandLineArgument += preset.GetRecordFolderPath() + workUnit.GetFileTitleName()+"."+convertAudioFileExtend;
 		commandLineArgument += " ";
 		commandLineArgument += "-y";
 		commandLineArgument += " ";
 		commandLineArgument += "-threads "+ThreadCount;
 		
		//
		Runtime runTime = Runtime.getRuntime();
		Process process = null;
		String commandLine = utility.CurrentExecutablePath() + "/ffmpeg";
		commandLine += " ";
		commandLine += commandLineArgument;
		
		//process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	if(null != process)
	    	{
	    		stream_reader_thread streamReaderThread1 = new stream_reader_thread(process.getInputStream());
	    		stream_reader_thread streamReaderThread2 = new stream_reader_thread(process.getErrorStream());
	    		streamReaderThread1.start();
	    		streamReaderThread2.start();
	    	   
	    		while (true)
	    		{
	    			if (!streamReaderThread1.isAlive() && !streamReaderThread2.isAlive()) 
	    			{  
	    				System.out.println("[ffmpeg_handler::ConvertAudioFile] - Finished : "+streamReaderThread1.getState()+" , "+streamReaderThread2.getState());
	    				//System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    				//System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
	    				process.waitFor();
	    				break;
	    			}
	    		}
	    	}
	    } 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }
	  
		return resultCode;
    }
    
    ///
    public static int MuxConvertedFile(work_unit workUnit)
    {
    	int resultCode = 0;
    	String commandLineArgument = "";
    	
 		if(false == utility.IsNullOrEmpty(workUnit.ConvertedVideoFileFullName))
 		{
 			commandLineArgument += "-i";
 			commandLineArgument += " ";
 			commandLineArgument += preset.GetRecordFolderPath() + workUnit.ConvertedVideoFileFullName;
 			commandLineArgument += " ";
 		}
 		
 		if(false == utility.IsNullOrEmpty(workUnit.ConvertedAudioFileFullName))
     	{
 			commandLineArgument += "-i";
 			commandLineArgument += " ";
 			commandLineArgument += preset.GetRecordFolderPath() + workUnit.ConvertedAudioFileFullName;
 			commandLineArgument += " ";
     	}
 		
 		if(false == utility.IsNullOrEmpty(workUnit.ConvertedVideoFileFullName))
     	{
 			commandLineArgument += "-c:v libx264";
 			commandLineArgument += " ";
     	}
 		
 		if(false == utility.IsNullOrEmpty(workUnit.ConvertedAudioFileFullName))
     	{
 			commandLineArgument += "-c:a aac";
 			commandLineArgument += " ";
     	}
 		
 		commandLineArgument += "-preset ultrafast -crf 21";
 		commandLineArgument += " ";
 		
 		commandLineArgument += preset.GetRecordFolderPath() + workUnit.GetMuxedFileFullName();
 		commandLineArgument += " ";
 		commandLineArgument += "-y";
 		commandLineArgument += " ";
 		commandLineArgument += "-threads "+ThreadCount;
 		
    	
		//
		Runtime runTime = Runtime.getRuntime();
		Process process = null;
		String commandLine = utility.CurrentExecutablePath() + "/ffmpeg";
		commandLine += " ";
		commandLine += commandLineArgument;
		
		//process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	if(null != process)
	    	{
	    		stream_reader_thread streamReaderThread1 = new stream_reader_thread(process.getInputStream());
	    		stream_reader_thread streamReaderThread2 = new stream_reader_thread(process.getErrorStream());
	    		streamReaderThread1.start();
	    		streamReaderThread2.start();
	    	   
	    		while (true)
	    		{
	    			if (!streamReaderThread1.isAlive() && !streamReaderThread2.isAlive()) 
	    			{  
	    				System.out.println("[ffmpeg_handler::MuxConvertedFile] - Finished : "+streamReaderThread1.getState()+" , "+streamReaderThread2.getState());
	    				//System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    				//System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
	    				process.waitFor();
	    				break;
	    			}
	    		}
	    	}
	    } 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }
	  
		return resultCode;
    }
    
    
    
    public static boolean MergeConvertedFile(merge_work_info mergeWorkInfo)
    {
    	// /media/psf/Home/file_merge_uploader/ffmpeg -loop 1 
    	// -i /media/psf/Home/file_merge_uploader/background_1280x720.png
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_business_yGxwDLlm2S_0_1559108360214.opus 
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_business_yGxwDLlm2S_0_1559108360214.webm
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_business_yGxwDLlm2S_0_1559108560214.opus
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_business_yGxwDLlm2S_0_1559108560214.webm
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_customer_xrEYZCYIyE_0_1559108360214.opus
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_customer_xrEYZCYIyE_0_1559108360214.webm
    	// -filter_complex 
    	// [1:a]atrim=200:531,asetpts=PTS-STARTPTS[configure1];
    	// [3:a]atrim=0:331,asetpts=PTS-STARTPTS[configure3];
    	// [5:a]atrim=200:531,asetpts=PTS-STARTPTS[configure5];
    	// [configure1][configure3][configure5]amix=inputs=3;
    	// [2:v]scale=640:720,trim=200:531,setpts=PTS-STARTPTS[configure2];
    	// [4:v]scale=640:720,trim=0:331,setpts=PTS-STARTPTS[configure4];
    	// [6:v]scale=640:720,trim=200:531,setpts=PTS-STARTPTS[configure6];
    	// [0:v][configure2]overlay=0:0[overlay0];
    	// [overlay0][configure4]overlay=0:0[overlay1];
    	// [overlay1][configure6]overlay=640:0" 
    	//-c:v libx264 -preset ultrafast -crf 21 -t 331.0 /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_1559108560214_1559108891214_331.mp4 -y
    	
        boolean result = false;
        int totalRenderInfoCount = mergeWorkInfo.CalculateRenderLayoutSize();
        if(0 == totalRenderInfoCount)
        	return false;
        
    	Runtime runTime = Runtime.getRuntime();
        Process process = null;

        String commandLine = utility.CurrentExecutablePath() + "/ffmpeg";
        commandLine += " ";
        commandLine += "-loop 1";
        commandLine += " -i " + utility.CurrentExecutablePath()+"/background_"+MergeResolutionWidth+"x"+MergeResolutionHeight+".png";
        
        int mediaCountIndex = 1;
        int audioCount = 0;
        int countIndex = 0;
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
        	merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
        	
        	if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetAudioConvertedFileFullName()))
            {
            	commandLine += " -i " + preset.GetRecordFolderPath() + mergeRenderInfo.GetAudioConvertedFileFullName();
            	
            	++audioCount;
            	
            	mergeRenderInfo.AudioCountIndex = mediaCountIndex++;
            }
        	
            if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetVideoConvertedFileFullName()))
            {
            	commandLine += " -i " + preset.GetRecordFolderPath() + mergeRenderInfo.GetVideoConvertedFileFullName();
            	
            	mergeRenderInfo.VideoCountIndex = mediaCountIndex++;
            }
        }

        
        if (totalRenderInfoCount > 0)
        {
        	commandLine += " -filter_complex";
    	
        	//window
        	//commandLine += " \"";
    	
        	//linux
        	commandLine += " ";
        }
        
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
            merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
            if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetAudioConvertedFileFullName()))
        	{
            	commandLine +=
            			"[" + mergeRenderInfo.AudioCountIndex + ":a]atrim=" + mergeRenderInfo.BetweenStartSecondTime + ":" + (mergeRenderInfo.BetweenStartSecondTime+mergeWorkInfo.GetDurationSecondsTime()) + ",asetpts=PTS-STARTPTS[configure" + mergeRenderInfo.AudioCountIndex + "];";
            }
        }
        
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
        	merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
        	if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetAudioConvertedFileFullName()))
         	{
                	commandLine +=
                			"[configure" + mergeRenderInfo.AudioCountIndex+"]";
         	}
        }
            
        //audio mix
        if(audioCount > 0)
        	commandLine += "amix=inputs=" + audioCount + ";";
        
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
            merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
            work_unit workUnit = mergeRenderInfo.GetWorkUnit();
            		
            if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetVideoConvertedFileFullName()))
        	{
            	commandLine +=
                        "[" + mergeRenderInfo.VideoCountIndex + ":v]scale=" + mergeRenderInfo.Width + ":" + mergeRenderInfo.Height + ",trim="+mergeRenderInfo.BetweenStartSecondTime+":"+(mergeRenderInfo.BetweenStartSecondTime+mergeWorkInfo.GetDurationSecondsTime())+",setpts=PTS-STARTPTS[configure" + mergeRenderInfo.VideoCountIndex + "];";
            }
        
            //startInfo.Arguments += "[2:v]scale = 576:360[configure2];";
            //startInfo.Arguments += "[3:v]scale = 576:360[configure3];";
            //startInfo.Arguments += "[4:v]scale = 576:360[configure4];";
        }

        int videoCountIndex = 0;
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
            merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
            work_unit workUnit = mergeRenderInfo.GetWorkUnit();
            		
            //startInfo.Arguments += "[0:v][configure1] overlay=0:0[overlay0];";

            if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetVideoConvertedFileFullName()))
            {
            	if (0 == countIndex)
            	{
            		if (2 > totalRenderInfoCount)
            		{
            			commandLine += "[0:v][configure" + mergeRenderInfo.VideoCountIndex + "]overlay=" + mergeRenderInfo.XPosition + ":" + mergeRenderInfo.YPosition;
            		}
            		else
            		{
            			commandLine += "[0:v][configure" + mergeRenderInfo.VideoCountIndex + "]overlay=" + mergeRenderInfo.XPosition + ":" + mergeRenderInfo.YPosition+"[overlay" + videoCountIndex + "];";
            		}
            	}
            	else
            	{
            		//last
            		if (countIndex == totalRenderInfoCount - 1)
            		{
            			commandLine += "[overlay" + (videoCountIndex-1) + "][configure" + mergeRenderInfo.VideoCountIndex + "]overlay=" + mergeRenderInfo.XPosition + ":" + mergeRenderInfo.YPosition;
            		}
            		else
            		{
            			commandLine += "[overlay" + (videoCountIndex-1) + "][configure" + mergeRenderInfo.VideoCountIndex + "]overlay=" + mergeRenderInfo.XPosition + ":" + mergeRenderInfo.YPosition+"[overlay" + videoCountIndex + "];";
            		}
            	}
            	
            	++videoCountIndex;
            }
            
            //commandLine += "[overlay0] [configure2] overlay=576:0[overlay1];";
            //commandLine += "[overlay1] [configure3] overlay=0:360[overlay2];";
            //commandLine += "[overlay2] [configure4] overlay=576:360";
        }

        if (totalRenderInfoCount > 0)
        {
        	//window
        	//commandLine += "\"";
        	
        	//linux
        	commandLine += "";
        	
        	commandLine += " -c:v libx264 -preset ultrafast -crf 21";
        	//commandLine += " -c:v libx264";
        }
        else
        {   
        	commandLine += " -c:v libx264 -vf fps=30 -pix_fmt yuv420p";
        }

        commandLine += " -t " + mergeWorkInfo.GetDurationSecondsTime();
        commandLine += " " + preset.GetRecordFolderPath() + mergeWorkInfo.GetMergedFileFullName();
        commandLine += " -y";
        commandLine += " ";
        commandLine += "-threads "+ThreadCount;

        log_handler.WriteConsole("ffmpeg_handler : " +commandLine);
        //startInfo.Arguments = "-loop 1 -i empty_image.png -i development_projectid_interviewid_interivew@camera_1554123600999_1554123795999_1554123677999_testuser1_0.mp4 -filter_complex  \"amix=inputs=1;[1:v]scale=576:360[configure1];[0:v][configure1]overlay=0:0\" -t 20 out.mp4";
		
        //process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	
	    	stream_reader_thread streamReaderThread1 = new stream_reader_thread(process.getInputStream());
	    	stream_reader_thread streamReaderThread2 = new stream_reader_thread(process.getErrorStream());
	    	streamReaderThread1.start();
	    	streamReaderThread2.start();
	    	   
	    	while (true)
	    	{
	    		if (!streamReaderThread1.isAlive() && !streamReaderThread2.isAlive()) 
	    	    {  
	    			//System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    	        //System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
	    	        process.waitFor();
	    	        break;
	    	    }
	    	}
	    	
	    	if(null != streamReaderThread1.Result() && 0 < streamReaderThread1.Result().length())
	    	{
	    		//System.out.println(streamReaderThread1.Result());
	    		result = true;
	    	}
	    } 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }

        return result; 
    }
    
    
    public static boolean MergeMuxedFile(merge_work_info mergeWorkInfo)
    {
    	// /media/psf/Home/file_merge_uploader/ffmpeg -loop 1 
    	// -i /media/psf/Home/file_merge_uploader/background_1280x720.png
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_business_yGxwDLlm2S_0_1559108360214_muxed.mp4 
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_business_yGxwDLlm2S_0_1559108360214_muxed.mp4
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_business_yGxwDLlm2S_0_1559108560214_muxed.mp4
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_business_yGxwDLlm2S_0_1559108560214_muxed.mp4
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_customer_xrEYZCYIyE_0_1559108360214_muxed.mp4
    	// -i /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_interview@camera_customer_xrEYZCYIyE_0_1559108360214_muxed.mp4
    	// -filter_complex 
    	// [1:a]atrim=200:531,asetpts=PTS-STARTPTS[configure1];
    	// [3:a]atrim=0:331,asetpts=PTS-STARTPTS[configure3];
    	// [5:a]atrim=200:531,asetpts=PTS-STARTPTS[configure5];
    	// [configure1][configure3][configure5]amix=inputs=3;
    	// [2:v]scale=640:720,trim=200:531,setpts=PTS-STARTPTS[configure2];
    	// [4:v]scale=640:720,trim=0:331,setpts=PTS-STARTPTS[configure4];
    	// [6:v]scale=640:720,trim=200:531,setpts=PTS-STARTPTS[configure6];
    	// [0:v][configure2]overlay=0:0[overlay0];
    	// [overlay0][configure4]overlay=0:0[overlay1];
    	// [overlay1][configure6]overlay=640:0" 
    	//-c:v libx264 -preset ultrafast -crf 21 -t 331.0 /media/psf/Home/record/dev_f3dbycW9Hp_wNc60LgZtU_1559108560214_1559108891214_331.mp4 -y
    	
    	boolean result = false;
    	int totalRenderInfoCount = mergeWorkInfo.CalculateRenderLayoutSize();
    	if(0 == totalRenderInfoCount)
    		return false;
        
    	Runtime runTime = Runtime.getRuntime();
        Process process = null;

        String commandLine = utility.CurrentExecutablePath() + "/ffmpeg";
        commandLine += " ";
        commandLine += "-loop 1";
        commandLine += " -i " + utility.CurrentExecutablePath()+"/background_"+MergeResolutionWidth+"x"+MergeResolutionHeight+".png";
        
        int mediaCountIndex = 1;
        int audioCount = 0;
        int countIndex = 0;
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
        	merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
        	
        	if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetAudioConvertedFileFullName()))
            {
            	commandLine += " -i " + preset.GetRecordFolderPath() + mergeRenderInfo.GetMuxedFileFullName();
            	
            	++audioCount;
            	
            	mergeRenderInfo.AudioCountIndex = mediaCountIndex++;
            }
        	
            if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetVideoConvertedFileFullName()))
            {
            	commandLine += " -i " + preset.GetRecordFolderPath() + mergeRenderInfo.GetMuxedFileFullName();
            	
            	mergeRenderInfo.VideoCountIndex = mediaCountIndex++;
            }
        }

        
        if (totalRenderInfoCount > 0)
        {
        	commandLine += " -filter_complex";
    	
        	//window
        	//commandLine += " \"";
    	
        	//linux
        	commandLine += " ";
        }
        
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
            merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
            if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetMuxedFileFullName()))
        	{
            	commandLine +=
            			"[" + mergeRenderInfo.AudioCountIndex + ":a]atrim=" + mergeRenderInfo.BetweenStartSecondTime + ":" + (mergeRenderInfo.BetweenStartSecondTime+mergeWorkInfo.GetDurationSecondsTime()) + ",asetpts=PTS-STARTPTS[configure" + mergeRenderInfo.AudioCountIndex + "];";
            }
        }
        
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
        	merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
        	if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetMuxedFileFullName()))
         	{
                	commandLine +=
                			"[configure" + mergeRenderInfo.AudioCountIndex+"]";
         	}
        }
            
        //audio mix
        if(audioCount > 0)
        	commandLine += "amix=inputs=" + audioCount + ";";
        
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
            merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
            work_unit workUnit = mergeRenderInfo.GetWorkUnit();
            		
            if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetMuxedFileFullName()))
        	{
            	commandLine +=
                        "[" + mergeRenderInfo.VideoCountIndex + ":v]scale=" + mergeRenderInfo.Width + ":" + mergeRenderInfo.Height + ",trim="+mergeRenderInfo.BetweenStartSecondTime+":"+(mergeRenderInfo.BetweenStartSecondTime+mergeWorkInfo.GetDurationSecondsTime())+",setpts=PTS-STARTPTS[configure" + mergeRenderInfo.VideoCountIndex + "];";
            }
        
            //startInfo.Arguments += "[2:v]scale = 576:360[configure2];";
            //startInfo.Arguments += "[3:v]scale = 576:360[configure3];";
            //startInfo.Arguments += "[4:v]scale = 576:360[configure4];";
        }

        int videoCountIndex = 0;
        for (countIndex = 0; countIndex < totalRenderInfoCount; ++countIndex)
        {
            merge_render_info mergeRenderInfo = mergeWorkInfo.RenderInfoList.get(countIndex);
            work_unit workUnit = mergeRenderInfo.GetWorkUnit();
            		
            //startInfo.Arguments += "[0:v][configure1] overlay=0:0[overlay0];";

            if(false == utility.IsNullOrEmpty(mergeRenderInfo.GetMuxedFileFullName()))
            {
            	if (0 == countIndex)
            	{
            		if (2 > totalRenderInfoCount)
            		{
            			commandLine += "[0:v][configure" + mergeRenderInfo.VideoCountIndex + "]overlay=" + mergeRenderInfo.XPosition + ":" + mergeRenderInfo.YPosition;
            		}
            		else
            		{
            			commandLine += "[0:v][configure" + mergeRenderInfo.VideoCountIndex + "]overlay=" + mergeRenderInfo.XPosition + ":" + mergeRenderInfo.YPosition+"[overlay" + videoCountIndex + "];";
            		}
            	}
            	else
            	{
            		//last
            		if (countIndex == totalRenderInfoCount - 1)
            		{
            			commandLine += "[overlay" + (videoCountIndex-1) + "][configure" + mergeRenderInfo.VideoCountIndex + "]overlay=" + mergeRenderInfo.XPosition + ":" + mergeRenderInfo.YPosition;
            		}
            		else
            		{
            			commandLine += "[overlay" + (videoCountIndex-1) + "][configure" + mergeRenderInfo.VideoCountIndex + "]overlay=" + mergeRenderInfo.XPosition + ":" + mergeRenderInfo.YPosition+"[overlay" + videoCountIndex + "];";
            		}
            	}
            	
            	++videoCountIndex;
            }
            
            //commandLine += "[overlay0] [configure2] overlay=576:0[overlay1];";
            //commandLine += "[overlay1] [configure3] overlay=0:360[overlay2];";
            //commandLine += "[overlay2] [configure4] overlay=576:360";
        }

        if (totalRenderInfoCount > 0)
        {
        	//window
        	//commandLine += "\"";
        	
        	//linux
        	commandLine += "";
        	
        	commandLine += " -c:v libx264 -preset ultrafast -crf 21";
        	//commandLine += " -c:v libx264";
        }
        else
        {   
        	commandLine += " -c:v libx264 -vf fps=30 -pix_fmt yuv420p";
        }

        commandLine += " -t " + mergeWorkInfo.GetDurationSecondsTime();
        commandLine += " " + preset.GetRecordFolderPath() + mergeWorkInfo.GetMergedFileFullName();
        commandLine += " -y";
        commandLine += " ";
        commandLine += "-threads "+ThreadCount;

        log_handler.WriteConsole("ffmpeg_handler : " +commandLine);
        //startInfo.Arguments = "-loop 1 -i empty_image.png -i development_projectid_interviewid_interivew@camera_1554123600999_1554123795999_1554123677999_testuser1_0.mp4 -filter_complex  \"amix=inputs=1;[1:v]scale=576:360[configure1];[0:v][configure1]overlay=0:0\" -t 20 out.mp4";
		
        //process
	    try 
	    {
	    	process = runTime.exec(commandLine);
	    	
	    	stream_reader_thread streamReaderThread1 = new stream_reader_thread(process.getInputStream());
	    	stream_reader_thread streamReaderThread2 = new stream_reader_thread(process.getErrorStream());
	    	streamReaderThread1.start();
	    	streamReaderThread2.start();
	    	   
	    	while (true)
	    	{
	    		if (!streamReaderThread1.isAlive() && !streamReaderThread2.isAlive()) 
	    	    {  
	    			//System.out.println("Thread 1 Status : "+streamReaderThread1.getState());
	    	        //System.out.println("Thread 2 Status : "+streamReaderThread2.getState());
	    	        
	    	        process.waitFor();
	    	        break;
	    	    }
	    	}
	    	
	    	if(null != streamReaderThread1.Result() && 0 < streamReaderThread1.Result().length())
	    	{
	    		//System.out.println(streamReaderThread1.Result());
	    		result = true;
	    	}
	    } 
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (InterruptedException e)
	    {
	    	e.printStackTrace();
	    }
	    finally
	    {
	    	if(process != null) 
	    		process.destroy();
	    }

        return result; 
    }
}
