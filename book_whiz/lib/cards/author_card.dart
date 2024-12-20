import 'package:book_whiz/models/author.dart';
import 'package:book_whiz/models/book.dart';
import 'package:flutter/material.dart';

class AuthorCard extends StatelessWidget {
  final Author author;

  AuthorCard({required this.author});

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(  // this is the coloumn
          children: [
            Text(author.name),
          ],
        ),
      ),
    );
  }
}