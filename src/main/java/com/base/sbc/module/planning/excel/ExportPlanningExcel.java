package com.base.sbc.module.planning.excel;

import com.base.sbc.client.ccm.entity.BasicBaseDict;
import com.base.sbc.client.ccm.entity.BasicStructureTree;
import com.base.sbc.module.band.entity.Band;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExportPlanningExcel {
    /**
     * 工作薄对象
     */
    private XSSFWorkbook objWb = null;

    /**
     * 初始化字体和样式
     *
     * @throws IOException
     */
    private void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("excelTemp/planningTempInit-test.xlsx");
        objWb = new XSSFWorkbook(resource.getInputStream());
    }

//    /**
//     *
//     * @param monthBandMap 月份波段Map
//     * @param categoryList 品类集合
//     * @param centreList 中类集合
//     * @param smallList 小类集合
//     * @return
//     * @throws Exception
//     */
//    public XSSFWorkbook createWorkBook(Map<String, List<Band>> monthBandMap, List<BasicStructureTree> categoryList, List<BasicStructureTree> centreList,
//                                       List<BasicStructureTree> smallList) throws Exception {
//        // 初始化字体和格式
//        init();
//
//        XSSFSheet monthBand = this.objWb.getSheetAt(1);//月份波段 sheet
//        XSSFSheet category = this.objWb.getSheetAt(2);//品类 sheet
//        XSSFSheet center = this.objWb.getSheetAt(3);//中类 sheet
//        XSSFSheet small = this.objWb.getSheetAt(4);//小类 sheet
//
//        int k = 0;
//        for (Map.Entry<String, List<Band>> entry : monthBandMap.entrySet()) {
//            for(Band band : entry.getValue()) {
//                monthBand.createRow(k).createCell(0).setCellValue(entry.getKey() + "-" + band.getBandName());
//                k++;
//            }
//        }
//        for (int i = 0; i < categoryList.size(); i++) {
//            category.createRow(i).createCell(0).setCellValue(categoryList.get(i).getName());
//        }
//        for (int i = 0; i < centreList.size(); i++) {
//            center.createRow(i).createCell(0).setCellValue(centreList.get(i).getName());
//        }
//        for (int i = 0; i < smallList.size(); i++) {
//            small.createRow(i).createCell(0).setCellValue(smallList.get(i).getName());
//        }
//
//        // 返回创建好的工作薄对象
//        return this.objWb;
//    }

    /**
     *
     * @param bandList 波段集合
     * @param monthList 月份集合
     * @return
     * @throws Exception
     */
    public XSSFWorkbook createWorkBook(List<Band> bandList, List<BasicBaseDict> monthList, List<BasicBaseDict> seriesList) throws Exception {
        // 初始化字体和格式
        init();

        XSSFSheet monthSheet = this.objWb.getSheetAt(3);//月份 sheet
        XSSFSheet bandSheet = this.objWb.getSheetAt(4);//波段 sheet
        XSSFSheet seriesSheet = this.objWb.getSheetAt(5);//系列 sheet

        int k = 0;
        for(BasicBaseDict month : monthList) {
            monthSheet.createRow(k).createCell(0).setCellValue(month.getName());
            k++;
        }
        for(Band band : bandList) {
            bandSheet.createRow(k).createCell(0).setCellValue(band.getBandName());
            k++;
        }
        for(BasicBaseDict series : seriesList) {
            seriesSheet.createRow(k).createCell(0).setCellValue(series.getName());
            k++;
        }

        // 返回创建好的工作薄对象
        return this.objWb;
    }
}
