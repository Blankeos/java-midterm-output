import javax.swing.*;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;

class Person {
    String name;
    int age;
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

class PersonListManager {
    public ArrayList<Person> personArrayList = new ArrayList<Person>();

    //Add Entry
    public void addEntry(String name, int age) {
        Person newPerson = new Person(name, age);
        personArrayList.add(newPerson);
    }
    //Delete Entry
    public void deleteEntry(int index) {
        personArrayList.remove(index);
    }
    //Update An Entry
    public void updateEntry(String name, int age, int index) {
        personArrayList.get(index).name = name;
        personArrayList.get(index).age = age;
    }
}

class FileManagement {
    String filePath = "data.txt";
    PersonListManager plm;
    public FileManagement(String filePath, PersonListManager plm) { //MakeShift Dependency Injection with Constructor
        this.plm = plm;
        this.filePath = filePath;
        CreateFile();
        ReadFile();
    }
    public void CreateFile(){
        try {
            File myFile = new File(filePath);
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    public void ReadFile () { //Converts Text File into PersonArrayList Format
        try {
            File myFile = new File(filePath);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] lineArray = line.split(",");
                Person newPerson = new Person(lineArray[0], Integer.parseInt(lineArray[1]));
                plm.personArrayList.add(newPerson);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void WriteToFile() {
        try {
            FileWriter myWriter = new FileWriter(filePath);
            for (Person p : plm.personArrayList) {
                myWriter.write(p.name+","+p.age+"\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

class WindowManagement {
    PersonListManager plm;
    FileManagement fm;
    public WindowManagement(PersonListManager plm, FileManagement fm) {
        this.plm = plm;
        this.fm = fm;
    }

    public void showMenu() {
        String[] options = {"Add Entry", "Delete Entry", "View All Entries", "Update An Entry", "Exit"};
        int choice = JOptionPane.showOptionDialog(null,
                "                                     Welcome to Carlo's Name & Age Database" +
                        "\n\n                                                                 Options",
                "Taleon Midterm - Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
                showAdd();
                break;
            case 1:
                showDelete();
                break;
            case 2:
                showEntries();
                break;
            case 3:
                showUpdate();
                break;
            case 4:
                showExit();
                break;
        }
    }

    public void showAdd() {
        JTextField name = new JTextField();
        JTextField age = new JTextField();
        Object[] addField = {
                "                  Add Entry",
                "\nEnter Name:",
                name,
                "Enter Age: (Integer Only)",
                age
        };
        int confirm = JOptionPane.showConfirmDialog(null, addField, "Taleon MidTerms - Add Entry", JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            try {
                int ageInt = Integer.parseInt(age.getText());
                plm.addEntry(name.getText(), ageInt);
                fm.WriteToFile();
                String message = "Successfully added " + name.getText();
                showSuccess(message);
            } catch (NumberFormatException e) {
                showAdd();
            }
        } else {
            showMenu();
        }
    }

    public void showDelete() {
        JTextField deleteIndex = new JTextField();
        String entriesField = "";
        for (int i = 0; i < plm.personArrayList.size(); i++) {
            entriesField += i+1 + ". " + plm.personArrayList.get(i).name + " is " + plm.personArrayList.get(i).age + " years old.\n";
        }
        entriesField += "\n";

        Object[] deleteField = {
                entriesField,
                "Number to delete: ",
                deleteIndex
        };
        int confirm = JOptionPane.showConfirmDialog(null, deleteField, "Taleon MidTerms - Delete Entry", JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            try {
                int deleteIndexInt = Integer.parseInt(deleteIndex.getText()) - 1;
                if (deleteIndexInt >= plm.personArrayList.size()) {
                    throw new IndexOutOfBoundsException("Index " + deleteIndexInt + " is out of bounds!");
                }
                String message = "Successfully deleted " + plm.personArrayList.get(deleteIndexInt).name;
                plm.deleteEntry(deleteIndexInt);
                fm.WriteToFile();

                showSuccess(message);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                showDelete();
            }
        } else {
            showMenu();
        }
    }

    public void showEntries() {
        String[] entriesButton = {"Back To Menu"};
        String entriesField = "";

        for (int i = 0; i < plm.personArrayList.size(); i++) {
            entriesField += i+1 + ". " + plm.personArrayList.get(i).name + " is " + plm.personArrayList.get(i).age + " years old.\n";
        }

        entriesField += "\n";

        JOptionPane.showOptionDialog(null, entriesField, "Taleon MidTerms - View All Entries", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, entriesButton, entriesButton[0]);
        showMenu();
    }

    public void showUpdate() {
        JTextField updateIndex = new JTextField();
        String entriesField = "";
        for (int i = 0; i < plm.personArrayList.size(); i++) {
            entriesField += i+1 + ". " + plm.personArrayList.get(i).name + " is " + plm.personArrayList.get(i).age + " years old.\n";
        }
        entriesField += "\n";

        Object[] updateField = {
                entriesField,
                "Number to update: ",
                updateIndex
        };
        int confirm = JOptionPane.showConfirmDialog(null, updateField, "Taleon MidTerms - Update An Entry", JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            try {
                int updateIndexInt = Integer.parseInt(updateIndex.getText()) - 1;
                if (updateIndexInt >= plm.personArrayList.size()) {
                    throw new IndexOutOfBoundsException("Index " + updateIndexInt + " is out of bounds!");
                }
                showUpdatePrompt(updateIndexInt);
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                showUpdate();
            }
        } else {
            showMenu();
        }
    }

    public void showUpdatePrompt(int updateIndexInt) {
        JTextField newName = new JTextField();
        JTextField newAge = new JTextField();
        Object[] updateField = {
                "Current Name: " + plm.personArrayList.get(updateIndexInt).name,
                "Current Age: " + plm.personArrayList.get(updateIndexInt).age + "\n\n",
                "New Name: ", newName,
                "New Age: ", newAge,
                "\n"
        };
        int confirm = JOptionPane.showConfirmDialog(null, updateField, "Taleon MidTerms - Updating " + plm.personArrayList.get(updateIndexInt).name, JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            try {
                int newAgeInt = Integer.parseInt(newAge.getText());
                String message = plm.personArrayList.get(updateIndexInt).name + " is successfully updated to\n" + newName.getText() + " with the age of " + newAge.getText();
                plm.updateEntry(newName.getText(), newAgeInt, updateIndexInt);
                fm.WriteToFile();
                showSuccess(message);
            } catch (NumberFormatException e) {
                showUpdatePrompt(updateIndexInt);
            }
        } else {
            showUpdate();
        }
    }

    public void showSuccess(String message) {
        String[] successField = {"Back To Menu"};
        JOptionPane.showOptionDialog(null, message, "Taleon MidTerms - Success", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, successField, successField[0]);
        showMenu();
    }

    public void showExit() {
        //nothing really...
    }
}
public class Main {
    public static void main(String[] args) {
        PersonListManager plm = new PersonListManager();
        FileManagement fm = new FileManagement("data.csv", plm);
        WindowManagement wm = new WindowManagement(plm, fm);

        wm.showMenu();
    }
}