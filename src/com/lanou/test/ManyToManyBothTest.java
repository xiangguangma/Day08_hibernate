package com.lanou.test;

import com.lanou.domain.Category;
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
public class ManyToManyBothTest {

    private Session session;
    private Transaction transaction;


    @Before
    public void init(){
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml"); //加载默认文件, src目录下的 hibernate.fcg.xml
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
     * 保存数据
     *  1. 双向n-n, 保存数据时以inverse为主动方的对象进行级联保存
     */
    @Test
    public void save(){
        Category category = new Category("办公软件");
        Category category1 = new Category("doc文件");
        Category category2 = new Category("Excel文件");

        Item item = new Item("杀毒软件");
        Item item1 = new Item("word");

        // 关联关系
        category.getItems().add(item);
        category.getItems().add(item1);

        category1.getItems().add(item1);
        category2.getItems().add(item1);

        // 保存数据
        session.save(category);
        session.save(category1);
        session.save(category2);
    }


    /**
     *  删除数据
     *  2. 在进行删除还有引用, 即在中间表中还包含该对象的索引时, 只能删除inverse等于false的对象
     *  删除不了inverse等于true的对象, inverse为true时放弃了中间表的主动权
     */
    @Test
    public void delete(){
        // 先删主动方, 分类对象
//        Category category = session.get(Category.class, 1);
//        session.delete(category);


        // 删除被动方, 有级联关系的item 对象
        Item item = session.get(Item.class, 2);
        session.delete(item);
    }
}
