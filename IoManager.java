/* Autor : Alejandro Mendieta - Julian Andres Mesa
Date : 31 - 10 - 2026
Description : Proyecto Algortimos y programación I.
*/  

import java.util.Scanner;

public class IoManager {
    public String requestInfo(String message){
        System.out.print(message);
        Scanner write = new Scanner(System.in);
        String data = write.nextLine();
        return data;
    }
    public String showInfo(String message){
        System.out.print(message);
        return message;
    }
}