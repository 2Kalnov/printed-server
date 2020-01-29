package org.vstu.printed.persistence.document;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="Documents")
@NoArgsConstructor(force = true)
@Data
public class Document {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "[Size]")
  private final Integer size;

  @Column(name = "[File]")
  private final byte[] fileData;

  @Column(name = "ContentType")
  private final String contentType;

  @Column(name = "FilePath")
  private final String filePath; // should i have this field if I store the byte[] file itself?

  @Column(name = "[Name]")
  private final String name;

  @Column(name = "PagesCount")
  private final Integer pagesCount;

  @Column(name = "UserId")
  private final Integer userId;

  public Document(byte[] file, int size, String name, String contentType, int pagesCount, int userId) {
    StringBuilder filePathBuilder = new StringBuilder();
    filePathBuilder.append(name);
    filePathBuilder.append("-");
    filePathBuilder.append("user");
    filePathBuilder.append("-");
    filePathBuilder.append(userId);

    this.contentType = contentType;
    this.fileData = file;
    this.size = size;
    this.name = name;
    this.filePath = filePathBuilder.toString();
    this.pagesCount = pagesCount;
    this.userId = userId;

    this.id = 0;
  }
}
