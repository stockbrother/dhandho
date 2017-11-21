package cc.dhandho.importer;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import cc.dhandho.Quarter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WashedFileLoader extends FileReaderIterator {
    //

    private static final Logger LOG = LoggerFactory.getLogger(WashedFileLoader.class);

    private Map<String, WashedFileProcessor> processMap = new HashMap<>();

    protected int limit = -1;

    private int processed;

    private Quarter quarter;

    public WashedFileLoader(File dir, Quarter quarter) {
        super(dir);
        this.typeToUpperCase = true;
        this.quarter = quarter;
    }

    @Override
    protected void onReader(String type, File file, Reader freader, int number) {

        int rows = 0;
        CSVReader reader = new CSVReader(freader);
        try {
            CsvHeaderRowMap headers = new CsvHeaderRowMap();
            CsvRowMap body = new CsvRowMap();
            CsvRowMap currentMap = null;
            int lineNumber = 0;

            while (true) {
                lineNumber++;
                String[] next = reader.readNext();
                if (next == null) {
                    break;
                }
                if ("Header".equals(next[0])) {
                    currentMap = headers;
                    continue;
                } else if ("Body".equals(next[0])) {
                    currentMap = body;
                    continue;
                }
                // the name of the item.
                String key = next[0];
                key = key.trim();
                if (key.length() == 0 && next.length <= 1) {
                    // ignore this empty line.
                    continue;
                }
                currentMap.put(key, new CsvRow(lineNumber, next));
            }
            rows = onTableData(type,file,number,headers,body);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LOG.info(
                "processor:" + this.getClass().getName() + " have processed file:" + file.getAbsolutePath() + ",total rows:" + rows);
        if (this.limit >= 0 && this.processed++ > this.limit) {
            this.interrupted = true;
        }

    }

    protected int onTableData(String type,File file,int number,CsvHeaderRowMap headers,CsvRowMap body ){
        int rt = 0;
        if (headers.keyList.isEmpty()) {
            LOG.debug("ignore file(for reason of no header found):" + file.getAbsolutePath());
            return rt;
        }

        //
        Date[] reportDateArray = headers.getReportDateArray();
        BigDecimal unit = headers.get("单位", true).getAsBigDecimal(1, true);
        String corpId = headers.get("公司代码", true).getString(1, true);
        List<String> itemKeyList = body.keyList;

        // TODO make sure the itemKeyList is the same sequence with all
        // other body in the same sheet.

        for (int i = 0; i < reportDateArray.length; i++) {

            Date reportDate = headers.get("报告日期", true).getAsDate(i + 1, headers.getDateFormat());
            if (reportDate == null) {
                break;
            }
            //filter by quarter
            if (this.quarter != null && !this.quarter.isEndDateOfQuarter(reportDate)) {
                //ignore the date other than the required quarter.
                continue;
            }

            // one row:
            List<String> keyList = new ArrayList<>();
            List<BigDecimal> valueList = new ArrayList<>();

            for (String key : itemKeyList) {
                BigDecimal value = body.get(key, true).getAsBigDecimal(i + 1, false);
                if (value != null) {
                    value = value.multiply(unit);
                }
                keyList.add(key);
                valueList.add(value);
            }
            onRowData(type, corpId, reportDate, keyList, valueList);
            rt++;
        }
        return rt;
    }

    protected abstract void onRowData(String reportType, String corpId, Date reportDate, List<String> keyList, List<BigDecimal> valueList);

}
