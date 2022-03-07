package com.mindHub.homebanking.services;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindHub.homebanking.models.Account;
import com.mindHub.homebanking.models.Client;
import com.mindHub.homebanking.models.Transaction;
import com.mindHub.homebanking.repositories.ClientRepository;
import com.mindHub.homebanking.repositories.TransactionsRepository;
import org.hibernate.type.LocalDateTimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.time.LocalDate.*;
import static java.time.LocalDate.parse;

@Service
public class PDFGeneratorService {

    public void export(HttpServletResponse response, Client client, Account account, String fechaDesde, String fechaHasta) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);

        Paragraph paragraph = new Paragraph("FREEBAKING Balance de cuenta", fontTitle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);

        PdfPTable table= new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.0f, 5.0f, 3.0f,3.0f});
        table.setSpacingBefore(10);

        addTableHeader(table);
        addTableTransactionsContent(table, account,fechaDesde,fechaHasta);

        Paragraph paragraph2= new Paragraph("Cliente: "+client.getFirstName()+" "+client.getLastName()+ " - Cuenta Nº: "+ account.getNumber(), fontParagraph);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph balance =  new Paragraph("Balance: $"+account.getBalance(), fontParagraph);
        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);
        fontTitle.setSize(14);

        //Creo una fila donde indico le fecha desde y hasta de las cuales realizo el filtro para mostrar las transacciones
        Paragraph subtitulo = new Paragraph("Movimientos de la cuenta desde: " + fechaDesde+  " - hasta: "+ fechaHasta,fontTitle);
        subtitulo.setAlignment(Paragraph.ALIGN_LEFT);

        // Creating a Document object
        Image imageData = Image.getInstance("./src/main/resources/static/img/logo_pdf.png");


        document.add(imageData);
        document.add(paragraph);
        document.add(paragraph2);
        document.add(balance);
        document.add(subtitulo);
        document.add(table);
        document.close();
    }

    public void addTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.lightGray);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.white);

        cell.setPhrase(new Phrase("Fecha", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Descripción", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tipo", font));
        table.addCell(cell);


        cell.setPhrase(new Phrase("Monto", font));
        table.addCell(cell);
    }

    public void addTableTransactionsContent(PdfPTable table, Account account,String fechaDesde,String fechaHasta){
        PdfPCell newCell = new PdfPCell();
        newCell.setBackgroundColor(Color.WHITE);
        newCell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);

        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");

        Set<Transaction> transactions=account.getTransactions();

        AtomicInteger numero = new AtomicInteger();
        numero.set(1);

        // Agrego al PDF las transacciones filtradas por las fechas desde y Hasta
        filtrarTransaccionesEntreFechas(fechaDesde,fechaHasta,transactions).forEach(transaction -> {
            changeColor(numero,newCell,font);
            newCell.setPhrase(new Phrase(String.valueOf(transaction.getDate().format(aFormatter)), font));
            table.addCell(newCell);

            newCell.setPhrase(new Phrase(String.valueOf(transaction.getDescription()),font));
            table.addCell(newCell);

            newCell.setPhrase(new Phrase(String.valueOf(transaction.getType()),font));
            table.addCell(newCell);

            newCell.setPhrase(new Phrase("$"+String.valueOf(transaction.getAmount()),font));
            table.addCell(newCell);
            numero.getAndIncrement();
        });
    }

    public static boolean esPar(int numero) {
        return numero % 2 == 0;
    }

    public static void changeColor(AtomicInteger numero, PdfPCell newCell,Font font){
        if (esPar(numero.get())){
            newCell.setBackgroundColor(Color.LIGHT_GRAY);
            font.setColor(Color.DARK_GRAY);
        }else
        {
            newCell.setBackgroundColor(Color.WHITE);
            font.setColor(Color.BLACK);
        }
    }

    public Set<Transaction> filtrarTransaccionesEntreFechas(String fechaDesde, String fechaHasta, Set<Transaction> transacciones){

        DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("DESDE");
        System.out.println(fechaDesde);
        System.out.println("DESDE");
        System.out.println(fechaHasta);
        System.out.println("*-*-**-*-**-*--*-*-*-");

        Set<Transaction> transaccionesFiltradas=transacciones.stream().filter(transaction -> {
            System.out.println(transaction.getDate().format(aFormatter));

            boolean b = transaction.getDate().format(aFormatter).toString().compareTo(fechaDesde) >= 0 && transaction.getDate().format(aFormatter).toString().compareTo(fechaHasta) <=0;
            return b;
        }).collect(Collectors.toSet());

        return transaccionesFiltradas;
    }
}
