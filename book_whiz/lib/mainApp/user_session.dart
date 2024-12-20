import 'dart:convert';

import 'package:book_whiz/models/user.dart';

import '../services/secure_storage.dart';
import '../utils/jwt_utils.dart';
import 'package:http/http.dart' as http;


Future<User?> fetchUserDetails() async {
  String? token = await SecureStorage.getToken();

  if (token != null && !JwtUtils.isTokenExpired(token)) {
    int? userId = JwtUtils.getUserId(token);

    if (userId != null) {
      final response = await http.get(Uri.parse('http://10.0.2.2:8080/user/$userId/details'));

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return User.fromJson(data);
      } else {
        print('Failed to fetch user details: ${response.statusCode}');
      }
    } else {
      print('Invalid userId');
    }
  } else {
    print('No valid token found or token expired');
  }
  return null;
}
