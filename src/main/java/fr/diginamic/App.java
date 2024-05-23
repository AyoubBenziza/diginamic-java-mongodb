package fr.diginamic;

import org.bson.Document;

import java.util.Objects;
import fr.diginamic.services.Users;

public class App {
    public static void main(String[] args) {
        System.out.println("Base de données MongoDB avec Java");

        Users.menu();
    }
}