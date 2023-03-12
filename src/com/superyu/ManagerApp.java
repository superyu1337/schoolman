package com.superyu;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.ActionListDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.superyu.datatypes.common.Address;
import com.superyu.datatypes.common.Description;
import com.superyu.datatypes.common.Name;
import com.superyu.datatypes.common.PhoneNumber;
import com.superyu.datatypes.common.address.*;
import com.superyu.datatypes.school.*;
import com.superyu.exceptions.NotFoundException;
import com.superyu.managers.SchoolManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

enum ESortType {
    ALPHABETICAL("Alphabetical"),
    ALPHABETICAL_REVERSE("Alphabetical Reverse");

    private final String name;

    ESortType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

public class ManagerApp {
    private final SchoolManager schoolManager = new SchoolManager();
    private final MultiWindowTextGUI gui;

    public ManagerApp(String windowName) throws IOException {
        Terminal terminal = new DefaultTerminalFactory()
                .setTerminalEmulatorTitle(windowName)
                .createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
    }

    private void addClass() {
        Panel currPanel = new Panel();
        currPanel.setLayoutManager(new GridLayout(2));

        BasicWindow currWindow = new BasicWindow();
        currWindow.setComponent(currPanel);
        currWindow.setHints(List.of(Window.Hint.CENTERED));

        TerminalSize baseSize = new TerminalSize(30, 1);

        TextBox className = new TextBox(baseSize);
        currPanel.addComponent(new Label("Class Name:"));
        currPanel.addComponent(className);

        TextBox classDesc = new TextBox(baseSize);
        currPanel.addComponent(new Label("Description:"));
        currPanel.addComponent(classDesc);

        TextBox classGrade = new TextBox(baseSize);
        currPanel.addComponent(new Label("Grade:"));
        currPanel.addComponent(classGrade);

        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(new Button("Submit", () -> {
            try {
                SchoolClass newClass = new SchoolClass(
                        new SchoolGrade(classGrade.getText()),
                        new Name(className.getText()),
                        new Description(classDesc.getText())
                );

                schoolManager.addClass(newClass);
                currWindow.close();
                MessageDialog.showMessageDialog(gui, "Success", "The class has been added to the system.");
            } catch (IllegalArgumentException e) {
                MessageDialog.showMessageDialog(gui, "An error occurred!", e.getMessage());
            }
        }));

        currPanel.addComponent(new Button("Cancel", currWindow::close));
        gui.addWindow(currWindow);
    }

