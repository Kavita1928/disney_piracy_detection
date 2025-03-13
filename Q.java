import java.util.InputMismatchException;
import java.util.Scanner;

class Items {

    int itemCode;
    String itemName;
    double unitPrice;
    int stockRemaining;
    int itemLimit;

    public Items(int itemCode, String itemName, double unitPrice, int stockRemaining, int itemLimit) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.stockRemaining = stockRemaining;
        this.itemLimit = itemLimit;
    }

    @Override
    public String toString() {
        return "\n\nItemCode: " + itemCode + "\nItemName: " + itemName + "\nUnitPrice: " + unitPrice + "\nStockRemaining: " + stockRemaining + "\nItemLimit: " + itemLimit;
    }
}

class User {

    String name;
    double budget;

    public User(String name, double budget) {
        this.name = name;
        this.budget = budget;
    }
}

class ItemNotFound extends Exception {

    public ItemNotFound(String message) {
        super(message);
    }
}

class OverBudget extends Exception {

    public OverBudget(String message) {
        super(message);
    }
}

class ItemLimit extends Exception {

    public ItemLimit(String message) {
        super(message);
    }
}

class OutOfStock extends Exception {

    public OutOfStock(String message) {
        super(message);
    }
}

class Shop {

    Items[] itemList;
    User user;

    public Shop(Items[] itemList, User user) {
        this.itemList = itemList;
        this.user = user;
    }

    public void displayItems() {
        for (Items item : itemList) {
            System.out.println(item);
        }
    }

    public void buyItem(int itemCode, int quantity) throws ItemNotFound, OverBudget, ItemLimit, OutOfStock {
        Items selectedItem = null;

        for (Items item : itemList) {
            if (item.itemCode == itemCode) {
                selectedItem = item;
                break;
            }
        }

        if (selectedItem == null) {
            throw new ItemNotFound("Item with code " + itemCode + " not found.");
        }
        if (quantity > selectedItem.stockRemaining) {
            throw new OutOfStock("Not enough stock remaining. Available stock: " + selectedItem.stockRemaining);
        }

        if (quantity > selectedItem.itemLimit) {
            throw new ItemLimit("You cannot buy more than " + selectedItem.itemLimit + " of this item.");
        }

        double totalCost = selectedItem.unitPrice * quantity;
        if (user.budget < totalCost) {
            throw new OverBudget("You do not have enough budget. Required: " + totalCost + ", Available: " + user.budget);
        }

        selectedItem.stockRemaining -= quantity;
        user.budget -= totalCost;

        System.out.println("You have successfully bought " + quantity + " " + selectedItem.itemName);
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        do {
            try {
                System.out.println("\nMenu:");
                System.out.println("1. Display Items");
                System.out.println("2. Buy Item");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.next();
                continue;
            }

            switch (choice) {
                case 1:
                    displayItems();
                    break;
                case 2:
                    try {
                        System.out.print("Enter item code: ");
                        int itemCode = scanner.nextInt();
                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        if(quantity<0)
                       { System.out.println("Invalid quantiy");
                        break;}
                        buyItem(itemCode, quantity);
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter numbers for item code and quantity.");
                        scanner.next();
                    } catch (ItemNotFound | OverBudget | ItemLimit | OutOfStock e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Thank You");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);

        scanner.close();
    }
}

public class Q{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Items[] items = new Items[]{
            new Items(1, "TV", 10000, 3, 2),
            new Items(2, "Mouse", 1000, 7, 3),
            new Items(3, "HeadPhone", 2000, 1, 6),
            new Items(4, "Mobile", 5000, 5, 1)
        };

        System.out.print("Enter the user name : ");
        String name = sc.nextLine();
        System.out.print("Enter the budget : ");
        double budget = sc.nextDouble();
        User user = new User(name, budget);

        Shop shop = new Shop(items, user);
        shop.menu();
    }
}..