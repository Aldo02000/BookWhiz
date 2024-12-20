class Review {
  final int? id;
  final String content;
  final String createdDate;
  final int rating;
  final String? username;
  
  Review({this.id, required this.content, required this.createdDate, required this.rating, this.username});

  factory Review.fromJson(Map<String, dynamic> json) {
    return Review(
        id: json['id'],
        content: json['content'],
        createdDate: json['createdDate'],
        rating: json['rating'],
        username: json['username']
    );
  }
}