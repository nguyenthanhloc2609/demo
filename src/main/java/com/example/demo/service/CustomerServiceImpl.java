package com.example.demo.service;

import com.example.demo.dao.Customer;
import com.example.demo.dao.Transaction;
import com.example.demo.dto.PagingDTO;
import com.example.demo.dto.StatisticCustomerDTO;
import com.example.demo.dto.Pagination;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    TransactionRepository transactionRepository;

    Logger logger = Logger.getLogger(CustomerServiceImpl.class.getName());

    @Override
    public Customer create(Customer customer) {
        long count = customerRepository.countAllByName(customer.getName().trim());
        if (count == 0)
            return customerRepository.save(customer);
        else {
            log.error("Khách hàng có tên " + customer.getName() + " đã tồn tại!");
            return null;
        }
    }

    @Override
    public PagingDTO<Customer> findAll(Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Customer> list = customerRepository.findAll(pageable);

        // remove duplicate customer

        // List<Customer> cus = list.getContent();
        // Map<String, Boolean> check = new HashMap<>();
        // cus.forEach(c -> {
        // if (check.containsKey(c.getName())) {
        // Logger.getLogger(CustomerServiceImpl.class.getName()).log(Level.INFO,
        // "Delete duplicate customer: " + c.getName() + " with id: " + c.getId());
        // customerRepository.delete(c);
        // } else {
        // check.put(c.getName(), true);
        // }
        // });
        Pagination pagination = new Pagination();
        pagination.setTotal(customerRepository.count());
        pagination.setLimit(limit);
        pagination.setOffset(offset);
        return PagingDTO.<Customer>builder().pagination(pagination).list(list.getContent())
                .count(list.getNumberOfElements()).build();
    }

    @Override
    public Customer update(Customer customer, String id) {
        if (id.equals(customer.getId())) {
            Customer cus = customerRepository.findById(id).orElse(null);
            if (cus != null) {
                if (!cus.getName().equals(customer.getName().trim()))
                    updateCustomerName(cus.getName(), customer.getName());
                Customer exist = customerRepository.findByName(customer.getName().trim());
                if (exist != null) {
                    Logger.getLogger(CustomerServiceImpl.class.getName()).log(Level.INFO, "Delete customer: " + cus.getName());
                    customer.setMoney(customer.getMoney() + exist.getMoney());
                    customerRepository.delete(exist);
                }
                Logger.getLogger(CustomerServiceImpl.class.getName()).log(Level.INFO, "Update customer: " + customer);
                return customerRepository.save(customer);
            }
        }
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Customer retrieve(String id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null && customer.getMoney() < 0) {
            //lịch sử nợ của bệnh nhân
            List<Transaction> trans = transactionRepository.findByCustomerNameOrderByDateDesc(customer.getName());
            int i;
            for (i = 0; i < trans.size(); i++) {
                if (trans.get(i).getDebt() != null && trans.get(i).getDebt().length() > 0) {
                    customer.addHistory(trans.get(i).getDate() + " tiền thủ thuật: " + String.format("%,d", trans.get(i).getExpProcMoney()).replaceAll(",", ".")
                            + " đã thanh toán: " + String.format("%,d", trans.get(i).getProceMoney()).replaceAll(",", "."));
                    if (trans.get(i).getMedicineMoney() - trans.get(i).getExpMedicineMoney() < 0) {
                        customer.addHistory(trans.get(i).getDate() + " tiền thuốc: " + String.format("%,d", trans.get(i).getExpMedicineMoney()).replaceAll(",", ".")
                                + " đã thanh toán: " + String.format("%,d", trans.get(i).getMedicineMoney()).replaceAll(",", "."));
                    }
                } else
                    break;
            }
            if (i < trans.size() - 1)
                customer.addHistory(trans.get(i).getDate() + " thanh toán thủ thuật: " + String.format("%,d", trans.get(i).getProceMoney()).replaceAll(",", ".")
                        + " thanh toán tiền thuốc: " + String.format("%,d", trans.get(i).getMedicineMoney()).replaceAll(",", "."));
        }
        return customer;
    }

    @Override
    public List<Customer> searchByName(String name) {
        Pageable pageable = PageRequest.of(0, 999999);
        Page<Customer> result = customerRepository.findByNameLike(name, pageable);
        return result.getContent();
    }

    @Override
    public PagingDTO<Customer> searchCustomer(String name, Integer type, Integer limit, Integer offset) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Customer> list;
        if (type == 0) {
            list = customerRepository.findByNameLike(name, pageable);
        } else {
            list = customerRepository.searchCustomer(name, type == 2, pageable);

        }

        Pagination pagination = new Pagination();
        pagination.setTotal(list.getTotalElements());
        pagination.setLimit(limit);
        pagination.setOffset(offset);

        return PagingDTO.<Customer>builder().pagination(pagination).list(list.getContent())
                .count(list.getNumberOfElements()).build();
    }

    public void updateCustomerName(String name, String update) {
        List<Transaction> trans = transactionRepository.findByCustomerNameOrderByDateDesc(name);

        trans.forEach(tran -> tran.setCustomerName(update));
        transactionRepository.saveAll(trans);
    }

    @Override
    public StatisticCustomerDTO statisticCustomer() {
        StatisticCustomerDTO dto = StatisticCustomerDTO.builder()
                .total(customerRepository.count())
                .prepaid(customerRepository.countAllByMoneyGreaterThan(0))
                .postpaid(customerRepository.countAllByMoneyLessThan(0))
                .turn(customerRepository.countAllByMoneyEquals(0))
                .build();
        return dto;
    }
}