    private void editClass(SchoolClass schoolClass) {
        Panel currPanel = new Panel();
        currPanel.setLayoutManager(new GridLayout(2));

        BasicWindow currWindow = new BasicWindow();
        currWindow.setComponent(currPanel);
        currWindow.setHints(List.of(Window.Hint.CENTERED));

        TerminalSize baseSize = new TerminalSize(30, 1);

        TextBox className = new TextBox(baseSize, schoolClass.getClassName().toString());
        currPanel.addComponent(new Label("Class Name:"));
        currPanel.addComponent(className);

        TextBox classDesc = new TextBox(baseSize, schoolClass.getDescription().toString());
        currPanel.addComponent(new Label("Description:"));
        currPanel.addComponent(classDesc);

        TextBox classGrade = new TextBox(baseSize, schoolClass.getSchoolGrade().toString());
        currPanel.addComponent(new Label("Grade:"));
        currPanel.addComponent(classGrade);

        Teacher assignedTeacher = schoolClass.getTeacher();

        currPanel.addComponent(new Label("Teacher:"));

        if (assignedTeacher == null)
            currPanel.addComponent(new Label("None"));
        else
            currPanel.addComponent(new Label(assignedTeacher.toString()));

        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        currPanel.addComponent(new Button("Edit Teacher", () -> {
            try {
                editSchoolPerson(schoolClass.getTeacher());
            } catch (NullPointerException e) {
                MessageDialog.showMessageDialog(gui, "An error occurred!", "This class currently doesn't have a teacher");
            }
        }));

        currPanel.addComponent(new Button("Edit Students", () -> {
            HashSet<Student> students = schoolClass.getStudents();

            ActionListDialogBuilder dialogBuilder = new ActionListDialogBuilder()
                    .setTitle("Students")
                    .setDescription("Choose student to edit");

            students.forEach((currStudent) -> dialogBuilder.addAction(currStudent.toString(), () -> editSchoolPerson(currStudent)));
            dialogBuilder.build().showDialog(gui);
        }));

        currPanel.addComponent(new Button("Save changes", () -> {
            MessageDialogButton confirmButton = new MessageDialogBuilder()
                    .setTitle("Are you sure?")
                    .setText("Changes can not be reversed!")
                    .addButton(MessageDialogButton.Yes)
                    .addButton(MessageDialogButton.Cancel)
                    .build()
                    .showDialog(gui);

            if (confirmButton.equals(MessageDialogButton.Cancel))
                return;

            try {
                schoolClass.setSchoolGrade(new SchoolGrade(classGrade.getText()));
                schoolClass.setClassName(new Name(className.getText()));
                schoolClass.setDescription(new Description(classDesc.getText()));

                currWindow.close();
                MessageDialog.showMessageDialog(gui, "Success", "Changes have been saved.");
            } catch (IllegalArgumentException e) {
                MessageDialog.showMessageDialog(gui, "An error occurred!", e.getMessage());
            }
        }));

        currPanel.addComponent(new Button("Cancel", currWindow::close));
        gui.addWindow(currWindow);
    }

    private void addTeacher() {
        Panel currPanel = new Panel();
        currPanel.setLayoutManager(new GridLayout(2));

        BasicWindow currWindow = new BasicWindow();
        currWindow.setComponent(currPanel);
        currWindow.setHints(List.of(Window.Hint.CENTERED));

        TerminalSize baseSize = new TerminalSize(30, 1);

        TextBox teacherName = new TextBox(baseSize);
        currPanel.addComponent(new Label("Name:"));
        currPanel.addComponent(teacherName);

        TextBox teacherSurname = new TextBox(baseSize);
        currPanel.addComponent(new Label("Surname:"));
        currPanel.addComponent(teacherSurname);

        ArrayList<SchoolClass> sortedClasses = schoolManager.sortedClasses();
        ArrayList<String> classesStrings = new ArrayList<>();

        for (SchoolClass currSchoolClass : sortedClasses) {
            classesStrings.add(currSchoolClass.getClassName().toString());
        }

        ComboBox<String> teacherAssignedClass = new ComboBox<>(classesStrings);

        currPanel.addComponent(new Label("Assigned Class:"));
        currPanel.addComponent(teacherAssignedClass);

        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(new Button("Submit", () -> {
            try {
                Teacher newTeacher = new Teacher(
                        new Name(teacherName.getText()),
                        new Name(teacherSurname.getText()),
                        schoolManager.getClassByName(new Name(teacherAssignedClass.getText()))
                );

                schoolManager.addTeacher(newTeacher);
                currWindow.close();
                MessageDialog.showMessageDialog(gui, "Success", "The teacher has been added to the system.");
            } catch (IllegalArgumentException | NotFoundException e) {
                MessageDialog.showMessageDialog(gui, "An error occurred!", e.getMessage());
            }
        }));

        currPanel.addComponent(new Button("Cancel", currWindow::close));
        gui.addWindow(currWindow);
    }

