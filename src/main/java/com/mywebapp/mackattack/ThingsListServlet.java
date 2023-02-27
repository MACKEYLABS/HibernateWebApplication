package com.mywebapp.mackattack;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import entity.ItemsEntity;

@WebServlet(name = "ThingsListServlet", urlPatterns = { "/things-list" })
public class ThingsListServlet extends HttpServlet {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void init() throws ServletException {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "add":
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                break;
            case "delete":
                deleteItem(req, resp);
                break;
            case "list":
                listItems(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/things-list?action=list");
                break;
        }
    }

    private void deleteItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            ItemsEntity itemsEntity = entityManager.find(ItemsEntity.class, id);
            if (itemsEntity != null) {
                entityManager.remove(itemsEntity);
                transaction.commit();
            } else {
                transaction.rollback();
                System.out.println("Item not found.");
            }
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        resp.sendRedirect(req.getContextPath() + "/things-list?action=list");
    }



    private void listItems(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<ItemsEntity> query = entityManager.createQuery("SELECT i FROM ItemsEntity i", ItemsEntity.class);
        List<ItemsEntity> items = query.getResultList();
        req.setAttribute("items", items);

        if (!resp.isCommitted()) {
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
        }

        entityManager.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String item = req.getParameter("item");
        int id = 0;
        if (req.getParameter("id") != null && !req.getParameter("id").isEmpty()) {
            id = Integer.parseInt(req.getParameter("id"));
        }
        System.out.println("Received request: action=" + action + ", item=" + item);

        if ("1".equals(action)) {
            System.out.println("Adding item: " + item);
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                ItemsEntity itemsEntity = new ItemsEntity();
                itemsEntity.setId(id); // set the id property
                itemsEntity.setItem(item);
                entityManager.persist(itemsEntity);
                transaction.commit();
                System.out.println("Item added successfully: " + itemsEntity);
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            } finally {
                entityManager.close();
            }
        } else if ("2".equals(action)) {
            deleteItem(req, resp);
            listItems(req, resp); // list the items after deletion
        }
    }

    @Override
    public void destroy() {
        entityManagerFactory.close();
    }
}