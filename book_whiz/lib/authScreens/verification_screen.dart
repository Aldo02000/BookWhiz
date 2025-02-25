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
    double screenWidth = MediaQuery.of(context).size.width;
    double screenHeight = MediaQuery.of(context).size.height;

    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
        title: Padding(
          padding: EdgeInsets.symmetric(horizontal: screenWidth * 0.073),
          child: Text(
            'Verify',
            style: TextStyle(
              fontSize: screenHeight * 0.034,
              color: const Color(0xFF8A6D47),
            ),
          ),
        ),
      ),
      body: Padding(
        padding: EdgeInsets.all(screenWidth * 0.097),
        child: Form(
          child: Column(
            children: [
              Container(
                margin: EdgeInsets.symmetric(vertical: screenHeight * 0.0056),
                child: TextFormField(
                  controller: _verificationController,
                  decoration: const InputDecoration(
                    focusedBorder: OutlineInputBorder(
                      borderSide: BorderSide(
                        width: 2.5,
                        color: Color(0xFFCFB499),
                      ),
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                    ),
                    enabledBorder: OutlineInputBorder(
                      borderSide: BorderSide(
                        width: 1,
                        color: Color(0xFFCFB499),
                      ),
                      borderRadius: BorderRadius.all(Radius.circular(10)),
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
                padding: EdgeInsets.fromLTRB(
                  screenWidth * 0.005,
                  screenHeight * 0.022,
                  screenWidth * 0.005,
                  screenHeight * 0.022,
                ),
                child: ElevatedButton(
                  style: TextButton.styleFrom(
                    minimumSize: Size(
                      screenWidth * 0.97,
                      screenHeight * 0.056,
                    ),
                    backgroundColor: const Color(0xFF9F723B),
                    foregroundColor: Colors.white,
                  ),
                  onPressed: () {
                    _verification();
                  },
                  child: Text(
                    style: TextStyle(
                      fontSize: screenHeight * 0.025,
                    ),
                    "Verify",
                  ),
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("Haven't Received the Code? ",
                    style: TextStyle(
                      fontSize: MediaQuery.of(context).size.height * 0.017,
                    )
                  ),
                  TextButton(
                    style: TextButton.styleFrom(
                      foregroundColor: Colors.brown,
                    ),
                    onPressed: () {
                      Navigator.pushNamed(context, '/');
                    },
                    child: Text("Resend",
                      style: TextStyle(
                        fontSize: MediaQuery.of(context).size.height * 0.018,
                      )
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
      bottomNavigationBar: Image.asset(
        "lib/assets/images/picture.png",
      ),
    );
  }
}