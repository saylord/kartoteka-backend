package kz.logitex.kartoteka.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import kz.logitex.kartoteka.outgoing.OutgoingDTO;
import kz.logitex.kartoteka.util.StringModifier;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class ReportOutgoingExportPdf {
    private final OutgoingDTO outgoing;
    private final static String title = "КАРТОЧКА №";
    private final static String title2 = "учета рукописных, отпечатанных, исходящих, оставшихся на хранение особой важности, совершенно секретных и секретных документов";
    private final static String header1 = "Дата отправки: ";
    private final static String header2 = "Исх № ";
    private final static String[] firstTableHeaders = new String[]{
            "1. Дата регистрации",
            "2. Гриф секретности",
            "3. Количество отпечатанных экземпляров документа",
            "4. Количество листов в самом экземпляре",
            "5. Индекс отдела",
            "6. Фамилия исполнителя, № листов черновика", // непонятно
            "7. Расписка исполнителя",
            "8. № дела, том, лист, где подшит документ", // непонятно
            "9. Краткое содержание, вид документа",
            "Кому направлено",
            "Номер экз.",
            "Кол-во листов экз.",
            "Всего листов",
            "Приложение",
            "11. Номер экземпляра документа, дата, номер реестр или Экз. №", // непонятно
            "12. Отметка об уничтожении (номер экземпляра, количество листов, номер и дата акта об уничтожении, расписка ПЗГС об уничтожении)",
            "13. Расписка работника ОЗГС в получении документа от исполнителя, дела, номера экземпляров",
            "14. Расписка работника ОЗГС в получении документа для отправки дата, номера экземпляров",
            "15. Расписка исполнителя в получении документа после отправки дата, номера экземпляров"
    };
    private final static String underline = "_______________________________________________________________________________________";

    @SneakyThrows
    public void generate(HttpServletResponse response) {
        var document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        var fontPath = "/fonts/TimesNewRoman.ttf";
        var baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        var fontBold12 = new Font(baseFont, 12, Font.BOLD, BaseColor.BLACK);
        var fontNormal12 = new Font(baseFont, 12, Font.NORMAL, BaseColor.BLACK);
        var fontBold14 = new Font(baseFont, 14, Font.BOLD, BaseColor.BLACK);

        var centerParagraph = new Paragraph(title + outgoing.getId() + " " + outgoing.getSecret().getName(), fontBold14);
        centerParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        centerParagraph.setSpacingBefore(0f);
        centerParagraph.setSpacingAfter(0f);
        document.add(centerParagraph);
        var centerParagraph2 = new Paragraph(title2, fontBold12);
        centerParagraph2.setAlignment(Paragraph.ALIGN_CENTER);
        centerParagraph2.setSpacingBefore(0f);
        centerParagraph2.setSpacingAfter(10f);
        document.add(centerParagraph2);

        var firstTable = new PdfPTable(2);
        firstTable.setWidthPercentage(100);
        firstTable.setSpacingAfter(10f);
        var leftCell = new PdfPCell(new Paragraph(header1 + StringModifier.timestampToDate(outgoing.getSendingTimestamp()), fontNormal12));
        leftCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        leftCell.setBorderWidth(0);
        var rightCell = new PdfPCell(new Paragraph(header2 + outgoing.getDocumentNumber(), fontNormal12));
        rightCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        rightCell.setBorderWidth(0);
        firstTable.addCell(leftCell);
        firstTable.addCell(rightCell);
        document.add(firstTable);

        var secondTable = new PdfPTable(8);
        secondTable.setWidthPercentage(100);
        secondTable.setWidths(new float[] {1.0f, 0.5f, 1.0f, 1.0f, 0.5f, 2.0f, 1.5f, 1.5f});
        writeTableHeader(secondTable, fontNormal12);
        writeTableData(secondTable, fontNormal12);
        document.add(secondTable);

        var thirdTable = new PdfPTable(6);
        thirdTable.setWidthPercentage(100);
        thirdTable.setWidths(new float[] {3.5f, 2.1f, 0.4f, 1.5f, 0.75f, 0.75f});
        writeTable2Header(thirdTable, fontNormal12);
        writeTable2Data(thirdTable, fontNormal12);
        document.add(thirdTable);

        var paragraph11 = new Paragraph(firstTableHeaders[14], fontNormal12);
        paragraph11.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraph11);
        var paragraph11_2 = new Paragraph(underline, fontNormal12);
        paragraph11_2.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph11_2.setSpacingAfter(5f);
        document.add(paragraph11_2);

        var paragraph12 = new Paragraph(firstTableHeaders[15], fontNormal12);
        paragraph12.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(paragraph12);
        var paragraph12_2 = new Paragraph(underline, fontNormal12);
        paragraph12_2.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph12_2.setSpacingAfter(5f);
        document.add(paragraph12_2);

        document.newPage();

        var fourthTable = new PdfPTable(3);
        fourthTable.setWidthPercentage(100);
        writeTable3Header(fourthTable, fontNormal12);
        writeTable3Data(fourthTable, fontNormal12);
        document.add(fourthTable);

        document.close();
    }

    private void writeTableHeader(PdfPTable table, Font font) {
        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (int i = 0; i < 8; i++) {
            cell.setPhrase(new Phrase(firstTableHeaders[i], font));
            table.addCell(cell);
        }
    }

    private void writeTableData(PdfPTable table, Font font) {
        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setPhrase(new Phrase(StringModifier.timestampToDate(outgoing.getCreatedTimestamp()), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(outgoing.getSecret().getName(), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(outgoing.getDocCopyPrint()), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(outgoing.getDocCopyPrint()), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(outgoing.getDocDepartmentIndex()), font));
        table.addCell(cell);

        if (outgoing.getExecutor() != null) {
            cell.setPhrase(new Phrase(outgoing.getExecutor().getFio(), font));
            table.addCell(cell);
        } else {
            cell.setPhrase(new Phrase(" ", font));
            table.addCell(cell);
        }

        cell.setPhrase(new Phrase(" ", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(" ", font));
        table.addCell(cell);
    }

    private void writeTable2Header(PdfPTable table, Font font) {
        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (int i = 8; i < 14; i++) {
            cell.setPhrase(new Phrase(firstTableHeaders[i], font));
            table.addCell(cell);
        }
    }

    private void writeTable2Data(PdfPTable table, Font font) {
        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setPhrase(new Phrase(outgoing.getDescription(), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(outgoing.getBuilding().getName(), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(outgoing.getExemplar(), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(outgoing.getCopySheet()), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(outgoing.getSheet()), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(String.valueOf(outgoing.getSchedule()), font));
        table.addCell(cell);

        for (int i = 0; i < 54; i++) {
            if (i == 49) {
                cell.setPhrase(new Phrase("Всего: ", font));
                table.addCell(cell);
                continue;
            }
            cell.setPhrase(new Phrase(" ", font));
            table.addCell(cell);
        }
    }

    private void writeTable3Header(PdfPTable table, Font font) {
        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        for (int i = 16; i < 19; i++) {
            cell.setPhrase(new Phrase(firstTableHeaders[i], font));
            table.addCell(cell);
        }
    }

    private void writeTable3Data(PdfPTable table, Font font) {
        var cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        for (int i = 0; i < 15; i++) {
            cell.setPhrase(new Phrase(" ", font));
            table.addCell(cell);
        }
    }
}
