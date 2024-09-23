import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

// Trie Node class
class TrieNode {
    HashMap<Character, TrieNode> children;
    boolean isEndOfWord;
    List<Contact> contacts;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        contacts = new ArrayList<>();
    }
}

// Contact class to store contact details
class Contact {
    String name;
    String phone;
    String email;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phone + ", Email: " + email;
    }
}

// Trie class
class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Insert a contact into the Trie
    public void insert(String name, String phone, String email) {
        TrieNode node = root;
        for (char c : name.toLowerCase().toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEndOfWord = true;
        node.contacts.add(new Contact(name, phone, email));
    }

    // Search contacts by prefix
    public List<Contact> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(c)) {
                return new ArrayList<>();
            }
            node = node.children.get(c);
        }
        return collectAllContacts(node);
    }

    // Collect all contacts under a Trie node
    private List<Contact> collectAllContacts(TrieNode node) {
        List<Contact> result = new ArrayList<>();
        if (node.isEndOfWord) {
            result.addAll(node.contacts);
        }
        for (char c : node.children.keySet()) {
            result.addAll(collectAllContacts(node.children.get(c)));
        }
        return result;
    }

    // Display all contacts
    public List<Contact> displayAllContacts() {
        return collectAllContacts(root);
    }

    // Delete a contact by name
    public void delete(String name) {
        TrieNode node = root;
        TrieNode prev = null;
        char prevChar = '\0';

        for (char c : name.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(c)) {
                System.out.println("Contact not found.");
                return;
            }
            prev = node;
            prevChar = c;
            node = node.children.get(c);
        }

        if (node.isEndOfWord) {
            node.contacts.removeIf(contact -> contact.name.equalsIgnoreCase(name));
            if (node.contacts.isEmpty()) {
                node.isEndOfWord = false;
            }
            if (node.children.isEmpty() && prev != null) {
                prev.children.remove(prevChar);
            }
            System.out.println("Contact deleted.");
        } else {
            System.out.println("Contact not found.");
        }
    }
}

// Driver class
public class ContactManagementSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Trie contactTrie = new Trie();

        while (true) {
            System.out.println("\n--- Contact Management System ---");
            System.out.println("1. Add Contact");
            System.out.println("2. Search Contact");
            System.out.println("3. Display All Contacts");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    contactTrie.insert(name, phone, email);
                    System.out.println("Contact added successfully.");
                    break;

                case 2:
                    System.out.print("Enter name or prefix to search: ");
                    String searchPrefix = scanner.nextLine();
                    List<Contact> searchResults = contactTrie.search(searchPrefix);
                    if (!searchResults.isEmpty()) {
                        System.out.println("\nSearch Results:");
                        for (Contact contact : searchResults) {
                            System.out.println(contact);
                        }
                    } else {
                        System.out.println("No contacts found.");
                    }
                    break;

                case 3:
                    List<Contact> contacts = contactTrie.displayAllContacts();
                    if (!contacts.isEmpty()) {
                        System.out.println("\nAll Contacts:");
                        for (Contact contact : contacts) {
                            System.out.println(contact);
                        }
                    } else {
                        System.out.println("No contacts available.");
                    }
                    break;

                case 4:
                    System.out.print("Enter the contact name to delete: ");
                    String deleteName = scanner.nextLine();
                    contactTrie.delete(deleteName);
                    break;

                case 5:
                    System.out.println("Exiting Contact Management System.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
