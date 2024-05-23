package fr.diginamic.services;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import fr.diginamic.entities.User;

public class Users {
    private static final String uri = "mongodb://localhost:27017";
    private static final String dbName = "java-mongodb";
    private static final String table = "users";

    public static ArrayList<Document> getAll() {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(table);
            // Retrieves all documents in the collection
            return collection.find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static Document getOne(String name) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(table);
            // Retrieves the first matching document, applying a projection and a descending sort to the results
            Document doc = collection.find(eq("name", name)).first();
            // Prints a message if there are no result documents, or prints the result document as JSON
            if (doc == null) {
                System.out.println("No results found.");
            } else {
                return doc;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static void insertOne(String name, int age, String email, String phone) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(table);
            // Inserts a new document into the collection
            Document doc = new Document("name", name)
                    .append("age", age)
                    .append("email", email)
                    .append("phone", phone);
            collection.insertOne(doc);
            System.out.println("Document inserted.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void updateOne(String name, User user) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(table);
            // Updates the first matching document
            Document updateDoc = new Document().append("name", user.getName())
                    .append("age", user.getAge())
                    .append("email", user.getEmail())
                    .append("phone", user.getPhone());
            collection.updateOne(eq("name", name), new Document("$set", updateDoc));
            System.out.println("Document updated.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void deleteOne(String name) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(table);
            // Deletes the first matching document
            collection.deleteOne(eq("name", name));
            System.out.println("Document deleted.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void optionsMenu(){
        System.out.println("--- Users Menu ---");
        System.out.println("1. Display all documents in the users collection");
        System.out.println("2. Display the first document in the users collection");
        System.out.println("3. Insert a new document into the users collection");
        System.out.println("4. Edit the document in the users collection");
        System.out.println("5. Delete the document in the users collection");
        System.out.println("99. Exit");
        System.out.println("--- Users Menu ---");
    }

    public static void menu() {
        Scanner scanner = new Scanner(System.in);

        int option;
        do {
            optionsMenu();
            System.out.println("Select your option: ");
            option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    try {
                        List<Document> users = Users.getAll();
                        assert users != null;
                        for(Document doc : users){
                            System.out.println(doc.toJson());
                        }
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Enter the name of the user: ");
                        String name = scanner.nextLine();
                        Document doc = Users.getOne(name);

                        assert doc != null;
                        System.out.println(doc.toJson());
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        System.out.println("Enter the name of the new user: ");
                        String name = scanner.nextLine();
                        System.out.println("Enter the age of the new user: ");
                        int age = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter the email of the new user: ");
                        String email = scanner.nextLine();
                        System.out.println("Enter the phone of the new user: ");
                        String phone = scanner.nextLine();
                        Users.insertOne(name, age, email, phone);
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        System.out.println("Enter the name of the user to edit: ");
                        String reference = scanner.nextLine();
                        System.out.println("Enter the new name of the user: ");
                        String name = scanner.nextLine();
                        System.out.println("Enter the new age of the user: ");
                        int age = scanner.nextInt();
                        System.out.println("Enter the new email of the user: ");
                        String email = scanner.nextLine();
                        System.out.println("Enter the new phone of the user: ");
                        String phone = scanner.nextLine();

                        Users.updateOne(reference, new User(name, age, email, phone));
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        System.out.println("Enter the name of the user to delete: ");
                        String name = scanner.nextLine();

                        Users.deleteOne(name);
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                    break;
                case 99:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        } while (option !=99);

        scanner.close();
    }
}
