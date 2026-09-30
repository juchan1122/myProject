import { StyleSheet, Text, View } from "react-native";

export default function About() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>나의 첫 React Native 앱</Text>
      <Text>React Native와 Expo로 만든 Todo 앱입니다.</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    gap: 8,
  },
  title: {
    fontSize: 20,
    fontWeight: "bold",
  },
});