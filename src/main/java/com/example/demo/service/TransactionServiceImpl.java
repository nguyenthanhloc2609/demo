package com.example.demo.service;

import com.example.demo.dao.Customer;
import com.example.demo.dao.Finance;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.Pagination;
import com.example.demo.dto.PagingDTO;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
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
            Integer money = cus.getMoney() -(tran.getProceMoney() + tran.getMedicineMoney() - 
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
                        //tra truoc
                        cus.setBilling((num - 1) + "/" + total);
                    } else {
                        //theo buoi
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
