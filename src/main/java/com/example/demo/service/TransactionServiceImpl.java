package com.example.demo.service;

import com.example.demo.dao.Customer;
import com.example.demo.dao.Finance;
import com.example.demo.dao.Spend;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.Pagination;
import com.example.demo.dto.PagingDTO;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ICustomerService customerService;

    @Autowired
    IFinanceService financeService;

    @Override
    public Transaction create(Transaction transaction) {
        // createCustomer(transaction);
        // update Finance
        Finance finance = financeService.getFinanceOnDay(transaction.getDate());
        finance.setIncome((long) transaction.getProceMoney() + transaction.getMedicineMoney());
        finance.setCountTran(1);
        financeService.update(finance, finance.getId());

        // update Customer: money + bill
        Customer cus = customerRepository.findByName(transaction.getCustomerName());
        if (cus == null) {
            cus = new Customer();
            cus.setName(transaction.getCustomerName());
            cus.setFullName(transaction.getCustomerName());
        }

        Integer money = cus.getMoney() + (transaction.getProceMoney() + transaction.getMedicineMoney())
                - (transaction.getExpMedicineMoney() + transaction.getExpProcMoney());
        String pre = transaction.getPrepaid();
        String post = transaction.getDebt();
        if (post != null && post.length() > 0) {
            cus.setBilling(post);
        } else {
            cus.setBilling(pre);
        }
        cus.setDiag(transaction.getDiagnostic());
        cus.setMoney(money);

        customerRepository.save(cus);

        return transactionRepository.save(transaction);
    }

    @Override
    public PagingDTO<Transaction> findAll(Integer limit, Integer offset) {
        // No need to get all transaction

        return null;
    }

    @Override
    public Transaction update(Transaction transaction, String id) {
        if (transaction.getId().equals(id)) {
            Transaction tran = transactionRepository.findById(id).orElse(null);
            Customer cus = customerRepository.findByName(tran.getCustomerName());
            if (tran != null) {
                // update Finance
                // if ((tran.getProceMoney() != transaction.getProceMoney())
                // || (tran.getMedicineMoney() != transaction.getMedicineMoney())) {
                Finance finance = financeService.getFinanceOnDay(transaction.getDate());
                finance.setIncome((long) (transaction.getMedicineMoney() + transaction.getProceMoney()
                        - tran.getProceMoney() - tran.getMedicineMoney()));
                financeService.update(finance, finance.getId());

                // update customer: money
                Integer money = cus.getMoney()
                        + (transaction.getProceMoney() + transaction.getMedicineMoney()
                        - transaction.getExpMedicineMoney() - transaction.getExpProcMoney())
                        - (tran.getProceMoney() + tran.getMedicineMoney() - tran.getExpMedicineMoney()
                        - tran.getExpProcMoney());
                cus.setMoney(money);
                // }

                String pre = transaction.getPrepaid();
                String post = transaction.getDebt();
                if (post != null && post.length() > 0) {
                    cus.setBilling(post);
                } else {
                    cus.setBilling(pre);
                }
                cus.setDiag(transaction.getDiagnostic());
                customerRepository.save(cus);

                return transactionRepository.save(transaction);
            }

        }

        return null;
    }

    @Override
    public void delete(String id) {

        transactionRepository.findById(id).ifPresent(tran -> {

            // update Customer: money + bill
            Customer cus = customerRepository.findByName(tran.getCustomerName());
            Integer money = cus.getMoney() - (tran.getProceMoney() + tran.getMedicineMoney() -
                    tran.getExpProcMoney() - tran.getExpMedicineMoney());
            String pre = tran.getPrepaid();
            String post = tran.getDebt();
            Integer num, total;
            if (post != null && post.length() > 0) {
                num = Integer.parseInt(post.substring(1));
                cus.setBilling("N" + (num - 1));
            } else {
                String[] tmp = pre.split("/");
                num = Integer.parseInt(tmp[0]);
                total = Integer.parseInt(tmp[1]);
                if (num < total)
                    cus.setBilling((num - 1) + "/" + total);
                else {
                    if (tran.getProceMoney() == 0) {
                        // tra truoc
                        cus.setBilling((num - 1) + "/" + total);
                    } else {
                        // theo buoi
                        cus.setBilling((num - 1) + "/" + (total - 1));
                    }
                }
            }
            cus.setDiag(tran.getDiagnostic());
            cus.setMoney(money);

            customerRepository.save(cus);

            // update Finance
            Finance finance = financeService.getFinanceOnDay(tran.getDate());
            finance.setIncome((0L - tran.getMedicineMoney() - tran.getProceMoney()));
            finance.setCountTran(-1);
            financeService.update(finance, finance.getId());

            transactionRepository.delete(tran);
        });
    }

    @Override
    public Transaction retrieve(String id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        // transactions.forEach(this::createCustomer);
        transactionRepository.saveAll(transactions);
    }

    @Override
    public PagingDTO<Transaction> findTranADay(String date, Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Transaction> trans = transactionRepository.getTransactionByDate(date, pageable);

        Pagination pagination = new Pagination();
        pagination.setTotal(transactionRepository.countTransactionByDate(date));
        pagination.setLimit(limit);
        pagination.setOffset(offset);

        return PagingDTO.<Transaction>builder().pagination(pagination).list(trans.getContent())
                .count(trans.getNumberOfElements()).build();
    }

    @Override
    public void importTransactionFromFile(MultipartHttpServletRequest request) {
        Iterator<String> itr = request.getFileNames();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<?>> futures = new ArrayList<Future<?>>();

        while (itr.hasNext()) {
            Future<?> f = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String uploadedFile = itr.next();
                    MultipartFile file = request.getFile(uploadedFile);
                    try {
                        String[] filename = file.getOriginalFilename().split("\\.");
                        File tmpFile = File.createTempFile(filename[0], "." + filename[1]);
                        file.transferTo(tmpFile);
                        System.out.println(tmpFile.getAbsoluteFile());

                        InputStream inp = new FileInputStream(tmpFile);
                        Workbook wb = WorkbookFactory.create(inp);
                        Integer numSheet = wb.getNumberOfSheets();
                        List<Transaction> trans = new ArrayList<>();
                        List<Spend> spends = new ArrayList<>();
                        List<Finance> finances = new ArrayList<>();
                        for (int i = 0; i < numSheet; i++) {
                            Sheet sheet = wb.getSheetAt(i);
                            int r = 0;
                            Row row = sheet.getRow(r);
                            Cell cell = row.getCell(3);
                            String val = cell.getStringCellValue();
                            String date = val.substring(24);
                            r = 4;
                            Finance fin = new Finance(date);
                            boolean addTran = true;
                            while (true) {
                                row = sheet.getRow(r);
                                if (row.getCell(1).getStringCellValue().equalsIgnoreCase("Tổng chi"))
                                    break;
                                if (addTran) {
                                    fin.setCountTran(1);
                                    Transaction tran = Transaction.builder().build();
                                    String cusName = row.getCell(2).getStringCellValue();

                                    String diag = row.getCell(3).getStringCellValue();
                                    if (diag != null && diag.length() > 0) {
                                        if (cusName != null && cusName.length() > 0) {
                                            tran.setCustomerName(cusName);
                                            if (customerRepository.countAllByName(cusName) == 0) {
                                                Customer cus = new Customer();
                                                cus.setName(cusName);
                                                cus.setFullName(cusName);
                                                cus.setDiag(diag);
                                                customerRepository.save(cus);
                                            }
                                        } else
                                            tran.setCustomerName(sheet.getRow(r - 1).getCell(2).getStringCellValue());
                                        tran.setDiagnostic(diag);
                                        tran.setListProcedure(row.getCell(4).getStringCellValue());
                                        tran.setMedicine(row.getCell(5).getStringCellValue());
                                        int procMoney, mecMoney;
                                        procMoney = Integer.parseInt(row.getCell(6).getStringCellValue().replaceAll("\\.", ""));
                                        mecMoney = Integer.parseInt(row.getCell(7).getStringCellValue().replaceAll("\\.", ""));
                                        tran.setProceMoney(procMoney);
                                        tran.setMedicineMoney(mecMoney);
                                        tran.setPrepaid(row.getCell(8).getStringCellValue());
                                        tran.setDebt(row.getCell(9).getStringCellValue());
                                        tran.setNote(row.getCell(10).getStringCellValue());
                                        fin.setIncome((long) procMoney + mecMoney);
                                        trans.add(tran);
                                    }

                                    if (row.getCell(3).getStringCellValue().equalsIgnoreCase("diễn giải chi")) {
                                        addTran = false;
                                        continue;
                                    }
                                } else {
                                    fin.setCountSpend(1);
                                    Spend spend = new Spend();
                                    spend.setDate(date);
                                    spend.setName(row.getCell(2).getStringCellValue());
                                    spend.setDetail(row.getCell(3).getStringCellValue());
                                    int money = Integer.parseInt(row.getCell(4).getStringCellValue().replaceAll("\\.", ""));
                                    spend.setMoney(money);
                                    fin.setSpend((long) money);
                                }

                                r++;
                            }
                            finances.add(fin);
                        }
                        //write tran, spend, fin to db


                        wb.close();
                        inp.close();
                        tmpFile.deleteOnExit();
                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (InvalidFormatException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        executorService.shutdown();
    }

    private void createCustomer(Transaction transaction) {
        List<Customer> customers = customerRepository.findAll();
        List<String> cusName = customers.stream().map(Customer::getName).collect(Collectors.toList());
        String pre = transaction.getPrepaid();
        String post = transaction.getDebt();
        Customer c;
        if (!cusName.contains(transaction.getCustomerName())) {

            if (post != null && post.length() > 0) {
                c = new Customer(transaction.getCustomerName(), post);
            } else {
                c = new Customer(transaction.getCustomerName(), pre);
            }
            c.setDiag(transaction.getDiagnostic());
            customerRepository.save(c);
        } else {
            c = customers.get(cusName.indexOf(transaction.getCustomerName()));
            // update thong tin benh nhan
            if (post != null && post.length() > 0) {
                c.setBilling(post);
            } else {
                c.setBilling(pre);
            }
            c.setDiag(transaction.getDiagnostic());
            customerService.update(c, c.getId());
        }
    }

}
