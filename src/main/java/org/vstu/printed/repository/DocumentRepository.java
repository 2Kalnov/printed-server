package org.vstu.printed.repository;

import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.vstu.printed.persistence.document.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
  @Modifying
  @Transactional
  @Query(value = "insert Documents values(:userId, :filePath, :name, :size, :pagesCount, :file, :contentType)", nativeQuery = true)
  void insert(
          @Param("userId") int userId,
          @Param("filePath") String filePath,
          @Param("name") String name,
          @Param("size") int size,
          @Param("pagesCount") int pagesCount,
          @Param("file") byte[] file,
          @Param("contentType") String contentType
  );

  @Query(value = "select top 1 Id, UserId, [Name], Size, PagesCount, [File], ContentType from Documents where Documents.Id = :docId", nativeQuery = true)
  Optional<Document> findByIdNative(@Param("docId") int docId);

  @Query(value = "select top 1 Id, UserId, [Name], Size, PagesCount, ContentType from Documents where Documents.Id = :docId and UserId = :userId", nativeQuery = true)
  Optional<Document> findByIdAndUserIdNative(@Param("docId") int documentId, @Param("userId") int userId);

  @Query(value = "select Id, UserId, [Name], Size, PagesCount, ContentType from Documents where UserId = :userId", nativeQuery = true)
  List<Document> getDocumentsByUserIdNative(@Param("userId") int userId);

  @Query(value = "select Documents.Id, Documents.[Name], Documents.ContentType, Documents.UserId, Documents.PagesCount from\n" +
          "OrdersDocuments join Documents on Documents.Id = OrdersDocuments.DocumentId\n" +
          "where OrdersDocuments.OrderId = :orderId", nativeQuery = true)
  List<Document> getDocumentsByOrderIdNative(@Param("orderId") int orderId);
}
