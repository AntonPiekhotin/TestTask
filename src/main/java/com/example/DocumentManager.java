package com.example;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {

    /**
     * Map of documents. Key is document id, value is document
     */
    private final Map<String, Document> documents = new HashMap<>();

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (!documents.containsKey(document.getId())) {
            document.setId(UUID.randomUUID().toString());
            document.setCreated(Instant.now());
        }
        documents.put(document.getId(), document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        // firstly check if "filter" present, then apply it
        return documents.values().stream()
                .filter(document -> isNullOrEmpty(request.getTitlePrefixes())
                        || request.getTitlePrefixes().stream().anyMatch(document.getTitle()::startsWith))
                .filter(document -> isNullOrEmpty(request.getContainsContents())
                        || request.getContainsContents().stream().anyMatch(document.getContent()::contains))
                .filter(document -> isNullOrEmpty(request.getAuthorIds())
                        || request.getAuthorIds().stream().anyMatch(document.getAuthor().getId()::equals))
                .filter(document -> request.getCreatedFrom() == null || !document.getCreated().isBefore(request.getCreatedFrom()))
                .filter(document -> request.getCreatedTo() == null || !document.getCreated().isAfter(request.getCreatedTo()))
                .toList();
    }

    private boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return Optional.ofNullable(documents.get(id));
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}