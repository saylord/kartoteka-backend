package kz.logitex.kartoteka.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import kz.logitex.kartoteka.ingoing.IngoingDTO;
import kz.logitex.kartoteka.util.StringModifier;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class ReportIngoingExportPdfRus {
    private final IngoingDTO ingoing;
    private final static String header1 = "Форма 39";
    private final static String header2 = "(к.п.п. 241, 310, 319, 359, 361)";
    private final static String title = "КАРТОЧКА №";
    private final static String title2 = "учета входящих документов";
    private final static String quantity = "Количество";
    private final static String[] firstTableHeaders = new String[]{
            "Дата регистрации",
            "Номер и дата поступившего документа",
            "Гриф секретности",
            "Откуда поступил документ",
            "экземпляров (копии) и их номера",
            "листов в экземпляре (копии)",
            "приложений в экземпляре (копии)",
            "листов основного документа",
            "листов приложения",
            "Номер экземпляра (копии)",
            "Кому передан документ (фамилия и инициалы)",
            "Подпись исполнителя в получении документа и дата",
            "Подпись работника подразделения по защите государственных секретов об обратном получении документа и дата",
            "Место нахождения документа (номер дела, тома, листов, номер и дата акта об уничтожении, номер реестра и дата)",
            "15. Отметка о проверке наличия документа"
    };
    private final static String description = "Вид и краткое содержание (без отражения секретных сведений): ";
    private final static String underline = "_______________________________________________________________________________________";
    private final static String movement = "Движение документа";

    @SneakyThrows
    public void generate(HttpServletResponse response) {
        var document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        var firstTable = new PdfPTable(1);
        firstTable.setWidthPercentage(100);
        var fontPath = "/fonts/TimesNewRoman.ttf";
        var baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        var fontBold12 = new Font(baseFont, 12, Font.BOLD, BaseColor.BLACK);
        var fontNormal12 = new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK);
        var fontBold14 = new Font(baseFont, 14, Font.BOLD, BaseColor.BLACK);

        var firstRow = new PdfPCell(new Paragraph(header1, fontBold12));
        firstRow.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        firstRow.setBorderWidth(0);
        firstRow.setBorder(Rectangle.BOTTOM); // Set the bottom border
        firstRow.setBorderColorBottom(BaseColor.BLACK); // Set the border color
        firstTable.addCell(firstRow);
        var secondRow = new PdfPCell(new Paragraph(header2, fontNormal12));
        secondRow.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        secondRow.setBorderWidth(0);
        firstTable.addCell(secondRow);
        document.add(firstTable);

        var centerParagraph = new Paragraph(title + ingoing.getCardNumber(), fontBold14);
        centerParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        centerParagraph.setSpacingBefore(0f);
        centerParagraph.setSpacingAfter(0f);
        document.add(centerParagraph);
        var centerParagraph2 = new Paragraph(title2, fontBold14);
        centerParagraph2.setAlignment(Paragraph.ALIGN_CENTER);
        centerParagraph2.setSpacingBefore(0f);
        centerParagraph2.setSpacingAfter(10f);
        document.add(centerParagraph2);

        // Table 1
        var secondTable = new PdfPTable(9);
        secondTable.setWidthPercentage(100);
        secondTable.setWidths(new float[] {1.5f, 1.5f, 1.0f, 1.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f});
        writeTableHeader(secondTable, fontNormal12);
        writeTableData(secondTable, fontNormal12);
        secondTable.setSpacingAfter(5f);
        document.add(secondTable);

        var paragraphDescription = new Paragraph(description + ingoing.getDescription(), fontNormal12);
        paragraphDescription.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraphDescription);
        var paragraphDescription2 = new Paragraph(underline, fontNormal12);
        paragraphDescription2.setAlignment(Paragraph.ALIGN_LEFT);
        paragraphDescription2.setSpacingAfter(20f);
        document.add(paragraphDescription2);

        // Table 2
        var thirdTable = new PdfPTable(5);
        thirdTable.setWidthPercentage(100);
        thirdTable.setWidths(new float[] {0.8f, 1.5f, 1.5f, 1.5f, 1.5f});
        writeTable2Header(thirdTable, fontNormal12);
        writeTable2Data(thirdTable, fontNormal12);
        document.add(thirdTable);

        // second page
        document.newPage();

        var fourthTable = new PdfPTable(1);
        fourthTable.setWidthPercentage(100);

        // Добавление пустых строк или ячеек перед таблицей
        var emptyCell = new PdfPCell(new Phrase(" "));
        emptyCell.setBorder(Rectangle.NO_BORDER);
        // Добавляем пустые ячейки
        for (var i = 0; i < 25; i++) {
            fourthTable.addCell(emptyCell);
        }

        var cellHeader = new PdfPCell();
        cellHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellHeader.setPhrase(new Phrase(firstTableHeaders[14], fontNormal12));
        fourthTable.addCell(cellHeader);
        cellHeader.setPhrase(new Phrase(" ", fontNormal12));
        fourthTable.addCell(cellHeader);
        fourthTable.addCell(cellHeader);
        fourthTable.addCell(cellHeader);
        document.add(fourthTable);

        var centerParagraph3 = new Paragraph(ingoing.getCardNumber(), fontBold14);
        centerParagraph3.setAlignment(Paragraph.ALIGN_CENTER);
        centerParagraph3.setSpacingBefore(10f);
        document.add(centerParagraph3);

        document.close();
    }

    private void writeTableHeader(PdfPTable table, Font font) {
        // Row 1: Header
        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (int i = 0; i < 4; i++) {
            cell.setPhrase(new Phrase(firstTableHeaders[i], font));
            table.addCell(cell);
        }

        // Nested table for Header 5-9
        var nestedTable = new PdfPTable(5);
        nestedTable.setWidthPercentage(100);
        var nestedCell = new PdfPCell(new Phrase(quantity, font));
        nestedCell.setColspan(5);
        nestedCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nestedTable.addCell(nestedCell);

        for (int i = 4; i < 9; i++) {
            var nestedTableCell = new PdfPCell(new Phrase(firstTableHeaders[i], font));
            nestedTableCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            nestedTable.addCell(nestedTableCell);
        }

        // Create a cell to contain the nested table
        var nestedTableCell = new PdfPCell(nestedTable);
        nestedTableCell.setColspan(5);
        table.addCell(nestedTableCell);

        // Row 2: Numeration
        for (int i = 1; i <= 9; i++) {
            cell.setPhrase(new Phrase(String.valueOf(i), font));
            table.addCell(cell);
        }
    }

    private void writeTableData(PdfPTable table, Font font) {
        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setPhrase(new Phrase(StringModifier.timestampToDate(ingoing.getCreatedTimestamp()), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(ingoing.getDocumentNumber() + "\n" + StringModifier.timestampToDate(ingoing.getDocumentTimestamp()), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(ingoing.getSecret().getName(), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(ingoing.getBuilding().getName(), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(ingoing.getExemplar(), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(ingoing.getTotalSheet()), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(new Phrase(String.valueOf(ingoing.getSchedule()), font)));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(ingoing.getSheet()), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(ingoing.getSchedule()), font));
        table.addCell(cell);
    }

    private void writeTable2Header(PdfPTable table, Font font) {
        // Row 1: Header
        var nested = new PdfPCell(new Phrase(movement, font));
        nested.setHorizontalAlignment(Element.ALIGN_CENTER);
        nested.setColspan(5);
        table.addCell(nested);

        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (int i = 9; i < 14; i++) {
            cell.setPhrase(new Phrase(firstTableHeaders[i], font));
            table.addCell(cell);
        }

        // Row 2: Numeration
        for (int i = 10; i <= 14; i++) {
            cell.setPhrase(new Phrase(String.valueOf(i), font));
            table.addCell(cell);
        }
    }

    private void writeTable2Data(PdfPTable table, Font font) {
        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setPhrase(new Phrase(ingoing.getExemplar(), font));
        table.addCell(cell);

        if (ingoing.getExecutor() != null) {
            cell.setPhrase(new Phrase(ingoing.getExecutor().getFio(), font));
            table.addCell(cell);
        } else {
            cell.setPhrase(new Phrase(" ", font));
            table.addCell(cell);
        }

        cell.setPhrase(new Phrase(" ", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(" ", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(ingoing.getCaseNumber(), font));
        table.addCell(cell);

        for (int i = 0; i < 25; i++) {
            cell.setPhrase(new Phrase(" ", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }
}
