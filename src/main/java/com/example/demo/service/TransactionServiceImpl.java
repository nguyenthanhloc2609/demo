package com.example.demo.service;

import com.example.demo.dao.Customer;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.Pagination;
import com.example.demo.dto.PagingDTO;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public Transaction create(Transaction transaction) {
        createCustomer(transaction);

        return transactionRepository.save(transaction);
    }

    @Override
    public PagingDTO<Transaction> findAll(Integer limit, Integer offset) {
        //No need to get all transaction

        return null;
    }

    @Override
    public Transaction update(Transaction transaction, String id) {
        if (transaction.getId().equals(id)) {
            Transaction tran = transactionRepository.findById(id).orElse(null);
            if (tran != null)
                return transactionRepository.save(transaction);
        }

//        Query query = new Query();
//        query.addCriteria(Criteria.where("id").is("appleF"));
//
//        Update update = new Update();
//        update6.set("age", 101);
//        update6.set("ic", 1111);
//
//        //FindAndModifyOptions().returnNew(true) = newly updated document
//        //FindAndModifyOptions().returnNew(false) = old document (not update yet)
//        User userTest6 = mongoOperation.findAndModify(
//                query6, update6,
//                new FindAndModifyOptions().returnNew(true), User.class);
        return null;
    }

    @Override
    public void delete(String id) {
        transactionRepository.findById(id).ifPresent(tran -> transactionRepository.delete(tran));
    }

    @Override
    public Transaction retrieve(String id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAll(List<Transaction> transactions) {
        transactions.forEach(this::createCustomer);
        transactionRepository.saveAll(transactions);
    }

    @Override
    public PagingDTO<Transaction> findTranADay(String date, Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Transaction> trans = transactionRepository.getTransactionByDate(date, pageable);

        Pagination pagination = new Pagination();
        pagination.setTotal(transactionRepository.count());
        pagination.setLimit(limit);
        pagination.setOffset(offset);

        //demo
//        List<Transaction> trans = new ArrayList<>();
//        for (int i = 0; i < limit; i++)
//            trans.add(Transaction.builder().build());
//
//        Pagination pagination = new Pagination();
//        pagination.setTotal(100);
//        pagination.setLimit(limit);
//        pagination.setOffset(offset);
        return PagingDTO.<Transaction>builder().pagination(pagination).list(trans.getContent()).count(trans.getNumberOfElements()).build();

    }

    private void createCustomer(Transaction transaction) {
        List<Customer> customers = customerRepository.findAll();
        List<String> cusName = customers.stream().map(Customer::getName).collect(Collectors.toList());
        String pre = transaction.getPrepaid();
        String post = transaction.getDebt();
        Customer c;
        if (!cusName.contains(transaction.getCustomerName())) {

            if (pre != null && pre.length() > 0) {
                c = new Customer(transaction.getCustomerName(), pre);
            } else {
                c = new Customer(transaction.getCustomerName(), post);
            }
            c.setNote("Ngày điều trị trước đó: " + transaction.getDate());
            customerRepository.save(c);
        } else {
            c = customers.get(cusName.indexOf(transaction.getCustomerName()));
            //update thong tin benh nhan
            if (pre != null && pre.length() > 0) {
                c.setBilling(pre);
            } else {
                c.setBilling(post);
            }
            c.setNote("Ngày điều trị trước đó: " + transaction.getDate());
            customerService.update(c, c.getId());
        }
    }
}
