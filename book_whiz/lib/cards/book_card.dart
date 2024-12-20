import 'package:book_whiz/books/specific_book.dart';
import 'package:book_whiz/models/book.dart';
import 'package:flutter/material.dart';

import '../authors/specific_author.dart';

class BookCard extends StatelessWidget {
  final Book book;

  BookCard({required this.book});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => SpecificBookScreen(
                bookId: book.id ?? 0,
            ),
          ),
        );
      },
      child: Card(
        margin: EdgeInsets.fromLTRB(5, 5, 5, 0),
        color: const Color(0xFFE8D9BE),
        child: Padding(
          padding: const EdgeInsets.all(8.0),
          child: Container(
            width: 400,
            height: 130,
            child: Row(// this is the coloumn
              children: [
                book.imageLink != null && book.imageLink!.isNotEmpty
                    ? Image.network(
                  book.imageLink!,
                  width: 90,
                  height: 110,
                  errorBuilder: (BuildContext context, Object error, StackTrace? stackTrace) {
                    return Image.asset(
                      'lib/assets/images/ImageNotExist.jpg', // Path to your local asset image
                      width: 90,
                      height: 110,
                    );
                  },
                )
                    : Image.asset(
                  'lib/assets/images/ImageNotExist.jpg', // Path to your local asset image
                  width: 90,
                  height: 110,
                ),
                Container(
                  width: 270,
                  margin: EdgeInsets.fromLTRB(15, 5, 0, 0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Container(
                        margin: EdgeInsets.fromLTRB(0, 0, 0, 0),
                        child: Text(
                          book.title,
                          style: const TextStyle(
                            fontSize: 17
                          ),
                        ),
                      ),
                      Row(
                        children: [
                          Container(
                            margin: EdgeInsets.fromLTRB(4, 5, 0, 0),
                            height: 70,
                            width: 150,
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                ...?book.authors?.map((author) =>
                                    InkWell(
                                      onTap: () {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder: (context) => SpecificAuthorScreen(
                                                authorName: author.name,
                                                authorId: author.id),
                                          ),
                                        );
                                      },
                                      child: Padding(
                                        padding: const EdgeInsets.all(1.0),
                                        child: Text(
                                          style: TextStyle(fontSize: 10.5),
                                          "${author.name},"
                                        ),
                                      ),
                                    )).toList()
                            ],),
                            ),
                        ],
                      ),
                    ],
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}