import 'package:book_whiz/cards/book_card.dart';
import 'package:book_whiz/mainApp/user_session.dart';
import 'package:book_whiz/models/book.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {

  Future<List<Book>> fetchTopRatedBooks() async {
    final response = await http.get(Uri.parse('http://10.0.2.2:8080/books/topRated'));

    if (response.statusCode == 200) {
      final List<dynamic> bookList = json.decode(response.body);
      return bookList.map((json) => Book.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load top-rated books');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFFE1C39C),
        title: Text('Top Rated Books'),
      ),
      body: FutureBuilder<List<Book>>(
        future: fetchTopRatedBooks(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return Center(child: Text('No top-rated books found'));
          }

          final books = snapshot.data!;
          return ListView.builder(
            itemCount: books.length,
            itemBuilder: (context, index) {
              final book = books[index];
              return BookCard(
                book: book, // Assuming BookCard takes a `book` parameter
              );
            },
          );
        },
      ),
    );
  }

}
