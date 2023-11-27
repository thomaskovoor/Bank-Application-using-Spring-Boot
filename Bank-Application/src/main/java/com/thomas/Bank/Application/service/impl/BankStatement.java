package com.thomas.Bank.Application.service.impl;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private TransactionRepository transactionRepo;
    private UserRepository userRepository;
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
        String userName = userInfo.getFirstName()+" "+userInfo.getLastName()+" "+userInfo.getLastName();
        String userAddress = userInfo.getAddress();

        //designing the heading
        PdfPTable heading = new PdfPTable(1);
        PdfPCell headingName = new PdfPCell(new Phrase("TK Bank "));
        headingName.setBorder(0);
        headingName.setHorizontalAlignment(Element.ALIGN_CENTER);
        headingName.setVerticalAlignment(Element.ALIGN_MIDDLE);
        headingName.setBackgroundColor(new BaseColor(51, 102, 153));
        headingName.setPadding(20f);

        //designing address space
        String address = "100 Park Avenue,India";
        String phNo="8075363994";
        PdfPCell addressSpace = new PdfPCell(new Phrase(address+","+phNo));
        addressSpace.setBorder(0);
        addressSpace.setHorizontalAlignment(Element.ALIGN_CENTER);
        addressSpace.setVerticalAlignment(Element.ALIGN_MIDDLE);
        addressSpace.setPadding(10f);

        heading.addCell(headingName);
        heading.addCell(address);

        PdfPTable accountInfo = new PdfPTable(2);
        accountInfo.setWidthPercentage(100);
        PdfPCell fromDateInfo = new PdfPCell(new Phrase("From Date: "+fromDate));
      //  fromDateInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("Statement of account"));
      //  statement.setBorder(1);
        PdfPCell toDateInfo = new PdfPCell(new Phrase("To Date: "+toDate));
      //  toDateInfo.setBorder(0);

        accountInfo.addCell(fromDateInfo);
        accountInfo.addCell(statement);
        accountInfo.addCell(toDateInfo);

        PdfPCell nameSpace = new PdfPCell(new Phrase("Customer Name: "+userName));
      //  nameSpace.setBorder(0);
        PdfPCell space1=new PdfPCell();
    //    space1.setBorder(0);
        PdfPCell userAddressSpace = new PdfPCell(new Phrase(userAddress));
      //  userAddressSpace.setBorder(0);

        accountInfo.addCell(nameSpace);
        accountInfo.addCell(space1);
        accountInfo.addCell(userAddressSpace);

        PdfPTable transactionTable = new PdfPTable(4);
        transactionTable.setWidthPercentage(100);
        transactionTable.setSpacingBefore(10f);

        PdfPCell dateSpace = new PdfPCell(new Phrase("Date"));
        dateSpace.setBackgroundColor(new BaseColor(173, 216, 230));


        PdfPCell transactionType = new PdfPCell(new Phrase("Description"));
        transactionType.setBackgroundColor(new BaseColor(173, 216, 230));

        PdfPCell amount = new PdfPCell(new Phrase("Amount"));
        amount.setBackgroundColor(new BaseColor(173, 216, 230));


        PdfPCell status = new PdfPCell(new Phrase("Status"));
        status.setBackgroundColor(new BaseColor(173, 216, 230));



        transactionTable.addCell(dateSpace);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(amount);
        transactionTable.addCell(status);


        transactionList.forEach(transaction -> {
                 transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
                 transactionTable.addCell(new Phrase(transaction.getTransactionType()));
                 transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
                 transactionTable.addCell(new Phrase(transaction.getStatus()));

        });





        document.add(heading);
        document.add(accountInfo);
        document.add(transactionTable);

        document.close();

    }


}