    private void addStudent() {
        Panel currPanel = new Panel();
        currPanel.setLayoutManager(new GridLayout(2));

        BasicWindow currWindow = new BasicWindow();
        currWindow.setComponent(currPanel);
        currWindow.setHints(List.of(Window.Hint.CENTERED));

        TerminalSize baseSize = new TerminalSize(30, 1);

        TextBox studentName = new TextBox(baseSize);
        currPanel.addComponent(new Label("Name:"));
        currPanel.addComponent(studentName);

        TextBox studentSurname = new TextBox(baseSize);
        currPanel.addComponent(new Label("Surname:"));
        currPanel.addComponent(studentSurname);

        ArrayList<SchoolClass> sortedClasses = schoolManager.sortedClasses();
        ArrayList<String> classesStrings = new ArrayList<>();

        for (SchoolClass currSchoolClass : sortedClasses) {
            classesStrings.add(currSchoolClass.getClassName().toString());
        }

        ComboBox<String> studentAssignedClass = new ComboBox<>(classesStrings);

        currPanel.addComponent(new Label("Assigned Class:"));
        currPanel.addComponent(studentAssignedClass);

        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(new Button("Submit", () -> {
            try {
                Student newStudent = new Student(
                        new Name(studentName.getText()),
                        new Name(studentSurname.getText()),
                        schoolManager.getClassByName(new Name(studentAssignedClass.getText()))
                );

                schoolManager.addStudent(newStudent);
                currWindow.close();
                MessageDialog.showMessageDialog(gui, "Success", "The student has been added to the system.");
            } catch (IllegalArgumentException | NotFoundException e) {
                MessageDialog.showMessageDialog(gui, "An error occurred!", e.getMessage());
            }
        }));

        currPanel.addComponent(new Button("Cancel", currWindow::close));
        gui.addWindow(currWindow);
    }

