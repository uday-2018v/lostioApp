import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, FlatList, StyleSheet, Alert } from 'react-native';
import axios from 'axios';

export default function UserListScreen({ navigation }) {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await axios.get('http://192.168.24.186:8080/api/auth/users');
        setUsers(response.data);
      } catch (error) {
        console.error(error);
        Alert.alert('Error', 'Failed to fetch users.');
      }
    };

    fetchUsers();
  }, []);

  const handleUserPress = (userId) => {
    navigation.navigate('ChatScreen', {
      senderId: 'currentUserId', // Replace with actual logged-in user ID
      receiverId: userId,
    });
  };

  const renderUserItem = ({ item }) => (
    <TouchableOpacity style={styles.userItem} onPress={() => handleUserPress(item.id)}>
      <Text style={styles.userName}>{item.name}</Text>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Users</Text>
      <FlatList
        data={users}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderUserItem}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
    textAlign: 'center',
  },
  userItem: {
    backgroundColor: '#A3C76E', // Green background
    padding: 15,
    borderRadius: 10,
    marginBottom: 15,
    alignItems: 'center', // Center the text horizontally
    justifyContent: 'center',
  },
  userName: {
    fontSize: 18,
    color: '#fff', // White text for contrast
    fontWeight: 'bold',
  },
});