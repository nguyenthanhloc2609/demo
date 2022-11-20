package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.demo.dao.Finance;
import com.example.demo.dao.Spend;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.PagingDTO;
import com.example.demo.repository.FinanceRepository;
import com.example.demo.repository.SpendRepository;
import com.example.demo.repository.TransactionRepository;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FinanceServiceImpl implements IFinanceService {

    @Autowired
    FinanceRepository financeRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    SpendRepository spendRepository;

    FileOutputStream outputStream;
    SXSSFWorkbook workbook;

    @Override
    public Finance create(Finance finance) {
        return financeRepository.save(finance);
    }

    @Override
    public PagingDTO<Finance> findAll(Integer limit, Integer offset) {
        return null;
    }

    @Override
    public Finance update(Finance finance, String id) {
        if (finance.getId().equals(id)) {
            Finance f = financeRepository.findById(id).orElse(null);
            if (f != null) {
                // Finance
                return financeRepository.save(finance);
            }

        }
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Finance retrieve(String id) {
        return null;
    }

    @Override
    public Finance getFinanceOnDay(String date) {
        Finance finance = financeRepository.getFinanceByDate(date);
        if (finance == null)
            finance = create(new Finance(date));
        return finance;
    }

    @Override
    public File exportExcel(String date) {
        // get Transaction and Spend
        List<Transaction> trans = transactionRepository.getTransactionByDateEndingWithOrderByDateAsc(date.substring(3));

        List<Spend> spends = spendRepository.getSpendByDateEndingWithOrderByDateAsc(date.substring(3));

        List<Finance> finances = financeRepository.getFinanceByDateEndingWithOrderByDateAsc(date.substring(3));

        List<Integer> indexTran = new ArrayList<>();
        List<Integer> indexSpend = new ArrayList<>();
        indexTran.add(0);
        indexSpend.add(0);
        int idx1 = 0, idx2 = 0;
        for (int i = 0; i < finances.size(); i++) {
            idx1 += finances.get(i).getCountTran();
            indexTran.add(idx1);
            idx2 += finances.get(i).getCountSpend();
            indexSpend.add(idx2);
        }
        // generate Excel
        Logger.getLogger(FinanceServiceImpl.class.getName()).log(Level.INFO,
                "Delete old excel file: " + deleteExistExcel("PK"));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<Future<?>>();
        try {
            String fileName = "PK-T" + date.substring(3).replace("/", "");
            File excel = File.createTempFile(fileName, ".xlsx");
            FileOutputStream outputStream = new FileOutputStream(excel);
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            for (int day = 0; day < finances.size(); day++) {
                Finance fin = finances.get(day);
                String sheetName = fin.getDate().substring(0, 5).replace("/", "_");
                workbook.createSheet(sheetName);
            }

            for (int day = 1; day <= finances.size(); day++) {
                int tmp = day;
                Future<?> f = executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        // Finance preFin = finances.get(tmp - 1);
                        Finance fin = finances.get(tmp - 1);
                        SXSSFSheet sheet = workbook.getSheetAt(tmp - 1);
                        sheet.setRandomAccessWindowSize(50);
                        Logger.getLogger(FinanceServiceImpl.class.getName()).log(Level.INFO,
                                "get sheet: " + sheet.getSheetName());
                        int row = 0, col = 0;
                        sheet.addMergedRegion(new CellRangeAddress(row, row, 3, 6));
                        Cell headerCell = CellUtil.createCell(sheet.createRow((short) 0), 3,
                                "BẢNG THU CHI TRONG NGÀY " + fin.getDate());
                        headerCell.setCellValue("BẢNG THU CHI TRONG NGÀY " + fin.getDate());

                        CellUtil.setAlignment(headerCell, HorizontalAlignment.CENTER);
                        headerCell.setCellStyle(createStyle(workbook, 1));

                        sheet.createRow(++row);
                        // for transaction
                        Logger.getLogger(FinanceServiceImpl.class.getName()).log(Level.INFO, "create transaction data");
                        Row rowHead = sheet.createRow(++row);
                        Row rowHead1 = sheet.createRow(row + 1);
                        // stt
                        sheet.setColumnWidth(0, 8 * 256);
                        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
                        Cell sttCell = CellUtil.createCell(rowHead, col, "STT");
                        CellUtil.setAlignment(sttCell, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(sttCell, VerticalAlignment.CENTER);
                        sttCell.setCellStyle(createStyle(workbook, 2));
                        setBorderRange(row, row + 1, col, col, sheet);

                        // ho va ten
                        sheet.setColumnWidth(1, 30 * 256);
                        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, ++col, col));
                        Cell nameCell = CellUtil.createCell(rowHead, col, "HỌ VÀ TÊN");
                        CellUtil.setAlignment(nameCell, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(nameCell, VerticalAlignment.CENTER);
                        nameCell.setCellStyle(createStyle(workbook, 2));
                        setBorderRange(row, row + 1, col, col, sheet);

                        // chan doan
                        sheet.setColumnWidth(2, 20 * 256);
                        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, ++col, col));
                        Cell diagCell = CellUtil.createCell(rowHead, col, "CHẨN ĐOÁN BỆNH");
                        CellUtil.setAlignment(diagCell, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(diagCell, VerticalAlignment.CENTER);
                        diagCell.setCellStyle(createStyle(workbook, 2));
                        setBorderRange(row, row + 1, col, col, sheet);
                        // thu thuat
                        sheet.setColumnWidth(3, 20 * 256);
                        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, ++col, col));
                        Cell proCell = CellUtil.createCell(rowHead, col, "THỦ THUẬT");
                        CellUtil.setAlignment(proCell, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(proCell, VerticalAlignment.CENTER);
                        proCell.setCellStyle(createStyle(workbook, 2));
                        setBorderRange(row, row + 1, col, col, sheet);
                        // thuoc
                        sheet.setColumnWidth(4, 20 * 256);
                        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, ++col, col));
                        Cell medicineCell = CellUtil.createCell(rowHead, col, "THUỐC");
                        CellUtil.setAlignment(medicineCell, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(medicineCell, VerticalAlignment.CENTER);
                        medicineCell.setCellStyle(createStyle(workbook, 2));
                        setBorderRange(row, row + 1, col, col, sheet);

                        // thu
                        sheet.addMergedRegion(new CellRangeAddress(row, row, ++col, col + 1));
                        Cell moneyCell = CellUtil.createCell(rowHead, col++, "THU");
                        CellUtil.setAlignment(moneyCell, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(moneyCell, VerticalAlignment.CENTER);
                        moneyCell.setCellStyle(createStyle(workbook, 2));
                        setBorderRange(row, row, col, col + 1, sheet);

                        // Thanh toan
                        sheet.addMergedRegion(new CellRangeAddress(row, row, ++col, col + 1));
                        Cell incomeCell = CellUtil.createCell(rowHead, col++, "THANH TOÁN");
                        CellUtil.setAlignment(incomeCell, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(incomeCell, VerticalAlignment.CENTER);
                        incomeCell.setCellStyle(createStyle(workbook, 2));
                        setBorderRange(row, row, col, col + 1, sheet);

                        // Ghi chu
                        sheet.setColumnWidth(9, 10 * 256);
                        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, ++col, col));
                        Cell noteCell = CellUtil.createCell(rowHead, col, "GHI CHÚ");
                        CellUtil.setAlignment(noteCell, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(noteCell, VerticalAlignment.CENTER);
                        noteCell.setCellStyle(createStyle(workbook, 2));
                        setBorderRange(row, row + 1, col, col, sheet);

                        col = 5;
                        // Thu: thu thuat
                        sheet.setColumnWidth(5, 15 * 256);
                        Cell inProcedure = CellUtil.createCell(rowHead1, col++, "THỦ THUẬT");
                        CellUtil.setAlignment(inProcedure, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(inProcedure, VerticalAlignment.CENTER);
                        inProcedure.setCellStyle(createStyle(workbook, 2));

                        // Thu: thuoc
                        sheet.setColumnWidth(6, 15 * 256);
                        Cell inMedicine = CellUtil.createCell(rowHead1, col++, "THUỐC");
                        CellUtil.setAlignment(inMedicine, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(inMedicine, VerticalAlignment.CENTER);
                        inMedicine.setCellStyle(createStyle(workbook, 2));
                        // Thanh toan: truoc
                        sheet.setColumnWidth(7, 15 * 256);
                        Cell prepaid = CellUtil.createCell(rowHead1, col++, "TRƯỚC");
                        CellUtil.setAlignment(prepaid, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(prepaid, VerticalAlignment.CENTER);
                        prepaid.setCellStyle(createStyle(workbook, 2));
                        // Thanh toan: sau
                        sheet.setColumnWidth(8, 15 * 256);
                        Cell postpaid = CellUtil.createCell(rowHead1, col++, "SAU");
                        CellUtil.setAlignment(postpaid, HorizontalAlignment.CENTER);
                        CellUtil.setVerticalAlignment(postpaid, VerticalAlignment.CENTER);
                        postpaid.setCellStyle(createStyle(workbook, 2));
                        row += 2;
                        Integer proMoney = 0, mecMoney = 0;
                        for (int i = indexTran.get(tmp - 1); i < indexTran.get(tmp); i++) {
                            // Logger.getLogger(FinanceServiceImpl.class.getName()).log(Level.INFO, i + "");
                            Row r = sheet.createRow(row);
                            col = 0;
                            r.createCell(col++).setCellValue(row++ - 3);
                            r.createCell(col++).setCellValue(trans.get(i).getCustomerName());
                            r.createCell(col++).setCellValue(trans.get(i).getDiagnostic());
                            r.createCell(col++).setCellValue(trans.get(i).getListProcedure());
                            r.createCell(col++).setCellValue(trans.get(i).getMedicine());
                            r.createCell(col++)
                                    .setCellValue(String.format("%,d", trans.get(i).getProceMoney()).replace(",", "."));
                            r.createCell(col++)
                                    .setCellValue(
                                            String.format("%,d", trans.get(i).getMedicineMoney()).replace(",", "."));
                            r.createCell(col++).setCellValue(trans.get(i).getPrepaid());
                            r.createCell(col++).setCellValue(trans.get(i).getDebt());
                            r.createCell(col++).setCellValue(trans.get(i).getNote());
                            proMoney += trans.get(i).getProceMoney();
                            mecMoney += trans.get(i).getMedicineMoney();
                        }

                        Row money = sheet.createRow(++row);
                        CellStyle styleMoney = createStyle(workbook, 4);
                        for (int c = 0; c < col; c++) {
                            money.createCell(c).setCellStyle(styleMoney);
                        }
                        sheet.addMergedRegion(new CellRangeAddress(row, row, 1, 2));
                        money.getCell(1).setCellValue("Thu");
                        money.getCell(5).setCellValue(String.format("%,d", proMoney).replace(",", "."));
                        money.getCell(6).setCellValue(String.format("%,d", mecMoney).replace(",", "."));

                        Row totalTran = sheet.createRow(++row);
                        CellStyle styleTran = createStyle(workbook, 4);
                        for (int c = 0; c < col; c++) {
                            totalTran.createCell(c).setCellStyle(styleTran);
                        }
                        sheet.addMergedRegion(new CellRangeAddress(row, row, 1, 2));
                        sheet.addMergedRegion(new CellRangeAddress(row, row, 5, 6));
                        totalTran.getCell(1).setCellValue("Tổng thu");
                        totalTran.getCell(5)
                                .setCellValue(String.format("%,d", (proMoney + mecMoney)).replace(",", "."));

                        setBorderRange(4, row, 0, 9, sheet);

                        // for spend
                        Logger.getLogger(FinanceServiceImpl.class.getName()).log(Level.INFO, "create spend data");
                        row++;
                        col = 0;
                        Row rowHeadSpend = sheet.createRow(++row);
                        rowHeadSpend.setHeightInPoints((2 * sheet.getDefaultRowHeightInPoints()));
                        // stt
                        rowHeadSpend.createCell(col).setCellValue("STT");
                        rowHeadSpend.getCell(col++).setCellStyle(createStyle(workbook, 2));

                        // ho va ten
                        rowHeadSpend.createCell(col).setCellValue("HỌ VÀ TÊN NGƯỜI NHẬN");
                        rowHeadSpend.getCell(col++).setCellStyle(createStyle(workbook, 2));
                        // dien giai chi
                        rowHeadSpend.createCell(col).setCellValue("DIỄN GIẢI CHI");
                        rowHeadSpend.getCell(col++).setCellStyle(createStyle(workbook, 2));
                        // so tien
                        rowHeadSpend.createCell(col).setCellValue("SỐ TIỀN");
                        rowHeadSpend.getCell(col++).setCellStyle(createStyle(workbook, 2));
                        // ghi chu
                        rowHeadSpend.createCell(col).setCellValue("GHI CHÚ");
                        rowHeadSpend.getCell(col++).setCellStyle(createStyle(workbook, 2));

                        int spendTotal = 0;
                        int sRow = ++row;
                        for (int i = indexSpend.get(tmp - 1); i < indexSpend.get(tmp) + fin.getCountSpend(); i++) {
                            Row r = sheet.createRow(row++);
                            col = 0;
                            r.createCell(col++).setCellValue(i + 1 - indexSpend.get(tmp - 1));
                            r.createCell(col++).setCellValue(spends.get(i).getName());
                            r.createCell(col++).setCellValue(spends.get(i).getDetail());
                            r.createCell(col++)
                                    .setCellValue(String.format("%,d", spends.get(i).getMoney()).replace(",", "."));
                            spendTotal += spends.get(i).getMoney();
                            r.createCell(col++).setCellValue(trans.get(i).getNote());
                            proMoney += trans.get(i).getProceMoney();
                            mecMoney += trans.get(i).getMedicineMoney();
                        }

                        Row rSpendTotal = sheet.createRow(row);
                        CellStyle styleSpend = createStyle(workbook, 4);
                        for (int c = 0; c < col; c++) {
                            rSpendTotal.createCell(c).setCellStyle(styleSpend);
                        }
                        sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 2));
                        setBorderRange(sRow, row, 0, 4, sheet);

                        rSpendTotal.getCell(0).setCellValue("Tổng chi");
                        rSpendTotal.getCell(3).setCellValue(String.format("%,d", spendTotal).replace(",", "."));

                        row += 2;
                        Row rFinal = sheet.createRow(row);

                        CellStyle styleTotal = createStyle(workbook, 3);
                        for (int c = 0; c < col; c++) {
                            rFinal.createCell(c).setCellStyle(styleTotal);
                        }

                        sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 1));
                        sheet.addMergedRegion(new CellRangeAddress(row, row, 2, 4));

                        rFinal.getCell(0).setCellValue("Tồn quỹ cuối ngày");
                        rFinal.getCell(2).setCellValue(
                                String.format("%,d", (proMoney + mecMoney - spendTotal)).replace(",", "."));
                    }

                });

                futures.add(f);

            }
            executorService.shutdown();
            // A) Await all runnables to be done (blocking)
            for (Future<?> future : futures)
                Logger.getLogger(FinanceServiceImpl.class.getName()).log(Level.INFO, String.valueOf(future.get()));
            // get will block until the future is done

            // write data
            try {

                workbook.write(outputStream);

                outputStream.flush();

            } catch (Exception e) {
                Logger.getLogger(FinanceServiceImpl.class.getName()).log(Level.SEVERE, e.getMessage());
                e.printStackTrace();
            } finally {
                Logger.getLogger(FinanceServiceImpl.class.getName()).log(Level.INFO, "Close!");
                outputStream.close();
                workbook.dispose();
            }

            return excel;
            // }
        } catch (Exception e) {
            Logger.getLogger(FinanceServiceImpl.class.getName()).log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        } finally {

        }

        return null;
    }

    private boolean deleteExistExcel(String prefix) {
        boolean deleted = false;
        String path = System.getProperty("java.io.tmpdir");
        File directory = new File(path);
        if (directory.exists()) {
            File[] listFile = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.startsWith(prefix)) {
                        return true;
                    }
                    return false;
                }
            });
            for (File f : listFile) {
                deleted = f.delete();
            }
        }
        return deleted;
    }

    private void setBorderRange(int row1, int row2, int col1, int col2, SXSSFSheet sheet) {
        CellRangeAddress region = new CellRangeAddress(row1, row2, col1, col2);
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
    }

    private CellStyle createStyle(Workbook workbook, Integer type) {
        CellStyle style = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        if (type == 1) {
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 14);
        } else if (type == 2) {
            headerFont.setBold(true);
            style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE1.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerFont.setFontHeightInPoints((short) 12);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        } else if (type == 3) {
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        } else if (type == 4) {
            headerFont.setBold(false);
            headerFont.setFontHeightInPoints((short) 11);
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
        }

        style.setFont(headerFont);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }
}
