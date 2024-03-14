import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;

/**
 *
 * @author Jose
 */
public class CrearPdf {
     public static void generatePDF(String filename, String content) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            document.add(new Paragraph(content));
            document.close();
            System.out.println("PDF generado exitosamente en: " + filename);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Ejemplo de uso
        String filename = "example.pdf";
        String content = "¡Hola! Este es un documento PDF generado desde Java usando iText.";
        generatePDF(filename, content);
    }
}
