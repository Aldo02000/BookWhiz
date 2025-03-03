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

  final TextEditingController _searchController = TextEditingController();
  Future<List<Book>>? _booksFuture;

  @override
  void initState() {
    super.initState();
    _booksFuture = fetchTopRatedBooks(); // Load top-rated books initially
  }

  Future<List<Book>> fetchBooks({String? searchQuery}) async {
    final String url = searchQuery != null && searchQuery.isNotEmpty
        ? 'http://10.0.2.2:8080/books/search/title?title=$searchQuery'
        : 'http://10.0.2.2:8080/books/topRated';

    final response = await http.get(Uri.parse(url));
    if (response.statusCode == 200) {
      final List<dynamic> bookList = json.decode(response.body);
      return bookList.map((json) => Book.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load books');
    }
  }

  void _performSearch() {
    setState(() {
      _booksFuture = fetchBooks(searchQuery: _searchController.text);
    });
  }

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
        title: Text('Books'),
      ),
      body: Column(
        children: [
          Center(
            child: Container(
              margin: const EdgeInsets.fromLTRB(0, 10, 0, 20),
              width: 300,
              height: 40,
              child: Center(
                child: Form(
                  child: TextFormField(
                    controller: _searchController,
                    textAlignVertical: TextAlignVertical.bottom,
                    decoration: InputDecoration(
                      fillColor: Colors.white,
                      filled: true,
                      focusedBorder: const OutlineInputBorder(
                        borderSide: BorderSide(
                          width: 2,
                          color: Color(0xFFCFB499),
                        ),
                        borderRadius: BorderRadius.all(Radius.circular(20)),
                      ),
                      enabledBorder: const OutlineInputBorder(
                        borderSide: BorderSide(
                          width: 1,
                          color: Color(0xFFCFB499),
                        ),
                        borderRadius: BorderRadius.all(Radius.circular(20)),
                      ),
                      hintText: 'Search By Title',
                      suffixIcon: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          if (_searchController.text.isNotEmpty)
                            IconButton(
                              icon: const Icon(Icons.clear),
                              onPressed: () {
                                _searchController.clear();
                                setState(() {
                                  _booksFuture = fetchTopRatedBooks();
                                });
                              },
                            ),
                          IconButton(
                            icon: const Icon(Icons.search),
                            onPressed: _performSearch,
                          ),
                        ],
                      ),
                    ),
                    onChanged: (value) {
                      if (value.isEmpty) {
                        setState(() {
                          _booksFuture = fetchTopRatedBooks();
                        });
                      }
                    },
                  ),
                ),
              ),
            ),
          ),
          Expanded(
            child: FutureBuilder<List<Book>>(
              future: _booksFuture,
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return Center(child: CircularProgressIndicator());
                } else if (snapshot.hasError) {
                  return Center(child: Text('Error: ${snapshot.error}'));
                } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                  return Center(child: Text('No books found'));
                }

                final books = snapshot.data!;
                return ListView.builder(
                  itemCount: books.length,
                  itemBuilder: (context, index) {
                    final book = books[index];
                    return BookCard(
                      book: book,
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }

}
