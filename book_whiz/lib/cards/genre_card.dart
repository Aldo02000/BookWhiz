import 'package:book_whiz/models/author.dart';
import 'package:book_whiz/models/book.dart';
import 'package:book_whiz/models/genre.dart';
import 'package:flutter/material.dart';

class GenreCard extends StatelessWidget {
  final Genre genre;

  GenreCard({required this.genre});

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(  // this is the coloumn
          children: [
            Text(genre.name),
          ],
        ),
      ),
    );
  }
}