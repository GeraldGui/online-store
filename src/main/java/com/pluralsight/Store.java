
package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Starter code for the Online Store workshop.
 * Students will complete the TODO sections to make the program work.
 */
public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: id|name|price)
        loadInventory("products.csv", inventory);

        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();                     // clear newline

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    /**
     * Reads product data from a file and populates the inventory list.
     * File format (pipe-delimited):
     * id|name|price
     * <p>
     * Example line:
     * A17|Wireless Mouse|19.99
     */
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        // TODO: read each line, split on "|",
        //       create a Product object, and add it to the inventory list

        // Start try and catch
        try {
            // Read the file
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            // Start while loop, line is reading each line with bufferReader and will stop when no more lines to read
            while ((line = bufferedReader.readLine()) != null) {

                // Start an array called tokens to split the file from |, then store them in tokens
                List<String> tokens = new ArrayList<>(Arrays.asList(line.split("\\|")));

                //Variables to store each token that is split
                String id = tokens.get(0);
                String name = tokens.get(1);
                double price = Double.parseDouble(tokens.get(2));

                // Adding to the array inventory, by putting in my tokens into the Class Product
                inventory.add(new Product(id, name, price));

            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Displays all products and lets the user add one to the cart.
     * Typing X returns to the main menu.
     */
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {
        // TODO: show each product (id, name, price),
        //       prompt for an id, find that product, add to cart
        int itemNumber = 0;

        // Start a loop to print out each object in inventory
        for (Product product : inventory) {
            itemNumber++;
            System.out.println("Item number: " + itemNumber + "\n----------------\n" + product);
        }

        // Start the while loop
        boolean stop = false;

        while (!stop) {
            System.out.print("Would you like to add and item to your cart? (Y/N) ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("y")) {

                // Keeps the loop going if they put wrong input
                while (!stop) {
                    System.out.print("Type in the item number, to take a product or type in X to exit ");
                    String item = scanner.nextLine();

                    int number = 0;

                    if (item.equalsIgnoreCase("x")) {
                        stop = true;
                    } else {
                        try {
                            // Makes the string into an int
                            number = Integer.parseInt(item);
                        }
                        // Catches anything that is not an int or an X
                        catch (NumberFormatException e) {
                            System.out.println("\nWrong input\n");
                        }
                    }

                    // Adds the cart array
                    if (number > 0) {
                        cart.add(inventory.get(number - 1));
                    }
                }
            } else if (command.equalsIgnoreCase("n")) {
                System.out.println("Thank you for browsing.");
                stop = true;

            } else {
                System.out.println("\nSorry wrong input\n");
            }
        }
    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        // TODO:
        //   • list each product in the cart
        //   • compute the total cost
        //   • ask the user whether to check out (C) or return (X)
        //   • if C, call checkOut(cart, totalAmount, scanner)

        int itemNumber = 0;
        for (Product product : cart) {
            itemNumber++;
            System.out.println("Item number: " + itemNumber + "\n----------------\n" + product);
        }
    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        // TODO: implement steps listed above
    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        // TODO: loop over the list and compare ids
        return null;
    }
}

 