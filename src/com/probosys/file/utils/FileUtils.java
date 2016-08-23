package com.probosys.file.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.probosys.fileupload.model.PimPojo;

public class FileUtils {

	public static List<PimPojo> fileRead(InputStream fileStream) {
		List<PimPojo> pojoList = new ArrayList<>();
		try {

			// FileInputStream fileStream = new FileInputStream(file);

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(fileStream);

			try {
				String fileName = "D:/uploadedfiles/xls_"
						+ DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss");

				FileUtils.writeXlsFile(workbook, fileName);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();
			int cnt = 0;

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (cnt == 0) {
					cnt++;
				} else {
					PimPojo pojo = new PimPojo();
					Cell c = row.getCell(0);
					if (Cell.CELL_TYPE_BLANK != c.getCellType()) {
						pojo.setParent(c.getStringCellValue());
					}
					c = row.getCell(1);
					if (Cell.CELL_TYPE_BLANK != c.getCellType()) {
						pojo.setChild(c.getStringCellValue());
					}
					c = row.getCell(2);
					if (Cell.CELL_TYPE_BLANK != c.getCellType()
							&& c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						Double val = c.getNumericCellValue();
						pojo.setStatusId(val != null ? val.longValue() : 0);
					} else if (Cell.CELL_TYPE_BLANK != c.getCellType()) {
						String val = c.getStringCellValue();
						pojo.setStatusId(val != null ? Long.parseLong(val) : 0);
					}
					c = row.getCell(3);
					if (Cell.CELL_TYPE_BLANK != c.getCellType()) {
						pojo.setItemType(c.getStringCellValue());
					}
					pojoList.add(pojo);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (PimPojo pimPojo : pojoList) {
			System.out.println(pimPojo.toString());
		}
		return pojoList;
	}

	public static List<PimPojo> readCSXlFile(InputStream fileStream) {

		int cnt = 0;
		List<PimPojo> pojoList = new ArrayList<>();

		// FileInputStream fileStream = new FileInputStream(file);

		// Get the workbook instance for XLS file

		try {
			XSSFWorkbook workbook = new XSSFWorkbook(fileStream);

			// Get first sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			// int cnt=0;

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (cnt == 0) {
					cnt++;
				} else {
					PimPojo pojo = new PimPojo();
					Cell c = row.getCell(0);
					if (Cell.CELL_TYPE_BLANK != c.getCellType()
							&& c.getCellType() == Cell.CELL_TYPE_STRING) {
						pojo.setParent(c.getStringCellValue());
					} else if (Cell.CELL_TYPE_BLANK != c.getCellType()
							&& c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						pojo.setParent(c.getNumericCellValue() + "");
					}
					
					c = row.getCell(1);
					if (Cell.CELL_TYPE_BLANK != c.getCellType()
							&& c.getCellType() == Cell.CELL_TYPE_STRING) {
						pojo.setCrossSellChild(c.getStringCellValue());
					} else if (Cell.CELL_TYPE_BLANK != c.getCellType()
							&& c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						pojo.setCrossSellChild(c.getNumericCellValue() + "");
					}
					
					c = row.getCell(2);
					
					if (Cell.CELL_TYPE_BLANK != c.getCellType()
							&& c.getCellType() == Cell.CELL_TYPE_STRING) {
						pojo.setCrossSellType(c.getStringCellValue());
					} else if (Cell.CELL_TYPE_BLANK != c.getCellType()
							&& c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
						pojo.setCrossSellType(c.getNumericCellValue() + "");
					}
					
					c = row.getCell(3);
					if (Cell.CELL_TYPE_BLANK != c.getCellType()) {
						pojo.setAction(c.getStringCellValue());
					}
					pojoList.add(pojo);
				}
			}
		} catch (POIXMLException ife) {
			ife.printStackTrace();
			throw new RuntimeException("File Not Found. Kindly upload file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("File Not Found. Kindly upload file");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("File Not Found. Kindly upload file");
		}

		return pojoList;
	}

	public static List<PimPojo> readXslsFile(InputStream fileStream) {
		List<PimPojo> pojoList = new ArrayList<>();
		try {

			// FileInputStream fileStream = new FileInputStream(file);

			// Get the workbook instance for XLS file
			XSSFWorkbook workbook = new XSSFWorkbook(fileStream);

			// Get first sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();
			int cnt = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (cnt == 0) {
					cnt++;
					continue;
				}
				PimPojo pojo = new PimPojo();
				Cell c = row.getCell(0);
				if (Cell.CELL_TYPE_BLANK != c.getCellType()
						&& c.getCellType() == Cell.CELL_TYPE_STRING) {
					pojo.setParent(c.getStringCellValue());
				} else if (Cell.CELL_TYPE_BLANK != c.getCellType()
						&& c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					pojo.setParent(c.getNumericCellValue() + "");
				}

				c = row.getCell(1);
				if (Cell.CELL_TYPE_BLANK != c.getCellType()
						&& c.getCellType() == Cell.CELL_TYPE_STRING) {
					pojo.setChild(c.getStringCellValue());
				} else if (Cell.CELL_TYPE_BLANK != c.getCellType()
						&& c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					pojo.setChild(c.getNumericCellValue() + "");
				}
				c = row.getCell(2);
				if (Cell.CELL_TYPE_BLANK != c.getCellType()
						&& c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					Double val = c.getNumericCellValue();
					pojo.setStatusId(val != null ? val.longValue() : 0);
				} else if (Cell.CELL_TYPE_BLANK != c.getCellType()) {
					String val = c.getStringCellValue();
					pojo.setStatusId(val != null ? Long.parseLong(val) : 0);
				}
				c = row.getCell(3);
				if (Cell.CELL_TYPE_BLANK != c.getCellType()
						&& c.getCellType() == Cell.CELL_TYPE_STRING) {
					pojo.setItemType(c.getStringCellValue());
				} else if (Cell.CELL_TYPE_BLANK != c.getCellType()
						&& c.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					pojo.setItemType(c.getNumericCellValue() + "");
				}
				pojoList.add(pojo);
			}

			// Iterate through each rows from first sheet
			/*
			 * Iterator<Row> rowIterator = sheet.iterator(); while
			 * (rowIterator.hasNext()) { Row row = rowIterator.next();
			 * 
			 * // For each row, iterate through each columns Iterator<Cell>
			 * cellIterator = row.cellIterator(); while (cellIterator.hasNext())
			 * {
			 * 
			 * Cell cell = cellIterator.next();
			 * 
			 * switch (cell.getCellType()) { case Cell.CELL_TYPE_BOOLEAN:
			 * System.out.print(cell.getBooleanCellValue() + "\t\t"); break;
			 * case Cell.CELL_TYPE_NUMERIC:
			 * System.out.print(cell.getNumericCellValue() + "\t\t"); break;
			 * case Cell.CELL_TYPE_STRING:
			 * System.out.print(cell.getStringCellValue() + "\t\t"); break; } }
			 * System.out.println(""); }
			 */
			fileStream.close();
			/*
			 * FileOutputStream out = new FileOutputStream(new
			 * File("C:\\test.xls")); workbook.write(out); out.close();
			 */
		} catch (POIXMLException ife) {
			ife.printStackTrace();
			throw new RuntimeException("File Not Found. Kindly upload file");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("File Not Found. Kindly upload file");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("File Not Found. Kindly upload file");
		}
		/*
		 * for (PimPojo pimPojo : pojoList) {
		 * System.out.println(pimPojo.getItemType()); }
		 */
		return pojoList;
	}

	public static void writeXlsFile(Workbook workBook, String fileName)
			throws IOException {
		// lets write to file
		File file = new File(fileName);
		file.mkdirs();
		FileOutputStream fos = new FileOutputStream(fileName);
		workBook.write(fos);
		fos.close();
		System.out.println(fileName + " written successfully");
	}

}