    private void editSchoolPerson(SchoolPerson schoolPerson) {
        Panel currPanel = new Panel();
        currPanel.setLayoutManager(new GridLayout(2));

        BasicWindow currWindow = new BasicWindow();
        currWindow.setComponent(currPanel);
        currWindow.setHints(List.of(Window.Hint.CENTERED));

        TerminalSize baseSize = new TerminalSize(30, 1);

        HashSet<Address> personAddresses = new HashSet<>(schoolPerson.getAddresses());
        HashSet<PhoneNumber> personPhoneNumbers = new HashSet<>(schoolPerson.getPhoneNumbers());

        TextBox personName = new TextBox(baseSize, schoolPerson.getName().toString());
        currPanel.addComponent(new Label("Name:"));
        currPanel.addComponent(personName);

        TextBox personSurname = new TextBox(baseSize, schoolPerson.getSurname().toString());
        currPanel.addComponent(new Label("Surname:"));
        currPanel.addComponent(personSurname);

        ArrayList<SchoolClass> sortedClasses = schoolManager.sortedClasses();
        ArrayList<String> classesStrings = new ArrayList<>();
        classesStrings.add("None");

        for (SchoolClass currSchoolClass : sortedClasses) {
            classesStrings.add(currSchoolClass.getClassName().toString());
        }

        ComboBox<String> personAssignedClass = new ComboBox<>(classesStrings);

        try {
            personAssignedClass.setSelectedItem(schoolPerson.getAssignedClass().getClassName().toString());
        } catch (NullPointerException e) {
            personAssignedClass.setSelectedIndex(0);
        }

        currPanel.addComponent(new Label("Assigned Class:"));
        currPanel.addComponent(personAssignedClass);

        Table<String> addressTable = new Table<>("Street", "House number", "Zipcode", "City", "Country");

        for (Address personAddress : personAddresses) {
            addressTable.getTableModel().addRow(
                    personAddress.getStreetName().toString(),
                    personAddress.getHouseNumber().toString(),
                    personAddress.getZipCode().toString(),
                    personAddress.getCityName().toString(),
                    personAddress.getCountryName().toString()
            );
        }

        currPanel.addComponent(new Label("Addresses:"));
        currPanel.addComponent(addressTable);

        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        Table<String> phoneNumberTable = new Table<>("Number");

        for (PhoneNumber phoneNumber : personPhoneNumbers) {
            phoneNumberTable.getTableModel().addRow(
                    phoneNumber.toString()
            );
        }

        currPanel.addComponent(new Label("Phone-numbers:"));
        currPanel.addComponent(phoneNumberTable);

        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        currPanel.addComponent(new Button("Add Phone-number", () -> {
            addPhoneNumber(personPhoneNumbers);
            phoneNumberTable.getTableModel().clear();
            for (PhoneNumber phoneNumber : personPhoneNumbers) {
                phoneNumberTable.getTableModel().addRow(
                        phoneNumber.toString()
                );
            }
        }));

        currPanel.addComponent(new Button("Remove Phone-number", () -> {
            if (personPhoneNumbers.size() == 0) {
                MessageDialog.showMessageDialog(gui, "An error occurred!", "This person currently doesn't have any phone-numbers.");
                return;
            }

            ActionListDialogBuilder dialogBuilder = new ActionListDialogBuilder()
                    .setTitle("Phone-numbers")
                    .setDescription("Choose phone-number to remove");

            for (PhoneNumber personPhoneNumber : personPhoneNumbers) {
                dialogBuilder.addAction(personPhoneNumber.toString(), () -> {
                    personPhoneNumbers.remove(personPhoneNumber);
                    phoneNumberTable.getTableModel().clear();
                    for (PhoneNumber phoneNumber : personPhoneNumbers) {
                        phoneNumberTable.getTableModel().addRow(
                                phoneNumber.toString()
                        );
                    }
                });
            }

            dialogBuilder.build().showDialog(gui);
        }));

        currPanel.addComponent(new Button("Add Address", () -> {
            addAddress(personAddresses);

            addressTable.getTableModel().clear();
            for (Address personAddress : personAddresses) {
                addressTable.getTableModel().addRow(
                        personAddress.getStreetName().toString(),
                        personAddress.getHouseNumber().toString(),
                        personAddress.getZipCode().toString(),
                        personAddress.getCityName().toString(),
                        personAddress.getCountryName().toString()
                );
            }
        }));

        currPanel.addComponent(new Button("Remove Address", () -> {
            if (personAddresses.size() == 0) {
                MessageDialog.showMessageDialog(gui, "An error occurred!", "This person currently doesn't have any addresses.");
                return;
            }

            ActionListDialogBuilder dialogBuilder = new ActionListDialogBuilder()
                    .setTitle("Addresses")
                    .setDescription("Choose address to remove");

            for (Address personAddress : personAddresses) {
                dialogBuilder.addAction(personAddress.toString(), () -> {
                    personAddresses.remove(personAddress);
                    addressTable.getTableModel().clear();
                    for (Address currAddress : personAddresses) {
                        addressTable.getTableModel().addRow(
                                currAddress.getStreetName().toString(),
                                currAddress.getHouseNumber().toString(),
                                currAddress.getZipCode().toString(),
                                currAddress.getCityName().toString(),
                                currAddress.getCountryName().toString()
                        );
                    }
                });
            }

            dialogBuilder.build().showDialog(gui);
        }));

        currPanel.addComponent(new Button("Save Changes", () -> {
            MessageDialogButton confirmButton = new MessageDialogBuilder()
                    .setTitle("Are you sure?")
                    .setText("Changes can not be reversed!")
                    .addButton(MessageDialogButton.Yes)
                    .addButton(MessageDialogButton.Cancel)
                    .build()
                    .showDialog(gui);

            if (confirmButton.equals(MessageDialogButton.Cancel))
                return;

            try {
                if (schoolPerson.getClass() == Teacher.class) {
                    if (personAssignedClass.getSelectedIndex() != 0) {
                        SchoolClass classToAssign = schoolManager.getClassByName(new Name(personAssignedClass.getText()));

                        if (schoolPerson.getAssignedClass() != null) // fuck null pointers
                            schoolPerson.getAssignedClass().setTeacher(null); // Remove teacher from previous assigned class


                        if (classToAssign.getTeacher() != null) // fuck null pointers
                            classToAssign.getTeacher().setAssignedClass(null); // Set previous teacher's assigned class to null

                        schoolPerson.setAssignedClass(classToAssign); // Assign class to this teacher
                        classToAssign.setTeacher((Teacher) schoolPerson); // Set class's teacher to this teacher
                    } else {
                        schoolPerson.getAssignedClass().setTeacher(null);
                        schoolPerson.setAssignedClass(null);
                    }
                }

                schoolPerson.setName(new Name(personName.getText()));
                schoolPerson.setSurname(new Name(personSurname.getText()));
                schoolPerson.setAddresses(personAddresses);
                schoolPerson.setPhoneNumbers(personPhoneNumbers);

                currWindow.close();
                MessageDialog.showMessageDialog(gui, "Success", "Changes have been saved.");
            } catch (IllegalArgumentException | NotFoundException e) {
                MessageDialog.showMessageDialog(gui, "An error occurred!", e.getMessage());
            }
        }));

        currPanel.addComponent(new Button("Cancel", currWindow::close));
        gui.addWindow(currWindow);
    }

