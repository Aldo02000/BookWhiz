import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class SecureStorage {
  static final _storage = FlutterSecureStorage();

  static Future<void> saveToken(String token) async {
    try {
      await _storage.write(key: 'token', value: token);
    } catch (e) {
      print("Error saving token: $e");
    }
  }

  static Future<String?> getToken() async {
    try {
      return await _storage.read(key: 'token');
    } catch (e) {
      print("Error getting token: $e");
      return null;
    }
  }

  static Future<void> deleteToken() async {
    try {
      await _storage.delete(key: 'token');
    } catch (e) {
      print("Error deleting token: $e");
    }
  }
}

