package org.example;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.grpc.BookOuterClass;
import org.example.grpc.LibraryServiceGrpc;

import java.util.ArrayList;
import java.util.List;

public class LibraryServiceImpl extends LibraryServiceGrpc.LibraryServiceImplBase {
    private final List<BookOuterClass.Book> books = new ArrayList<>();

    @Override
    public void getAllBooks(Empty request, StreamObserver<BookOuterClass.Book> responseObserver) {
        for (BookOuterClass.Book book : books) {
            responseObserver.onNext(book);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void addBook(BookOuterClass.Book request, StreamObserver<BookOuterClass.Book> responseObserver) {
        books.add(request);
        responseObserver.onNext(request);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(BookOuterClass.Book request, StreamObserver<BookOuterClass.Book> responseObserver) {
        boolean updated = false;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getTitle().equals(request.getTitle())) {
                books.set(i, request);
                updated = true;
                break;
            }
        }
        if (updated) {
            responseObserver.onNext(request);
        } else {
            responseObserver.onError(new Throwable("Книга не найдена"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(BookOuterClass.Book request, StreamObserver<Empty> responseObserver) {
        boolean deleted = books.removeIf(b -> b.getTitle().equals(request.getTitle()));
        if (deleted) {
            responseObserver.onNext(Empty.getDefaultInstance());
        } else {
            responseObserver.onError(new Throwable("Книга не найдена"));
        }
        responseObserver.onCompleted();
    }
}