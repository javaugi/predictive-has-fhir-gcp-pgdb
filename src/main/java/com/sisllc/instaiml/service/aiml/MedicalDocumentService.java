/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.aiml;

import com.sisllc.instaiml.model.aiml.MedicalDocument;
import com.sisllc.instaiml.repository.aiml.MedicalDocumentRepository;
import io.r2dbc.spi.R2dbcException;
import io.r2dbc.spi.R2dbcTransientResourceException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/*
Transaction Best Practices
    Keep transactions short - Especially with pessimistic locks
    Use appropriate isolation levels - Configure in @Transactional
    Implement retry logic - For optimistic locking scenarios
    Monitor for deadlocks - Set up proper logging
    Consider alternative approaches - Like event sourcing for high-contention scenarios
 */

@Service
@RequiredArgsConstructor
public class MedicalDocumentService {

    private final MedicalDocumentRepository repository;
    private final DatabaseClient databaseClient;

    public Flux<MedicalDocument> findAll() {
        return repository.findAll();
    }

    public Mono<MedicalDocument> findById(String id) {
        return repository.findById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Mono<Void> highIsolationOperation() {
        // Your transactional code
        return Mono.just("Test of highIsolationOperation").then();
    }

    //Spring Data R2DBC will automatically handle version checks during updates: if the @Version is added
    //When you save an entity, if the version has changed, it will throw an OptimisticLockingFailureException:
    @Transactional
    public Mono<MedicalDocument> updateDocument(String id, String newContent) {
        return repository.findById(id)
            .flatMap(doc -> {
                doc.setTextContent(newContent);
                return repository.save(doc);
            })
            .onErrorResume(OptimisticLockingFailureException.class, ex
                -> Mono.error(new RuntimeException("Document was modified by another transaction"))
            );
    }

    //2. Pessimistic Locking with R2DBC
    //  Pessimistic locking requires custom SQL since R2DBC repositories don't directly support it. Here's how to implement it:
    //  Custom Repository Method
    @Transactional
    public Mono<MedicalDocument> updateWithPessimisticLock(String id, String newContent) {
        return repository.findByIdForUpdate(id)
            .switchIfEmpty(Mono.error(new RuntimeException("Document not found or locked")))
            .flatMap(doc -> {
                doc.setTextContent(newContent);
                return repository.save(doc);
            });
    }

    // Alternative using DatabaseClient for more control
    @Transactional
    public Mono<MedicalDocument> pessimisticUpdate(String id, String newContent) {
        return databaseClient.sql("""
                SELECT * FROM medical_documents
                WHERE id = :id
                FOR UPDATE
                """)
            .bind("id", id)
            .mapProperties(MedicalDocument.class)
            .one()
            .flatMap(doc -> {
                doc.setTextContent(newContent);
                return repository.save(doc);
            })
            .onErrorResume(R2dbcException.class, ex
                -> Mono.error(new RuntimeException("Failed to acquire lock: " + ex.getMessage()))
            );
    }

    /*
    4. Locking Strategies Compared
        Optimistic Locking
            Best for: High contention scenarios with few conflicts
            Pros: Better performance, no blocking
            Cons: Requires retry logic for conflicts
     */
    @Transactional
    public Mono<MedicalDocument> optimisticUpdate(String id, String content) {
        return repository.findById(id)
            .flatMap(doc -> {
                doc.setTextContent(content);
                return repository.save(doc);
            })
            .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
            .onErrorResume(OptimisticLockingFailureException.class, ex
                -> Mono.error(new RuntimeException("Update failed after retries"))
            );
    }

    /*
    Pessimistic Locking
        Best for: Critical sections where you must prevent concurrent modifications
        Pros: Guarantees exclusive access
        Cons: Can cause deadlocks, reduces concurrency
     */
    @Transactional
    public Mono<MedicalDocument> processCriticalUpdate(String id) {
        return repository.findByIdForUpdate(id)
            .flatMap(doc -> {
                // Critical section
                performCriticalOperation(doc);
                return repository.save(doc);
            })
            .timeout(Duration.ofSeconds(5))
            .onErrorResume(e -> {
                // Handle timeout or lock acquisition failure
                return Mono.error(new RuntimeException("Operation timed out"));
            });
    }

    private void performCriticalOperation(MedicalDocument doc) {

    }

    /*
    5. Handling Lock Timeouts
        Configure lock timeouts at the database level:
            -- Set lock timeout to 5 seconds
            ALTER DATABASE your_database SET lock_timeout = '5s';
        Or handle them in your application:
     */
    @Transactional
    public Mono<MedicalDocument> processWithTimeout(String id) {
        return databaseClient.sql("SET LOCAL lock_timeout = '5s'").then()
            .then(repository.findByIdForUpdate(id))
            .flatMap(doc -> {
                // Process document
                return repository.save(doc);
            })
            .onErrorResume(ex -> {
                //if (ex instanceof PostgresqlException) {
                //PostgresqlException pgEx = (PostgresqlException) ex;
            if (ex instanceof R2dbcException) {
                R2dbcException pgEx = (R2dbcException) ex;
                if ("55P03".equals(pgEx.getSqlState())) { // lock_not_available
                    return Mono.error(new RuntimeException("Could not acquire lock in time"));
                }
            } else if (ex instanceof R2dbcTransientResourceException) {
                return Mono.error(new RuntimeException("Database operation timed out"));
            }
            return Mono.error(ex);
            });
    }

}

/*
Understanding Mono<Void> in Spring WebFlux
Mono<Void> is a specialized reactive type that represents an asynchronous operation that:
    Completes (signals when the operation is done)
    Doesn't emit any actual value (just signals completion)
    May or may not carry an error (if something goes wrong)

Key Characteristics
    No Value: Unlike Mono<String> or Mono<Document>, Mono<Void> doesn't produce any data payload
    Completion Signal: Only indicates when the operation finishes
    Error Handling: Can still propagate errors if the operation fails

Common Use Cases
    Write Operations (when you only care about completion):
        @PostMapping
        public Mono<Void> saveDocument(@RequestBody Document doc) {
            return repository.save(doc).then();
        }
    Delete Operations:
        @DeleteMapping("/{id}")
        public Mono<Void> deleteDocument(@PathVariable String id) {
            return repository.deleteById(id);
        }
    Side-Effect Operations (logging, notifications):
        public Mono<Void> auditAction(String action) {
            return auditRepository.log(action).then();
        }

How to Work with Mono<Void>
    Creating a Mono<Void>
        Mono<Void> completionSignal = Mono.empty(); // Immediate completion
        Mono<Void> delayedCompletion = Mono.delay(Duration.ofSeconds(1)).then();
    Transforming Operations
        public Mono<Void> processDocument(String id) {
            return repository.findById(id)
                .flatMap(doc -> {
                    // Process document
                    return repository.save(doc);
                })
                .then(); // Convert Mono<Document> to Mono<Void>
        }
    Chaining Operations
        public Mono<Void> multiStepOperation() {
            return stepOne()
                .then(stepTwo())
                .then(stepThree());
        }
    Error Handling
        public Mono<Void> riskyOperation() {
            return dangerousAction()
                .onErrorResume(e -> {
                    log.error("Failed", e);
                    return Mono.empty(); // Recover with completion
                });
        }

HTTP Response Implications
    When used in WebFlux controllers:
        Successful Mono<Void>: Returns HTTP 200 (OK) with empty body
        Errored Mono<Void>: Returns appropriate error status (4xx/5xx)
            @PostMapping
            public Mono<Void> createResource(@RequestBody Resource resource) {
                return service.save(resource);
            }
            // Successful call → 200 OK with no body

Why Use Mono<Void> Instead of Mono<Object>?
    Clear Intent: Shows the operation is side-effect only
    Performance: Avoids unnecessary serialization
    Correctness: Prevents accidental value emission
    HTTP Semantics: Matches REST conventions for write operations

Common Pitfalls
    Forgetting to Subscribe:
        // WRONG: Operation never executes
        repository.deleteById(id);
        // RIGHT
        return repository.deleteById(id);
    Ignoring Errors:
        // BAD: Swallows errors
        public void delete(String id) {
            repository.deleteById(id).subscribe();
        }
        // GOOD: Propagates errors
        public Mono<Void> delete(String id) {
            return repository.deleteById(id);
        }
    Unnecessary Conversion:
        1. // Unnecessary
            return repository.findAll().then();
        3. // Better - return the actual result
        return repository.findAll();
In your specific example with pessimistic locking, Mono<Void> is appropriate because:
    The main concern is completion of the transactional operation
    You're modifying state rather than returning data
    You want to signal when the lock can be released
 */
