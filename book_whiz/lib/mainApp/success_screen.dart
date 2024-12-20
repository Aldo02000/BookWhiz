import 'package:book_whiz/mainApp/user_session.dart';
import 'package:flutter/material.dart';

import '../models/user.dart';

class SuccessScreen extends StatefulWidget {
  const SuccessScreen({super.key});

  @override
  State<SuccessScreen> createState() => _SuccessScreenState();
}

class _SuccessScreenState extends State<SuccessScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('User Details')),
      body: FutureBuilder<User?>(
        future: fetchUserDetails(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return CircularProgressIndicator();
          } else if (snapshot.hasError) {
            return Text('Error: ${snapshot.error}');
          } else if (snapshot.data == null) {
            return const Text('No user data found');
          } else {
            User user = snapshot.data!;
            return Column(
              children: [
                Text('Username: ${user.username}'),
                Text('Email: ${user.email}'),
                Text('Id: ${user.id}')
              ],
            );
          }
        },
      ),
    );
  }

}