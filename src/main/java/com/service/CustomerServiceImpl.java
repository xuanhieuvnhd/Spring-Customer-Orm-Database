package com.service;

import com.model.Customer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerServiceImpl implements ICustomerService {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;

    static {
        try {
            sessionFactory = new Configuration().configure("./hibernate.conf.xml").buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    public static final String FIND_ALL = "SELECT c FROM Customer AS c";
    public static final String FIND_BY_ID = "SELECT c FROM Customer AS c WHERE c.id =: id";

    @Override
    public List<Customer> findAll() {
        TypedQuery<Customer> customerTypedQuery = entityManager.createQuery(FIND_ALL, Customer.class);
        return customerTypedQuery.getResultList();
    }

    @Override
    public Customer findById(Long id) {
        TypedQuery<Customer> customerTypedQuery = entityManager.createQuery(FIND_BY_ID, Customer.class);
        customerTypedQuery.setParameter("id", id);
        return customerTypedQuery.getSingleResult();
    }

    @Override
    public Customer save(Customer customer) {
        Session session = null;
        Transaction transaction = null;
        Customer newCustomer = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            if (Long.valueOf(customer.getId()) == null){
                session.save(customer);
                transaction.commit();
                return customer;
            }
            newCustomer = findById(customer.getId());
            newCustomer.setName(customer.getName());
            newCustomer.setEmail(customer.getEmail());
            newCustomer.setAddress(customer.getAddress());
            session.save(newCustomer);
            transaction.commit();
            return newCustomer;
        }catch (Exception e){
            e.printStackTrace();
            newCustomer = new Customer();
            newCustomer.setName(customer.getName());
            newCustomer.setEmail(customer.getEmail());
            newCustomer.setAddress(customer.getAddress());
            session.save(newCustomer);
        } finally {
            session.close();
        }
        return newCustomer;
    }

    @Override
    public void delete(Long id) {
        Customer customer = findById(id);
        entityManager.getTransaction().begin();
        entityManager.remove(customer);
        entityManager.getTransaction().commit();
    }
}
