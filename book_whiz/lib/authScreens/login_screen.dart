import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import '../services/secure_storage.dart';

class LogInScreen extends StatefulWidget {
  const LogInScreen({super.key});

  @override
  State<LogInScreen> createState() => _LogInScreenState();
}

class _LogInScreenState extends State<LogInScreen> {

  bool _obscuredPassword = true;

  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passController = TextEditingController();

  Future<void> _login() async {
    final String email = _emailController.text;
    final String password = _passController.text;

    final url = Uri.parse('http://10.0.2.2:8080/auth/login');

    try {
      final response = await http.post(
        url,
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'email': email,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        String? token = data['token']; // Extract token from response
        await SecureStorage.saveToken(token!); // Save token to SecureStorage
        Navigator.pushReplacementNamed(context, '/');
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Login failed: ${response.body}')),
        );
      }
    } catch (error) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('An error occurred: $error')),
      );
    }
  }

  void _togglePasswrod() {
    setState(() {
      _obscuredPassword = !_obscuredPassword;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
        title: const Padding(
          padding: EdgeInsets.symmetric(horizontal: 30),
          child: Text(
            'Log In',
            style: TextStyle(
              fontSize: 30,
              color: Color(0xFF8A6D47),
            ),
          ),
        ),
      ),
      body: Padding(
        padding: const EdgeInsets.all(40),
        child: Form(
            child: Column(
              children: [
                Container(
                  margin: const EdgeInsets.symmetric(vertical: 5),
                  child: TextFormField(
                    controller: _emailController,
                    decoration: const InputDecoration(
                      focusedBorder: OutlineInputBorder(
                          borderSide: BorderSide(
                            width: 2.5,
                            color: Color(0xFFCFB499),
                          ),
                          borderRadius: BorderRadius.all(Radius.circular(10))
                      ),
                      enabledBorder: OutlineInputBorder(
                          borderSide: BorderSide(
                            width: 1,
                            color: Color(0xFFCFB499),
                          ),
                          borderRadius: BorderRadius.all(Radius.circular(10))
                      ),
                      hintText: 'Email',
                      suffixIcon: Icon(Icons.email),
                    ),
                    validator: (String? value) {
                      if (value == null || value.isEmpty) {
                        return "Please Enter Some Text";
                      }
                      return null;
                    },
                  ),
                ),
                Container(
                  margin: const EdgeInsets.symmetric(vertical: 5),
                  child: TextFormField(
                    controller: _passController,
                    keyboardType: TextInputType.visiblePassword,
                    obscureText: _obscuredPassword,
                    decoration: InputDecoration(
                      focusedBorder: const OutlineInputBorder(
                          borderSide: BorderSide(
                            width: 2.5,
                            color: Color(0xFFCFB499),
                          ),
                          borderRadius: BorderRadius.all(Radius.circular(10))
                      ),
                      enabledBorder: const OutlineInputBorder(
                          borderSide: BorderSide(
                            width: 1,
                            color: Color(0xFFCFB499),
                          ),
                          borderRadius: BorderRadius.all(Radius.circular(10))
                      ),
                      hintText: 'Password',
                      suffixIcon: Padding(
                        padding: const EdgeInsets.fromLTRB(0, 0, 4, 0),
                        child: GestureDetector(
                          onTap: _togglePasswrod,
                          child: Icon(
                            _obscuredPassword
                                ? Icons.visibility_rounded
                                : Icons.visibility_off_rounded,
                            size: 24,
                          ),
                        ),
                      ),
                    ),
                    validator: (String? value) {
                      if (value == null || value.isEmpty) {
                        return "Please Enter Some Text";
                      }
                      return null;
                    },
                  ),
                ),

                Padding(
                  padding: const EdgeInsets.fromLTRB(2, 20, 2, 20),
                  child: ElevatedButton(
                      style: TextButton.styleFrom(
                        minimumSize: const Size(400, 50),
                        backgroundColor: const Color(0xFF9F723B),
                        foregroundColor: Colors.white,
                      ),
                      onPressed: () {
                        _login();
                      },
                      child: const Text(
                          style: TextStyle(
                            fontSize: 22,
                          ),
                          "Log In")),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text("Don't Have an Account? "),
                    TextButton(
                      style: TextButton.styleFrom(
                        foregroundColor: Colors.brown,
                      ),
                      onPressed: (){
                        Navigator.pushNamed(context, '/signup');
                      },
                      child: const Text("Sign Up"),
                    )
                  ],
                )
              ],
            )
        ),
      ),
      bottomNavigationBar: Image.asset(
        "lib/assets/images/picture.png",
      ),
    );
  }

}