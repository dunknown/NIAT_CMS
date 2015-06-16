package com.niat.cms.repo;

import com.niat.cms.domain.Material;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author dunknown
 */
@Repository
public class MaterialSearch {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Material> search(String searchQuery) {
        FullTextEntityManager fullTextEntityManager =
                org.hibernate.search.jpa.Search.
                        getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder =
                fullTextEntityManager.getSearchFactory()
                        .buildQueryBuilder().forEntity(Material.class).get();

        org.apache.lucene.search.Query query =
                queryBuilder
                        .keyword()
                        .onFields("title", "shortText", "mainText", "author.username", "tag.text")
                        .matching(searchQuery)
                        .createQuery();

        org.hibernate.search.jpa.FullTextQuery jpaQuery =
                fullTextEntityManager.createFullTextQuery(query, Material.class);

        @SuppressWarnings("unchecked")
        List<Material> results = jpaQuery.getResultList();

        return results;
    }
}
