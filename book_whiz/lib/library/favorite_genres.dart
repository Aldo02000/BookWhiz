import 'package:book_whiz/cards/genre_card.dart';
import 'package:book_whiz/mainApp/user_session.dart';
import 'package:book_whiz/models/genre.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../genres/specific_genre.dart';

class FavoriteGenresScreen extends StatefulWidget {
  const FavoriteGenresScreen({super.key});

  @override
  State<FavoriteGenresScreen> createState() => _FavoriteGenresScreenScreenState();
}

class _FavoriteGenresScreenScreenState extends State<FavoriteGenresScreen> {
  List<Genre> genres = [];
  int? userId;

  @override
  void initState() {
    super.initState();
    fetchUserDetails().then((user) {
      if (user != null) {
        setState(() {
          userId = user.id;
        });
        fetchGenres();
      }
    });
  }

  Future<void> fetchGenres() async {
    // you can replace your api link with this link
    final response = await http.get(
        Uri.parse('http://10.0.2.2:8080/user/$userId/genreList/genres'));
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
        title: const Text('Favorite Genres'),
      ),
      body: userId == null
          ? const Center(child: CircularProgressIndicator())
          : genres.isEmpty
          ? const Center(child: Text('No authors found'))
          : Container(
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
    );
  }

}