import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class work_message
{
	public static BlockingQueue<work_message> QueueCheck = new ArrayBlockingQueue<work_message>(100);
	public static BlockingQueue<work_message> QueueConvert = new ArrayBlockingQueue<work_message>(100);
	public static BlockingQueue<work_message> QueueMuxMerge = new ArrayBlockingQueue<work_message>(100);
	
	public static final int TYPE_NONE = -1;
	
	public static final int TYPE_CONVERT_MJR_FILE = 0;
	public static final int TYPE_UPLOAD_CONVERTED_FILE_TO_S3 = 1;
	public static final int TYPE_JANUS_ARCHIVE_CALLBACK_CONVERTED_FILE = 2;
	
	public static final int TYPE_MUX_CONVERTED_FILE = 3;
	public static final int TYPE_UPLOAD_MUXED_FILE_TO_S3 = 4;
	public static final int TYPE_JANUS_ARCHIVE_CALLBACK_MUXED_FILE = 5;
	
	public static final int TYPE_MERGE_CONVERTED_FILE = 6;
	public static final int TYPE_UPLOAD_MERGED_FILE_TO_S3 = 7;
	public static final int TYPE_JANUS_ARCHIVE_CALLBACK_MERGED_FILE = 8;
	
	public static final int TYPE_SET_MUXED_FILE_ATTRIBUTE = 9;
	public static final int TYPE_SET_CONVERTED_FILE_ATTRIBUTE = 10;
	
	public static final int TYPE_DELETE_MJR_FILE = 11;
	public static final int TYPE_DELETE_CONVERTED_FILE = 12;
	public static final int TYPE_DELETE_MUXED_FILE = 13;
	public static final int TYPE_DELETE_MERGED_FILE = 14;
	
	public static final int TYPE_QUIT_THREAD = 15;
	
	
	public int Type = TYPE_NONE;
	
}

