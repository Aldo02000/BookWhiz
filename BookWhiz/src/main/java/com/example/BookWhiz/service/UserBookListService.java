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

import java.util.Optional;

@Service
public class UserBookListService {

    @Autowired
    private final UserBookListRepository userBookListRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired// Assuming this exists
    private final BookRepository bookRepository; // Assuming this exists

    public UserBookListService(UserBookListRepository userBookListRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.userBookListRepository = userBookListRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public void saveUserBookList(UserBookList userBookList) {
        userBookListRepository.save(userBookList);
    }

    public UserBookList getUserBookListById(Long id) {
        Optional<UserBookList> userBookListOptional = userBookListRepository.findById(id);
        return userBookListOptional.orElse(null);
    }

    public void addBookInUserBookList(Book book, UserBookList userBookList) {
        userBookList.getBooks().add(book);
    }

    public void saveUserInUserBookList(User user, UserBookList userBookList) {
        userBookList.setUser(user);
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
}
