package com.medicisupply.reader;

import com.medicisupply.model.ProductRow;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.*;

@Log4j2
public class ExcelReader {

    public List<ProductRow> read(File file) throws Exception {
        List<ProductRow> list = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            if (!it.hasNext()) return list;

            Row header = it.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : header) {
                headers.add(cell.getStringCellValue().trim());
            }

            while (it.hasNext()) {
                Row row = it.next();
                if (rowIsEmpty(row)) continue;

                ProductRow pr = new ProductRow();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i);
                    String value = (cell != null) ? cell.toString().trim() : "";
                    pr.setField(headers.get(i), value);
                }
                list.add(pr);
            }
        }
        log.info("Non-empty rows read from Excel: {}", list.size());
        return list;
    }

    private boolean rowIsEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null &&
                    cell.getCellType() != CellType.BLANK &&
                    cell.getCellType() != CellType._NONE &&
                    !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
