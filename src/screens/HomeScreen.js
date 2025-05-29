import React, { useState, useEffect, useCallback } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  Alert,
  SafeAreaView,
} from 'react-native';
import Ionicons from 'react-native-vector-icons/Ionicons';
import axios from 'axios';
import { useFocusEffect } from '@react-navigation/native';

export default function HomeScreen({ navigation, route }) {
  const [reports, setReports] = useState([]);
const mockReports = [
  { title: "Lost watch near park", time: "Reported at 10:15AM" },
  { title: "Reward for finding keys", button: "View details" },
  { title: "Message from user", button: "Reply" },
  { title: "Found backpack at cafe", time: "Last seen at 12:15PM" },
  { title: "Claimed item", time: "Reward for return" },
  { title: "Claimed lost item", time: "Reported at 10:15PM" },
];

  useEffect(() => {
    axios
      .get('http://192.168.24.186:8080/api/reports')
      .then((response) => setReports(response.data))
      .catch((error) => {
        console.error(error);
        Alert.alert('Error', 'Failed to fetch reports.');
      });
  }, []);

  useFocusEffect(
    useCallback(() => {
      if (route?.params?.showWelcome) {
        Alert.alert('Login Successful', 'Welcome!');
      }
    }, [route?.params?.showWelcome])
  );

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>LostAndFound</Text>
        <TouchableOpacity
          style={styles.searchButton}
          onPress={() => navigation.navigate('SearchScreen')}
        >
          <Ionicons name="search" size={24} color="#fff" />
        </TouchableOpacity>
      </View>
      

      {/* Add your content here if needed */}

      {/* Fixed bottom navigation bar */}
      <View style={styles.bottomNav}>
        <TouchableOpacity onPress={() => navigation.navigate('HomeScreen')}>
          <Ionicons name="home" size={24} color="#444" />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => navigation.navigate('SettingScreen')}>
          <Ionicons name="settings" size={24} color="#444" />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => navigation.navigate('UserListScreen')}>
          <Ionicons name="chatbubble" size={24} color="#444" />
        </TouchableOpacity>
        <TouchableOpacity onPress={() => navigation.navigate('Detail')}>
          <Ionicons name="information-circle-outline" size={24} color="#444" />
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    justifyContent: 'space-between', // Push bottom nav to bottom
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 15,
    backgroundColor: '#4CAF50',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#fff',
  },
  searchButton: {
    padding: 10,
  },
  bottomNav: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding :10,
    position: "relative",
    bottom:30,
  
    backgroundColor: 'lightgray',
    borderTopColor: '#ccc',
  },
});
