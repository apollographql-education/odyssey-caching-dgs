package com.example.listings;

import graphql.execution.preparsed.PreparsedDocumentEntry;
import org.springframework.stereotype.Component;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.CompletableFuture;
import graphql.ExecutionInput;
import java.time.Duration;
import java.util.function.Function;

@Component
public class CachingPreparsedDocumentProvider implements PreparsedDocumentProvider {
    private final AsyncCache<String, PreparsedDocumentEntry> cache = Caffeine.newBuilder()
            .maximumSize(250)
            .expireAfterAccess(Duration.ofMinutes(2))
            .buildAsync();

    @Override
    public CompletableFuture<PreparsedDocumentEntry> getDocumentAsync(
            ExecutionInput executionInput,
            Function<ExecutionInput, PreparsedDocumentEntry> parseAndValidateFunction
    ) {
        return cache.get(
                executionInput.getQuery(),
                s -> parseAndValidateFunction.apply(executionInput)
        );
    }

}
