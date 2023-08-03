# LVAppQuiz

LVAppQuiz is an Android application designed to make art learning fun and engaging. The app uses a game format to help users study art and learn about various artists and their works. It leverages data from the Harvard Museum and DALL-E to generate images, providing a rich and interactive learning experience.

## Features

- **Art Quiz**: Test your knowledge about various artists and their works through a series of quizzes.
- **AI Image Generation**: The app uses DALL-E to generate images, adding an element of surprise and fun to the learning process.
- **Harvard Museum Data**: The app utilizes data from the Harvard Museum to provide accurate and informative content about various art pieces and artists.
- **Progress Tracking**: Users can track their progress and see how much they've learned over time.

## Structure

The app is structured into several key components:

- `MainActivity.kt`: The main activity that launches when the app starts.
- `ai_generation`: This package contains classes related to AI image generation and handling data from the Harvard Museum.
- `db`: This package contains classes related to the app's database operations.
- `dialogues`: This package contains classes for various dialogues used in the app.
- `lives`: This package contains classes for managing the user's lives in the game.
- `menu`: This package contains classes for the app's menu.
- `questions`: This package contains classes for generating and managing quiz questions.
- `quests`: This package contains classes for managing the quests in the game.
- `quiz`: This package contains classes for the quiz functionality.
- `user_progress`: This package contains classes for tracking the user's progress.

## Installation

To install the app, clone the repository and build it using Android Studio.

```bash
git clone https://github.com/a-kruglov/LVAppQuiz.git
