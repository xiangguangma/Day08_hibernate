package com.lanou.test;

import com.lanou.domain.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by dllo on 17/10/19.
 */
public class SessionCacheTest {
    // 一级缓存,  session
    private Session session;
    private Transaction transaction;


    @Before
    public void init(){
        Configuration configuration = new Configuration();
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    @After
    public void destroy(){
        transaction.commit();
        session.close();
    }


    /**
     * 一级缓存的查询, 重复的查询只查询一次
     */
    @Test
    public void find(){

        Item item = session.get(Item.class, 1);
        long startTime = System.currentTimeMillis();

        Item item1 = session.get(Item.class, 2);

        long end = System.currentTimeMillis();
        System.out.println("运行时间: " + (end - startTime));

        startTime = System.currentTimeMillis();

        Item item2 = session.get(Item.class, 3);
        Item item3 = session.get(Item.class, 4);
        end = System.currentTimeMillis();
        System.out.println("运行时间: " + (end - startTime));
    }


    /**
     *  修改数据
     *  1. 当缓存中的数据发生变化时, 会在下一次事务提交时将修改后的数据同步到数据库中
     */
    @Test
    public void update(){
        // 从数据库查询
        Item item = session.get(Item.class, 1);

        // 从一级缓存中获取
        Item item1 = session.get(Item.class, 1);

        // 修改缓存中的数据
        item1.setName("学习用具");
    }


    /**
     * 数据库中数据同步到缓存中
     * 2. 时机 session 重新开启
     */
    @Test
    public void update2(){
        Item item = session.get(Item.class, 1);
        System.out.println(item);
        session.close();

        // 睡 10 秒
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init();

        System.out.println("睡醒后: " + item);

        Item item1 = session.get(Item.class, 1);

        System.out.println("重新获取: " + item1);
    }







}
