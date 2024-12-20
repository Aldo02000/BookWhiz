import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class SingUpScreen extends StatefulWidget {
  const SingUpScreen({super.key});

  @override
  State<SingUpScreen> createState() => _SingUpScreenState();
}

class _SingUpScreenState extends State<SingUpScreen> {

  bool _obscuredPassword = true;
  bool _obscuredPassword2 = true;

  final TextEditingController _username = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passController = TextEditingController();
  final TextEditingController _repassController = TextEditingController();

  Future<void> _signup() async {
    final String email = _emailController.text;
    final String password = _passController.text;
    final String username = _username.text;

    if (_passController.text != _repassController.text) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Passwords do not match')),
      );
      return;
    }

    final url = Uri.parse('http://10.0.2.2:8080/auth/signup');

    try {
      final response = await http.post(
        url,
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'email': email,
          'password': password,
          'username': username,
        }),
      );

      if (response.statusCode == 200) {
        Navigator.pushNamed(
          context,
          '/verify',
          arguments: {'email': email},
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Signup failed: ${response.body}')),
        );
      }
    } catch (error) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('An error occurred: $error')),
      );
    }
  }

  // Regex pattern for email validation
  bool isValidEmail(String email) {
    // This regex matches most common email formats
    final emailRegex = RegExp(
        r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$");
    return emailRegex.hasMatch(email);
  }

  void _togglePasswrod() {
    setState(() {
      _obscuredPassword = !_obscuredPassword;
    });
  }

  void _togglePasswrod2() {
    setState(() {
      _obscuredPassword2 = !_obscuredPassword2;
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
            'Sign Up',
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
                    controller: _username,
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
                      hintText: 'Username',
                      suffixIcon: Icon(Icons.person)
                    ),
                    validator: (String? value) {
                      if (value == null || value.isEmpty) {
                        return "Please Enter Some Text";
                      }
                      return null;
                    },
                  ),
                ),
                Row(
                  children: [
                    Container(
                      width: 180,
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
                    Container(
                      width: 177,
                      margin: const EdgeInsets.fromLTRB(10, 5, 0, 10),
                      child: TextFormField(
                        controller: _repassController,
                        keyboardType: TextInputType.visiblePassword,
                        obscureText: _obscuredPassword2,
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
                          hintText: 'Confirm Password',
                          suffixIcon: Padding(
                            padding: const EdgeInsets.fromLTRB(0, 0, 4, 0),
                            child: GestureDetector(
                              onTap: _togglePasswrod2,
                              child: Icon(
                                _obscuredPassword2
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
                  ],
                ),
                 Padding(
                  padding: const EdgeInsets.fromLTRB(2, 20, 2, 20),
                  child: ElevatedButton(
                    style: TextButton.styleFrom(
                      minimumSize: Size(400, 50),
                      backgroundColor: Color(0xFF9F723B),
                      foregroundColor: Colors.white,
                    ),
                    onPressed: () {
                      _signup();
                    },
                    child: const Text(
                      style: TextStyle(
                        fontSize: 22,
                      ),
                      "Sign Up")),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text("Have an Account? "),
                    TextButton(
                      style: TextButton.styleFrom(
                        foregroundColor: Colors.brown,
                      ),
                      onPressed: (){
                        Navigator.pushNamed(context, '/login');
                      },
                      child: const Text("Log In"),
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