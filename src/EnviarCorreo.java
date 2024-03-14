import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class EnviarCorreo {

    private String host;
    private String username;
    private String password;
    private Properties properties;

    public EnviarCorreo(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;

        // Configuración de propiedades para la sesión de JavaMail
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587"); // Usaremos el puerto 587
        properties.put("mail.smtp.ssl.trust", host);
    }

    public void sendEmail(String recipientEmail, String subject, String body, String attachmentContent) {
        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Crear un mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);

            // Contenido del mensaje
            message.setText(body);

            // Crear el archivo PDF adjunto
            String attachmentPath = "attachment.pdf";
            createPDF(attachmentPath, attachmentContent);

            // Crear parte del archivo adjunto
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(attachmentPath);

            // Crear el multipart para el mensaje
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(attachmentBodyPart);

            // Agregar el multipart al mensaje
            message.setContent(multipart);

            // Enviar el mensaje
            Transport.send(message);

            JOptionPane.showMessageDialog(null, "Correo enviado exitosamente.");
        } catch (MessagingException | IOException e) {
            JOptionPane.showMessageDialog(null, "Error al enviar el correo: " + e.getMessage());
        }
    }

    private void createPDF(String filename, String content) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            document.add(new Paragraph(content));
            document.close();
            JOptionPane.showMessageDialog(null, "PDF generado exitosamente en: " + filename);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String host = "sandbox.smtp.mailtrap.io";
        String username = "fd1ed2f7a7a612";
        String password = "3a9e8e95cb025f";

        String recipientEmail = JOptionPane.showInputDialog("Ingrese el correo electrónico del destinatario:");
        String subject = JOptionPane.showInputDialog("Ingrese el asunto del correo:");
        String body = JOptionPane.showInputDialog("Ingrese el contenido del correo:");
        String attachmentContent = JOptionPane.showInputDialog("Ingrese el contenido del archivo PDF adjunto:");

        EnviarCorreo sender = new EnviarCorreo(host, username, password);
        sender.sendEmail(recipientEmail, subject, body, attachmentContent);
    }
}

