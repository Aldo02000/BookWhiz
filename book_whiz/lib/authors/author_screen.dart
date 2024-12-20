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

  @override
  void initState() {
    super.initState();
    fetchAuthors();
  }

  Future<void> fetchAuthors() async {
    // you can replace your api link with this link
    final response = await http.get(
        Uri.parse('http://10.0.2.2:8080/authors/allAuthors'));
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
                      textAlignVertical: TextAlignVertical.bottom,
                      decoration: InputDecoration(
                        fillColor: Colors.white,
                        filled: true,
                        focusedBorder: const OutlineInputBorder(
                            borderSide: BorderSide(
                              width: 2,
                              color: Color(0xFFCFB499),
                            ),
                            borderRadius: BorderRadius.all(Radius.circular(20))
                        ),
                        enabledBorder: const OutlineInputBorder(
                            borderSide: BorderSide(
                              width: 1,
                              color: Color(0xFFCFB499),
                            ),
                            borderRadius: BorderRadius.all(Radius.circular(20))
                        ),
                        hintText: 'Search Author',
                        suffixIcon: TextButton(
                          style: TextButton.styleFrom(
                            foregroundColor: Colors.brown,
                          ),
                            onPressed: () {},
                        child: const Icon(Icons.search)
                        ),
                      )
                    )
                ),
              ),
            ),
          ),
          Container(
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
        ],
      )
    );
  }

}