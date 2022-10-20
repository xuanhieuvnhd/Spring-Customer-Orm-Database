package com.service;

import com.model.Customer;

import java.util.List;

public interface ICustomerService {
    List<Customer> findAll();
    Customer findById(Long id);
    Customer save(Customer customer);
    void delete(Long id);
}