    private void addPhoneNumber(HashSet<PhoneNumber> phoneNumbers) {
        Panel currPanel = new Panel();
        currPanel.setLayoutManager(new GridLayout(2));

        BasicWindow currWindow = new BasicWindow();
        currWindow.setComponent(currPanel);
        currWindow.setHints(List.of(Window.Hint.CENTERED));
        TerminalSize baseSize = new TerminalSize(30, 1);

        TextBox phoneNumber = new TextBox(baseSize);
        currPanel.addComponent(new Label("Phone Number:"));
        currPanel.addComponent(phoneNumber);

        currPanel.addComponent(new Button("Submit", () -> {
            try {
                PhoneNumber newPhoneNumber = new PhoneNumber(phoneNumber.getText());

                if (!phoneNumbers.add(newPhoneNumber))
                    MessageDialog.showMessageDialog(gui, "Notice", "This phone-number was already existed before. It wasn't added.");

                currWindow.close();
            } catch (IllegalArgumentException e) {
                    MessageDialog.showMessageDialog(gui, "An error occurred!", e.getMessage());
            }
        }));
        currPanel.addComponent(new Button("Cancel", currWindow::close));

        gui.addWindowAndWait(currWindow);
    }

    private void addAddress(HashSet<Address> addresses) {
        Panel currPanel = new Panel();
        currPanel.setLayoutManager(new GridLayout(2));

        BasicWindow currWindow = new BasicWindow();
        currWindow.setComponent(currPanel);
        currWindow.setHints(List.of(Window.Hint.CENTERED));
        TerminalSize baseSize = new TerminalSize(30, 1);

        TextBox streetName = new TextBox(baseSize);
        currPanel.addComponent(new Label("Street:"));
        currPanel.addComponent(streetName);

        TextBox houseNumber = new TextBox(baseSize);
        currPanel.addComponent(new Label("House number:"));
        currPanel.addComponent(houseNumber);

        TextBox cityName = new TextBox(baseSize);
        currPanel.addComponent(new Label("City:"));
        currPanel.addComponent(cityName);

        TextBox zipCode = new TextBox(baseSize);
        currPanel.addComponent(new Label("Zipcode:"));
        currPanel.addComponent(zipCode);

        TextBox countryName = new TextBox(baseSize);
        currPanel.addComponent(new Label("Country:"));
        currPanel.addComponent(countryName);

        currPanel.addComponent(new Button("Submit", () -> {
            try {
                Address newAddress = new Address(
                        new StreetName(streetName.getText()),
                        new HouseNumber(Integer.parseInt(houseNumber.getText())),
                        new CityName(cityName.getText()),
                        new ZipCode(zipCode.getText()),
                        new CountryName(countryName.getText())
                );

                if (!addresses.add(newAddress))
                    MessageDialog.showMessageDialog(gui, "Notice", "This address was already existed before. It wasn't added.");

                currWindow.close();
            } catch (IllegalArgumentException e) {
                if (e.getClass() == NumberFormatException.class)
                    MessageDialog.showMessageDialog(gui, "An error occurred!", "Your House-number is not valid.");
                else
                    MessageDialog.showMessageDialog(gui, "An error occurred!", e.getMessage());
            }
        }));
        currPanel.addComponent(new Button("Cancel", currWindow::close));
        gui.addWindowAndWait(currWindow);
    }

