import React from 'react';
import { View, Text, StyleSheet, Image, TouchableOpacity, BackHandler } from 'react-native';

export default  WelcomeScreen =({ navigation })=> {
  const handleExit = () => {
    BackHandler.exitApp();
  };

  const handleGetStarted = () => {
    navigation.navigate('AuthScreen'); // Uncomment if you have navigation
    console.log('Get Started pressed');
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={handleExit} style={styles.backButton}>
        <Text style={styles.backArrow}>←</Text>
      </TouchableOpacity>

      <Text style={styles.title}>LostAndFound</Text>
      <Text style={styles.subtitle}>Report lost items and find them easily.</Text>
  

      <TouchableOpacity style={styles.button} onPress={handleGetStarted}>
        <Text style={styles.buttonText}>Get Started</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  backButton: {
    position: 'absolute',
    top: 50,
    left: 20,
  },
  backArrow: {
    fontSize: 24,
  },
  title: {
    fontSize: 28,
    fontWeight: 'bold',
    marginTop: 80,
    textAlign: 'center',
  },
  subtitle: {
    fontSize: 16,
    color: '#555',
    textAlign: 'center',
    marginVertical: 10,
  },
  image: {
    width: 200,
    height: 200,
    marginVertical: 30,
  },
  button: {
    backgroundColor: '#A3C76E',
    paddingVertical: 15,
    paddingHorizontal: 40,
    borderRadius: 10,
    marginTop: 20,
  },
  buttonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
});