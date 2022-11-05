package com.example.demo.service;

import com.example.demo.dao.Customer;
import com.example.demo.dao.Transaction;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements ITransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Transaction create(Transaction transaction) {
        createCustomer(transaction);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAll() {
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

    private void createCustomer(Transaction transaction) {
        List<Customer> customers = customerRepository.findAll();
        List<String> cusName = customers.stream().map(Customer::getName).collect(Collectors.toList());
        String pre = transaction.getPrepaid();
        String post = transaction.getDebt();
        if (!cusName.contains(transaction.getCustomerName())) {
            Customer c;
            if (pre != null && pre.length() > 0)
                c = new Customer(transaction.getCustomerName(), pre);
            else
                c = new Customer(transaction.getCustomerName(), post);
            customerRepository.save(c);
        }
    }
}
