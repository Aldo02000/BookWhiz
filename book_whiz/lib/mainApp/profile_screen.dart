import 'package:book_whiz/mainApp/user_session.dart';
import 'package:flutter/material.dart';

import '../services/secure_storage.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {

  String? username;

  @override
  void initState() {
    super.initState();
    fetchUserDetails().then((user) {
      if (user != null) {
        setState(() {
          username = user.username;
        });
      }
    });
  }

  void logout() async {
    await SecureStorage.deleteToken();
    Navigator.of(context, rootNavigator: true).pushReplacementNamed('/login');
  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Profile Tab"),
      ),
      body: Column(
        children: [
          Container(
            margin: const EdgeInsets.fromLTRB(15, 15, 0, 0),
            child:  Text(
              "Hello ${username!}",
              style: const TextStyle(fontSize: 18),
            ),
          ),
          Container(
            margin: EdgeInsets.symmetric(vertical: 30, horizontal: 30),
            child: Center(
              child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    minimumSize: Size(200, 50), // Width: 200, Height: 50
                  ),
                onPressed: () => logout(),
                child: const Text(
                  "Sign Out",
                  style: TextStyle(
                      fontSize: 20,
                      color: Color(0xFFED9247)
                  ),
                ),
              ),
            ),
          )
        ],
      ),
    );
  }

}