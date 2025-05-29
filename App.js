import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import AuthScreen from './src/screens/AuthScreen.js'; // Adjust the path as needed
import WelcomeScreen from './src/screens/WelcomeScreen.js';
import HomeScreen from './src/screens/HomeScreen.js';
import ForgotPasswordScreen  from './src/screens/ForgotPasswordScreen.js';
import SettingScreen from './src/screens/SettingScreen.js';
import LostItemScreen  from './src/screens/LostItemScreen.js';
import SignUpScreen from './src/screens/SignUpScreen.js';
import SearchScreen from './src/screens/SearchScreen.js';
import ChatScreen from './src/screens/ChatScreen.js';
import UserListScreen from './src/screens/UserListScreen.js';
import EditProfileScreen from './src/screens/EditProfileScreen.js';
import Detail from './src/screens/Detail.js';
import clm from './src/screens/clm.js';
import LostItemScreen1  from './src/screens/LostItemScreen1.js';
import AboutApp from './src/screens/AboutApp.js'; // Adjust the path as needed
 // Adjust the path as needed
 // Create this screen if it doesn't exist
const Stack = createStackNavigator();


export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="WelcomeScreen">
      <Stack.Screen
          name="WelcomeScreen"
          component={WelcomeScreen}
          options={{ title: 'Welcome' }} // Customize the header for HomeScreen
        />
        <Stack.Screen
          name="AuthScreen"
          component={AuthScreen}
          options={{ headerShown: false }} // Hide the header for AuthScreen
        />
     
         <Stack.Screen
          name="SignUpScreen"
          component={SignUpScreen}
           // Hide the header for AuthScreen
        />
         <Stack.Screen
          name="HomeScreen"
          component={HomeScreen}
           // Hide the header for AuthScreen
        />
        <Stack.Screen
          name="ForgotPasswordScreen"
          component={ForgotPasswordScreen}
           // Hide the header for AuthScreen
        />
         <Stack.Screen
          name="LostItemScreen"
          component={LostItemScreen}
           // Hide the header for AuthScreen
        />
           <Stack.Screen
          name="LostItemScreen1"
          component={LostItemScreen1}
           // Hide the header for AuthScreen
        />
      
      <Stack.Screen
          name="SettingScreen"
          component={SettingScreen}
           // Hide the header for AuthScreen
        />
         <Stack.Screen
          name="SearchScreen"
          component={SearchScreen}
           // Hide the header for AuthScreen
        />
        <Stack.Screen
          name="ChatScreen"
          component={ChatScreen}
           // Hide the header for AuthScreen
        />
        <Stack.Screen
          name="UserListScreen"
          component={UserListScreen}
           // Hide the header for AuthScreen
        />
        <Stack.Screen
          name="EditProfileScreen"
          component={EditProfileScreen}
           // Hide the header for AuthScreen
        />
        <Stack.Screen
          name="Detail"
          component={Detail}
           // Hide the header for AuthScreen
        />
       <Stack.Screen
          name="clm"
          component={clm}
           // Hide the header for AuthScreen
        />
         <Stack.Screen
          name="AboutApp"
          component={AboutApp}
           // Hide the header for AuthScreen
        />
      </Stack.Navigator>
    </NavigationContainer>

  );
}