    private void ListUI(ArrayList<String> list, String title) {
        class Sorter {
            ArrayList<String> alpabetical(ArrayList<String> list) {
                ArrayList<String> sortedList = new ArrayList<>(list);
                Collections.sort(sortedList);
                return sortedList;
            }

            ArrayList<String> alphabeticalReverse(ArrayList<String> list) {
                ArrayList<String> sortedList = new ArrayList<>(list);
                Collections.sort(sortedList);
                Collections.reverse(sortedList);
                return sortedList;
            }
        }

        Panel currPanel = new Panel();
        currPanel.setLayoutManager(new GridLayout(1));

        BasicWindow currWindow = new BasicWindow();
        currWindow.setTitle(title);
        currWindow.setComponent(currPanel);
        currWindow.setHints(List.of(Window.Hint.CENTERED));

        ComboBox<ESortType> sortType = new ComboBox<>(ESortType.ALPHABETICAL, ESortType.ALPHABETICAL_REVERSE);

        Table<String> table = new Table<>("Full Name");
        ArrayList<String> sortedList = new Sorter().alpabetical(list);

        for (String s : sortedList) {
            table.getTableModel().addRow(s);
        }

        currPanel.addComponent(new Label("Sorting mode"));
        currPanel.addComponent(sortType);
        currPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        currPanel.addComponent(table);
        currPanel.addComponent(new Button("Exit", currWindow::close));

        sortType.addListener((a, b, c) -> {
            ESortType currSortType = sortType.getItem(a);
            ArrayList<String> newSortedList = switch (currSortType) {
                case ALPHABETICAL -> new Sorter().alpabetical(list);
                case ALPHABETICAL_REVERSE -> new Sorter().alphabeticalReverse(list);
            };

            table.getTableModel().clear();
            for (String s : newSortedList) {
                table.getTableModel().addRow(s);
            }
        });

        gui.addWindowAndWait(currWindow);
    }

    public void InitUI() {
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(3));

        BasicWindow window = new BasicWindow();
        window.setComponent(panel);
        window.setHints(List.of(Window.Hint.CENTERED));

        panel.addComponent(new Button("Add Class", this::addClass));
        panel.addComponent(new Button("Edit Classes", () -> {
            ActionListDialogBuilder dialogBuilder = new ActionListDialogBuilder()
                    .setTitle("Classes")
                    .setDescription("Choose class to view");

            ArrayList<SchoolClass> sortedClasses = schoolManager.sortedClasses();

            for (SchoolClass schoolClass : sortedClasses) {
                dialogBuilder.addAction(schoolClass.getClassName().toString(), () -> editClass(schoolClass));
            }

            dialogBuilder.build().showDialog(gui);
        }));
        panel.addComponent(new Button("List Classes", () -> {
            ArrayList<SchoolClass> sortedClasses = schoolManager.sortedClasses();
            List<String> classNames = sortedClasses.stream().map(SchoolClass::toString).toList();
            ArrayList<String> finalClassNames = new ArrayList<>(classNames);
            ListUI(finalClassNames, "Class list");
        }));

