class Genre {
  final int id;
  final String name;

  Genre({required this.name, required this.id});

  factory Genre.fromJson(Map<String, dynamic> json) {
    return Genre(
      id: json['id'],
      name: json['name'],
    );
  }
}