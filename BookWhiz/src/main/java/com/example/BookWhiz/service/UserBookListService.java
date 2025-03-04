package com.example.BookWhiz.service;

import com.example.BookWhiz.model.Book;
import com.example.BookWhiz.model.BookListType;
import com.example.BookWhiz.model.User;
import com.example.BookWhiz.model.UserBookList;
import com.example.BookWhiz.repository.BookRepository;
import com.example.BookWhiz.repository.UserBookListRepository;
import com.example.BookWhiz.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserBookListService {

    @Autowired
    private final UserBookListRepository userBookListRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final BookService bookService;

    public UserBookListService(UserBookListRepository userBookListRepository, UserRepository userRepository, BookRepository bookRepository, UserService userService, BookService bookService) {
        this.userBookListRepository = userBookListRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    public void addBookToUserList(Long userId, Long bookId, BookListType listType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        UserBookList userBookList = userBookListRepository
                .findByUserIdAndTypeOfList(userId, listType)
                .orElseGet(() -> {
                    UserBookList newList = new UserBookList();
                    System.out.println(newList.getId());
                    newList.setUser(user);
                    newList.setTypeOfList(listType);
                    return newList;
                });

        if (userBookList.getBooks().contains(book)) {
            throw new IllegalArgumentException("Book is already in the list");
        }

        userBookList.getBooks().add(book);
        userBookListRepository.save(userBookList);
    }

    public void removeBookFromUserList(Long userId, Long bookId, BookListType listType) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        UserBookList userBookList = userBookListRepository
                .findByUserIdAndTypeOfList(userId, listType)
                .orElseThrow(() -> new EntityNotFoundException("UserBookList not found"));

        if (!userBookList.getBooks().contains(book)) {
            throw new RuntimeException("Book not found in user's list");
        }

        userBookList.getBooks().remove(book);

        userBookListRepository.save(userBookList);
    }

    public Set<Book> getBooksByUserIdAndListType(Long userId, BookListType listType) {

        User user1 = userService.getUserById(userId);
        Set<Book> books = userBookListRepository.findBooksByUserAndTypeOfList(user1, listType);
        return books;
    }

    public boolean existsBookInUserList(Long userId, Long bookId, BookListType listType) {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);

        return userBookListRepository.existsByBooksAndUserAndTypeOfList(Set.of(book), user, listType);
    }
}
