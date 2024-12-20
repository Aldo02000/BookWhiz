import 'package:book_whiz/genres/specific_genre.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:intl/intl.dart';
import '../authors/specific_author.dart';
import '../mainApp/user_session.dart';
import '../models/Review.dart';
import '../models/book.dart';

class SpecificBookScreen extends StatefulWidget {
  final int bookId;
  SpecificBookScreen({Key? key, required this.bookId}) : super(key: key);

  @override
  State<SpecificBookScreen> createState() => _SpecificBookScreenState();
}

class _SpecificBookScreenState extends State<SpecificBookScreen> {

  String selectedLibrary = 'FAVORITES';
  double? averageRating;
  late Book book;
  int? userId;
  List<Review> reviews = [];
  bool isLoading = true;
  bool _reviewExists = false;
  String? username;
  int _selectedRating = 0; // Holds the selected star rating
  final TextEditingController _reviewController = TextEditingController();

  Future<double?> fetchAverageRating(int bookId) async {
    final url = Uri.parse('http://10.0.2.2:8080/reviews/book/${widget.bookId}/averageRating');
    try {
      final response = await http.get(url);

      if (response.statusCode == 200) {
        // Parse the JSON response. Assume the API returns a numeric value.
        final double averageRating = double.parse(response.body);
        return averageRating;
      } else {
        print('Failed to fetch average rating: ${response.reasonPhrase}');
        return null;
      }
    } catch (e) {
      print('Error fetching average rating: $e');
      return null;
    }
  }


  Future<bool> isBookInLibrary(String library) async {
    final url = Uri.parse('http://10.0.2.2:8080/user/$userId/bookList/$library/book/${widget.bookId}');

    try {
      final response = await http.get(url);

      if (response.body == "true") {
        return true; // Book exists in the library
      } else if (response.body == "false") {
        return false; // Book is not in the library
      } else {
        throw Exception('Failed to check book: ${response.statusCode}');
      }
    } catch (error) {
      print('Error checking book in library: $error');
      return false;
    }
  }

