package com.base.sbc.module.pack.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.base.sbc.config.common.base.BaseGlobal;
import com.base.sbc.config.exception.OtherException;
import com.base.sbc.config.generator.utils.UtilFreemarker;
import com.base.sbc.module.pack.dto.PackTechAttachmentVo;
import com.base.sbc.module.pack.vo.PackSizeVo;
import com.base.sbc.module.pack.vo.PackTechSpecVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.layout.HtmlPageBreak;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：
 *
 * @author lixianglin
 * @version 1.0
 * @address com.base.sbc.module.pack.utils.GenTechSpecPdfFile
 * @email li_xianglin@126.com
 * @date 创建时间：2023-08-19 13:33
 */
@Data
public class GenTechSpecPdfFile {


    @ApiModelProperty(value = "企业名称")
    private String companyName;
    @ApiModelProperty(value = "尺码")
    private String productSizes;

    @ApiModelProperty(value = "洗后尺码标识")
    private String washSkippingFlag;

    @ApiModelProperty(value = "品牌")
    private String brandName;
    @ApiModelProperty(value = "图片")
    private String stylePic;
    @ApiModelProperty(value = "设计款号")
    private String designNo;
    @ApiModelProperty(value = "大货款号")
    private String styleNo;
    @ApiModelProperty(value = "版师名称")
    private String patternDesignName;

    @ApiModelProperty(value = "品类名称")
    private String prodCategoryName;
    @ApiModelProperty(value = "成分信息")
    private String composition;

    @ApiModelProperty(value = "执行标准")
    private String executeStandardCode;

    @ApiModelProperty(value = "外辅工艺")
    private String extAuxiliaryTechnics;

    @ApiModelProperty(value = "质量等级")
    private String qualityGrade;
    @ApiModelProperty(value = "注意事项")
    private String mattersAttention;
    @ApiModelProperty(value = "安全标题")
    private String saftyTitle;
    @ApiModelProperty(value = "洗唛材质备注")
    private String washingMaterialRemarks;
    @ApiModelProperty(value = "安全类别")
    private String saftyType;
    @ApiModelProperty(value = "充绒量")
    private String downContent;

    @ApiModelProperty(value = "包装形式")
    private String packagingForm;
    @ApiModelProperty(value = "特殊规格")
    private String specialSpec;

