package com.ib.updateFeatureFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class method {
	Logger log = Logger.getLogger("LOG");

	// Read cell value in Excel
	public String readExcel(int column, int row) throws BiffException, IOException {
		try {
			String FilePath = Constants.DATA_FILE_PATH;
			FileInputStream fs = new FileInputStream(FilePath);
			Workbook wb = Workbook.getWorkbook(fs);
			// TO get the access to the sheet
			Sheet sh = wb.getSheet("Sheet1");
			String cellValue = sh.getCell(column, row).getContents();
			wb.close();
			fs.close();
			return cellValue;
		} catch (BiffException e) {
			log.error("error:", e);
			throw (e);
		} catch (IOException e) {
			log.error("error:", e);
			throw (e);
		}
	}

	// Get number of rows used in XL
	public int noOfRows() throws BiffException, IOException {
		try {
			String FilePath = Constants.DATA_FILE_PATH;
			FileInputStream fs = new FileInputStream(FilePath);
			Workbook wb = Workbook.getWorkbook(fs);

			// TO get the access to the sheet
			Sheet sh = wb.getSheet("Sheet1");

			int noOfRows = sh.getColumn(0).length;
			fs.close();
			wb.close();
			return noOfRows;
		} catch (BiffException e) {
			log.error("error:", e);
			throw (e);
		} catch (IOException e) {
			log.error("error:", e);
			throw (e);
		}

	}

	// Read file list in directory
	public String[] readFileList() {
		try {
			File file = new File(Constants.FEATURE_FILE_LOCATION);
			String[] fileList = file.list();
			return fileList;
		} catch (Exception e) {
			log.error("error:", e);
			throw (e);
		}

	}

	// Replace value in files
	public void replaceString() throws Throwable {
		String[] fileList = readFileList();
		try {
			for (int i = 0; i < fileList.length; i++) {
				Path path = Paths.get(Constants.FEATURE_FILE_LOCATION + fileList[i]);
				int row = noOfRows();
				for (int a = 0; a < row; a++) {
					Charset charset = StandardCharsets.UTF_8;
					String content = new String(Files.readAllBytes(path), charset);
					if(readExcel(3, a).equals("UPDATE")){
						content = content.replace(readExcel(1, a), readExcel(2, a));
						Files.write(path, content.getBytes(charset));
					}
				}
			}
		} catch (Exception e) {
			log.error("error:", e);
			throw (e);
		}
	}
	
	// Replace excel from updated value
	// Value increase from one
		public void updateExcel() throws Throwable {
			String FilePath = Constants.DATA_FILE_PATH;
			String tempFilePath = Constants.TMP_DATA_FILE_PATH;
			FileInputStream fs = new FileInputStream(FilePath);
			Workbook rs = Workbook.getWorkbook(fs);
			WritableWorkbook copy = Workbook.createWorkbook(new File(tempFilePath), rs);
			Sheet sh = rs.getSheet("Sheet1");
			WritableSheet ws = copy.getSheet("Sheet1"); 
			try {
					int row = noOfRows();
					Label Name=null;
					for (int a = 0; a < row; a++) {
						for(int b=0; b < 4; b++){
							switch(b){
							case 0:
								Name=new Label(0,a,sh.getCell(0,a).getContents());
								ws.addCell(Name);
								break;
							case 1:
								Name=new Label(1,a,sh.getCell(2,a).getContents());
								ws.addCell(Name);
								break;
							case 2:
								String value = sh.getCell(2,a).getContents();
								String number=Long.toString(Long.parseLong(value.replaceAll("[^0-9]", ""))+1);
								String letters=value.replaceAll("[^aA-zZ]", "");
								String newValue=number+letters;
								Name=new Label(2,a,newValue);
								ws.addCell(Name);
								break;
							case 3:
								Name=new Label(3,a,sh.getCell(3,a).getContents());
								ws.addCell(Name);
								break;
							}
						}
						
					}
			        copy.write();
					rs.close();
					copy.close();
					fs.close();
			} 
			catch (Exception e) {
				log.error("error:", e);
				throw (e);
			}
		}
		
		//Delete old file
		//Rename temp file
		
		public void renameFile(){
			try{
				File oldfile = new File(Constants.TMP_DATA_FILE_PATH);
		        File newFile = new File(Constants.DATA_FILE_PATH);
		        newFile.delete();
		        oldfile.renameTo(newFile);
			}
			catch (Exception e) {
				log.error("error:", e);
				throw (e);
			}
		}

}
