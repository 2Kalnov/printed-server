package org.vstu.printed.service.document;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vstu.printed.dto.DocumentDto;
import org.vstu.printed.persistence.document.Document;
import org.vstu.printed.repository.DocumentRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

  private final DocumentRepository repository;
  private final Tika tikaParser;

  @Autowired
  public DocumentService(Tika parser, DocumentRepository repository) {
    this.repository = repository;
    this.tikaParser = parser;
  }

  public DocumentDto storeDocument(MultipartFile documentFile, int userId) throws IOException {
    InputStream fileStream = documentFile.getInputStream();
    Metadata documentInfo = new Metadata();
    tikaParser.parse(fileStream, documentInfo);

    String filename = documentFile.getOriginalFilename();
    int size = (int)(documentFile.getSize());
    String contentType = documentInfo.get("Content-Type");
    byte[] file = documentFile.getBytes();
    int pagesCount = Integer.parseInt(documentInfo.get("xmpTPg:NPages"));

    Document document = new Document(file, size, filename, contentType, pagesCount, userId);
    StringBuilder filePathBuilder = new StringBuilder();
    filePathBuilder.append('/');
    filePathBuilder.append(filename);

    repository.insert(userId, filePathBuilder.toString(), filename, size, pagesCount, file, contentType);

    return mapToDto(document);
  }

  public Document getDocument(int id) {
    Optional<Document> document = repository.findByIdNative(id);
    return document.orElse(null);
  }

  public Document getDocumentData(String name, int documentId) {
    Optional<Document> documentFound = repository.findByNameAndId(name, documentId);
    return documentFound.orElse(null);
  }

  public DocumentDto getDocumentForUserWithId(int documentId, int userId) {
    Optional<Document> foundDocument = repository.findByIdAndUserIdNative(documentId, userId);

    if(foundDocument.isPresent())
      return mapToDto(foundDocument.get());
    else
      return null;
  }

  public List<DocumentDto> getAllDocumentsForUser(int userId) {
    List<Document> foundDocuments = repository.getDocumentsByUserIdNative(userId);
    return foundDocuments.stream().map(this::mapToDto).collect(Collectors.toList());
  }

  public List<DocumentDto> getAllDocumentsFromOrder(int orderId) {
    List<Document> foundDocuments = repository.getDocumentsByOrderIdNative(orderId);
    return foundDocuments.stream().map(this::mapToDto).collect(Collectors.toList());
  }

  public void deleteDocumentById(int documentId) {
    repository.deleteByIdNative(documentId);
  }

  private DocumentDto mapToDto(Document document) {
    DocumentDto dto = new DocumentDto();

    dto.setId(document.getId());
    dto.setName(document.getName());
    dto.setSize(document.getSize());
    dto.setUserId(document.getUserId());
    dto.setPagesCount(document.getPagesCount());

    return dto;
  }

}
