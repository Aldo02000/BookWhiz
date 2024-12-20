import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class VerificationScreen extends StatefulWidget {
  const VerificationScreen({super.key});

  @override
  State<VerificationScreen> createState() => _VerificationScreenState();
}

class _VerificationScreenState extends State<VerificationScreen> {

  final TextEditingController _verificationController = TextEditingController();

  Future<void> _verification() async {
    final arguments = ModalRoute.of(context)!.settings.arguments as Map;
    final email = arguments['email'];
    final String verificationCode = _verificationController.text;

    final url = Uri.parse('http://10.0.2.2:8080/auth/verify');

    try {
      final response = await http.post(
        url,
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode({
          'email': email,
          'verificationCode': verificationCode,
        }),
      );

      if (response.statusCode == 200) {
        print('Verification successful');
        Navigator.pushReplacementNamed(context, '/');
      } else {
        print('Verification failed: ${response.body}');
      }
    } catch (error) {
      print('Error during verification: $error');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
        title: const Padding(
          padding: EdgeInsets.symmetric(horizontal: 30),
          child: Text(
            'Verify',
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
                    controller: _verificationController,
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
                      hintText: 'Verification Code',
                      suffixIcon: Icon(Icons.verified),
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
                        _verification();
                      },
                      child: const Text(
                          style: TextStyle(
                            fontSize: 22,
                          ),
                          "Verify")),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Text("Haven't Received the Code? "),
                    TextButton(
                      style: TextButton.styleFrom(
                        foregroundColor: Colors.brown,
                      ),
                      onPressed: (){
                        Navigator.pushNamed(context, '/');
                      },
                      child: const Text("Resend"),
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