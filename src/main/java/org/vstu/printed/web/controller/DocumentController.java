package org.vstu.printed.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vstu.printed.persistence.document.Document;
import org.vstu.printed.dto.DocumentDto;
import org.vstu.printed.security.jwt.JwtUser;
import org.vstu.printed.service.document.DocumentService;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {
  private final DocumentService documentService;

  private int getUserIdFromAuthToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    JwtUser userInfo = (JwtUser)authentication.getPrincipal();
    return userInfo.getId();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<DocumentDto> loadDocument(@RequestParam("document") MultipartFile document, @RequestParam("orderId") int orderId) {
    try {
      int userId = getUserIdFromAuthToken();

      return ResponseEntity.ok(documentService.storeDocument(document, userId, orderId));
    } catch(IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<Resource> downloadFile(@PathVariable int id) {
    Document document = documentService.getDocument(id);
    if(document != null) {
      /*StringBuilder contentDispositionBuilder = new StringBuilder();
      contentDispositionBuilder.append("\"attachment; filename*=UTF-8''");
      contentDispositionBuilder.append(StringEscapeUtils.escapeJava(document.getName()));
      contentDispositionBuilder.append("\"");*/
      ContentDisposition documentContentDisposition = ContentDisposition.builder("attachment").filename(document.getName(), Charset.forName("utf-8")).build();

      return ResponseEntity.ok()
              .contentType(MediaType.asMediaType(MimeType.valueOf(document.getContentType())))
              .header(HttpHeaders.CONTENT_DISPOSITION, documentContentDisposition.toString())
              .body(new ByteArrayResource(document.getFileData(), document.getName()));
    }

    else
      return ResponseEntity.notFound().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<DocumentDto> getDocumentInfo(@PathVariable int id, @RequestParam("userId") int userId) {
    DocumentDto document = documentService.getDocumentForUserWithId(id, userId);
    if(document != null)
      return ResponseEntity.ok(document);
    else
      return ResponseEntity.notFound().build();
  }

  @GetMapping
  public ResponseEntity<List<DocumentDto>> getDocumentsInfo(@RequestParam("userId") int userId) {
    List<DocumentDto> userDocuments = documentService.getAllDocumentsForUser(userId);
    if(!userDocuments.isEmpty())
      return ResponseEntity.ok(userDocuments);
    else
      return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteById(@PathVariable int id) {
    try {
      documentService.deleteDocumentById(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
