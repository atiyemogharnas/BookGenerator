package com.library.bookgenerator.repository;

import com.library.bookgenerator.entity.Book;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book,Integer> {

    @Query("{\"match\": {\"content\": \"?0\"}}")
    List<Book> findByContent(String content);

    @Query("{\"term\": {\"content\": \"?0\"}}")
    List<Book> findByExactMatch(String content);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"owner\": \"?0\"}},  {\"match\": {\"content\": \"?1\"}}]}}")
    List<Book> findByOwnerAndContent(String owner, String content);

    @Query("{\"match\": {\"title\": {\"query\": \"?0\",\"fuzziness\": \"Auto\"}}}")
    List<Book> findByWrongContent(String content);

    @Query("{ \"intervals\": {\"content\": {\"match\": {\"query\": \"Second War\",\"max_gaps\": 5,\"ordered\": false}}}}}")
    List<Book> findBooksBySpaceInContent();
    
    @Query("{ \"bool\": {\"must\": [{\"match\": {\"title\": \"?0\"} },{\"match\": { \"owner\": \"?1\"}}]}}")
    Optional<Book> findBookByTitleAndOwner(String title, String owner);
}
