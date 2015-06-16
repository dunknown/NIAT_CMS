package com.niat.cms;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author dunknown
 */
@Component
public class SearchIndexBuilder implements ApplicationListener {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create an initial Lucene index for the data already present in the
     * database.
     * This method is called during Spring's startup.
     */
    @Override
    public void onApplicationEvent(final ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent) {
            try {
                FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
                fullTextEntityManager.createIndexer().startAndWait();
            } catch (InterruptedException e) {
                System.out.println(
                        "An error occurred trying to build the serach index: " +
                                e.toString());
            }
        }
    }
}