    @ApiModelProperty(value = "包装袋标准")
    private String packagingBagStandard;
    @ApiModelProperty(value = "面料详情")
    private String fabricDetails;
    @ApiModelProperty(value = "工艺师名称")
    private String technologistName;
    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "放码师名称")
    private String gradingName;
    @ApiModelProperty(value = "号型类型名称")
    private String sizeRangeName;

    @ApiModelProperty(value = "样衣工名称")
    private String sampleMakerName;

    @ApiModelProperty(value = "模板部件")
    private String templatePart;

    @ApiModelProperty(value = "设计师")
    private String designer;

    @ApiModelProperty(value = "温馨提示")
    private String warmTips;
    @ApiModelProperty(value = "贮藏要求")
    private String storageDemand;

    @ApiModelProperty(value = "产地")
    private String producer;

    @ApiModelProperty(value = "生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date produceDate;
    private String produceDateStr;
    @ApiModelProperty(value = "洗标")
    private String washingLabel;
    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date placeOrderDate;
    private String placeOrderDateStr;
    private String createDate;
    private String createTime;
    @ApiModelProperty(value = "工艺信息")
    private List<PackTechSpecVo> techSpecVoList;


    @ApiModelProperty(value = "图片")
    private List<PackTechAttachmentVo> picList;

    @ApiModelProperty(value = "尺寸表")
    private List<PackSizeVo> sizeList;

    public ByteArrayOutputStream gen() {
        try {
            this.placeOrderDateStr = DateUtil.format(placeOrderDate, DatePattern.NORM_DATETIME_PATTERN);
            this.produceDateStr = DateUtil.format(produceDate, DatePattern.NORM_DATETIME_PATTERN);
            Date newDate = new Date();
            this.createDate = DateUtil.format(newDate, DatePattern.NORM_DATE_PATTERN);
            this.createTime = DateUtil.format(newDate, "a HH:mm");

            List<String> sizeList = StrUtil.split(this.getProductSizes(), CharUtil.COMMA);
            boolean washSkippingFlag = StrUtil.equals(this.getWashSkippingFlag(), BaseGlobal.YES);
            Configuration config = new Configuration();
            config.setDefaultEncoding("UTF-8");
            config.setTemplateLoader(new ClassTemplateLoader(UtilFreemarker.class, "/"));
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            Template template = config.getTemplate("ftl/process.html.ftl");

            String str = JSON.toJSONString(this, JSONWriter.Feature.WriteNullStringAsEmpty);

            JSONObject dataModel = JSON.parseObject(str);
            dataModel.put("sizeList", sizeList);
            int sizeColspan = washSkippingFlag ? 3 : 2;
            dataModel.put("sizeColspan", sizeColspan);
            dataModel.put("sizeTitleColspan", sizeColspan * CollUtil.size(sizeList) + 4);
            dataModel.put("washSkippingFlag", washSkippingFlag);
            //处理尺码数据
            List<List<Object>> dataList = new ArrayList<>(16);
            if (CollUtil.isNotEmpty(this.getSizeList())) {
                for (PackSizeVo packSize : this.getSizeList()) {
                    List<Object> row = new ArrayList<>();
                    row.add(Opt.ofNullable(packSize.getPartName()).orElse(""));
                    row.add(Opt.ofNullable(packSize.getMethod()).orElse(""));
                    JSONObject jsonObject = JSONObject.parseObject(packSize.getStandard());
                    for (String size : sizeList) {
                        row.add(MapUtil.getStr(jsonObject, "template" + size, "-"));
                        row.add(MapUtil.getStr(jsonObject, "garment" + size, "-"));
                        if (washSkippingFlag) {
                            row.add(MapUtil.getStr(jsonObject, "washing" + size, "-"));
                        }

                    }
                    row.add(StrUtil.isBlank(packSize.getCodeError()) ? "-" : StrUtil.DASHED + packSize.getCodeError());
                    row.add(Opt.ofNullable(packSize.getCodeError()).orElse(""));
                    dataList.add(row);
                }
            }
            Map<String, List<PackTechAttachmentVo>> picMap = new HashMap<>(16);
            if (CollUtil.isNotEmpty(this.getPicList())) {
                picMap = this.getPicList().stream().collect(Collectors.groupingBy(PackTechAttachmentVo::getSpecType));

            }
            Map<String, List<PackTechSpecVo>> gyMap = new HashMap<>(16);
            if (CollUtil.isNotEmpty(this.getTechSpecVoList())) {
//                gyMap = this.getTechSpecVoList().stream().collect(Collectors.groupingBy(PackTechSpecVo::getSpecType));
                gyMap = JSON.parseArray(JSON.toJSONString(this.getTechSpecVoList(), JSONWriter.Feature.WriteNullStringAsEmpty))
                        .toJavaList(PackTechSpecVo.class)
                        .stream()
                        .collect(Collectors.groupingBy(PackTechSpecVo::getSpecType));
            }
            dataModel.put("sizeDataList", dataList);
            dataModel.put("ztbzDataList", Optional.ofNullable(gyMap.get("整烫包装")).orElse(CollUtil.newArrayList()));
            dataModel.put("cjgyDataList", Optional.ofNullable(gyMap.get("裁剪工艺")).orElse(CollUtil.newArrayList()));
            dataModel.put("cjgyImgList", Optional.ofNullable(picMap.get("裁剪工艺")).orElse(CollUtil.newArrayList()));
            List<PackTechSpecVo> xbjDataList = Optional.ofNullable(gyMap.get("小部件")).orElse(CollUtil.newArrayList());
            dataModel.put("xbjDataList", xbjDataList);
            dataModel.put("xbjImgList", Optional.ofNullable(picMap.get("小部件")).orElse(CollUtil.newArrayList()));
            dataModel.put("xbjRowsPan", xbjDataList.size() + 1);

            List<PackTechSpecVo> jcgyDataList = Optional.ofNullable(gyMap.get("基础工艺")).orElse(CollUtil.newArrayList());
            dataModel.put("jcgyDataList", jcgyDataList);
            dataModel.put("jcgyImgList", Optional.ofNullable(picMap.get("基础工艺")).orElse(CollUtil.newArrayList()));
            dataModel.put("jcgyRowsPan", jcgyDataList.size());

            dataModel.put("zysxImgList", Optional.ofNullable(picMap.get("注意事项")).orElse(CollUtil.newArrayList()));
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            String output = writer.toString();
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
//            FileUtil.writeString(output, new File("d://process.html"), Charset.defaultCharset());
//            FileOutputStream fos=new FileOutputStream("D://htmltoPdf.pdf");
            // 创建PDF写入器
            ConverterProperties props = new ConverterProperties();
            props.setCharset("UFT-8");
            FontProvider provider = new DefaultFontProvider(true, true, true);
            props.setFontProvider(provider);
            List<IElement> elements = HtmlConverter.convertToElements(output, props);
            PdfWriter writer1 = new PdfWriter(pdfOutputStream);
            IElement pageStart = CollUtil.getFirst(elements);
            PdfDocument pdfDocument = new PdfDocument(writer1);
            Document document = new Document(pdfDocument, PageSize.A4.rotate(), false);

            PageFootEventHandler event = new PageFootEventHandler(pdfDocument, document, pageStart);
            pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE, event);
            // 定义页眉
            document.setMargins(40, 10, 0, 10);
            for (int i = 1; i < elements.size(); i++) {
                IElement element = elements.get(i);
                // 分页符
                if (element instanceof HtmlPageBreak) {
                    document.add((HtmlPageBreak) element);
                    //普通块级元素
                } else {
                    document.add((IBlockElement) element);
                }
            }


            // 设置页眉中的总页数
            int numberOfPages = pdfDocument.getNumberOfPages();
            for (int i = 1; i <= numberOfPages; i++) {
                Paragraph pageNumber = new Paragraph(String.format(" %d  /  %d ", i, numberOfPages));
                pageNumber.setTextAlignment(TextAlignment.CENTER);
                float x = pdfDocument.getDefaultPageSize().getWidth() - 20;
                // 距离底部的距离
                float y = pdfDocument.getDefaultPageSize().getTop() - 15;
                document.showTextAligned(pageNumber, x, y, i, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            }

            document.close();
            return pdfOutputStream;
        } catch (Exception e) {
            throw new OtherException("生成工艺单失败:" + e.getMessage());
        }


    }

    class PageFootEventHandler implements IEventHandler {
        private PdfDocument pdfDocument;
        private Document document;
        private IElement element;

        public PageFootEventHandler(PdfDocument pdfDocument, Document document, IElement element) {
            this.pdfDocument = pdfDocument;
            this.document = document;
            this.element = element;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            //通过 page 进行一些处理  ，这个需要去了解如何在page上进行添加内容
            //也可以传入  List<IElement> iElements ，直接添加 ，
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page);
            float pageWith = pageSize.getWidth();
            float footHeight = 30;
            float marginBottom = pageSize.getHeight() - footHeight - 10;

            Rectangle rectangle = new Rectangle(0, marginBottom, pageWith, footHeight);
            Canvas canvas = new Canvas(pdfCanvas, pdfDocument, rectangle);
            canvas.add((IBlockElement) element);
            pdfCanvas.release();
        }
    }
}
