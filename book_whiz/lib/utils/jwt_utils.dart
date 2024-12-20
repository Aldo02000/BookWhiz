import 'package:jwt_decoder/jwt_decoder.dart';

class JwtUtils {
  static bool isTokenExpired(String token) {
    return JwtDecoder.isExpired(token);
  }

  static Map<String, dynamic> decodeToken(String token) {
    return JwtDecoder.decode(token);
  }

  static int? getUserId(String token) {
    final decodedToken = decodeToken(token);
    return decodedToken['userId']; // Extract userId from the token
  }

}
