import 'package:book_whiz/library/favorite_genres.dart';
import 'package:book_whiz/library/next_books.dart';
import 'package:book_whiz/library/read_books.dart';
import 'package:flutter/material.dart';
import 'favorite_authors.dart';
import 'favorite_books.dart';
import 'favorite_genres.dart';

class LibraryScreen extends StatefulWidget {
  const LibraryScreen({super.key});

  @override
  State<LibraryScreen> createState() => _LibraryScreenState();
}

class _LibraryScreenState extends State<LibraryScreen> {

  final List<String> bookCategories = [
    "Favorites",
    "Want To Read",
    "Read"
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFFE1C39C),
        title: const Text("My Library"),
      ),
      body: Container(
        margin: EdgeInsets.fromLTRB(20, 30, 20, 10),
        width: 500,
        height: 700,
        child: Column(
          children: [
            Container(
              margin: EdgeInsets.fromLTRB(0, 0, 0, 15),
              child: const Text("My Books",
              style: TextStyle(
                  fontSize: 20
                )
              ),
            ),
            Container(
              decoration: BoxDecoration(
                borderRadius: BorderRadius.all(Radius.circular(10)),
                border: Border.all(
                    width: 2,
                    color: Colors.brown)
              ),
              height: 230,
              width: 450,
              child: ListView.builder(
                scrollDirection: Axis.vertical,
                itemCount: 3,
                itemBuilder: (context, index) {
                  return ListTile(
                    title: Container(
                      decoration: const BoxDecoration(
                          border: Border(
                              bottom: BorderSide(
                                  color: Colors.black,
                                  width: 0.7
                              )
                          )
                      ),
                      child: TextButton(
                        style: TextButton.styleFrom(
                          foregroundColor: const Color(0xFF352106),
                        ),
                        onPressed: () {
                          if (index == 0) {
                            // Navigate to the Favorites page
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const FavoriteScreen(),
                              ),
                            );
                          } else if (index == 1) {
                            // Navigate to the Favorites page
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const NextScreen(),
                              ),
                            );
                          } else if (index == 2) {
                            // Navigate to the Favorites page
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const ReadScreen(),
                              ),
                            );
                          }
                          else {
                            // Handle other cases or show a placeholder
                            ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(
                                content: Text("Page not implemented yet."),
                              ),
                            );
                          }
                        },
                        child: Row(
                          children: [
                            Container(
                              width: 280,
                              child: Text(
                                bookCategories[index],
                                style: const TextStyle(fontSize: 16),
                              ),
                            ),
                            const Icon(
                              Icons.arrow_forward_ios,
                              size: 17,
                            )
                          ],
                        ),
                      ),
                    ),
                  );
                },
              ),
            ),
            Container(
              margin: EdgeInsets.fromLTRB(0, 50, 0, 30),
              height: 100,
              width: 450,
              child:
              ListTile(
                title: Container(
                  decoration: const BoxDecoration(
                      border: Border(
                          bottom: BorderSide(
                              color: Colors.black,
                              width: 0.7
                          )
                      )
                  ),
                  child: TextButton(
                    style: TextButton.styleFrom(
                      foregroundColor: const Color(0xFF352106),
                    ),
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const FavoriteAuthorsScreen(),
                        ),
                      );
                    },
                    child: Row(
                      children: [
                        Container(
                          width: 280,
                          child: const Text(
                            "My Authors",
                            style: TextStyle(fontSize: 16),
                          ),
                        ),
                        const Icon(
                          Icons.arrow_forward_ios,
                          size: 17,
                        )
                      ],
                    ),
                  ),
                ),
              ),
            ),
            Container(
              height: 100,
              width: 450,
              child:
              ListTile(
                title: Container(
                  decoration: const BoxDecoration(
                      border: Border(
                          bottom: BorderSide(
                              color: Colors.black,
                              width: 0.7
                          )
                      )
                  ),
                  child: TextButton(
                    style: TextButton.styleFrom(
                      foregroundColor: const Color(0xFF352106),
                    ),
                    onPressed: () {
                      Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => const FavoriteGenresScreen(),
                        ),
                      );
                    },
                    child: Row(
                      children: [
                        Container(
                          width: 280,
                          child: const Text(
                            "My Genres",
                            style: TextStyle(fontSize: 16),
                          ),
                        ),
                        const Icon(
                          Icons.arrow_forward_ios,
                          size: 17,
                        )
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ],
        )
      ),
    );
  }

}