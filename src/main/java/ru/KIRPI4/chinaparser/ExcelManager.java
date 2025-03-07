package ru.KIRPI4.chinaparser;

import org.dhatim.fastexcel.Workbook;
import ru.KIRPI4.chinaparser.http.models.ProductInfoModel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelManager {

    public static void saveProductsInfo(Set<ProductInfoModel> models, OutputStream outputStream) throws IOException {
        var workbook = new Workbook(outputStream, "ChinaParse", "1.0");
        var sheet = workbook.newWorksheet("Products info");

        sheet.range(0, 0, 0, 14).style().fontName("Arial").fontSize(12).bold().set();
        sheet.value(0, 0, "Name");
        sheet.value(0, 1, "Price");
        sheet.value(0, 2, "Minimum order quantity");
        sheet.value(0, 4, "Contact name");
        sheet.value(0, 5, "Contact phone");
        sheet.value(0, 6, "Contact email");
        sheet.value(0, 8, "Product code");
        sheet.value(0, 9, "Color");
        sheet.value(0, 10, "Model");
        sheet.value(0, 11, "Material");
        sheet.value(0, 12, "Place of shipment");
        sheet.value(0, 13, "place of origin");
        //sheet.value(0, 14, "Target markets");

        sheet.range(1, 0, models.size(), 14).style().fontName("Arial").fontSize(10).set();

        AtomicInteger index = new AtomicInteger();

        models.stream().forEachOrdered(model -> {
            var i = index.incrementAndGet();
            sheet.value(i, 0, model.name);
            sheet.value(i, 1, model.price);
            sheet.value(i, 2, model.minimumOrderQuantity);
            sheet.value(i, 4, model.contacts.name);
            sheet.value(i, 5, model.contacts.phone);
            sheet.value(i, 6, model.contacts.email);
            sheet.value(i, 8, model.specifications.productCode);
            sheet.value(i, 9, model.specifications.color);
            sheet.value(i, 10, model.specifications.model);
            sheet.value(i, 11, model.specifications.material);
            sheet.value(i, 12, model.specifications.placeOfShipment);
            sheet.value(i, 13, model.specifications.placeOfOrigin);
            //sheet.value(i, 14, model.targetMarkets);
        });

        workbook.finish();
        workbook.close();
    }
}
