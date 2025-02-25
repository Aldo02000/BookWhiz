import 'package:book_whiz/authors/specific_author.dart';
import 'package:book_whiz/models/author.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class AuthorScreen extends StatefulWidget {
  const AuthorScreen({super.key});

  @override
  State<AuthorScreen> createState() => _AuthorScreenState();
}

class _AuthorScreenState extends State<AuthorScreen> {
  List<Author> authors = [];
  final TextEditingController _searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    fetchAuthors(); // Fetch all authors on init
  }

  Future<void> fetchAuthors({String? searchQuery}) async {
    String url = 'http://10.0.2.2:8080/authors';
    if (searchQuery != null && searchQuery.isNotEmpty) {
      url += '/search?name=${Uri.encodeComponent(searchQuery)}';
    } else {
      url += '/allAuthors'; // Fetch all authors if no search query
    }

    final response = await http.get(Uri.parse(url));

    if (response.statusCode == 200) {
      List<dynamic> jsonData = json.decode(response.body);
      setState(() {
        authors = jsonData.map((json) => Author.fromJson(json)).toList();
      });
    } else {
      // Handle error
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFFE1C39C),
        title: const Text("Authors"),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
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
                      hintText: 'Search Author',
                      suffixIcon: Row(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          if (_searchController.text.isNotEmpty)
                            IconButton(
                              icon: const Icon(Icons.clear),
                              onPressed: () {
                                _searchController.clear();
                                fetchAuthors(); // Reload all authors when cleared
                              },
                            ),
                          IconButton(
                            icon: const Icon(Icons.search),
                            onPressed: () {
                              fetchAuthors(searchQuery: _searchController.text);
                            },
                          ),
                        ],
                      ),
                    ),
                    onChanged: (value) {
                      if (value.isEmpty) {
                        fetchAuthors(); // Show all authors if input is empty
                      }
                    },
                  ),
                ),
              ),
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: authors.length,
              itemBuilder: (context, index) {
                return ListTile(
                  title: Container(
                    decoration: const BoxDecoration(
                      border: Border(
                        bottom: BorderSide(
                          color: Colors.black,
                          width: 0.7,
                        ),
                      ),
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
                              authorId: authors[index].id,
                            ),
                          ),
                        );
                      },
                      child: Row(
                        children: [
                          SizedBox(
                            width: 320,
                            child: Text(
                              authors[index].name,
                              style: const TextStyle(fontSize: 16),
                            ),
                          ),
                          const Icon(
                            Icons.arrow_forward_ios,
                            size: 17,
                          ),
                        ],
                      ),
                    ),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
