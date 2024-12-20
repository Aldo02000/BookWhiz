import 'package:book_whiz/genres/specific_genre.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import '../models/genre.dart';

class GenreScreen extends StatefulWidget {
  const GenreScreen({super.key});

  @override
  State<GenreScreen> createState() => _GenreScreenState();
}

class _GenreScreenState extends State<GenreScreen> {

  List<Genre> genres = [];

  @override
  void initState() {
    super.initState();
    fetchGenres();
  }

  Future<void> fetchGenres() async {
    // you can replace your api link with this link
    final response = await http.get(
        Uri.parse('http://10.0.2.2:8080/genres/allGenres'));
    if (response.statusCode == 200) {
      List<dynamic> jsonData = json.decode(response.body);
      setState(() {
        genres = jsonData.map((json) => Genre.fromJson(json)).toList();
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
        title: const Text("Genres"),
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
                        hintText: 'Search Genre',
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
              itemCount: genres.length,
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
                            builder: (context) => SpecificGenreScreen(
                              genreId: genres[index].id,
                              genreName: genres[index].name
                            ),
                          ),
                        );
                      },
                      child: Row(
                        children: [
                          Container(
                            width: 320,
                            child: Text(
                              genres[index].name,
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