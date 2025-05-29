import React, { useState, useEffect, useRef } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  FlatList,
  StyleSheet,
  KeyboardAvoidingView,
  Alert,
  Platform,
} from 'react-native';
import axios from 'axios';

export default function ChatScreen({ route }) {
  const { senderId, receiverId } = route.params; // Sender and receiver IDs passed from navigation
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const flatListRef = useRef(null); // Reference to FlatList

  // Fetch messages when component mounts or senderId/receiverId changes
  useEffect(() => {
    const fetchMessages = async () => {
      try {
        const response = await axios.get(
          `http://192.168.24.186:8080/api/chat?senderId=${senderId}&receiverId=${receiverId}`
        );
        setMessages(response.data);
      } catch (error) {
        console.error(error);
        Alert.alert('Error', 'Failed to fetch chat messages.');
      }
    };

    fetchMessages();
  }, [senderId, receiverId]);

  // Handle sending a new message
  const handleSendMessage = async () => {
    if (!newMessage.trim()) {
      Alert.alert('Error', 'Message cannot be empty.');
      return;
    }

    try {
      const response = await axios.post('http://192.168.24.186:8080/api/chat', {
        senderId,
        receiverId,
        message: newMessage,
        timestamp: new Date().toISOString(),
      });

      if (response.status === 200) {
        setMessages((prevMessages) => [...prevMessages, response.data]);
        setNewMessage(''); // Clear the input field

        // Scroll to the latest message after sending
        flatListRef.current.scrollToEnd({ animated: true });
      }
    } catch (error) {
      console.error(error);
      Alert.alert('Error', 'Failed to send message.');
    }
  };

  // Render each message in the FlatList
  const renderMessageItem = ({ item }) => (
    <View
      style={[
        styles.messageBubble,
        item.senderId === senderId ? styles.ownMessage : styles.otherMessage,
      ]}
    >
      <Text style={styles.messageText}>{item.message}</Text>
      <Text style={styles.timestamp}>
        {new Date(item.timestamp).toLocaleTimeString()}
      </Text>
    </View>
  );

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior="padding"
      keyboardVerticalOffset={Platform.OS === 'ios' ? 100 : 0}
    >
      <FlatList
        ref={flatListRef}
        data={messages}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderMessageItem}
        contentContainerStyle={styles.messageList}
      />

      <View style={styles.inputContainer}>
        <TextInput
          style={styles.input}
          placeholder="Type a message..."
          value={newMessage}
          onChangeText={setNewMessage}
        />
        <TouchableOpacity style={styles.sendButton} onPress={handleSendMessage}>
          <Text style={styles.sendButtonText}>Send</Text>
        </TouchableOpacity>
      </View>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  messageList: {
    padding: 15,
  },
  messageBubble: {
    padding: 10,
    borderRadius: 8,
    marginBottom: 10,
    maxWidth: '70%',
  },
  ownMessage: {
    alignSelf: 'flex-end',
    backgroundColor: '#DCF8C6',
  },
  otherMessage: {
    alignSelf: 'flex-start',
    backgroundColor: '#ECECEC',
  },
  messageText: {
    fontSize: 16,
  },
  timestamp: {
    fontSize: 10,
    color: '#666',
    marginTop: 5,
    textAlign: 'right',
  },
  inputContainer: {
    flexDirection: 'row',
    padding: 10,
    borderTopWidth: 1,
    borderColor: '#ccc',
  },
  input: {
    flex: 1,
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 25,
    paddingHorizontal: 15,
    marginRight: 10,
  },
  sendButton: {
    backgroundColor: '#4CAF50',
    borderRadius: 25,
    paddingHorizontal: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  sendButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
});
