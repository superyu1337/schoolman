package com.superyu;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ManagerApp app = null;

        try {
            app = new ManagerApp("SchoolMan");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        assert app != null;
        app.generateTestData();
        app.InitUI();
    }
}
