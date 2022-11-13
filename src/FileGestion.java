import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileGestion implements Serializable {
    String salt = PasswordUtils.getSalt(30);
    public FileGestion() {}

    public void createFileWithVerif(String s){
        File myObj=new File(s);
        try {
            myObj.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void writeToFile(String fileName, String content) {
        File myObj=new File(fileName);
        try {
            FileWriter myWriter = new FileWriter(fileName, true); // L'ajout de true permet de ne pas écraser le txt précédent
            myWriter.write(content);
            myWriter.close();
            System.out.println("Write was Successfull");
        } catch (IOException e) {
            System.out.println("Something has happened not previewed");
            e.printStackTrace();
        }
    }

    public void writeToFileLineBreak(String fileName, String content) {
        File myObj=new File(fileName);
        try {
            // FileWriter myWriter = new FileWriter(fileName);
            FileWriter myWriter = new FileWriter(fileName, true);
            myWriter.write(content + "\n");
            myWriter.close();
            // System.out.println("Write was Successfull");
        } catch (IOException e) {
            System.out.println("Something has happened not previewed");
            e.printStackTrace();
        }
    }

    public void EraseAndWriteToFileLineBreak(String fileName, String content) {
        File myObj=new File(fileName);
        try {
            // FileWriter myWriter = new FileWriter(fileName);
            FileWriter myWriter = new FileWriter(fileName, false);
            myWriter.write(content + "\n");
            myWriter.close();
            // System.out.println("Write was Successfull");
        } catch (IOException e) {
            System.out.println("Something has happened not previewed");
            e.printStackTrace();
        }
    }

    public void eraseToFile (String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(fileName);
        writer.print("");
        writer.close();
    }

    public void writeToFileAnObject(String fileName, User objectName) {

        // Protect user's password
        String mySecurePassword = PasswordUtils.generateSecurePassword(Integer.toString(objectName.getPin()), salt);

        String outputText = objectName.getLogin() + "|" + mySecurePassword + "|" + objectName.getBalance();
        writeToFileLineBreak(fileName, outputText);
    }

    public void writeToFileObjects(String fileName, ArrayList<User> listUser) throws FileNotFoundException {
        eraseToFile(fileName);
        for (int i = 0; i < listUser.size(); i++) {
            User userIndex = listUser.get(i);

            // Protect user's password
            String mySecurePassword = PasswordUtils.generateSecurePassword(Integer.toString(userIndex.getPin()), salt);

            String outpuText = userIndex.getLogin() + "|" + mySecurePassword + "|" + userIndex.getBalance();
            writeToFileLineBreak(fileName, outpuText);
        }
    }


    public void readFromFile(String fileName){
        File myObj=new File(fileName);
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public ArrayList<User> readUsersFromFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        ArrayList<User> userList = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] items = line.split("\\|");

            String login = items[0];
            int pin = Integer.parseInt(items[1]);
            Double balance = Double.parseDouble(items[2]);

            User newUser = new User(login, pin, balance);

            userList.add(newUser);

        }
        return userList;
    }

    public boolean verifAccountLoginAndPassword(String fileName, String login, int pin) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] items = line.split("\\|");

            String loginItem = items[0];
            int pinItem = Integer.parseInt(items[1]);

            if (login.equals(loginItem) && pin == pinItem) {
                return true;
            }
        }
        return false;
    }

    public boolean verifAccountLogin(String fileName, String login) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] items = line.split("\\|");

            String loginItem = items[0];

            if (login.equals(loginItem)) {
                return true;
            }
        }
        return false;
    }

    public boolean verifAccountPassword(String fileName, int pin) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] items = line.split("\\|");

            String loginItem = items[0];
            int pinItem = Integer.parseInt(items[1]);

            boolean passwordMatch = PasswordUtils.verifyUserPassword(Integer.toString(pin), Integer.toString(pinItem), salt);

            if (passwordMatch) {
                return true;
            }
        }
        return false;
    }
    public boolean verifAccountExist(String fileName, String login) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] items = line.split("\\|");

            String loginItem = items[0];

            if (login.equals(loginItem)) {
                return true;
            }
        }
        return false;
    }

    public int findIndexAccount(String fileName, String login) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        int nbIndex = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] items = line.split("\\|");

            String loginItem = items[0];

            if (login.equals(loginItem)) {
                return nbIndex++;
            }
            nbIndex++;
        }
        throw new RuntimeException("Le compte n'existe pas !");
    }

    public boolean fileExist(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }


}
