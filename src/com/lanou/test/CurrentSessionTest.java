package com.lanou.test;

import com.lanou.domain.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dllo on 17/10/19.
 */
public class CurrentSessionTest {

    private SessionFactory sessionFactory;

    @Before
    public void init(){
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();

    }


    /**
     *  1, 如果使用 getCurrentSession 的方式获得基于当前线程的session对象,
     *  则数据库的增删改查操作必须放在 transaction 中进行
     *
     *  2. 使用 openSession 就不用
     *
     */
    @Test
    public void find(){
        Session session = sessionFactory.getCurrentSession();
        System.out.println("第一次获取的session: " + session.getClass().hashCode());
        Transaction transaction =session.beginTransaction();

        Item item = session.get(Item.class, 1);
        System.out.println(item);

        transaction.commit();
        System.out.println("事务提交后的session状态: " + session.isOpen());

        session = sessionFactory.getCurrentSession();
        System.out.println("第二次获取的session: " + session.getClass().hashCode());
        Transaction transaction1 =session.beginTransaction();

        Item item1 = session.get(Item.class, 1);
        System.out.println(item1);

        transaction1.commit();

    }
}
