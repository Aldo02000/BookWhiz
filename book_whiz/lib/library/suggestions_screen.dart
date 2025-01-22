import 'package:flutter/material.dart';
import '../models/book.dart';
import '../cards/book_card.dart';

class SuggestionScreen extends StatelessWidget {
  final List<Book> books;

  const SuggestionScreen({super.key, required this.books});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFFE1C39C),
        title: const Text('Suggested Books'),
      ),
      body: books.isEmpty
          ? const Center(child: Text('No suggestions available'))
          : ListView.builder(
        itemCount: books.length,
        itemBuilder: (context, index) {
          return BookCard(book: books[index]);
        },
      ),
    );
  }
}
