import 'package:flutter/material.dart';
import 'package:book_whiz/services/secure_storage.dart';
import 'package:book_whiz/utils/jwt_utils.dart';

class AuthCheckScreen extends StatefulWidget {
  @override
  _AuthCheckScreenState createState() => _AuthCheckScreenState();
}

class _AuthCheckScreenState extends State<AuthCheckScreen> {
  @override
  void initState() {
    super.initState();
    _checkAuthentication();
  }

  Future<void> _checkAuthentication() async {
    String? token = await SecureStorage.getToken();

    if (token != null && !JwtUtils.isTokenExpired(token)) {
      Navigator.pushReplacementNamed(context, '/success');
    } else {
      Navigator.pushReplacementNamed(context, '/login');
    }
  }

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: CircularProgressIndicator(),
      ),
    );
  }
}