        panel.addComponent(new Button("Add Teacher", this::addTeacher));
        panel.addComponent(new Button("Edit Teachers", () -> {
            ActionListDialogBuilder dialogBuilder = new ActionListDialogBuilder()
                    .setTitle("Teachers")
                    .setDescription("Choose teacher to edit");

            ArrayList<Teacher> sortedTeachers = schoolManager.sortedTeachers();

            for (Teacher teacher : sortedTeachers) {
                dialogBuilder.addAction(teacher.toString(), () -> editSchoolPerson(teacher));
            }

            dialogBuilder.build().showDialog(gui);
        }));
        panel.addComponent(new Button("List Teachers", () -> {
            ArrayList<Teacher> sortedTeachers = schoolManager.sortedTeachers();
            List<String> teacherNames = sortedTeachers.stream().map(Teacher::toString).toList();
            ArrayList<String> finalTeacherNames = new ArrayList<>(teacherNames);
            ListUI(finalTeacherNames, "Teacher list");
        }));

        panel.addComponent(new Button("Add Student", this::addStudent));
        panel.addComponent(new Button("Edit Students", () -> {
            ActionListDialogBuilder dialogBuilder = new ActionListDialogBuilder()
                    .setTitle("Student")
                    .setDescription("Choose student to edit");

            ArrayList<Student> sortedStudents = schoolManager.sortedStudents();

            for (Student student : sortedStudents) {
                dialogBuilder.addAction(student.toString(), () -> editSchoolPerson(student));
            }

            dialogBuilder.build().showDialog(gui);
        }));
        panel.addComponent(new Button("List Students", () -> {
            ArrayList<Student> sortedStudents = schoolManager.sortedStudents();
            List<String> studentNames = sortedStudents.stream().map(Student::toString).toList();
            ArrayList<String> finalStudentNames = new ArrayList<>(studentNames);
            ListUI(finalStudentNames, "Student list");
        }));


        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new Button("Exit", () -> System.exit(0)));
        gui.addWindowAndWait(window);
    }

    public void generateTestData() {
        try {
            // Add some classes
            schoolManager.addClass(new SchoolClass(
                    new SchoolGrade("11e"),
                    new Name("Informatik"),
                    new Description("Beste Informatikklasse")
            ));

            schoolManager.addClass(new SchoolClass(
                    new SchoolGrade("11c"),
                    new Name("Mathe"),
                    new Description("Wir sind schlau, aber unsportlich.")
            ));

            schoolManager.addClass(new SchoolClass(
                    new SchoolGrade("11a"),
                    new Name("Allerbeste-Sportklasse"),
                    new Description("Wir sind dumm, aber sportlich.")
            ));

            // Add some teachers
            schoolManager.addTeacher(
                    new Teacher(
                            new Name("Tim"),
                            new Name("Schulz"),
                            schoolManager.getClassByName(new Name("Allerbeste-Sportklasse"))
                    )
            );

            schoolManager.addTeacher(
                    new Teacher(
                            new Name("Max"),
                            new Name("Musterlehrer"),
                            schoolManager.getClassByName(new Name("Informatik"))
                    )
            );

            schoolManager.addTeacher(
                    (Teacher) new Teacher(
                            new Name("Albert"),
                            new Name("Einstein"),
                            schoolManager.getClassByName(new Name("Mathe"))
                    ).addPhoneNumber(new PhoneNumber("+49 174 3231234"))
            );

            // Add some students
            schoolManager.addStudent(
                    new Student(
                            new Name("Max"),
                            new Name("Musterschüler"),
                            schoolManager.getClassByName(new Name("Allerbeste-Sportklasse"))
                    )
            );

            schoolManager.addStudent(
                    new Student(
                            new Name("Steven"),
                            new Name("Unsportlich"),
                            schoolManager.getClassByName(new Name("Mathe"))
                    )
            );

            schoolManager.addStudent(
                    new Student(
                            new Name("Janek"),
                            new Name("Cooler-schüler"),
                            schoolManager.getClassByName(new Name("Informatik"))
                    )
            );
        } catch (IllegalArgumentException | NotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}