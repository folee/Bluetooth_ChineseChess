package com.topkc.chinesechess.chess;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

import com.topkc.chinesechess.util.FileManager;

/**
 * �浵ǰ�����������̣��������������Ǳ�ʾ��ǰ��Ȩ��˭�����һ��Ϊ���壬�ڶ���Ϊ���壬0Ϊ��1Ϊ��
 * @author WEI
 *
 */
public class ArchiveContorller {
	
	Context context = null;
	FileManager fileManager;
	
	public ArchiveContorller(Context context){
		this.context = context;
		fileManager = new FileManager(context);
	}
	/**
	 * ����浵�ļ�
	 * ��ʽ ����-����-��Ȩ
	 * @param context
	 * @return
	 */
	public int saveArchiveFile(){
		
		//String filePath = GameViewActivity.this.getFilesDir().getAbsolutePath();
		//System.out.println();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hhmmss");  
		String timeStr = sdf.format(new Date());  
		//�õ������ļ�
		int chessboard[][] = ChineseChess.chessboard;
		StringBuffer strBuffer = new StringBuffer();
		
		
		//д��������Ϣ
		for(int i =0 ;i<=9;i++){
			for(int j=0;j<=8;j++){
				//System.out.print("chessboard["+i+"]["+j+"] = "+c[i][j]+";");	
				strBuffer.append(chessboard[i][j]+"-");
			}	
		}
		//���浱ǰ��Ȩ��˭����
		if(ChineseChess.isRedTurn == true){
			strBuffer.append("1-0");//����Ϊ1������Ϊ0
		}else{
			strBuffer.append("0-1");//����Ϊ1������Ϊ0
		}
		
		try {
			fileManager.save(timeStr, strBuffer.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Save file err!!!");
		}
		
		Toast.makeText(context, "������Ϸ�ɹ���\n�浵:"+timeStr, Toast.LENGTH_LONG).show();
		return 1;
	}
	
	/**
	 * ��ȡ�浵�ļ�
	 * ���ﲻ����ת����txt��ʽ
	 * @param context
	 * @return
	 */
	public boolean readArchiveFile(String archiveName){
		
		String archiveStr = null;
		
		try {
			archiveStr = fileManager.read(archiveName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String archiveInt[] = archiveStr.split("-");
		
		int counter  = 0;
		
		for(int i =0 ;i<=9;i++){
			for(int j=0;j<=8;j++){
				//System.out.print("chessboard["+i+"]["+j+"] = "+c[i][j]+";");	
				ChineseChess.chessboard[i][j] = Integer.parseInt(archiveInt[counter++]);
			}	
		}
		//���浱ǰ��Ȩ��˭����
		if(Integer.parseInt(archiveInt[counter++])==1 ){
			ChineseChess.isRedTurn = true;
			ChineseChess.isBlackTurn = false;
			//strBuffer.append("1-0");//����Ϊ1������Ϊ0
		}else{
			ChineseChess.isRedTurn = false;
			ChineseChess.isBlackTurn = true;
			//strBuffer.append("0-1");//����Ϊ1������Ϊ0
		}
		
		return true;
	}
	
	public boolean deleteArchiveFile(String fileName){
		
		//FileManager fileManager = new FileManager(context);
		
		//fileManager.
		return fileManager.deleteFile(fileName);
	}
	
	public String[] getArchiveDir(){
		
		return fileManager.getFileDir();
	}
}
