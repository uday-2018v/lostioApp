import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ScrollView, Image, Alert } from 'react-native';
import * as ImagePicker from 'expo-image-picker';
import * as FileSystem from 'expo-file-system';
import DateTimePickerModal from 'react-native-modal-datetime-picker';
import axios from 'axios';

export default function LostItemScreen1({ navigation }) {
  const [category, setCategory] = useState('');
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState('');
  const [isDatePickerVisible, setDatePickerVisibility] = useState(false);
  const [isTimePickerVisible, setTimePickerVisibility] = useState(false);
  const [photoUri, setPhotoUri] = useState(null);
  const [photoBase64, setPhotoBase64] = useState(null);

  useEffect(() => {
    if (photoBase64) {
      console.log('Updated Base64:', photoBase64.slice(0, 100));
    }
  }, [photoBase64]);

  const takePhoto = async () => {
    const permissionResult = await ImagePicker.requestCameraPermissionsAsync();
    if (!permissionResult.granted) {
      Alert.alert('Permission Required', 'Camera permission is required!');
      return;
    }

    const result = await ImagePicker.launchCameraAsync({
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
      base64: true,
    });

    if (!result.canceled && result.assets && result.assets.length > 0) {
      const imageUri = result.assets[0].uri;
      const base64 = result.assets[0].base64;
      setPhotoUri(imageUri);
      setPhotoBase64(`data:image/jpeg;base64,${base64}`);
    } else {
      Alert.alert('Cancelled', 'Camera closed or cancelled');
    }
  };

  const handleConfirmDate = (date) => {
    setSelectedDate(date.toDateString());
    setDatePickerVisibility(false);
  };

  const handleConfirmTime = (time) => {
    const formatted = time.toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit',
    });
    setSelectedTime(formatted);
    setTimePickerVisibility(false);
  };

  const handleSubmit = async () => {
    if (!category || !selectedDate || !selectedTime || !photoBase64) {
      Alert.alert('Error', 'Please fill in all fields before submitting.');
      return;
    }

    try {
      const formData = new FormData();
      formData.append('category', category);
      formData.append('date', selectedDate);
      formData.append('time', selectedTime);
      formData.append('photo', {
        uri: photoUri,
        type: 'image/jpeg',
        name: 'lost_item.jpg',
      });

      const response = await axios.post('http://192.168.24.186:8085/api/reportsWithImage/create', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      if (response.status === 200 || response.status === 201) {
        Alert.alert('Success', 'Lost item reported successfully!');
        navigation.navigate('Detail');
      } else {
        Alert.alert('Failed', 'Unexpected server response.');
      }
    } catch (error) {
      console.error('Submit error:', error);
      Alert.alert('Error', 'Failed to report the lost item. Please try again.');
    }
  };

  const categories = ['Report', 'Personal', 'Electronic'];

  return (
    <ScrollView style={styles.container} contentContainerStyle={styles.scrollViewContainer}>
      <Text style={styles.title}>Report Lost Item</Text>

      <View style={styles.container1}>
        <Text style={styles.title1}>Upload photo of lost item</Text>
        <TouchableOpacity onPress={takePhoto} style={styles.takePhoto1}>
          <Text style={styles.submitText}>Camera</Text>
        </TouchableOpacity>
        {photoUri && <Image source={{ uri: photoUri }} style={styles.image1} />}
      </View>

      <View style={styles.section}>
        <Text style={styles.label}>Item category</Text>
        <View style={styles.categoryContainer}>
          {categories.map((cat) => (
            <TouchableOpacity
              key={cat}
              style={[styles.category, category === cat && styles.categorySelected]}
              onPress={() => setCategory(cat)}
            >
              <Text style={[styles.categoryText, category === cat && styles.categoryTextSelected]}>
                {cat}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      </View>

      <View style={styles.section}>
        <Text style={styles.label}>Select last seen date</Text>
        <TouchableOpacity onPress={() => setDatePickerVisibility(true)} style={styles.selector}>
          <Text style={styles.selectorText}>{selectedDate || 'Tap to choose a date'}</Text>
        </TouchableOpacity>
        <DateTimePickerModal
          isVisible={isDatePickerVisible}
          mode="date"
          onConfirm={handleConfirmDate}
          onCancel={() => setDatePickerVisibility(false)}
        />
      </View>

      <View style={styles.section}>
        <Text style={styles.label}>Select last seen time</Text>
        <TouchableOpacity onPress={() => setTimePickerVisibility(true)} style={styles.selector}>
          <Text style={styles.selectorText}>{selectedTime || 'Tap to choose time'}</Text>
        </TouchableOpacity>
        <DateTimePickerModal
          isVisible={isTimePickerVisible}
          mode="time"
          onConfirm={handleConfirmTime}
          onCancel={() => setTimePickerVisibility(false)}
        />
      </View>

      <TouchableOpacity style={styles.submitButton} onPress={handleSubmit}>
        <Text style={styles.submitText}>Submit</Text>
      </TouchableOpacity>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#fff',
    marginTop: 56,
    flex: 1,
  },
  scrollViewContainer: {
    padding: 16,
    flexGrow: 1,
    justifyContent: 'space-between',
  },
  title: {
    fontSize: 22,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 20,
  },
  label: {
    fontSize: 16,
    marginBottom: 8,
    fontWeight: '600',
  },
  section: {
    marginBottom: 20,
  },
  categoryContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 10,
  },
  category: {
    borderWidth: 1,
    borderColor: '#ccc',
    paddingVertical: 8,
    paddingHorizontal: 12,
    borderRadius: 20,
    marginRight: 10,
    marginBottom: 10,
  },
  categorySelected: {
    backgroundColor: '#A3C76E',
    borderColor: '#A3C76E',
  },
  categoryText: {
    color: '#333',
  },
  categoryTextSelected: {
    color: '#fff',
  },
  submitButton: {
    backgroundColor: '#A3C76E',
    padding: 15,
    borderRadius: 12,
    alignItems: 'center',
    marginTop: 10,
    marginBottom: 35,
  },
  submitText: {
    color: '#fff',
    fontWeight: 'bold',
    fontSize: 16,
  },
  container1: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
    backgroundColor: '#fff',
  },
  title1: {
    fontSize: 22,
    marginBottom: 20,
  },
  image1: {
    marginTop: 20,
    width: 300,
    height: 300,
    borderRadius: 10,
  },
  takePhoto1: {
    maxWidth: 100,
    backgroundColor: '#A3C76E',
    padding: 15,
    borderRadius: 12,
    alignItems: 'center',
    marginTop: 10,
  },
  selector: {
    padding: 10,
    backgroundColor: '#f9f9f9',
    borderRadius: 10,
    borderColor: '#ccc',
    borderWidth: 1,
    marginBottom: 10,
  },
  selectorText: {
    color: '#333',
    fontSize: 16,
  },
});
