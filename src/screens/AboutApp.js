import { View, Text ,ScrollView,StyleSheet} from 'react-native'
import React from 'react'

export default function AboutApp() {
    const AppInfo =`1. Authentication
Add Login/Register screen.

Use JWT from Spring Boot backend (/auth/login).

2. Claim Flow
Add a Claim Item screen that:

Shows reported items (GET /api/reports)

Allows users to claim (POST /api/claims)
 Upload Photos to Backend
Currently, you're sending photoUri (local file path). For production, consider:

Uploading image to Cloudinary/S3

Send the cloud URL in the photo field to Backend
Use expo-location to tag where the item was lost.`;
  return (
     <ScrollView style={styles.container}>
      <Text style={styles.title}>Information about Application</Text>
      <View styles={styles.container}>
        <Text>{AppInfo}</Text>
      </View>
    </ScrollView>
  )
}


const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'lightgreen',
    padding: 16,
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 16,
    textAlign: 'center',
  },
  codeContainer: {
    backgroundColor: '#333',
    padding: 16,
    borderRadius: 8,
  },
  codeText: {
    color: '#fff',
    fontFamily: 'monospace', // Use a monospace font for code
    fontSize: 14,
  },
});