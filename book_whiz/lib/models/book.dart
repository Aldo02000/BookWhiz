import 'package:book_whiz/models/author.dart';

import 'genre.dart';

class Book {
  final int? id;
  final String title;
  final String? imageLink;
  final List<Author>? authors;
  final List<Genre>? genres;
  final String? isbn10;
  final String? isbn13;
  final String? publishingDate;
  final String? publishingHouse;
  final String? language;
  final String? pageCount;
  final String? description;


  Book({
    this.id,
    required this.title, // Required field
    this.imageLink,       // Optional field
    this.authors,         // Optional field
    this.genres,          // Optional field
    this.isbn10,          // Optional field
    this.isbn13,          // Optional field
    this.publishingDate,  // Optional field
    this.publishingHouse, // Optional field
    this.language,        // Optional field
    this.pageCount,
    this.description// Optional field
  });

  factory Book.fromJson(Map<String, dynamic> json) {
    return Book(
      id: json['id'],
      title: json['title'],
      imageLink: json['imageLink'],
      authors: List<Author>.from(
          json['authors'].map((authorJson) => Author.fromJson(authorJson))
      ),
      genres: List<Genre>.from(
          json['genres'].map((genreJson) => Genre.fromJson(genreJson))
      ),
      isbn10: json['isbn10'],
      isbn13: json['isbn13'],
      publishingDate: json['publishingDate'],
      publishingHouse: json['publishingHouse'],
      language: json['language'],
      pageCount: json['pageCount'],
      description: json['description']

    );
  }
}