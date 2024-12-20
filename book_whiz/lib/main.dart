import 'package:book_whiz/authScreens/auth_check_screen.dart';
import 'package:book_whiz/authScreens/login_screen.dart';
import 'package:book_whiz/authScreens/signup_screen.dart';
import 'package:book_whiz/authScreens/verification_screen.dart';
import 'package:book_whiz/mainApp/home_screen.dart';
import 'package:book_whiz/mainApp/main_screen.dart';
import 'package:book_whiz/mainApp/success_screen.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<StatefulWidget> createState() {
    return _BookWhiz();
  }
}

class _BookWhiz extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Auth Navigation App',
      theme: ThemeData(scaffoldBackgroundColor: const Color(0xFFE4DACF)),
      initialRoute: '/',
      routes: {
        '/': (context) => AuthCheckScreen(),
        // '/': (context) => AuthCheckScreen(),
        // '/signup': (context) => const SingUpScreen(),
        // '/verify': (context) => const VerificationScreen(),
        '/login': (context) => const LogInScreen(),
        '/success': (context) => const MainScreen(),
      },
    );
  }

}
