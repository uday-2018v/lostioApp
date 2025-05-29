import React from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  Alert,
} from 'react-native';
import Ionicons from 'react-native-vector-icons/Ionicons';
import AsyncStorage from '@react-native-async-storage/async-storage'; // ✅ Import AsyncStorage

export default function SettingScreen({ navigation }) {
  const handleEditProfile = () => {
    navigation.navigate('EditProfileScreen');
  };

  const handleChangePassword = () => {
    navigation.navigate('ForgotPasswordScreen');
  };
 const handleAboutApp = () => {
    navigation.navigate('AboutApp');
  };
  const handleLogout = () => {
    Alert.alert('Logout', 'Are you sure you want to logout?', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Logout',
        style: 'destructive',
        onPress: async () => {
          try {
            await AsyncStorage.removeItem('userToken'); // ✅ Clear token
            navigation.reset({
              index: 0,
              routes: [{ name: 'AuthScreen' }], // ✅ Reset stack to AuthScreen
            });
          } catch (error) {
            console.error('Error clearing session:', error);
          }
        },
      },
    ]);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Settings</Text>

      <TouchableOpacity style={styles.item} onPress={handleEditProfile}>
        <Ionicons name="person-outline" size={22} color="#4B5563" />
        <Text style={styles.itemText}>Edit Profile</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.item} onPress={handleChangePassword}>
        <Ionicons name="lock-closed-outline" size={22} color="#4B5563" />
        <Text style={styles.itemText}>Change Password</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.item} >
        <Ionicons name="notifications-outline" size={22} color="#4B5563" />
        <Text style={styles.itemText}>Notifications</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.item} onPress={handleAboutApp}>
        <Ionicons name="information-circle-outline" size={22} color="#4B5563" />
        <Text style={styles.itemText}>About App</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.logoutButton} onPress={handleLogout}>
        <Text style={styles.logoutText}>Logout</Text>
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
    marginBottom: 20,
  },
  item: {
    flexDirection: 'row',
    alignItems: 'center',
    padding: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#ccc',
  },
  itemText: {
    fontSize: 16,
    marginLeft: 10,
    color: '#4B5563',
  },
  logoutButton: {
    marginTop: 30,
    backgroundColor: '#FF3B30',
    padding: 15,
    borderRadius: 8,
    alignItems: 'center',
  },
  logoutText: {
    color: '#fff',
    fontWeight: 'bold',
    fontSize: 16,
  },
});
