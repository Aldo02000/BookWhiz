import 'package:book_whiz/mainApp/user_session.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../cards/book_card.dart';
import '../models/book.dart';

class NextScreen extends StatefulWidget {
  const NextScreen({super.key});

  @override
  State<NextScreen> createState() => _NextScreenState();
}

class _NextScreenState extends State<NextScreen> {
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
        Uri.parse('http://10.0.2.2:8080/user/$userId/bookList/NEXT/books'));
    if (response.statusCode == 200) {
      List<dynamic> jsonData = json.decode(response.body);
      setState(() {
        books = jsonData.map((json) => Book.fromJson(json)).toList();
      });
    } else {
      // Handle error if needed
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFFE1C39C),
        title: const Text('Want To Read'),
      ),
      body: userId == null
          ? const Center(child: CircularProgressIndicator())
          : books.isEmpty
          ? const Center(child: Text('No books found'))
          : ListView.builder(
        itemCount: books.length,
        itemBuilder: (context, index) {
          return BookCard(book: books[index]);
        },
      ),
    );
  }

}