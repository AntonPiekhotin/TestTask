package com.example;

import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DocumentManager documentManager = new DocumentManager();
        DocumentManager.Author author = DocumentManager.Author.builder().id("1").name("John Doe").build();
        DocumentManager.Author author2 = DocumentManager.Author.builder().id("2").name("Alex").build();
        DocumentManager.Document document1 = DocumentManager.Document.builder().title("Title").content("Content").author(author).created(Instant.now()).build();
        DocumentManager.Document document2 = DocumentManager.Document.builder().title("2Title").content("Content2").author(author2).created(Instant.now()).build();
        DocumentManager.Document document3 = DocumentManager.Document.builder().title("22Title").content("Content3").author(author2).created(Instant.now()).build();

        documentManager.save(document1);
        documentManager.save(document2);
        documentManager.save(document3);

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("2"))
                .containsContents(List.of("Content2"))
                .authorIds(List.of("2"))
                .createdFrom(Instant.now().minusSeconds(10))
                .createdTo(Instant.now().plusSeconds(10))
                .build();
        List<DocumentManager.Document> documents = documentManager.search(searchRequest);

        System.out.println(documents);
        String id = document1.getId();
        System.out.println(documentManager.findById(id));
    }
}