
package com.pluralsight;

import java.io.*;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


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


    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {

        int itemNumber = 0;

        System.out.print("Would you like to search by ID? (Y/N) ");
        String idSearch = scanner.nextLine();

        if (idSearch.equalsIgnoreCase("y")) {
            Product found = null;

            while (found == null) {
                System.out.print("What is the product ID you would like to search: ");
                String id = scanner.nextLine();

                found = findProductById(id, inventory);

                if (found == null) {
                    System.out.println("Invalid ID, please try again.");
                }
            }
            } else if (idSearch.equalsIgnoreCase("n")) {
                // Start a loop to print out each object in inventory
                for (Product product : inventory) {
                    itemNumber++;
                    System.out.println("Item number: " + itemNumber + "\n----------------\n" + product);
                }
            }

        // Start the while loop
        boolean stop = false;

        while (!stop) {
            System.out.print("\nWould you like to add a item to your cart? (Y/N) ");
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

    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {

        double totalAmount = 0;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.indexOf(cart.get(i)) == i) {
                int quantity = Collections.frequency(cart, cart.get(i));
                totalAmount += cart.get(i).getPrice() * quantity;
                System.out.println("Quantity: " + quantity + "\n----------------\n" + cart.get(i));
            }
        }

        boolean running = false;

        while (!running) {
            System.out.print("Would you like to check out (C) or return (X): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("c")) {
                checkOut(cart, totalAmount, scanner);
                running = true;

            } else if (choice.equalsIgnoreCase("x")) {
                running = true;
            }
        }
    }


    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {

        boolean runningLoop = false;

        while (!runningLoop) {
            System.out.print("Are you sure you want to checkout? (Y/N) " + totalAmount + ": ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("y")) {
                System.out.print("Please put in the amount you will be paying today: ");
                double money = scanner.nextDouble();
                scanner.nextLine();

                boolean run = false;

                while (!run) {
                    if (money == totalAmount) {
                        System.out.println("Exact Amount!\n");
                        createReceipt(cart, totalAmount);
                        runningLoop = true;
                        run = true;
                    } else if (money > totalAmount) {
                        double change = money - totalAmount;
                        System.out.println("Your Change will be " + change + "\n");
                        createReceipt(cart, totalAmount);
                        runningLoop = true;
                        run = true;
                    } else if (money < totalAmount) {
                        System.out.println("insufficient Amount.");
                        break;
                    }
                }

            } else if (choice.equalsIgnoreCase("n")) {
                System.out.println("Thank you for browsing!");
                runningLoop = true;
            }

        }
    }

    public static Product findProductById(String id, ArrayList<Product> inventory) {
        int itemNumber = 1;

        for (Product product : inventory) {
            if (id.equalsIgnoreCase(product.getId())) {
                System.out.println("\nItem number: " + itemNumber + "\n----------------\n" + product);
                return product;
            }
            itemNumber++;
        }
        return null;
    }

    public static void createReceipt(ArrayList<Product> cart, double totalAmount) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            String fileName = now.format(formatter);

            File createFolder = new File("Receipts");
            createFolder.mkdir();

            PrintWriter writer = new PrintWriter("Receipts/" + fileName + ".txt");
            System.out.println("\n\n==========Receipt==========\n");

            for (int i = 0; i < cart.size(); i++) {
                if (cart.indexOf(cart.get(i)) == i) {
                    int quantity = Collections.frequency(cart, cart.get(i));
                    System.out.println(cart.get(i).getName() + " | Quantity: " + quantity + "\n----------------\n" + cart.get(i));
                    writer.println(cart.get(i).getName() + " | Quantity: " + quantity + "\n----------------\n" + cart.get(i));
                }
            }

            System.out.println("Total: " + totalAmount);
            writer.println("Total: " + totalAmount);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error creating receipt!");
        }
    }
}

 