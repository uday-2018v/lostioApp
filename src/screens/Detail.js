import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  FlatList,
  Alert,
} from 'react-native';
import Ionicons from 'react-native-vector-icons/Ionicons';
import axios from 'axios';

export default function Detail({ navigation }) {
  const [reports, setReports] = useState([]);

  useEffect(() => {
    axios
      .get('http://192.168.24.186:8080/api/reports')
      .then((response) => setReports(response.data))
      .catch((error) => {
        console.error(error);
        Alert.alert('Error', 'Failed to fetch reports.');
      });
  }, []);

  const renderReportItem = ({ item }) => (
    <TouchableOpacity
      style={styles.reportItem}
      onPress={() => navigation.navigate('clm', { report: item })}
    >
      <View style={styles.card}>
        <View style={styles.cardContent}>
          <Text style={styles.reportTitle}>Title: {item.title}</Text>
          <Text style={styles.reportDescription}>Description: {item.description}</Text>
          <Text style={styles.reportLocation}>Location: {item.location}</Text>
        </View>
        <View style={styles.cardFooter}>
          <Text style={styles.reportDays}>Days</Text>
          <Text style={styles.reportTime}>{item.time}</Text>
        </View>
      </View>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Details</Text>
      </View>

      <FlatList
        data={reports}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderReportItem}
        contentContainerStyle={styles.reportList}
      />

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
          <Ionicons name="document-text-outline" size={24} color="#444" />
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
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
  reportList: {
    padding: 15,
  },
  reportItem: {
    marginBottom: 10,
  },
  card: {
    backgroundColor: '#e8f5e9',
    borderRadius: 8,
    padding: 15,
    marginBottom: 10,
  },
  cardContent: {
    marginBottom: 10,
  },
  cardFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  reportTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#388e3c',
    textAlign: 'center',
  },
  reportDescription: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#388e3c',
    marginTop: 5,
  },
  reportLocation: {
    fontSize: 14,
    fontWeight: 'bold',
    color: '#388e3c',
    marginTop: 5,
  },
  reportDays: {
    fontSize: 12,
    color: '#388e3c',
    marginTop: 5,
  },
  reportTime: {
    fontSize: 12,
    color: '#388e3c',
    marginTop: 5,
  },
  bottomNav: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: 15,
    borderTopWidth: 1,
    borderTopColor: '#ccc',
    marginBottom: 30,
  },
});
