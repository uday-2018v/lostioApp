import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from 'react-native';
import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function LostItemScreen({ navigation }) {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [location, setLocation] = useState('');
  const [userId, setUserId] = useState('');
  const [userName, setUserName] = useState('');

  useEffect(() => {
    const getUserData = async () => {
      const id = await AsyncStorage.getItem('userId');
      const name = await AsyncStorage.getItem('userName');
      if (id) setUserId(id);
      if (name) setUserName(name);
    };

    getUserData();
  }, []);

  const handleReport = async () => {
    if (!title || !description || !location) {
      Alert.alert('Error', 'Please fill in all fields.');
      return;
    }

    const report = {
      title,
      description,
      location,
      userId,
      userName,
      time: new Date().toLocaleString(),
    };

    try {
      // Optional: send to API
      await axios.post('http://192.168.24.186:8082/api/reports', report);

      // Save data to AsyncStorage
      await AsyncStorage.setItem('lastReport', JSON.stringify(report));

      navigation.navigate('LostItemSummaryScreen');
    } catch (error) {
      Alert.alert('Error', 'Failed to report the lost item.');
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Report Lost Item</Text>
      <TextInput
        style={styles.input}
        placeholder="Title"
        value={title}
        onChangeText={setTitle}
      />
      <TextInput
        style={styles.input}
        placeholder="Description"
        value={description}
        onChangeText={setDescription}
        multiline
      />
      <TextInput
        style={styles.input}
        placeholder="Location"
        value={location}
        onChangeText={setLocation}
      />
      <TouchableOpacity style={styles.button} onPress={handleReport}>
        <Text style={styles.buttonText}>Submit & View</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
});
