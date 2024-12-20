class Author {
  final int id;
  final String name;

  Author({required this.name, required this.id});

  factory Author.fromJson(Map<String, dynamic> json) {
    return Author(
      name: json['name'],
      id: json['id']
    );
  }
}