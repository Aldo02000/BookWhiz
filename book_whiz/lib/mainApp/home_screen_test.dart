import 'package:flutter/material.dart';

class HomeScreenTest extends StatefulWidget {
  const HomeScreenTest({super.key});

  @override
  State<HomeScreenTest> createState() => _HomeScreenTestState();
}

class _HomeScreenTestState extends State<HomeScreenTest> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFFE1C39C),
        title: const Text("Home Screen"),
      ),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Padding(
            padding: EdgeInsets.fromLTRB(20, 20, 0, 0),
            child: Text(
                "Book Suggestions Based on Authors",
              style: TextStyle(
                fontSize: 18
              ),
            ),
          ),
          Container(
            height: 180,
            width: double.maxFinite,
            child: ListView.builder(
              padding: const EdgeInsets.fromLTRB(7, 0, 3, 0),
              scrollDirection: Axis.horizontal,
              itemCount: 20,
              itemBuilder: (context, index) {
                return Container(
                  margin: EdgeInsets.symmetric(vertical: 6, horizontal: 5),
                  width: 60,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      SizedBox(
                        height: 100,
                        child: Image.network('https://upload.wikimedia.org/wikipedia/commons/9/92/THE_BOOK_cover_image.png'),
                      ),
                       Text(
                        "Title $index",
                        style: const TextStyle(
                            fontSize: 15
                        ),
                      ),
                       Text(
                        "Author $index",
                        style: const TextStyle(
                            fontSize: 11
                        ),
                      )
                    ])
                );
              },
            ),
          ),
          const Padding(
            padding: EdgeInsets.fromLTRB(20, 0, 0, 0),
            child: Text(
              "Book Suggestions Based on Genres",
              style: TextStyle(
                  fontSize: 18
              ),
            ),
          ),
          Container(
            height: 180,
            width: double.maxFinite,
            child: ListView.builder(
              padding: EdgeInsets.fromLTRB(7, 0, 3, 0),
              scrollDirection: Axis.horizontal,
              itemCount: 20,
              itemBuilder: (context, index) {
                return Container(
                  margin: EdgeInsets.symmetric(vertical: 6, horizontal: 5),
                  width: 60,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      SizedBox(
                        height: 100,
                        child: Image.network('https://upload.wikimedia.org/wikipedia/commons/9/92/THE_BOOK_cover_image.png'),
                      ),
                      Text(
                        "Title $index",
                        style: const TextStyle(
                            fontSize: 15
                        ),
                      ),
                      Text(
                        "Author $index",
                        style: const TextStyle(
                            fontSize: 11
                        ),
                      )
                    ])
                );
              },
            ),
          ),
        ],
      )
    );
  }

}