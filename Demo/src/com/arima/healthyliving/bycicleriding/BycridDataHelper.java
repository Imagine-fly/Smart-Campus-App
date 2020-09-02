package com.arima.healthyliving.bycicleriding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.arima.healthyliving.bycicleriding.BycridData.BycridImgInfo;

import android.os.Parcel;
import android.text.format.DateFormat;

//2014/02/27-34295-WendyWan,[AAP1302][Bycicleriding]Bycrid data save and load interface
public class BycridDataHelper {

	private final static String data_file_name = "bycicle_data.txt";
	private final static String app_save_path = "/storage/sdcard0/HealthyLiving";
	private final static String bycicle_folder_name = "bycicle";
	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
	private final static String bycicle_tmp_folder_name = "tmp";
	private final static String info_file_name = "info.txt";
	//>2014/03/08-Task_34590-xiangrongji
	
	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
	private static BycridDataHelper mInstance;
	
	public static BycridDataHelper getInstance() {
		if (mInstance == null) {
			mInstance = new BycridDataHelper();
		}
		return mInstance;
	}
	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
	/*
	 * input record folder name or tmp folder name
	 */
	public final static String getFolderPathByName(String folderName) {
		return getRecordParentFolderPath() + "/" + folderName;
	}
	public final static String getTmpFolderPath() {
		return getFolderPathByName(bycicle_tmp_folder_name);
	}
	public final static String getTmpFilePathByName(String fileName) {
		return getTmpFolderPath() + "/" + fileName;
	}
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private final static String getRecordParentFolderPath() {
		return app_save_path + "/" + bycicle_folder_name;
	}
	//>2014/03/06-Task_34540-xiangrongji
	private static String generateNameByDate(long date) {
		return (String) DateFormat.format("yyyyMMdd-kkmm", date);
	}

	private static File generateNewFolder(String full_path) {
		File folder = new File(full_path);
		int index = 0;
		while (folder.exists()) {
			index++;
			folder = new File(full_path + "-" + index);
		}
		//folder.mkdirs();
		
		return folder;
	}

	public String refreshTmpFolder() {
		String path = getTmpFolderPath();
		File folder = new File(path);
		deleteFiles(folder);
		folder.mkdirs();
		return path;
	}
	//>2014/03/08-Task_34590-xiangrongji

	public BycridData createData() {
		return new BycridData();
	}
	
