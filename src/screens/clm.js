import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from 'react-native';
import axios from 'axios';

export default function Clm({ route }) {
  const [message, setMessage] = useState('');
  const [contactInfo, setContactInfo] = useState('');

  const isFormValid = message.length > 0 && contactInfo.length > 0;

  const handleSubmit = async () => {
    if (!isFormValid) {
      Alert.alert('Error', 'Please fill all fields.');
      return;
    }
  
    try {
      const response = await axios.post('http://192.168.24.186:8084/api/claims', {
        message,
        contactInfo,
        itemId: route.params?.itemId || null,  // Optional: if you're passing itemId through navigation
      });
  
      if (response.status === 200 || response.status === 201) {
        Alert.alert('Claim Submitted', 'Your request has been sent.');
        setMessage('');
        setContactInfo('');
      } else {
        Alert.alert('Error', 'Something went wrong. Please try again.');
      }
    } catch (error) {
      console.error(error);
      Alert.alert('Error', 'Network error. Please check your connection.');
    }
  };
  

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Claim Found Item</Text>

      <Text style={styles.label}>Why is this item yours?</Text>
      <TextInput
        style={[styles.input, { height: 100 }]}
        placeholder="Describe unique details about the item..."
        multiline
        value={message}
        onChangeText={setMessage}
      />

      <Text style={styles.label}>Your Contact Info</Text>
      <TextInput
        style={styles.input}
        placeholder="Email or phone"
        value={contactInfo}
        onChangeText={setContactInfo}
      />

      <TouchableOpacity
        style={[styles.button, { opacity: isFormValid ? 1 : 0.5 }]}
        onPress={handleSubmit}
        disabled={!isFormValid}
      >
        <Text style={styles.buttonText}>Submit Claim</Text>
      </TouchableOpacity>
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
    marginBottom: 25,
  },
  label: {
    fontSize: 15,
    fontWeight: '500',
    marginBottom: 5,
    color: '#333',
  },
  input: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 10,
    paddingHorizontal: 15,
    paddingVertical: 10,
    fontSize: 16,
    marginBottom: 20,
    backgroundColor: '#f9f9f9',
  },
  button: {
    backgroundColor: '#A3C76E',
    paddingVertical: 15,
    borderRadius: 10,
    alignItems: 'center',
  },
  buttonText: {
    color: '#fff',
    fontWeight: '600',
    fontSize: 16,
  },
});