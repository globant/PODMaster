package com.globant.agilepodmaster.metrics;

import static com.mysema.query.collections.CollQueryFactory.from;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.EntityPathBase;

/**
 * QueryDslPredicateExecutor that works on a collection.
 * @author Andres Postiglioni.
 *
 * @param <T>
 */
public class CollectionBasedQueryDslPredicateExecutor<T> implements QueryDslPredicateExecutor<T> {
  private EntityPathBase<T> query;
  private Iterable<T> collection;

  /**
   * Constructor.
   * @param query source expression
   * @param collection source collection
   */
  public CollectionBasedQueryDslPredicateExecutor(EntityPathBase<T> query, Iterable<T> collection) {
    this.query = query;
    this.collection = collection;
  }
  
  
  @Override
  public T findOne(Predicate predicate) {
    return from(query, collection).where(predicate).uniqueResult(query);
  }

  @Override
  public Iterable<T> findAll(Predicate predicate) {
    return from(query, collection).where(predicate).list(query);
  }

  @Override
  public Iterable<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
    return from(query, collection).where(predicate).orderBy(orders).list(query);
  }

  @Override
  public Page<T> findAll(Predicate predicate, Pageable pageable) {
    List<T> list = from(query, collection).where(predicate).list(query);

    int fromIndex = pageable.getOffset();
    int toIndex = fromIndex + pageable.getPageNumber() * pageable.getPageSize();
    List<T> page = list.subList(fromIndex, toIndex);
    
    return new PageImpl<T>(page, pageable, list.size());
  }

  @Override
  public long count(Predicate predicate) {
    return from(query, collection).where(predicate).count();
  }
}