	public ArrayList<BycridData> loadHistoryHeaderList() {
		//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		File bycridFolder = new File(getRecordParentFolderPath());
		//>2014/03/06-Task_34540-xiangrongji
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.equals(bycicle_tmp_folder_name))
					return false;
				else
					return true;
			}
		};
		File[] folderList = bycridFolder.listFiles(filter);
		if (folderList == null) {
			return null;
		}
		sortFileList(folderList);
		//>2014/03/08-Task_34590-xiangrongji
		
		ArrayList<BycridData> dataList = new ArrayList<BycridData>();
		BycridData data;
		for (File folder : folderList) {
			if (folder.isDirectory()) {
				try {
					//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
					data = load(folder.getName(), true);
					//>2014/03/06-Task_34540-xiangrongji
					if (data != null) {
						dataList.add(data);
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return dataList;
	}
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	public void delete(String recordFolderName) {
		String folderPath = getFolderPathByName(recordFolderName);
		File folder = new File(folderPath);
		deleteFiles(folder);
	}
	//>2014/03/06-Task_34540-xiangrongji

	public String save(BycridData data) {
		//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
		//String folderName = (String) DateFormat.format("yyyyMMdd-kkmm", data.date);
		//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
		//String folderPath = getRecordParentFolderPath() + "/" + folderName;
		//>2014/03/06-Task_34540-xiangrongji
		String folderName = generateNameByDate(data.date);
		String folderPath = getFolderPathByName(folderName);
		File folder = generateNewFolder(folderPath);
		
		File tmpFolder = new File(getTmpFolderPath());
		tmpFolder.renameTo(folder);

		saveFile(data, folder.getPath());
		
		saveImgInfos(data.img_info_list, folderPath);
		//>2014/03/08-Task_34590-xiangrongji
		
		return folder.getPath();
	}
	//>2014/03/03-Task_34403-xiangrongji

	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	public BycridData load(String recordFolderName, boolean header_only) throws FileNotFoundException {
		String folderPath = getFolderPathByName(recordFolderName);

		//load bycicle_data.txt into data
		BycridData data = loadFile(folderPath, header_only);
		if (data == null)
			return null;
		
		//set folder name in data
		data.record_name = recordFolderName;
		
		//set picture info in data
		//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
		List<BycridImgInfo> list;
		try {
			list = loadImgInfos(folderPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			list = null;
			e.printStackTrace();
		}
		data.img_info_list = list;
		data.picture_num = 0;
		if (list != null) {
			data.picture_num = list.size();
		}
		//>2014/03/08-Task_34590-xiangrongji
		
		return data;
	}
	//>2014/03/06-Task_34540-xiangrongji

	private void saveFile(BycridData data, String folderPath) {
		File file = null;
		String path = folderPath + "/" + data_file_name;
		file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);

			if (null != fos) {
				writeDataStream(fos, data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private void writeDataStream(FileOutputStream fos, BycridData data)
			throws IOException {
		long track_len = data.track_len;
		byte dataByte[] = new byte[8];

		// date
		dataByte = getBytes(data.date);
		fos.write(dataByte, 0, 8);

		// distance
		dataByte = getBytes(data.distance);
		fos.write(dataByte, 0, 8);

		// duration
		dataByte = getBytes(data.duration);
		fos.write(dataByte, 0, 8);

		// altitude_rise
		dataByte = getBytes(data.altitude_rise);
		fos.write(dataByte, 0, 8);

		// altitude_drop
		dataByte = getBytes(data.altitude_drop);
		fos.write(dataByte, 0, 8);

		// reference_longitude
		dataByte = getBytes(data.reference_longitude);
		fos.write(dataByte, 0, 8);

		//reference_latitude
		dataByte = getBytes(data.reference_latitude);
		fos.write(dataByte, 0, 8);
		// track_len
		dataByte = getBytes(track_len);
		fos.write(dataByte, 0, 8);

		// track_list
		List<float[][]> track_list = data.track_list;
		for (int i = 0; i < track_len / 1024; i++) {
			float[][] nodeBuffer;
			nodeBuffer = track_list.get(i);
			for (int j = 0; j < 1024; j++) {
				streamWriteNode(fos, nodeBuffer[j]);
			}
		}
		//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
		if (track_list.size() > 0) {
			float[][] nodeBuffer;
			nodeBuffer = track_list.get(track_list.size() - 1);
			long len = track_len - (track_len / 1024) * 1024;
			for (int j = 0; j < len; j++) {
				streamWriteNode(fos, nodeBuffer[j]);
			}
		}
		//>2014/03/03-Task_34403-xiangrongji
	}

	private void streamWriteNode(FileOutputStream fos, float[] node)
			throws IOException {
		byte dataByte[] = new byte[4];
		// longitude
		dataByte = getBytes(node[0]);
		fos.write(dataByte, 0, 4);
		// latitude
		dataByte = getBytes(node[1]);
		fos.write(dataByte, 0, 4);
	}

	private void streamReadNode(FileInputStream fis, float[] node)
			throws IOException {
		byte dataByte[] = new byte[4];
		// longitude
		fis.read(dataByte, 0, 4);
		node[0] = getFloat(dataByte);
		// latitude
		fis.read(dataByte, 0, 4);
		node[1] = getFloat(dataByte);
	}

	//<2014/03/03-Task_34403-xiangrongji, [AAP1302][BycicleRiding]update save/load and history viewer
	private void readDataStream(FileInputStream fis, BycridData data, boolean header_only)
			throws IOException {
		long track_len = 0;
		byte dataByte[] = new byte[8];

		// date
		fis.read(dataByte, 0, 8);
		data.date = getLong(dataByte);

		// distance
		fis.read(dataByte, 0, 8);
		data.distance = getDouble(dataByte);

		// duration
		fis.read(dataByte, 0, 8);
		data.duration = getLong(dataByte);

		// altitude_rise
		fis.read(dataByte, 0, 8);
		data.altitude_rise = getDouble(dataByte);

		// altitude_drop
		fis.read(dataByte, 0, 8);
		data.altitude_drop = getDouble(dataByte);
		
		if (header_only)
			return;

		// reference_longitude
		fis.read(dataByte, 0, 8);
		data.reference_longitude = getDouble(dataByte);
		
		//reference_latitude
		fis.read(dataByte, 0, 8);
		data.reference_latitude = getDouble(dataByte);

		// track_len
		fis.read(dataByte, 0, 8);
		track_len = getLong(dataByte);
		data.track_len = track_len;

		// track_list
		List<float[][]> track_list = new ArrayList<float[][]>();
		for (int i = 0; i < track_len / 1024; i++) {
			float[][] nodeBuffer = new float[1024][2];
			for (int j = 0; j < 1024; j++) {
				streamReadNode(fis, nodeBuffer[j]);
			}
			track_list.add(nodeBuffer);
		}
		long len = track_len - (track_len / 1024) * 1024;
		if (len > 0) {
			float[][] nodeBuffer = new float[1024][2];
			for (int j = 0; j < len; j++) {
				streamReadNode(fis, nodeBuffer[j]);
			}
			track_list.add(nodeBuffer);
		}
		data.track_list = track_list;

	}
	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private BycridData loadFile(String folderPath, boolean header_only) throws FileNotFoundException {
		String path = folderPath + "/" + data_file_name;
	//>2014/03/06-Task_34540-xiangrongji
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("file not fould: " + path);
		}

		BycridData data = new BycridData();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			if (fis != null) {
				readDataStream(fis, data, header_only);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return data;

	}
	//>2014/03/03-Task_34403-xiangrongji

	private byte[] getBytes(int data) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data >> 8) & 0xff);
		bytes[2] = (byte) ((data >> 16) & 0xff);
		bytes[3] = (byte) ((data >> 24) & 0xff);
		return bytes;
	}

	private byte[] getBytes(long data) {
		byte[] bytes = new byte[8];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (data & 0xff);
			data = data >> 8;
		}
		return bytes;
	}

	private byte[] getBytes(float data) {
		int intBits = Float.floatToIntBits(data);
		return getBytes(intBits);
	}

	private byte[] getBytes(double data) {
		long longBits = Double.doubleToLongBits(data);
		return getBytes(longBits);
	}

	private int getInt(byte[] bytes) {
		int t = 0;
		int t0 = bytes[0] & 0xff;
		int t1 = bytes[1] & 0xff;
		int t2 = bytes[2] & 0xff;
		int t3 = bytes[3] & 0xff;
		t1 <<= 8;
		t2 <<= 16;
		t3 <<= 24;
		t = t0 | t1 | t2 | t3;
		return t;
	}

	private long getLong(byte[] bytes) {
		long s = 0;
		long s0 = bytes[0] & 0xff;
		long s1 = bytes[1] & 0xff;
		long s2 = bytes[2] & 0xff;
		long s3 = bytes[3] & 0xff;
		long s4 = bytes[4] & 0xff;
		long s5 = bytes[5] & 0xff;
		long s6 = bytes[6] & 0xff;
		long s7 = bytes[7] & 0xff;
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 32;
		s5 <<= 40;
		s6 <<= 48;
		s7 <<= 56;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	private float getFloat(byte[] bytes) {
		return Float.intBitsToFloat(getInt(bytes));
	}

	private double getDouble(byte[] bytes) {
		return Double.longBitsToDouble(getLong(bytes));
	}

	//<2014/03/06-Task_34540-xiangrongji, [AAP1302][BycicleRiding]update altitude & Viewer options & Viewer & Riding & history & function of delete
	private static void deleteFiles(File file) {
		if (!file.exists()) {
			return;
		}
		//when file, delete and ok
		if (file.isFile()) {
			file.delete();
			return;
		}
		
		//when folder
		if (file.isDirectory()) {
			//delete sub folders and files
			File[] children = file.listFiles();
			if ((children == null) || (children.length == 0)) {
				file.delete();
				return;
			}
			for (File f : children) {
				deleteFiles(f);
			}
			
			//when folder is empty, delete and ok
			file.delete();
		}
	}
	//>2014/03/06-Task_34540-xiangrongji
	//<2014/03/08-Task_34590-xiangrongji, [AAP1302][BycicleRiding]update history sorting & capture func & folder for save/load
	/*
	 * compare whether from is larger than to
	 */
	private boolean checkInOrder(String before, String later) {
		if (before.compareTo(later) >= 0)
			return true;
		else
			return false;
	}
	private void sortFileList(File[] list) {
		if (list == null)
			return;
		else if (list.length == 0)
			return;
		
		File file;
		for (int i = 0; i < list.length; i++) {
			for (int j = i + 1; j < list.length; j++) {
				if (!checkInOrder(list[i].getName(), list[j].getName())) {
					file = list[i];
					list[i] = list[j];
					list[j] = file;
				}
			}
		}
	}
	private void saveImgInfos(List<BycridImgInfo> list, String folderPath) {
		if (list == null) {
			return;
		}
		Parcel parcel = Parcel.obtain();
		parcel.writeInt(list.size());
		for (BycridImgInfo info : list) {
			info.writeToParcel(parcel, 0);
		}
		
		File file = null;
		String path = folderPath + "/" + info_file_name;
		file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fos = null;
		try {
			file.createNewFile();
			fos = new FileOutputStream(file);

			if (null != fos) {
				//writeDataStream(fos, data);
				byte[] arr = parcel.marshall();
				fos.write(arr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private List<BycridImgInfo> loadImgInfos(String folderPath) throws Exception {
		List<BycridImgInfo> list = new ArrayList<BycridImgInfo>();

		String path = folderPath + "/" + info_file_name;
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}

		Parcel parcel = Parcel.obtain();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			if (fis != null) {
				//readDataStream(fis, data, header_only);
				int length = fis.available();
				byte[] buffer = new byte[length];
				fis.read(buffer);
				parcel.unmarshall(buffer, 0, length);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		parcel.setDataPosition(0);
		int num = parcel.readInt();
		BycridImgInfo info;
		for (int i = 0; i < num; i++) {
			info = BycridImgInfo.CREATOR.createFromParcel(parcel);
			list.add(info);
		}
		
		return list;
	}
	//>2014/03/08-Task_34590-xiangrongji
}
//2014/02/27-34295-WendyWan