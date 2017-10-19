package com.lanou.test;

import com.lanou.domain.Category;
import com.lanou.domain.Item;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

/**
 * Created by dllo on 17/10/19.
 */
public class ManyToManySingleTest {

    private Session session;
    private Transaction transaction;


    @Before
    public void init(){
        Configuration configuration = new Configuration();
        configuration.configure(); //加载默认文件, src目录下的 hibernate.fcg.xml
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    @After
    public void destroy(){
        transaction.commit();
        session.close();
    }

    @Test
    public void save(){
        Category category = new Category("办公用品");
        Item item = new Item("签字笔");
        Item item1 = new Item("打印机");

        // 关联关系
        category.getItems().add(item);
        category.getItems().add(item1);

        // 保存对象
        session.save(category);
    }


    /**
     * 删除数据
     *  1. 删除关联的集合中的某个对象时, 需要先解除关联关系, 再调用 session.delete
     *
     */
    @Test
    public void delete(){
        // 已存在的分类对象
        Category category = session.get(Category.class, 1);

        // 1. 先删除该分类下的某个 item
        Iterator<Item> iterator = category.getItems().iterator();

        Item item = iterator.next(); // 取第一个

        // 解除关联
        category.getItems().remove(item);

        session.delete(item);
    }


    /**
     *  删除分类
     *  2. 删除设置了set关联关系的对象时, 如果cascade级联没有包含delete级联, 则只删除本对象,
     *  以及中间表中本对象id的数据, 不会删除级联的对方对象
     */
    @Test
    public void deleteCategory(){
        Category category = session.get(Category.class, 1);

        session.delete(category); // 删除分类
    }


    /**
     *  查找
     */
    @Test
    public void find(){
        Category category = new Category("厨具");

        Item item = new Item("菜刀");
        Item item1 = new Item("锅");

        // 关联关系
        category.getItems().add(item);
        category.getItems().add(item1);

        session.save(category);

        // 按名字查找
        Query query = session.createQuery("from Category where name=:na");
        // 设置参数
        query.setParameter("na", "厨具");

        List<Category> categories = query.list();

        for (Category category1 : categories) {
            System.out.println("基础信息: " + category1);

            // 得到当前分类下的item集合
            Iterator<Item> iterator = category1.getItems().iterator();

            // 遍历item集合
            while (iterator.hasNext()){
                System.out.println("item: " + iterator.next());
            }

        }

    }
}
