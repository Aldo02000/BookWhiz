import 'package:book_whiz/library/suggestions_screen.dart';
import 'package:book_whiz/mainApp/user_session.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../cards/book_card.dart';
import '../models/book.dart';

class FavoriteScreen extends StatefulWidget {
  const FavoriteScreen({super.key});

  @override
  State<FavoriteScreen> createState() => _FavoriteScreenState();
}

class _FavoriteScreenState extends State<FavoriteScreen> {
  List<Book> books = [];
  int? userId;

  @override
  void initState() {
    super.initState();
    fetchUserDetails().then((user) {
      if (user != null) {
        setState(() {
          userId = user.id;
        });
        fetchBooks();
      }
    });
  }

  Future<void> fetchBooks() async {
    // you can replace your api link with this link
    final response = await http.get(
        Uri.parse('http://10.0.2.2:8080/user/$userId/bookList/FAVORITES/books'));
    if (response.statusCode == 200) {
      List<dynamic> jsonData = json.decode(response.body);
      setState(() {
        books = jsonData.map((json) => Book.fromJson(json)).toList();
      });
    } else {
      // Handle error if needed
    }
  }

  Future<void> fetchSuggestions(List<Book> favoriteBooks) async {
    final List<String> favoriteBookTitles =
    favoriteBooks.map((book) => book.title).toList();

    final response = await http.post(
      Uri.parse('http://10.0.2.2:8080/ai/suggest'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode(favoriteBookTitles),
    );

    if (response.statusCode == 200) {
      List<dynamic> jsonData = json.decode(response.body);
      List<Book> suggestedBooks =
      jsonData.map((json) => Book.fromJson(json)).toList();

      // Navigate to the suggestion screen
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => SuggestionScreen(books: suggestedBooks),
        ),
      );
    } else {
      // Handle the error appropriately
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Failed to fetch suggestions')),
      );
    }
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFFE1C39C),
        title: const Text('Favorite Books'),
      ),
      body: userId == null
          ? const Center(child: CircularProgressIndicator())
          : Column(
        children: [
          Expanded(
            child: books.isEmpty
                ? const Center(child: Text('No books found'))
                : ListView.builder(
              itemCount: books.length,
              itemBuilder: (context, index) {
                return BookCard(book: books[index]);
              },
            ),
          ),
          ElevatedButton(
            onPressed: books.isNotEmpty
                ? () => fetchSuggestions(books)
                : null,
            child: const Text('Get Suggestions'),
          ),
        ],
      ),
    );
  }


}