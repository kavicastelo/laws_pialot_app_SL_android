# Service Oriented Concept Project

This is a pilot project that demonstrates the use of service-oriented concepts, combining Android (Java) and PHP technologies. The project is designed to work with Android up to version 6 and is supported by Android Studio 2021 Bumble Bee version. It utilizes APIs stored in the `slpolice` directory and communicates with a PHP backend.

## Project Overview

- **Technology Stack:** Android (Java), PHP
- **Database:** Utilizes the 000webhost localhost database
- **Android Version Support:** Up to Android 6
- **Android Studio Support:** Android Studio 2021 Bumble Bee version

## Setup Instructions

1. Clone the repository to your local machine.
2. After cloning, remove the `slmobile` folder from the project directory. This folder contains PHP APIs data.
3. Open the project in Android Studio (version 2021 Bumble Bee or compatible).
4. Configure your Android emulator or device to run the application.
5. Make sure the necessary PHP backend is set up to handle the API requests from the Android application.
6. Ensure that you have access to the Google Drive API if you intend to use Google Drive to store PDF files.

## Important Notes

- The project interacts with the 000webhost localhost database for data storage.
- APIs are stored in the `slpolice` directory. Ensure that the API endpoints are correctly configured in both the Android application and the PHP backend.
- Google Drive is used to store PDF files. Make sure you have the necessary credentials and permissions to interact with the Google Drive API.
- This is a pilot project and does not cover all legal aspects. It's meant for demonstration and learning purposes.

## Support and Compatibility

This project is designed to work with Android up to version 6 and is built using Android Studio 2021 Bumble Bee version. If you encounter any issues or have questions, feel free to reach out for support.

## Important Note Regarding `slmobile` Folder

The `slmobile` folder included in this repository contains sensitive PHP API files that are crucial for the functionality of the project. These PHP files are designed to support the project's backend operations and are **not intended** to be included in any public repositories or shared openly.

When working with this project, **take the following precautions**:

1. **Privacy and Security:** The contents of the `slmobile` folder include sensitive information and API keys that must be kept confidential. Avoid sharing, committing, or publishing this folder in public repositories.

2. **Local Configuration:** When setting up the project locally, ensure that you properly configure the necessary PHP backend files within the `slmobile` folder. These files are essential for the interaction between the Android application and the backend services.

3. **Secure Storage:** Store the `slmobile` folder securely and ensure that only authorized team members have access to it. Make sure to back up this folder in a safe location.

Remember that responsible handling of sensitive data is essential to maintain the security and integrity of the project and its associated services.

---
**Note:** The `slmobile` folder contains PHP API files that are not compatible with Android Studio and are only meant for the backend functionality. For Android development, focus on the relevant Android-related components.


---
**Note:** This README provides a basic outline of the project and setup instructions. For detailed implementation and usage guidelines, please refer to additional documentation or comments within the codebase.
