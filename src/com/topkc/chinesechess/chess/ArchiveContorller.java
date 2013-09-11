package com.topkc.chinesechess.chess;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

import com.topkc.chinesechess.util.FileManager;

/**
 * 存档前面是整个棋盘，后面两个数字是表示当前棋权在谁手里，第一个为红棋，第二个为黑棋，0为否，1为是
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
	 * 保存存档文件
	 * 格式 名字-棋盘-棋权
	 * @param context
	 * @return
	 */
	public int saveArchiveFile(){
		
		//String filePath = GameViewActivity.this.getFilesDir().getAbsolutePath();
		//System.out.println();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hhmmss");  
		String timeStr = sdf.format(new Date());  
		//拿到棋盘文件
		int chessboard[][] = ChineseChess.chessboard;
		StringBuffer strBuffer = new StringBuffer();
		
		
		//写入棋盘信息
		for(int i =0 ;i<=9;i++){
			for(int j=0;j<=8;j++){
				//System.out.print("chessboard["+i+"]["+j+"] = "+c[i][j]+";");	
				strBuffer.append(chessboard[i][j]+"-");
			}	
		}
		//保存当前棋权在谁手里
		if(ChineseChess.isRedTurn == true){
			strBuffer.append("1-0");//红棋为1，黑棋为0
		}else{
			strBuffer.append("0-1");//红棋为1，黑棋为0
		}
		
		try {
			fileManager.save(timeStr, strBuffer.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Save file err!!!");
		}
		
		Toast.makeText(context, "保存游戏成功！\n存档:"+timeStr, Toast.LENGTH_LONG).show();
		return 1;
	}
	
	/**
	 * 读取存档文件
	 * 这里不处理转换成txt格式
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
		//保存当前棋权在谁手里
		if(Integer.parseInt(archiveInt[counter++])==1 ){
			ChineseChess.isRedTurn = true;
			ChineseChess.isBlackTurn = false;
			//strBuffer.append("1-0");//红棋为1，黑棋为0
		}else{
			ChineseChess.isRedTurn = false;
			ChineseChess.isBlackTurn = true;
			//strBuffer.append("0-1");//红棋为1，黑棋为0
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
