package com.thomas.Bank.Application.service.impl;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.thomas.Bank.Application.dto.EmailDetails;
import com.thomas.Bank.Application.entity.Transaction;
import com.thomas.Bank.Application.entity.User;
import com.thomas.Bank.Application.repository.TransactionRepository;
import com.thomas.Bank.Application.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private TransactionRepository transactionRepo;
    private UserRepository userRepository;
    private EmailService emailService;
    private static final String FILE = "D:\\BankStatement\\MyStatement.pdf";
    public List<Transaction> generateBankStatement(String accountNumber, String fromDate, String toDate) throws DocumentException, FileNotFoundException {

        //parsing string date to local date format yy-mm-dd and
        //converting it to local date time yy-mm-dd-h:m:s
        LocalDateTime startDate = LocalDate.parse(fromDate,DateTimeFormatter.ISO_DATE).atTime(LocalTime.MIN);//00:00:00
        LocalDateTime endDate = LocalDate.parse(toDate,DateTimeFormatter.ISO_DATE).atTime(LocalTime.MAX);//23:59:59

        //returning all transactions of specified account number from start date to end date
        // including both start and end date
        List<Transaction> transactionList = transactionRepo.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction ->
                        (transaction.getCreatedAt().isEqual(startDate) || transaction.getCreatedAt().isAfter(startDate))
                                && (transaction.getCreatedAt().isEqual(endDate) || transaction.getCreatedAt().isBefore(endDate))
                       )
                .sorted(Comparator.comparing(Transaction::getCreatedAt))
                .toList();

        designBankStatement(transactionList,fromDate,toDate,accountNumber);

        return transactionList;

    }
    //design for the pdf
    private void designBankStatement(List<Transaction> transactionList,String fromDate,String toDate,String accountNumber) throws DocumentException, FileNotFoundException {

        Rectangle pageSize = new Rectangle(PageSize.A4); // size of page
        Document document = new Document(pageSize);

           OutputStream outputStream = new FileOutputStream(FILE);
           PdfWriter.getInstance(document,outputStream);
           document.open();



        User userInfo = userRepository.findByAccountNumber(accountNumber);
        String userName = userInfo.getFirstName()+" "+userInfo.getLastName()+" "+userInfo.getOtherName();
        String userAddress = userInfo.getAddress();

        BigDecimal initialBalance = transactionList.get(0).getAmount();

        //designing the heading
        PdfPTable heading = new PdfPTable(1);
        PdfPCell headingName = new PdfPCell(new Phrase("TK Bank ",getHeaderFont()));
        headingName.setHorizontalAlignment(Element.ALIGN_CENTER);
        headingName.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headingName.setBackgroundColor(new BaseColor(209,204,220));
        headingName.setPadding(20f);

        //designing address space
        String address = "100 Park Avenue,India";
        String phNo="8075363994";
        PdfPCell addressSpace = new PdfPCell(new Phrase(address+","+phNo,getAddressFont()));
        addressSpace.setHorizontalAlignment(Element.ALIGN_CENTER);
        addressSpace.setVerticalAlignment(Element.ALIGN_MIDDLE);
        addressSpace.setPadding(10f);

        PdfPCell statement = new PdfPCell(new Phrase("Statement of account",getTitleFont()));
        statement.setHorizontalAlignment(Element.ALIGN_CENTER);
        statement.setVerticalAlignment(Element.ALIGN_MIDDLE);
        statement.setPadding(10f);

        heading.addCell(headingName);
        heading.addCell(addressSpace);
        heading.addCell(statement);

        PdfPTable accountInfo = new PdfPTable(2);
        accountInfo.setWidthPercentage(100);
        PdfPCell fromDateInfo = new PdfPCell(new Phrase("From Date: "+fromDate,getDateFont()));
        PdfPCell toDateInfo = new PdfPCell(new Phrase("To Date: "+toDate,getDateFont()));




        PdfPCell nameSpace = new PdfPCell(new Phrase("Customer Name: "+userName,getAddressFont()));

        PdfPCell accountNumberSpace = new PdfPCell(new Phrase("Account Number: "+userInfo.getAccountNumber()));
        PdfPCell space1=new PdfPCell();

        PdfPCell userAddressSpace = new PdfPCell(new Phrase(userAddress+","+userInfo.getPhoneNumber(),getAddressFont()));

        accountInfo.addCell(fromDateInfo);
        accountInfo.addCell(nameSpace);
        accountInfo.addCell(toDateInfo);
        accountInfo.addCell(accountNumberSpace);
        accountInfo.addCell(space1);
        accountInfo.addCell(userAddressSpace);

        PdfPTable transactionTable = new PdfPTable(5);
        transactionTable.setWidthPercentage(100);
        transactionTable.setSpacingBefore(10f);

        PdfPCell dateSpace = new PdfPCell(new Phrase("Date",getTableHeaderFont()));
        dateSpace.setBackgroundColor(new BaseColor(173, 216, 230));


        PdfPCell transactionType = new PdfPCell(new Phrase("Description",getTableHeaderFont()));
        transactionType.setBackgroundColor(new BaseColor(173, 216, 230));

        PdfPCell amount = new PdfPCell(new Phrase("Amount",getTableHeaderFont()));
        amount.setBackgroundColor(new BaseColor(173, 216, 230));


        PdfPCell status = new PdfPCell(new Phrase("Status",getTableHeaderFont()));
        status.setBackgroundColor(new BaseColor(173, 216, 230));

        PdfPCell balance = new PdfPCell(new Phrase("Account Balance",getTableHeaderFont()));
        balance.setBackgroundColor(new BaseColor(173, 216, 230));



        transactionTable.addCell(dateSpace);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(amount);
        transactionTable.addCell(status);
        transactionTable.addCell(balance);


      ArrayList<BigDecimal> accountBalanceList = new ArrayList<>();
        accountBalanceList.add(initialBalance);

        for (int j = 1; j < transactionList.size(); j++) {
            if(transactionList.get(j).getTransactionType().equals("CREDIT") || transactionList.get(j).getTransactionType().equals("CREDIT TRANSFER"))
                accountBalanceList.add(accountBalanceList.get(j-1).add(transactionList.get(j).getAmount()));
            else
                accountBalanceList.add(accountBalanceList.get(j-1).subtract(transactionList.get(j).getAmount()));
        }
        int i=0;
        for (Transaction transaction : transactionList) {

            transactionTable.addCell(new Phrase(dateParser(transaction.getCreatedAt()),getTransactionTypeFont()));
            transactionTable.addCell(new Phrase(transaction.getTransactionType(),getTransactionTypeFont()));
            transactionTable.addCell(new Phrase(transaction.getAmount().toString(),getTableCellFont(transaction.getTransactionType())));
            transactionTable.addCell(new Phrase(transaction.getStatus(),getTransactionTypeFont()));

            transactionTable.addCell(new Phrase(accountBalanceList.get(i++).toString()));

        }

        document.add(heading);
        document.add(accountInfo);
        document.add(transactionTable);


        document.close();
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userInfo.getEmail())
                .subject("Statement of Account")
                .messageBody("Kindly find your account statement below")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

    }
    private Font getHeaderFont() {
        return new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.BLACK);
    }

    private Font getTitleFont() {
        return new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    }

    private Font getAddressFont() {
        return new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    }

    private Font getDateFont() {
        return new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);
    }

    private Font getTableHeaderFont() {
        return new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    }
    private Font getTransactionTypeFont(){
        return new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL,BaseColor.BLACK);
    }

    private Font getTableCellFont(String transactionType) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
        if (transactionType.equals("CREDIT") || transactionType.equals("CREDIT TRANSFER")){
            font.setColor(BaseColor.GREEN);
        }
        else
            font.setColor(BaseColor.RED);

        return font;
    }
    private String dateParser(LocalDateTime originalDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return originalDate.format(formatter);
    }


}