  Future<void> addBook() async {
    bool isInLibrary = await isBookInLibrary(selectedLibrary);

    if (isInLibrary) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Book is already in $selectedLibrary!')),
      );
    } else {
      await addBookToLibrary(selectedLibrary);
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Book added to $selectedLibrary!')),
      );
    }
  }

  Future<void> addBookToLibrary(String library) async {
    final url = Uri.parse('http://10.0.2.2:8080/user/$userId/bookList/$library/book/${widget.bookId}');

    try {
      final response = await http.put(url);

      if (response.statusCode == 200) {
        print('Book successfully added to $library');
      } else {
        throw Exception('Failed to add book: ${response.statusCode}');
      }
    } catch (error) {
      print('Error adding book to library: $error');
    }
  }


  Future<void> fetchUsername() async {
    final url = Uri.parse('http://10.0.2.2:8080/user/$userId/username');

    try {
      final response = await http.get(url);

      if (response.statusCode == 200) {
        username = response.body; // Assuming the API returns the username as plain text
      } else {
        throw Exception('Failed to fetch username: ${response.statusCode}');
      }
    } catch (error) {
      print('Error fetching username: $error');
    }
  }

  Future<void> _submitReview() async {
    String content = _reviewController.text;
    int rating = _selectedRating;

    if (rating == 0 || content.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Please provide both rating and review content')),
      );
      return;
    }

    final url =
    Uri.parse('http://10.0.2.2:8080/reviews/book/${widget.bookId}/user/$userId/content/$content/rating/$rating');

    try {
      final response = await http.put(url);

      if (response.statusCode == 200) {
        setState(() {
          String formattedDate = DateFormat('yyyy-MM-dd HH:mm:ss').format(DateTime.now());
          reviews.add(Review(
            content: content,
            rating: rating,
            createdDate: formattedDate,
            username: username // Use the formatted date string
          ));
          _reviewExists = true; // Prevent further submissions
        });
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Review submitted successfully!')),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to submit review: ${response.reasonPhrase}')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    }
  }

  Future<void> fetchBook(int id) async {
    final url = Uri.parse('http://10.0.2.2:8080/books/book/$id');

    try {
      final response = await http.get(url);

      if (response.statusCode == 200) {
        // Parse the JSON response
        final jsonData = json.decode(response.body);
        setState(() {
          book = Book.fromJson(jsonData);
        });
      } else {
        throw Exception('Failed to load book, status code: ${response.statusCode}');
      }
    } catch (error) {
      throw Exception('Error fetching book: $error');
    }
  }

  Future<void> fetchReviews() async {
    // you can replace your api link with this link
    final response = await http.get(
        Uri.parse('http://10.0.2.2:8080/reviews/book/${widget.bookId}'));
    if (response.statusCode == 200) {
      List<dynamic> jsonData = json.decode(response.body);
      setState(() {
        reviews = jsonData.map((json) => Review.fromJson(json)).toList();
      });
    } else {
      // Handle error if needed
    }
  }

  Future<void> fetchBookAndReviews() async {
    try {
      // Fetch the book first
      await fetchBook(widget.bookId);
      // Then fetch reviews
      await fetchReviews();
    } catch (e) {
      print('Error: $e');
    } finally {
      setState(() {
        isLoading = false;
      });
    }
  }

  Future<void> _checkIfReviewExists() async {
    final url = Uri.parse('http://10.0.2.2:8080/reviews/review/book/${widget.bookId}/user/$userId');
    try {
      final response = await http.get(url);

      if (response.statusCode == 200) {
        setState(() {
          _reviewExists = true;
        });
      } else if (response.statusCode == 403) {
        setState(() {
          _reviewExists = false; // User has not reviewed yet
        });
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Error checking review: ${response.reasonPhrase}')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
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
        fetchUsername();
      }
    });
    fetchBookAndReviews().then((_) {
      // Call _checkIfReviewExists only after book and reviews are fetched
      _checkIfReviewExists();
    });
    fetchAverageRating(widget.bookId).then((rating) {
      if (rating != null) {
        setState(() {
          averageRating = rating;
        });
      }
    });

  }

  @override
  Widget build(BuildContext context) {
    final List<String> libraries = ['FAVORITES', 'NEXT', 'READ'];

    if (isLoading) {
      return Scaffold(
        appBar: AppBar(
          backgroundColor: const Color(0xFFE1C39C),
          title: Text('Loading...'),
        ),
        body: const Center(child: CircularProgressIndicator()),
      );
    } else {
      return Scaffold(
        appBar: AppBar(
          backgroundColor: const Color(0xFFE1C39C),
          title: Text(book.title),
        ),
        body: SingleChildScrollView(
          child: Container(
            margin: EdgeInsets.fromLTRB(0, 20, 0, 0),
            child: Column(
              children: [
                book.imageLink != null && book.imageLink!.isNotEmpty
                    ? Image.network(
                  book.imageLink!,
                  width: 140,
                  height: 160,
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
                  margin: const EdgeInsets.fromLTRB(15, 12, 0, 8),
                  child: Text(
                    book.title,
                    style: const TextStyle(fontSize: 20),
                  ),
                ),
                Container(
                  margin: EdgeInsets.fromLTRB(0, 0, 0, 10),
                  child: Column(
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
                          child: Text(
                              style: TextStyle(fontSize: 15),
                              "${author.name}"
                          ),
                        )).toList()
                    ],
                  ),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text('Genres:  '),
                    ...?book.genres?.map((genre) => InkWell(
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => SpecificGenreScreen(
                                genreName: genre.name,
                                genreId: genre.id),
                          ),
                        );
                      },
                      child: Padding(
                        padding: const EdgeInsets.all(1.0),
                        child: Text(
                            style: TextStyle(fontSize: 14),
                            "${genre.name},"
                        ),
                      ),
                    )).toList(),
                  ],
                ),
                Text("ISBN10: ${book.isbn10 ?? "Unknown"}"),
                Text("ISBN13: ${book.isbn13 ?? "Unknown"}"),
                Text("Publishing Date: ${book.publishingDate ?? "Unknown"}"),
                Text("Publishing House: ${book.publishingHouse ?? "Unknown"}"),
                Text("Language: ${book.language ?? "Unknown"}"),
                Text("Page Count: ${book.pageCount ?? "Unknown"}"),
                Container(
                  margin: EdgeInsets.fromLTRB(0, 20, 0, 0),
                  width: 360,
                  child: Text(
                    "${book.description ?? "No Description"}  ",
                    textAlign: TextAlign.justify,
                    style: TextStyle(fontSize: 15),
                  )
                ),
                Column(
                  children: [
                    DropdownButton<String>(
                      value: selectedLibrary,
                      onChanged: (String? value) {
                        if (value != null) {
                          setState(() {
                            selectedLibrary = value; // Update the selected library
                          });
                          print('Selected library: $selectedLibrary'); // Debug print
                        }
                      },
                      items: ['FAVORITES', 'NEXT', 'READ']
                          .map((library) => DropdownMenuItem<String>(
                        value: library,
                        child: Text(library),
                      ))
                          .toList(),
                    ),
                    ElevatedButton(
                      onPressed: addBook,
                      child: Text('Add to $selectedLibrary'),
                    ),
                  ],
                ),
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        'Rate the Book:',
                        style: TextStyle(fontSize: 18),
                      ),
                      Row(
                        children: List.generate(5, (index) {
                          return IconButton(
                            icon: Icon(
                              Icons.star,
                              color: _selectedRating > index ? Colors.orange : Colors.grey,
                            ),
                            onPressed: _reviewExists
                                ? null // Disable star selection if review exists
                                : () {
                              setState(() {
                                _selectedRating = index + 1;
                              });
                            },
                          );
                        }),
                      ),
                      SizedBox(height: 20),
                      TextField(
                        controller: _reviewController,
                        enabled: !_reviewExists, // Disable input if review exists
                        decoration: const InputDecoration(
                          border: OutlineInputBorder(),
                          labelText: 'Write your review here',
                        ),
                        maxLines: 5,
                      ),
                      SizedBox(height: 20),
                      Center(
                        child: ElevatedButton(
                          onPressed: _reviewExists ? null : _submitReview, // Disable button if review exists
                          child: Text(_reviewExists ? 'Review Already Submitted' : 'Submit Review'),
                        ),
                      ),
                    ],
                  ),
                ),
                const Divider(),
                Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        'Average Rating:',
                        style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                      SizedBox(height: 8),
                      averageRating != null
                          ? Text(
                        averageRating!.toStringAsFixed(1),
                        style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                      )
                          : Text(
                        'No ratings yet',
                        style: TextStyle(fontSize: 16, fontStyle: FontStyle.italic),
                      ),
                    ],
                  ),
                ),
                const Divider(),
                const Text(
                  "Reviews",
                  style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
                reviews != null && reviews!.isNotEmpty
                    ? ListView.builder(
                  shrinkWrap: true, // Prevent infinite height
                  physics: NeverScrollableScrollPhysics(), // Use parent's scroll
                  itemCount: reviews!.length,
                  itemBuilder: (context, index) {
                    final review = reviews![index];
                    return Container(
                      margin: EdgeInsets.symmetric(vertical: 8.0),
                      padding: EdgeInsets.all(25.0),
                      decoration: BoxDecoration(
                        color: Colors.grey[200],
                        borderRadius: BorderRadius.circular(8.0),
                      ),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                              review.username ?? 'Unknown User',
                              style: TextStyle(
                                  fontSize: 12, color: Colors.grey),
                          ),
                          Text(
                            review.content,
                            textAlign: TextAlign.justify,
                            style: TextStyle(fontSize: 14),
                          ),
                          SizedBox(height: 8.0),
                          Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(
                                "Rating: ${review.rating}/5",
                                style: TextStyle(
                                    fontSize: 12, fontWeight: FontWeight.bold),
                              ),
                              Text(
                                "Date: ${review.createdDate}",
                                style: TextStyle(
                                    fontSize: 12, color: Colors.grey),
                              ),
                            ],
                          ),
                        ],
                      ),
                    );
                  },
                )
                    : Text(
                  "No reviews available.",
                  style: TextStyle(fontSize: 14, color: Colors.grey),
                ),
              ],
            ),
          ),
        ),
      );
    }
  }
}