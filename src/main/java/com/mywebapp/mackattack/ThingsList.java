package com.mywebapp.mackattack;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import entity.ItemsEntity;
import java.util.List;

public class ThingsList {

    private EntityManager entityManager;

      public ThingsList() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("itemDB");
        entityManager = emf.createEntityManager();
    }

    public void add(String item) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ItemsEntity itemsEntity = new ItemsEntity();
        itemsEntity.setItem(item);
        entityManager.persist(itemsEntity);
        transaction.commit();
        System.out.println("Item added successfully: " + itemsEntity);
    }


    public void delete(int id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        ItemsEntity itemsEntity = entityManager.find(ItemsEntity.class, id);
        if (itemsEntity != null) {
            entityManager.remove(itemsEntity);
        } else {
            System.out.println("Item not found.");
        }

        transaction.commit();
    }

    public List<ItemsEntity> list() {
        TypedQuery<ItemsEntity> query = entityManager.createQuery("SELECT i FROM entity.ItemsEntity i", entity.ItemsEntity.class);
        List<ItemsEntity> resultList = query.getResultList();
        return resultList;
    }
}
