
package com.medicisupply.reader;

import com.medicisupply.model.ProductRow;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.util.*;

public class ExcelReader {
    public List<ProductRow> read(File file) throws Exception {
        List<ProductRow> list = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            if (!it.hasNext()) return list;

            Row header = it.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : header) headers.add(cell.getStringCellValue().trim());

            while (it.hasNext()) {
                Row row = it.next();
                ProductRow pr = new ProductRow();
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i);
                    pr.setField(headers.get(i), cell != null ? cell.toString() : "");
                }
                list.add(pr);
            }
        }
        return list;
    }
}
