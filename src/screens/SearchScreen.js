import React, { useState } from 'react';
import {
  View,
  Text,
  TextInput,
  TouchableOpacity,
  FlatList,
  Image,
  StyleSheet,
} from 'react-native';
import Ionicons from 'react-native-vector-icons/Ionicons';

// 🔒 Static options with local image
const options = [
  { title: 'Report Missing Items1', image: require('../assets/favicon.png'), screen: 'LostItemScreen1' },
  { title: 'Claim Found Items', image: require('../assets/favicon.png'), screen: 'clm' },
  { title: 'View Item Details', image: require('../assets/favicon.png'), screen: 'Detail' },
  { title: 'View Messages', image: require('../assets/favicon.png'), screen: 'Chat' },
  { title: 'Report Missing Items', image: require('../assets/favicon.png'), screen: 'LostItemScreen' }
];

export default function SearchScreen({ navigation }) {
  const [query, setQuery] = useState('');

  // Filter options based on query
  const filteredOptions = options.filter(opt =>
    opt.title.toLowerCase().includes(query.toLowerCase())
  );

  // UI
  return (
    <View style={styles.container}>
      {/* Search Bar */}
      <View style={styles.searchBox}>
        <TextInput
          style={styles.input}
          placeholder="Search for Options"
          value={query}
          onChangeText={setQuery}
        />
        <Ionicons name="search" size={20} color="gray" style={{ marginHorizontal: 10 }} />
        <TouchableOpacity onPress={() => setQuery('')} style={styles.clearBtn}>
          <Text style={{ color: 'white' }}>Clear</Text>
        </TouchableOpacity>
      </View>

      {/* Static Options List */}
      <FlatList
        data={filteredOptions}
        keyExtractor={(item, index) => index.toString()}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.option}
            onPress={() => navigation.navigate(item.screen)}
          >
            <Image source={item.image} style={styles.icon} />
            <Text style={styles.title}>{item.title}</Text>
          </TouchableOpacity>
        )}
      />
    </View>
  );
}

// 💅 Styles
const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: '#fff',
  },
  searchBox: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 12,
    backgroundColor: '#eee',
    paddingHorizontal: 10,
    marginBottom: 20,
  },
  input: {
    flex: 1,
    height: 40,
    paddingHorizontal: 8,
    fontSize: 16,
  },
  clearBtn: {
    backgroundColor: '#648c4b',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 8,
  },
  option: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 15,
    backgroundColor: '#f3f3f3',
    padding: 12,
    borderRadius: 10,
  },
  icon: {
    width: 40,
    height: 40,
    borderRadius: 20,
    marginRight: 12,
  },
  title: {
    fontSize: 16,
  },
});
