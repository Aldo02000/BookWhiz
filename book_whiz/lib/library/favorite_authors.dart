import 'package:book_whiz/cards/author_card.dart';
import 'package:book_whiz/mainApp/user_session.dart';
import 'package:book_whiz/models/author.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../authors/specific_author.dart';

class FavoriteAuthorsScreen extends StatefulWidget {
  const FavoriteAuthorsScreen({super.key});

  @override
  State<FavoriteAuthorsScreen> createState() => _FavoriteAuthorsScreenState();
}

class _FavoriteAuthorsScreenState extends State<FavoriteAuthorsScreen> {
  List<Author> authors = [];
  int? userId;

  @override
  void initState() {
    super.initState();
    fetchUserDetails().then((user) {
      if (user != null) {
        setState(() {
          userId = user.id;
        });
        fetchAuthors();
      }
    });
  }

  Future<void> fetchAuthors() async {
    // you can replace your api link with this link
    final response = await http.get(
        Uri.parse('http://10.0.2.2:8080/user/$userId/authorList/authors'));
    if (response.statusCode == 200) {
      List<dynamic> jsonData = json.decode(response.body);
      setState(() {
        authors = jsonData.map((json) => Author.fromJson(json)).toList();
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
        title: const Text('Favorite Authors'),
      ),
      body: userId == null
          ? const Center(child: CircularProgressIndicator())
          : authors.isEmpty
          ? const Center(child: Text('No authors found'))
          : Container(
        height: 630,
        child: ListView.builder(
          scrollDirection: Axis.vertical,
          itemCount: authors.length,
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
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => SpecificAuthorScreen(
                            authorName: authors[index].name,
                            authorId: authors[index].id),
                      ),
                    );
                  },
                  child: Row(
                    children: [
                      Container(
                        width: 320,
                        child: Text(
                          authors[index].name,
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
    );
  }

}