import 'package:book_whiz/mainApp/user_session.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../cards/book_card.dart';
import '../models/book.dart';

class SpecificAuthorScreen extends StatefulWidget {
  final int authorId;
  final String authorName;

  SpecificAuthorScreen({Key? key, required this.authorId, required this.authorName}) : super(key: key);

  @override
  State<SpecificAuthorScreen> createState() => _SpecificAuthorScreenState();
}

class _SpecificAuthorScreenState extends State<SpecificAuthorScreen> {
  List<Book> books = [];
  int? userId;
  bool isBookmarked = false;

  Future<void> _checkIfBookmarked() async {
    final url = Uri.parse("http://10.0.2.2:8080/user/$userId/authorList/author/${widget.authorId}");
    try {
      final response = await http.get(url);
      if (response.statusCode == 200) {
        final result = jsonDecode(response.body); // Expecting `true` or `false`
        setState(() {
          isBookmarked = result == true;
        });
      } else {
        print("Failed to fetch bookmark status: ${response.statusCode}");
      }
    } catch (e) {
      print("Error during GET request: $e");
    }
  }

  Future<void> bookmarkAuthor() async {
    final url = Uri.parse("http://10.0.2.2:8080/user/$userId/authorList/author/${widget.authorId}");
    try {
      final response = await http.put(url);
      if (response.statusCode == 200) {
        setState(() {
          isBookmarked = true;
        });
        print("Successfully bookmarked: ${response.statusCode}");
      } else {
        print("Failed to bookmark: ${response.statusCode}");
      }
    } catch (e) {
      print("Error during PUT request: $e");
    }
  }

  Future<void> removeBookmark() async {
    final url = Uri.parse("http://10.0.2.2:8080/user/$userId/authorList/author/${widget.authorId}");
    try {
      final response = await http.delete(url);
      if (response.statusCode == 200) {
        setState(() {
          isBookmarked = false;
        });
        print("Successfully removed bookmark: ${response.statusCode}");
      } else {
        print("Failed to remove bookmark: ${response.statusCode}");
      }
    } catch (e) {
      print("Error during DELETE request: $e");
    }
  }

  @override
  void initState() {
    super.initState();
    fetchUserDetails().then((user) {
      if (user != null) {
        setState(() {
          userId = user.id;
        });
        fetchBooks();
        _checkIfBookmarked();
      }
    });
  }

  Future<void> fetchBooks() async {
    // you can replace your api link with this link
    final response = await http.get(
        Uri.parse('http://10.0.2.2:8080/books/search/author/${widget.authorId}'));
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
        title: Text('${widget.authorName}'),
        actions: [
          const Text(
            "Add to Favorites",
            style: TextStyle(
                fontSize: 10
            ),
          ),
          IconButton(
            icon: Icon(
              isBookmarked ? Icons.bookmark : Icons.bookmark_border,
              color: isBookmarked ? Colors.brown : Colors.brown,
            ),
            onPressed: () {
              if (isBookmarked) {
                removeBookmark();
              } else {
                bookmarkAuthor();
              }
            },
          ),
        ],
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